package utn.gallino.mspedido.Service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import utn.gallino.mspedido.domain.Producto;
import utn.gallino.mspedido.Service.MaterialService;
import org.springframework.stereotype.Service;
import utn.gallino.mspedido.repository.MaterialRepository;

@Service
public class MaterialServiceImpl implements MaterialService {

	private static final String REST_API_URL = "http://localhost:9001/";
	private static final String STOCK_ENDPOINT = "api/material/";
	RestTemplate rest = new RestTemplate();


	@Autowired
	MaterialRepository materialRepository;
	@Override
	public Integer stockDisponible(Producto m) {
		System.out.println("en checkeo de stock disponible");
		Integer id= m.getId();
		//Integer id= materialRepository.getOne(m.getId()).getId();
		String url = REST_API_URL + STOCK_ENDPOINT +"/stock_disponible/"+ id;
		ResponseEntity<Integer> respuesta = rest.exchange(url, HttpMethod.GET,null, Integer.class);

		//return 1500;
		System.out.println(m.toString() + " stock: "+respuesta.getBody());
		return respuesta.getBody();
	}

}
