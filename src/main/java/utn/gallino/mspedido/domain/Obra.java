package utn.gallino.mspedido.domain;

import javax.persistence.*;

@Entity
@Table(name = "USR_OBRA", schema = "MS-USR")
public class Obra {

	@Id
	private Integer id;
	@Column
	private String descripcion;



	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Obra{" +
				"id=" + id +
				", descripcion='" + descripcion + '\'' +
				'}';
	}
}
