package ec.gob.mdg.dinardap.modelo;

import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AntLicenciaDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private boolean conDatos = false;
	private String identifica;
	private String apellidos;
	private String nombres;
	private String caducidad;
	private String correo;
	private String direccion;
	private String fechaPrimeraVez;
	private String infracciones;
	private String puntos;
	private String restriccion;
	private String telefono;
	private String tipoLicen;
	
}
