package utn.gallino.mspedido.Service.impl;

import org.springframework.boot.autoconfigure.integration.IntegrationProperties;
import utn.gallino.mspedido.domain.*;
import  utn.gallino.mspedido.repository.PedidoRepository;
import utn.gallino.mspedido.Service.ClienteService;
import utn.gallino.mspedido.Service.MaterialService;
import utn.gallino.mspedido.Service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.gallino.mspedido.repository.PedidoRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoServiceImpl implements PedidoService {

	@Autowired
	MaterialService materialSrv;

	@Autowired
	ClienteService clienteService;

	@Autowired
	PedidoRepository pedidoRepository;

	@Override
	public Pedido crearPedido(Pedido p) {
		System.out.println("HOLA PEDIDO " + p);
		boolean hayStock = p.getDetalle()
				.stream()
				.allMatch(dp -> verificarStock(dp.getProducto(), dp.getCantidad()));

		Double totalOrden = p.getDetalle()
				.stream()
				.mapToDouble(dp -> dp.getCantidad() * dp.getPrecio())
				.sum();


		Double saldoCliente = clienteService.deudaCliente(p.getObra());                //sale consulta http hasta rest de cliente para averiguar su maxcuenta corriente
		Double nuevoSaldo = saldoCliente - totalOrden;

		Boolean generaDeuda = nuevoSaldo < 0;
		if (hayStock) {
			if (!generaDeuda || (generaDeuda && saldoDeudor(p.getObra(), nuevoSaldo) && this.esDeBajoRiesgo(p.getObra()))) {
				p.setEstado(new EstadoPedido(1, "ACEPTADO"));
			} else {
				throw new RuntimeException("No tiene aprobacion crediticia");
			}
		} else {
			p.setEstado(new EstadoPedido(2, "PENDIENTE"));
		}
		guardarPedido(p);
		return p;
	}


	public Pedido actualizarPedido(Pedido p, Integer id){
		p.setId(id);
		pedidoRepository.save(p);
		return p;
	}

	@Override
	public Pedido guardarPedido(Pedido p) {
		pedidoRepository.save(p);
		return p;
	}

	public void agregarDetalle(DetallePedido dp, Integer IdPedido){


		Pedido pedidoActualizado = pedidoRepository.findById(IdPedido).get();
		pedidoActualizado.getDetalle().add(dp);
		guardarPedido(pedidoActualizado);		 //si ya existe lo actualiza

	}

	@Override
	public void eliminarDetalle(Integer idDp, Integer IdPedido) {
		Pedido pedidoActualizado = pedidoRepository.findById(IdPedido).get();
		pedidoActualizado.getDetalle().remove(idDp);
		guardarPedido(pedidoActualizado);
	}

	@Override
	public void bajaPedido(Integer id) throws Exception {


		if (pedidoRepository.findById(id).isPresent()){
			pedidoRepository.deleteById(id);
		}else throw new Exception("Not found");
	}

	@Override
	public List<Pedido> listarPedidos() {
		List<Pedido> result = new ArrayList<>();
		pedidoRepository.findAll().forEach(pedido -> result.add(pedido));
		return result;
	}

	@Override
	public Pedido buscarPedidoPorId(Integer id) throws Exception {

		Pedido auxP;
		try {auxP= pedidoRepository.findById(id).get();
		}catch (Exception e ){throw new Exception("not found");}
		return auxP;

	}

	@Override
	public Pedido buscarPedidoPorIdObra(Integer idObra) throws Exception {
		List<Pedido> result = new ArrayList<>();
		Pedido auxP;
		try {
			pedidoRepository.findAll().forEach(pedido -> result.add(pedido));                //CONVERT ITERABLE TO lIST
			auxP = result.stream()
					.filter(p -> p.getObra().getId().equals(idObra))
					.findFirst()
					.get();
		}catch (Exception e ){throw new Exception("not found");}

		return  auxP;
	}

	@Override
	public DetallePedido buscarDetallePorId(Integer idDetalle, Integer idPedido) throws Exception {

			DetallePedido auxDp;
		try {
			Pedido auxP = pedidoRepository.findById(idPedido).get();
			 auxDp= auxP.getDetalle().stream()
					.filter(dp -> dp.getId().equals(idDetalle))
					.findFirst()
					.get();
		}catch (Exception e ){throw new Exception("not found");}

		return auxDp;
		}

	boolean verificarStock(Producto p, Integer cantidad) {
		return materialSrv.stockDisponible(p) >= cantidad;
	}

	boolean esDeBajoRiesgo(Obra o) {
		Integer situacion = clienteService.situacionCrediticiaBCRA(o);
		if (situacion == 1 || situacion == 2) return true;
		else
			return false;
	}

	boolean saldoDeudor(Obra o, Double saldoNuevo) {
		Double maximoSaldoNegativo = clienteService.maximoSaldoNegativo(o);
		Boolean tieneSaldo = Math.abs(saldoNuevo) < maximoSaldoNegativo;
		return tieneSaldo;

	}
}
