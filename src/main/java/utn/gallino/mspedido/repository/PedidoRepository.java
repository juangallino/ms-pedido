package utn.gallino.mspedido.repository;


import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utn.gallino.mspedido.domain.DetallePedido;
import utn.gallino.mspedido.domain.Pedido;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

public Pedido findPedidoByObra_Id(Integer Id);
}
