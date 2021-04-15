package utn.gallino.mspedido.Rest;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.gallino.mspedido.domain.DetallePedido;
import utn.gallino.mspedido.domain.Pedido;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/pedido")
public class PedidoRest {

    //aca iria autowired la interface implementada por el service correspondiente al rest controller

    private static final List<Pedido> listaPedidos = new ArrayList<>();
    private static Integer ID_GEN_PEDIDO = 1;
    private static Integer ID_GEN_DETALLE = 1;


    //METODOS POST
    @PostMapping
    @ApiOperation(value = "Crea un pedido")
    public ResponseEntity<Pedido> crearPedido(@RequestBody Pedido unPedido){

        System.out.println(" crear Pedido "+ unPedido);
        unPedido.setId(ID_GEN_PEDIDO++);
        listaPedidos.add(unPedido);
        return ResponseEntity.ok(unPedido);
        /*
        if(unPedido.getObra()==null) {
            return ResponseEntity.badRequest().body("Debe elegir una obra");
        }
        if(unPedido.getDetalle()==null || unPedido.getDetalle().isEmpty() ) {
            return ResponseEntity.badRequest().body("Debe agregar items al pedido");
        }
        //pedidoSrv.crearPedido(unPedido);
        return ResponseEntity.status(HttpStatus.CREATED).body("OK");
*/
    }


    @PostMapping("/{idPedido}/detalle)")
    @ApiOperation(value = "agregar item a un pedido")
    public ResponseEntity<String> agregarItem(@RequestBody DetallePedido detalle, @RequestParam(name="idPedido") Integer idPedido){
      //todo


        System.out.println("Agregar detalle: "+detalle);
        detalle.setId(ID_GEN_DETALLE++);

      /*  Optional<Pedido> pd = listaPedidos.stream()
                .filter(unPed->unPed.getId().equals(idPedido))
                .findFirst();
        pd.get().getDetalle().add(detalle);*/
        //obtenemos el indice en qu se encuentra el pedido en la lista(dps lo vamos a envesitar, por eso no usala opcin de abajo)

        OptionalInt indexOpt =   IntStream.range(0, listaPedidos.size())
                .filter(i -> listaPedidos.get(i).getId().equals(idPedido))
                .findFirst();


        if (indexOpt.isPresent()){
            Pedido pedidoActualizado = listaPedidos.get(indexOpt.getAsInt());
            pedidoActualizado.getDetalle().add(detalle);
            listaPedidos.set(indexOpt.getAsInt(),pedidoActualizado);//actualizamos el pedido en la lista, sino supongo que abria q hacer un put (?
            return ResponseEntity.accepted().body("Item agregado correctamente");
        } else {//Fail
            return ResponseEntity.notFound().build();
         // return ResponseEntity.badRequest().body("Algo malo ha ocurrido");
        }


    };


    //METODOS GET

    @GetMapping(path = "/{id}")
    @ApiOperation(value = "Busca un Pedido por id")
    public ResponseEntity<Pedido> pedidoPorId(@PathVariable Integer id){

        Optional<Pedido> c =  listaPedidos
                .stream()
                .filter(pedido -> pedido.getId().equals(id))
                .findFirst();
        return ResponseEntity.of(c);
    }


    @GetMapping(path = "/obra/{id}")
    @ApiOperation(value = "Busca un Pedido por id obra")
    public ResponseEntity<Pedido> pedidoPorIdObra(@PathVariable Integer idObra){

        Optional<Pedido> c =  listaPedidos
                .stream()
                .filter(pedido -> pedido.getObra().getId().equals(idObra))
                .findFirst();
        return ResponseEntity.of(c);
    }

//todo buscar por id o cuit en una misma llamada, x por separado con diferente path es facil
    @GetMapping(path = "/cliente/{id}oCuit")
    @ApiOperation(value = "Busca un Pedido por id o Cuit de Cliente ")
    public ResponseEntity<Pedido> pedidoPorCuitoDni(@PathVariable Integer idObra){

        Optional<Pedido> c =  listaPedidos
                .stream()
                .filter(pedido -> pedido.getObra().getId().equals(idObra))
                .findFirst();
        return ResponseEntity.of(c);
    }

    @GetMapping(path = "/api/pedido/{idPedido}/detalle/{idDetalle}")
    @ApiOperation(value = "Busca un Pedido por id o Cuit de Cliente ")
    public ResponseEntity<DetallePedido> buscarDetalleXId(@PathVariable Integer idPedido, @PathVariable Integer idDetalle){

        Optional<Pedido> c =  listaPedidos
                .stream()
                .filter(unPed -> unPed.getId().equals(idPedido))
                .findFirst();

        Optional<DetallePedido> detalle = c.get().getDetalle()
                .stream()
                .filter(unDetPed-> unDetPed.getId().equals(idDetalle))
                .findFirst();
        return ResponseEntity.of(detalle);
    }


    //METODOS PUT

    @PutMapping(path = "/{id}")
    @ApiOperation(value = "Actualiza un Pedido")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Actualizado correctamente"),
            @ApiResponse(code = 401, message = "No autorizado"),
            @ApiResponse(code = 403, message = "Prohibido"),
            @ApiResponse(code = 404, message = "El ID no existe")})
    public ResponseEntity<Pedido> actualizar(@RequestBody Pedido pedido,  @PathVariable Integer id){
        OptionalInt indexOpt =   IntStream.range(0, listaPedidos.size())
                .filter(i -> listaPedidos.get(i).getId().equals(id))
                .findFirst();

        if(indexOpt.isPresent()){
            listaPedidos.set(indexOpt.getAsInt(), pedido);
            return ResponseEntity.ok(pedido);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    //METODOS DELETE


    @DeleteMapping(path = "/{id}")
    @ApiOperation(value = "Elimina un Pedido x id")
    public ResponseEntity<String> borrar(@PathVariable Integer id){

        OptionalInt indexOpt =   IntStream.range(0, listaPedidos.size())            //Busco el index en la list
                .filter(i -> listaPedidos.get(i).getId().equals(id))
                .findFirst();

        if(indexOpt.isPresent()){                                                   //si estaba lo elimino
            listaPedidos.remove(indexOpt.getAsInt());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    @DeleteMapping(path = "/{idPedido}/detalle/{idDetalle}")
    @ApiOperation(value = "Eliminar un detalle de un pedido")
    public ResponseEntity<String> borrarDetalle(@PathVariable Integer idPedido, @PathVariable Integer idDetalle){

        OptionalInt indexOpt =   IntStream.range(0, listaPedidos.size())
                .filter(i -> listaPedidos.get(i).getId().equals(idPedido))
                .findFirst();

        if(indexOpt.isPresent()){

                    Pedido pedidoActualizado = listaPedidos.get(indexOpt.getAsInt());
                    OptionalInt dpIndex =   IntStream.range(0, pedidoActualizado.getDetalle().size())
                        .filter(i -> pedidoActualizado.getDetalle().get(i).getId().equals(idDetalle))
                        .findFirst();

                    pedidoActualizado.getDetalle().remove(dpIndex.getAsInt());
                    listaPedidos.set(indexOpt.getAsInt(),pedidoActualizado);//actualizamos el pedido en la lista, sino supongo que habria q hacer un put (?
                    return ResponseEntity.ok().body("Item eliminado correctamente");

        } else {
            return ResponseEntity.notFound().build();
        }
    }









}
