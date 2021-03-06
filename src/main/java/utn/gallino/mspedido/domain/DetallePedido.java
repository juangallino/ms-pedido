package utn.gallino.mspedido.domain;


import javax.persistence.Entity;
import javax.persistence.*;
import javax.persistence.Table;

@Entity
@Table(name = "PED_DETALLE_PEDIDO", schema = "MS-PED")
public class DetallePedido {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne//(cascade = CascadeType.ALL)
	@JoinColumn(name = "ID_MATERIAL")
	private Producto producto;

	@Column
	private Integer cantidad;

	@Column(name = "PRECIO_DP")
	private Double precio;



	public DetallePedido(){
	}
	
	public DetallePedido(Producto producto, Integer cantidad, Double precio) {
		super();
		this.producto = producto;
		this.cantidad = cantidad;
		this.precio = precio;
	}


	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Producto getProducto() {
		return producto;
	}
	public void setProducto(Producto producto) {
		this.producto = producto;
	}
	public Integer getCantidad() {
		return cantidad;
	}
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	public Double getPrecio() {
		return precio;
	}
	public void setPrecio(Double precio) {
		this.precio = precio;
	}


}
