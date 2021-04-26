package utn.gallino.mspedido.repository;


import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import utn.gallino.mspedido.domain.DetallePedido;

import java.util.List;
import java.util.Optional;

public interface DetalleRepository extends JpaRepository<DetallePedido, Integer> {

}