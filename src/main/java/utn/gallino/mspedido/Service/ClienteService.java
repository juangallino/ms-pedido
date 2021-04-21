package utn.gallino.mspedido.Service;

import utn.gallino.mspedido.domain.Obra;

public interface ClienteService {

	public Double deudaCliente(Obra id);
	public Double maximoSaldoNegativo(Obra id);			//maxCuentaCorriente
	public Integer situacionCrediticiaBCRA(Obra id);

}
