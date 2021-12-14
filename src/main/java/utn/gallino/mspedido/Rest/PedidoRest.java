package utn.gallino.mspedido.Rest;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import utn.gallino.mspedido.Service.PedidoService;
import utn.gallino.mspedido.domain.DetallePedido;
import utn.gallino.mspedido.domain.Pedido;

import javax.swing.table.TableRowSorter;
import java.util.List;

@RestController
@RequestMapping("/api/pedido")
public class PedidoRest {


    @Autowired
    PedidoService pedidoService;





 //   -----------------------------------------------------------------------------------------------------------------//METODOS POST
 @CrossOrigin(maxAge = 86400)
    @PostMapping
    @ApiOperation(value = "Crea un pedido")
    public ResponseEntity<String> crearPedido(@RequestBody Pedido unPedido) {

        Pedido p = new Pedido();

        if(unPedido.getObra()==null) {
            return ResponseEntity.badRequest().body("Debe elegir una obra");
        }
        if(unPedido.getDetalle()==null || unPedido.getDetalle().isEmpty() ) {
            return ResponseEntity.badRequest().body("Debe agregar items al pedido");
        }
        try {
          p =  pedidoService.crearPedido(unPedido);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        }
        return ResponseEntity.status(HttpStatus.CREATED).body(p.getEstado().getEstado());

    }

    @CrossOrigin(maxAge = 86400)
    @PostMapping("/{idPedido}/detalle)")
    @ApiOperation(value = "agregar item a un pedido")
    public ResponseEntity<String> agregarItem(@RequestBody DetallePedido detalle, @PathVariable Integer idPedido) {


        System.out.println("Agregar detalle: " + detalle);

        try {
            pedidoService.agregarDetalle(detalle, idPedido);
        }catch (Exception e){ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());}

        return ResponseEntity.status(HttpStatus.CREATED).body("OK");

    }



    //-----------------------------------------------------------------------------------------------------------------   //METODOS GET
    @CrossOrigin(maxAge = 86400)
    @GetMapping(path = "/{id}")
    @ApiOperation(value = "Busca un Pedido por id")
    public ResponseEntity<Pedido> pedidoPorId(@PathVariable Integer id) {


        try {
            return ResponseEntity.ok(pedidoService.buscarPedidoPorId(id));
        }catch (Exception e){return ResponseEntity.notFound().build();}

    }

    @CrossOrigin(maxAge = 86400)
    @GetMapping(path = "/obra/{idObra}")
    @ApiOperation(value = "Busca un Pedido por id obra")
    public ResponseEntity<Pedido> pedidoPorIdObra(@PathVariable Integer idObra) {


        try {
            return ResponseEntity.ok(pedidoService.buscarPedidoPorIdObra(idObra));
        }catch (Exception e){
            return  ResponseEntity.notFound().build();}



    }

    @CrossOrigin(maxAge = 86400)
    @GetMapping(path = "/obra/listapedidos/{idObra}")
    @ApiOperation(value = "Busca un Pedido por id obra")
    public ResponseEntity<List<Pedido>> pedidosPorIdObra(@PathVariable Integer idObra) {


        try {
            return ResponseEntity.ok(pedidoService.buscarPedidosPorIdObra(idObra));
        }catch (Exception e){
            return  ResponseEntity.notFound().build();}



    }

//todo buscar por id o cuit en una misma llamada, x por separado con diferente path es facil
   /* @GetMapping(path = "/cliente/{id}oCuit")
    @ApiOperation(value = "Busca un Pedido por id o Cuit de Cliente ")
    public ResponseEntity<Pedido> pedidoPorCuitoDni(@PathVariable Integer idObra){

        Optional<Pedido> c =  listaPedidos
                .stream()
                .filter(pedido -> pedido.getObra().getId().equals(idObra))
                .findFirst();
        return ResponseEntity.of(c);
    }*/
@CrossOrigin(maxAge = 86400)
    @GetMapping(path = "/api/pedido/{idPedido}/detalle/{idDetalle}")
    @ApiOperation(value = "Busca un Detalle por id")
    public ResponseEntity<DetallePedido> buscarDetalleXId(@PathVariable Integer idPedido, @PathVariable Integer idDetalle) {



        try {
            return ResponseEntity.ok(pedidoService.buscarDetallePorId(idDetalle,idPedido));

        }catch (Exception e){
            return ResponseEntity.notFound().build();}


    }


    //----------------------------------------------------------------------------------------------------------------- //METODOS PUT
    @CrossOrigin(maxAge = 86400)
    @PutMapping(path = "/{id}")
    @ApiOperation(value = "Actualiza un Pedido")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Actualizado correctamente"),
            @ApiResponse(code = 401, message = "No autorizado"),
            @ApiResponse(code = 403, message = "Prohibido"),
            @ApiResponse(code = 404, message = "El ID no existe")})
    public ResponseEntity<Pedido> actualizar(@RequestBody Pedido pedido, @PathVariable Integer id) {

        try {
            return ResponseEntity.ok(pedidoService.actualizarPedido(pedido, id));
        }catch (Exception e){
        return ResponseEntity.notFound().build();}

}



    //-----------------------------------------------------------------------------------------------------------------//METODOS DELETE

    @CrossOrigin(maxAge = 86400)
    @DeleteMapping(path = "/{id}")
    @ApiOperation(value = "Elimina un Pedido x id")
    public ResponseEntity<String> borrar(@PathVariable Integer id)  {


        try {
            pedidoService.bajaPedido(id);
            String respuesta = "ok "+id;
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(respuesta );

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

    }

    @CrossOrigin(maxAge = 86400)
    @DeleteMapping(path = "/{idPedido}/detalle/{idDetalle}")
    @ApiOperation(value = "Eliminar un detalle de un pedido")
    public ResponseEntity<String> borrarDetalle(@PathVariable Integer idPedido, @PathVariable Integer idDetalle) {


        try {
            pedidoService.eliminarDetalle(idDetalle, idPedido);

            return ResponseEntity.status(HttpStatus.ACCEPTED).body("OK");
        }catch (Exception e){
            return ResponseEntity.notFound().build();}

    }
                                                                    //EXTRAS
    //***************************************************************----------*************************************************************************************


    @CrossOrigin(maxAge = 86400)
    @GetMapping(path = "/obra/clienteactivo/")
    @ApiOperation(value = "Servicio de checko de clietne activo mediante lista de id obras")
    public ResponseEntity<Boolean> checkClienteActivo(@RequestParam(value="listaId_Obras") List<Integer> listaId_Obras) {
        System.out.println("Lista de obras : "+listaId_Obras.toString());

        Boolean respuesta =listaId_Obras.stream().anyMatch(obra->pedidoService.checkPedidoPorIdObra(obra));
        System.out.println(respuesta);
        return ResponseEntity.ok(respuesta);

    }




















}
