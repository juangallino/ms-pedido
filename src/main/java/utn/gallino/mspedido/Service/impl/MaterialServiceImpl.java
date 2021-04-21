package utn.gallino.mspedido.Service.impl;

import utn.gallino.mspedido.domain.Producto;
import utn.gallino.mspedido.Service.MaterialService;
import org.springframework.stereotype.Service;

@Service
public class MaterialServiceImpl implements MaterialService {

	@Override
	public Integer stockDisponible(Producto m) {
		// TODO Auto-generated method stub
		//consulta a la database con un numero de retorno
		return null;
	}

}
