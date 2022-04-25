package ec.gob.mdg.controller;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.mindrot.jbcrypt.BCrypt;

import ec.gob.mdg.sias.ejb.modelo.Usuario;
import ec.gob.mdg.sias.ejb.service.IUsuarioService;
import ec.gob.mdg.sias.ejb.utils.UtilsDate;



@Named
@ViewScoped
public class UsuarioModificaBean implements Serializable {

	private static final long serialVersionUID = -6912327508466146927L;

	@Inject
	private IUsuarioService serviceUsuario;

	Usuario usuario;

	boolean estadeshabilitado;

	@PostConstruct
	public void init() {
		mostrar();
	}

	// MOSTRAR INFORMACION DEL USUARIO
	public void mostrar() {
		usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
		System.out.println("mostrar .... imprime usuario:  " + usuario.getNombre());
	}

	// registrar usuario
	public void modificar() {
		try {
			String clave = this.usuario.getPassword();
			boolean respuesta = validaClave(clave);
		
			if (respuesta == true) {
				String claveHash = BCrypt.hashpw(clave, BCrypt.gensalt());
				usuario.setPassword(claveHash);
				Date fechaActual = UtilsDate.fechaActual();
				usuario.setFechaCambioClave(fechaActual);
				usuario.setFechaReinicioClave(null);
				this.serviceUsuario.modificar(usuario);
				estadeshabilitado = true;
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Se cambio la clave con éxito", "Aviso"));
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/// VALIDAR CLAVE
	public boolean validaClave(String clave) {
		boolean respuesta = false;
		String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=.])(?=\\S+$).{8,12}";
		respuesta = clave.matches(pattern);

		if (respuesta == false) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,
					"La clave debe contener mínimo 8 y máximo 12 caracteres, "
							+ "al menos una letra mayúscula, al menos una letra minúscula, al menos un caracter especial",
					"ERROR"));
		}
		return respuesta;
	}

	// NUEVO REGISTR0
	public Boolean nuevo() {
		estadeshabilitado = false;
		return estadeshabilitado;
	}

	// GETTERS Y SETTERS
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public boolean isEstadeshabilitado() {
		return estadeshabilitado;
	}

	public void setEstadeshabilitado(boolean estadeshabilitado) {
		this.estadeshabilitado = estadeshabilitado;
	}

}