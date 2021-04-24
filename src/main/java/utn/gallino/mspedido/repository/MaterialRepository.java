package utn.gallino.mspedido.repository;

import frsf.isi.dan.InMemoryRepository;
import utn.gallino.mspedido.domain.Producto;

public class MaterialRepository extends InMemoryRepository<Producto> {
    @Override
    public Integer getId(Producto producto) {
        return null;
    }

    @Override
    public void setId(Producto producto, Integer integer) {

    }
}
