package ec.gob.mdg.controller;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ec.gob.mdg.sias.ejb.modelo.Usuario;
import ec.gob.mdg.sias.ejb.modelo.UsuarioArea;
import ec.gob.mdg.sias.ejb.operaciones.OperacionesConUsuario;
import ec.gob.mdg.sias.ejb.utils.UtilsDate;
import lombok.Data;

@Data
@Named
@ViewScoped
public class PrincipalBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private OperacionesConUsuario serviceOpUsuarios;

	private UsuarioArea usuArea = new UsuarioArea();

	Usuario p = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
	Date fechaActual;

	@PostConstruct
	public void init() {

		try {
            System.out.println("1. entra en principal ...." + p.getUsername());
			fechaActual = UtilsDate.fechaActual();
			usuArea = serviceOpUsuarios.listarUsuarioAreaPorIdUsuarioLogueado(p);
			 System.out.println(". usuArea...." + usuArea.getUsuario().getNombre());			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	
}
