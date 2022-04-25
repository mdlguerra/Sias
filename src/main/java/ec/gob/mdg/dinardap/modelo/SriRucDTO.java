package ec.gob.mdg.dinardap.modelo;

import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SriRucDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private boolean conDatos = false;
	private String numeroRuc;
	private String razonSocial;
	private String nombreFantasiaComercial;
	private String actividadEconomicaPrincipal;
	private String fechaInicioActividades;
	private String tipoContribuyente;
	private String estadoContribuyente;
	private String actividadEstablecimiento;
	private String claseContribuyente;
	
	

}
