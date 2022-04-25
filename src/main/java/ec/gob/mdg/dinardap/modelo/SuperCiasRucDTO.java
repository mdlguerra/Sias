package ec.gob.mdg.dinardap.modelo;
import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SuperCiasRucDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private boolean conDatos = false;
	private String ruc;
	private String nombre_compania;
	private String expediente;
	private String fecha_constitucion;
	private String pais_origen;
	private String canton_legal;
	private String provincia_legal;
	private String calle_postal;
	private String numero_calle_postal;
	private String interseccion;
	private String edificio;
	private String provincia_postal;
	private String canton_postal;
	private String ciudad_postal;
	private String telefono;
	private String email;
	private String tipo_compania;
	private String estado_legal;
	private String celular;
	private String email2;
	private String sitio_web;
	private String ubicacion_referencia;

	
	
	
	
	

	

}
