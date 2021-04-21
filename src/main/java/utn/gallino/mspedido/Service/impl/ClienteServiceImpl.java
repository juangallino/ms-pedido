package utn.gallino.mspedido.Service.impl;

import  utn.gallino.mspedido.domain.Obra;
import  utn.gallino.mspedido.Service.ClienteService;
import org.springframework.stereotype.Service;

@Service
public class ClienteServiceImpl implements ClienteService {

	@Override
	public Double deudaCliente(Obra id) {
		// TODO Auto-generated method stub 				 //El fin es llegar al cliente//Esto sera una consulta http al rest de user con el id de la obra para averiguar quien es su cliente
		return null;
	}

	@Override
	public Integer situacionCrediticiaBCRA(Obra id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double maximoSaldoNegativo(Obra id) {
		// TODO Auto-generated method stub
		return null;
	}

}
