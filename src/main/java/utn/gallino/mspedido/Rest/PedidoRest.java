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

@RestController
@RequestMapping("/api/pedido")
public class PedidoRest {


    @Autowired
    PedidoService pedidoService;





 //   -----------------------------------------------------------------------------------------------------------------//METODOS POST
    @PostMapping
    @ApiOperation(value = "Crea un pedido")
    public ResponseEntity<String> crearPedido(@RequestBody Pedido unPedido) {

        System.out.println(" crear Pedido " + unPedido);


        if(unPedido.getObra()==null) {
            return ResponseEntity.badRequest().body("Debe elegir una obra");
        }
        if(unPedido.getDetalle()==null || unPedido.getDetalle().isEmpty() ) {
            return ResponseEntity.badRequest().body("Debe agregar items al pedido");
        }
        try {
            pedidoService.crearPedido(unPedido);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        }
        return ResponseEntity.status(HttpStatus.CREATED).body("OK");

    }


    @PostMapping("/{idPedido}/detalle)")
    @ApiOperation(value = "agregar item a un pedido")
    public ResponseEntity<String> agregarItem(@RequestBody DetallePedido detalle, @PathVariable Integer idPedido) {


        System.out.println("Agregar detalle: " + detalle);

        try {
            pedidoService.agregarDetalle(detalle, idPedido);
        }catch (Exception e){ResponseEntity.notFound().build();}

        return ResponseEntity.status(HttpStatus.CREATED).body("OK");

    }



    //-----------------------------------------------------------------------------------------------------------------   //METODOS GET

    @GetMapping(path = "/{id}")
    @ApiOperation(value = "Busca un Pedido por id")
    public ResponseEntity<Pedido> pedidoPorId(@PathVariable Integer id) {


        try {
            return ResponseEntity.ok(pedidoService.buscarPedidoPorId(id));
        }catch (Exception e){return ResponseEntity.notFound().build();}

    }


    @GetMapping(path = "/obra/{idObra}")
    @ApiOperation(value = "Busca un Pedido por id obra")
    public ResponseEntity<Pedido> pedidoPorIdObra(@PathVariable Integer idObra) {


        try {
            return ResponseEntity.ok(pedidoService.buscarPedidoPorIdObra(idObra));
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

    @GetMapping(path = "/api/pedido/{idPedido}/detalle/{idDetalle}")
    @ApiOperation(value = "Busca un Detalle por id")
    public ResponseEntity<DetallePedido> buscarDetalleXId(@PathVariable Integer idPedido, @PathVariable Integer idDetalle) {

      /*  Optional<Pedido> c = listaPedidos
                .stream()
                .filter(unPed -> unPed.getId().equals(idPedido))
                .findFirst();
        if (c.isPresent()) {
            Optional<DetallePedido> detalle = c.get().getDetalle()
                    .stream()
                    .filter(unDetPed -> unDetPed.getId().equals(idDetalle))
                    .findFirst();
            if (detalle.isPresent()) {
                return ResponseEntity.of(detalle);
            } else {
                return ResponseEntity.badRequest().build();
            }
        } else {
            return ResponseEntity.badRequest().build();
        }*/

        try {
            return ResponseEntity.ok(pedidoService.buscarDetallePorId(idDetalle,idPedido));

        }catch (Exception e){
            return ResponseEntity.notFound().build();}


    }


    //----------------------------------------------------------------------------------------------------------------- //METODOS PUT

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


    @DeleteMapping(path = "/{idPedido}/detalle/{idDetalle}")
    @ApiOperation(value = "Eliminar un detalle de un pedido")
    public ResponseEntity<String> borrarDetalle(@PathVariable Integer idPedido, @PathVariable Integer idDetalle) {

        /*OptionalInt indexOpt = IntStream.range(0, listaPedidos.size())
                .filter(i -> listaPedidos.get(i).getId().equals(idPedido))
                .findFirst();

        if (indexOpt.isPresent()) {

            Pedido pedidoActualizado = listaPedidos.get(indexOpt.getAsInt());
            OptionalInt dpIndex = IntStream.range(0, pedidoActualizado.getDetalle().size())
                    .filter(i -> pedidoActualizado.getDetalle().get(i).getId().equals(idDetalle))
                    .findFirst();
            if (dpIndex.isPresent()) {
                pedidoActualizado.getDetalle().remove(dpIndex.getAsInt());
                listaPedidos.set(indexOpt.getAsInt(), pedidoActualizado);//actualizamos el pedido en la lista, sino supongo que habria q hacer un put (?
                return ResponseEntity.ok().body("Item eliminado correctamente");

            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }*/

        try {
            pedidoService.eliminarDetalle(idDetalle, idPedido);

            return ResponseEntity.status(HttpStatus.ACCEPTED).body("OK");
        }catch (Exception e){
            return ResponseEntity.notFound().build();}

    }
}
