package utn.gallino.mspedido.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import utn.gallino.mspedido.domain.EstadoPedido;

public interface EstadoPedidoRepository extends JpaRepository<EstadoPedido,Integer> {

    public EstadoPedido  getEstadoPedidoByEstado(String estado);
}
