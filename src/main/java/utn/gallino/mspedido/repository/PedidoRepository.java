package utn.gallino.mspedido.repository;

import frsf.isi.dan.InMemoryRepository;
import utn.gallino.mspedido.domain.Pedido;

public class PedidoRepository extends InMemoryRepository<Pedido> {
    @Override
    public Integer getId(Pedido pedido) {
       return pedido.getId();
    }

    @Override
    public void setId(Pedido pedido, Integer integer) {
        pedido.setId(integer);
    }
}
