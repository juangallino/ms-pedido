package utn.gallino.mspedido.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utn.gallino.mspedido.domain.Obra;
import utn.gallino.mspedido.domain.Producto;

import java.util.List;

@Repository
public interface ObraRepo extends JpaRepository<Obra, Integer> {


                    Obra findObraByDescripcionEquals(String descripcion);
                    List<Obra> findObrasByDescripcionStartsWith(String str);
    }
