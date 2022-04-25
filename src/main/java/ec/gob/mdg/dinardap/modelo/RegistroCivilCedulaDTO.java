package ec.gob.mdg.dinardap.modelo;

import java.io.Serializable;

import lombok.Data;

import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RegistroCivilCedulaDTO implements Serializable{

		private static final long serialVersionUID = 1L;
		
		private boolean conDatos = false;
		private String cedula;
		private String nombre;
		private String genero;
		private String condicionCiudadano;
		private String fechaNacimiento;
		private String lugarNacimiento;
		private String nacionalidad;
	    private String estadoCivil;
	    private String individualDactilar;
	    private String conyuge;
	    private String nombrePadre;
	    private String nacionalidadPadre;
	    private String nombreMadre;
	    private String nacionalidadMadre;
	    private String domicilio;
	    private String callesDomicilio;
	    private String numeroCasa;
	    private String fechaMatrimonio;
        private String lugarMatrimonio;
        private String fechaDefuncion;
        private String fechaInscripcionDefuncion;
        private String actaDefuncion;
        private String actaMatrimonio;
        private String anioInscripcionNacimiento;
        private String fechaExpedicion;
        private String profesion;
        private String fechaExpiracion;
	    private String sexo;
	    private String fotografia;
	    private String firma;
	    
	    
	    
}
