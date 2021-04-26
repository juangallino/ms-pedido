package utn.gallino.mspedido;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.jdbc.Sql;
import utn.gallino.mspedido.domain.Obra;
import utn.gallino.mspedido.repository.ObraRepo;
import utn.gallino.mspedido.repository.PedidoRepository;

import java.util.List;


@SpringBootTest
@Profile({"testing"})
 class PedidoRepoTest {


   @Autowired
   ObraRepo obraRepo;


    @Test
    @Sql({"/obras.sql"})
    void test(){

       Long cantidad =obraRepo.count();
       Obra o =obraRepo.findObraByDescripcionEquals("POZO");
       System.out.println(o.toString());
        assertEquals(o.getId(),1);

    }

    @Test
   // @Sql({"/obras.sql"})
    void test2(){

        List<Obra> obras =obraRepo.findObrasByDescripcionStartsWith("PUE");
        boolean result = true;
        System.out.println(obras.toString());
        assertEquals(obras.size(),2L);

    }

}
