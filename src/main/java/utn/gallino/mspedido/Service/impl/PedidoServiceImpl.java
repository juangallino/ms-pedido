package utn.gallino.mspedido.Service.impl;

import org.springframework.boot.autoconfigure.integration.IntegrationProperties;
import org.springframework.http.HttpMethod;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.client.RestTemplate;
import utn.gallino.mspedido.domain.*;
import utn.gallino.mspedido.repository.DetalleRepository;
import utn.gallino.mspedido.repository.EstadoPedidoRepository;
import  utn.gallino.mspedido.repository.PedidoRepository;
import utn.gallino.mspedido.Service.ClienteService;
import utn.gallino.mspedido.Service.MaterialService;
import utn.gallino.mspedido.Service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import utn.gallino.mspedido.repository.PedidoRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoServiceImpl implements PedidoService {

	@Autowired
	MaterialService materialSrv;

	@Autowired
	ClienteService clienteService;
	@Autowired
	DetalleRepository detalleRepository;

	@Autowired
	PedidoRepository pedidoRepository;
	@Autowired
	EstadoPedidoRepository estadoPedidoRepository;

	@Autowired
	JmsTemplate jms;

	private static final Logger logger =LoggerFactory.getLogger(PedidoServiceImpl.class);

	private static final String STOCK_REST_API_URL = "http://localhost:9001";
	private static final String Stock_ENDPOINT = "/api/stock";
	RestTemplate rest = new RestTemplate();

	@Override
	public Pedido crearPedido(Pedido p) {

		p.setFechaPedido(Instant.now().plusSeconds(1));

		logger.trace("Creando pedido "+p);
		boolean hayStock = p.getDetalle()
				.stream()
				.allMatch(dp -> verificarStock(dp.getProducto(), dp.getCantidad()));   //sale consulta http hasta rest de stock para averiguar stock de producto
		System.out.println(2);

		Double totalOrdenPedido=0.0;
		for (DetallePedido dp : p.getDetalle()){
			Double costoDp=dp.getCantidad() * dp.getProducto().getPrecio();
			totalOrdenPedido+= costoDp;
			logger.trace("costo del dpPed: " +costoDp +" suma total del pedido: "+totalOrdenPedido);
			dp.setPrecio(costoDp);

		}
		/*Double totalOrden = p.getDetalle()
				.stream()
				.mapToDouble(dp -> dp.getCantidad() * dp.getProducto().getPrecio())
				.sum();
		*/
		System.out.println("total orden= " +totalOrdenPedido);
		System.out.println(3);

		Double saldoCliente = clienteService.deudaCliente(p.getObra());                //sale consulta http hasta rest de cliente para averiguar su maxcuenta corriente
		Double nuevoSaldo = saldoCliente - totalOrdenPedido;


		Boolean generaDeuda = nuevoSaldo < 0;
		if (hayStock) {
			System.out.println("hay stock");
			if (!generaDeuda || (generaDeuda && saldoDeudor(p.getObra(), nuevoSaldo) && this.esDeBajoRiesgo(p.getObra()))) {
				System.out.println("puede ser generado el pedido. adentro del if");
				p.setEstado(estadoPedidoRepository.getEstadoPedidoByEstado("NUEVO"));
				System.out.println(p.getEstado().toString()+" ESTADO NUEVO");

				try{
					guardarPedido(p);
					actualizarStock(p.getId());  //JMS //llamada http a ms-stock

				}catch (Exception e){e.printStackTrace();}



			} else {
				System.out.println("No tiene aprobacion crediticia");
				throw new RuntimeException("No tiene aprobacion crediticia");
			}
		} else {
			p.setEstado(estadoPedidoRepository.getEstadoPedidoByEstado("PENDIENTE"));
			System.out.println(p.getEstado().toString()+" ESTADO PENDIENTE");
			logger.trace("El pedido se guardo con estado: {}",p.getEstado().getEstado());
			guardarPedido(p);
			//metodo crear solicitudProvision(p);

		}
		return p;
	}

	private Boolean actualizarStock(Integer id_Pedido) {

		Pedido ped = pedidoRepository.findById(id_Pedido).get();
		String id_DetallesPedidos = ped.getDetalle().stream()
				.map(dp->dp.getId().toString())
				.collect(Collectors.joining(","));

		System.out.println("en actualizarstockdp");
		//return true;

		//JMS
		jms.convertAndSend("COLA_PEDIDOS",id_DetallesPedidos);
		logger.trace("Se envio el msj : {}", id_DetallesPedidos);
		//System.out.println("llamada http a stock api rest");
		//String url = STOCK_REST_API_URL + Stock_ENDPOINT +"/pedido/actualizarStockPorPedido/?listaId_dp="+ id_DetallesPedidos;

	//	try {
	//		rest.exchange(url, HttpMethod.POST,null, Boolean.class).getBody();
	//	}catch (Exception e){return  false;}
		return  true;
	}


	public Pedido actualizarPedido(Pedido p, Integer id){
		p.setId(id);
		pedidoRepository.save(p);
		return p;
	}

	@Override
	public Pedido guardarPedido(Pedido p) {
		for (DetallePedido dp : p.getDetalle()){
			detalleRepository.save(dp);
		}
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
				Pedido auxP;
				List<Pedido> result =new ArrayList<>();
		try {

		//	auxP=pedidoRepository.findPedidoByObra_Id(idObra);
			pedidoRepository.findAll().forEach(pedido -> result.add(pedido));                //CONVERT ITERABLE TO lIST
			auxP = result.stream()
					.filter(p -> p.getObra().getId().equals(idObra))
					.findFirst()
					.get();
		}catch (Exception e ){throw new Exception("not found");}

		return  auxP;
	}
	@Override			//Este metodo se fija si existe algun pedido asociado a la cuenta y devuelve true si hay de lo contrario false
	public Boolean checkPedidoPorIdObra(Integer idObra){
		System.out.println("check cliente activo iniciado con id obra= " + idObra);
		Pedido auxP;
		try {
			//auxP= pedidoRepository.findPedidoByObra_Id(idObra);    //todo NO ANDA EL METODO FINDBY AUTOMATICO DEL REPO
			auxP= buscarPedidoPorIdObra(idObra);
		}
			catch (Exception e ){    System.out.println("falso por el catch"); return false;}
		if(auxP==null){ System.out.println("falso por null");  return false;}

		System.out.println("true el pedido de la obra");
		return true;

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
		System.out.println("metodo verificar stock con cantidad "+ cantidad+" de producto "+p.getDescripcion());
		System.out.println(materialSrv.stockDisponible(p) >= cantidad);
		return materialSrv.stockDisponible(p) >= cantidad;
	}

	boolean esDeBajoRiesgo(Obra o) {
		Integer situacion = clienteService.situacionCrediticiaBCRA(o);
		if (situacion == 1 || situacion == 2) return true;
		else
			return false;
	}

	boolean saldoDeudor(Obra o, Double saldoNuevo) {
		Double maximoSaldoNegativo = (Double) clienteService.maximoSaldoNegativo(o);
		Boolean tieneSaldo = Math.abs(saldoNuevo) < maximoSaldoNegativo;
		return tieneSaldo;

	}
}
