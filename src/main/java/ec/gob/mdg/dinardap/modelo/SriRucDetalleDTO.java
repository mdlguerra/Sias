package ec.gob.mdg.dinardap.modelo;

import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SriRucDetalleDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private boolean conDatos = false;
	private String numeroRuc;
	private String numeroEstablecimiento;
	private String nombreFantasiaComercial;
	private String estadoEstablecimiento;
	private String calle;
	private String interseccion;
	private String numero;
	private String tipoEstablecimiento;
	

	
	

}
