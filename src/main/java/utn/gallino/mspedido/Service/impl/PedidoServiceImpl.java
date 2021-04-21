package utn.gallino.mspedido.Service.impl;

import  utn.gallino.mspedido.domain.EstadoPedido;
import  utn.gallino.mspedido.domain.Obra;
import  utn.gallino.mspedido.domain.Pedido;
import  utn.gallino.mspedido.domain.Producto;
//import  utn.gallino.mspedido.repository.PedidoRepository;
import utn.gallino.mspedido.Service.ClienteService;
import utn.gallino.mspedido.Service.MaterialService;
import utn.gallino.mspedido.Service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PedidoServiceImpl implements PedidoService {

	@Autowired
	MaterialService materialSrv;

	//@Autowired
	//PedidoRepository repo;

	@Autowired
	ClienteService clienteSrv;


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


		Double saldoCliente = clienteSrv.deudaCliente(p.getObra());                //sale consulta http hasta rest de cliente para averiguar su maxcuenta corriente
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
		//todo		return this.repo.save(p);
		return p;
	}


	boolean verificarStock(Producto p, Integer cantidad) {
		return materialSrv.stockDisponible(p) >= cantidad;
	}

	boolean esDeBajoRiesgo(Obra o) {
		Integer situacion = clienteSrv.situacionCrediticiaBCRA(o);
		if (situacion == 1 || situacion == 2) return true;
		else
			return false;
	}

	boolean saldoDeudor(Obra o, Double saldoNuevo) {
		Double maximoSaldoNegativo = clienteSrv.maximoSaldoNegativo(o);
		Boolean tieneSaldo = Math.abs(saldoNuevo) < maximoSaldoNegativo;
		return tieneSaldo;

	}
}
