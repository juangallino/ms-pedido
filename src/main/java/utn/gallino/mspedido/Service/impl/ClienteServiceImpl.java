package utn.gallino.mspedido.Service.impl;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import  utn.gallino.mspedido.domain.Obra;
import  utn.gallino.mspedido.Service.ClienteService;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ClienteServiceImpl implements ClienteService {


	private  String USR_REST_API_URL = "http://dan-gateway:8080/";
	private static final String OBRA_ENDPOINT = "usuarios/api/obra/";
		//api rest pagos
	private static final String PAGOS_REST_API_URL = "http://localhost:9003";
	private static final String OBRA_ENDPOINnT = "/api/obra/";
	RestTemplate rest = new RestTemplate();



	@Override
	public Double deudaCliente(Obra id) {
		System.out.println("metodo deuda cliente con obra id= "+ id);
		//ponele q seria la sumatoria de lo pagos - la sumatoria de los pedidos,seria meido burrada



		// TODO Auto-generated method stub 				 //El fin es llegar al cliente//Esto sera una consulta http al rest de user con el id de la obra para averiguar quien es su cliente
		return 0.0;
	}

	@Override
	public Integer situacionCrediticiaBCRA(Obra id) {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public Double maximoSaldoNegativo(Obra obra) {

		Integer id= obra.getId();
		System.out.println("maximo saldo negativo de cliente por obra id="+id);
		String url = USR_REST_API_URL + OBRA_ENDPOINT +"/pedidos/cliente/"+ id;
		ResponseEntity<Double> respuesta = rest.exchange(url, HttpMethod.GET,null, Double.class);
		System.out.println(respuesta.getBody());

		return respuesta.getBody();


	}


}
