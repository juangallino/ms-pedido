package utn.gallino.mspedido.repository;

import frsf.isi.dan.InMemoryRepository;
import utn.gallino.mspedido.domain.DetallePedido;

public class DetalleRepository extends InMemoryRepository<DetallePedido> {
    @Override
    public Integer getId(DetallePedido detallePedido) {
        return detallePedido.getId();
    }

    @Override
    public void setId(DetallePedido detallePedido, Integer integer) {
        detallePedido.setId(integer);
    }
}
