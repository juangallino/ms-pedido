package utn.gallino.mspedido.domain;

import javax.persistence.*;

@Entity
@Table(name = "STK_MATERIAL", schema = "MS-STK")
public class Producto {

	@Id
	private Integer id;

	@Column
	private String descripcion;

	@Column
	private Double precio;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Double getPrecio() {
		return precio;
	}
	public void setPrecio(Double precio) {
		this.precio = precio;
	}
	
	
}
