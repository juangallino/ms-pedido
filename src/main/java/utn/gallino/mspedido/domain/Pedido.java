package utn.gallino.mspedido.domain;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;



@Entity
public class Pedido {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Integer id;
	@Column(columnDefinition = "TIMESTAMP")
	private Instant fechaPedido;

	@OneToOne
	@JoinColumn(name = "obra_ID")
	private Obra obra;

	@OneToMany()
	@JoinColumn(name = "detalle_pedido_ID")
	private List<DetallePedido> detalle;

	@OneToOne
	@JoinColumn(name = "estado_pedido_ID")
	private EstadoPedido estado;



	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}
	public Instant getFechaPedido() {
		return fechaPedido;
	}
	public void setFechaPedido(Instant fechaPedido) {
		this.fechaPedido = fechaPedido;
	}
	public Obra getObra() {
		return obra;
	}
	public void setObra(Obra obra) {
		this.obra = obra;
	}
	public List<DetallePedido> getDetalle() {
		return detalle;
	}
	public void setDetalle(List<DetallePedido> detalle) {
		this.detalle = detalle;
	}
	public EstadoPedido getEstado() {
		return estado;
	}
	public void setEstado(EstadoPedido estado) {
		this.estado = estado;
	}
	
	
}
