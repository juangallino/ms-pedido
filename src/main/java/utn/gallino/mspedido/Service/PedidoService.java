package utn.gallino.mspedido.Service;

import utn.gallino.mspedido.domain.DetallePedido;
import  utn.gallino.mspedido.domain.Pedido;

import java.util.List;

public interface PedidoService {

	public Pedido crearPedido(Pedido p);
	public void agregarDetalle(DetallePedido dp, Integer idPedido);
	public void eliminarDetalle(Integer idDp, Integer idPedido);
	public void bajaPedido(Integer id) throws Exception;
	public List<Pedido> listarPedidos();
	public Pedido buscarPedidoPorId(Integer id) throws Exception;
	public Pedido actualizarPedido(Pedido p, Integer id);
	public Pedido guardarPedido(Pedido p);
	Pedido buscarPedidoPorIdObra(Integer idObra) throws Exception;

	DetallePedido buscarDetallePorId(Integer idDetalle, Integer idPedido) throws Exception;
	public Boolean checkPedidoPorIdObra(Integer idObra);
}
