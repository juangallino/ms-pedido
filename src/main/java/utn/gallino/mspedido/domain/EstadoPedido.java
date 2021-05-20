package utn.gallino.mspedido.domain;

import javax.persistence.*;

@Entity
@Table(name = "PED_ESTADO_PEDIDO", schema = "MS-PED")
public class EstadoPedido {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Integer id;

	@Column
	private String estado;

	public EstadoPedido() {

	}

	public EstadoPedido(Integer id, String estado) {
		super();
		this.id = id;
		this.estado = estado;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	
}
