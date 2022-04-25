package ec.gob.mdg.dinardap.modelo;

import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AntPlacaDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private boolean conDatos = false;
	private String placa;
	private String anioMatriculado;
	private String chasis;
	private String cilindraje;
	private String color;
	private String color2;
	private String docPropietario;
	private String fechaCaducidad;
	private String modelo;
	private String motor;
	private String pasajeros;
	private String propietario;
	private String tipoIdent;
	private String tipoVehiculo;
	private String tipoServicio;

	
	
}
