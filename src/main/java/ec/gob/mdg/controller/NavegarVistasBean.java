package ec.gob.mdg.controller;

import java.io.Serializable;

import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class NavegarVistasBean implements Serializable {

	private static final long serialVersionUID = 1L;

	public NavegarVistasBean() {

	}

	//// DIRECCIONAR A LA PAGINA PRINCIPAL

	public void principal() {
		try {

			FacesContext.getCurrentInstance().getExternalContext().redirect("./../principal.xhtml");
		} catch (Exception e) {
			System.out.println("FALLO LA REDIRECCION A UN NUEVO FORMULARIO");
			e.printStackTrace();
		}
	}

	

	//// DIRECCIONAR A LA PAGINA INDEXX

	public void index() {
		try {

			FacesContext.getCurrentInstance().getExternalContext().redirect("./../index.xhtml");
		} catch (Exception e) {
			System.out.println("FALLO LA REDIRECCION A UN NUEVO FORMULARIO");
			e.printStackTrace();
		}
	}


	
	
	////// DIRECCIONAR USUARIOS
	// REGISTRAR
	public void usuarioregistrar() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("./../protegido/usuarioregistro.xhtml");
		} catch (Exception e) {
			System.out.println("FALLO LA REDIRECCION A UN NUEVO FORMULARIO");
			e.printStackTrace();
		}
	}

	// REGRESAR
	public void usuarioconsultar() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("./../protegido/usuarioconsulta.xhtml");
		} catch (Exception e) {
			System.out.println("FALLO LA REDIRECCION A UN NUEVO FORMULARIO");
			e.printStackTrace();
		}
	}

	// DIRECCIONAR A USUARIO PUNTO
	// REGISTRAR
	public void usuariopuntoregistro(String id) {
		try {
			FacesContext.getCurrentInstance().getExternalContext().getFlash().put("param", id);
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect("./../protegido/usuariopuntoregistro.xhtml");
		} catch (Exception e) {
			System.out.println("FALLO LA REDIRECCION A UN NUEVO FORMULARIO");
			e.printStackTrace();
		}
	}

	// CONSULTAR
	public void usuariopuntoconsulta(String id) {
		try {

//				System.out.println("ID NAVEGAR CONSULTAR " + id);
			FacesContext.getCurrentInstance().getExternalContext().getFlash().put("param", id);
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect("./../protegido/usuariopuntoconsulta.xhtml");
		} catch (Exception e) {
			System.out.println("FALLO LA REDIRECCION A UN NUEVO FORMULARIO");
			e.printStackTrace();
		}
	}

	// IR A ASIGNAR MENU
	public void irDetalleRol(String idRol) {
		try {
			FacesContext.getCurrentInstance().getExternalContext().getFlash().put("idRol", idRol);
			FacesContext.getCurrentInstance().getExternalContext().redirect("./../protegido/rolesasignamenu.xhtml");
		} catch (Exception e) {
			System.out.println("FALLO LA REDIRECCION A UN NUEVO FORMULARIO");
			e.printStackTrace();
		}
	}
	
	
	/// IR  DETALLE  USUARIO ROL
	public String irDetalleUsuarioRol(String idUsuario) {
		final FacesContext context = FacesContext.getCurrentInstance();
		final Flash flash = context.getExternalContext().getFlash();
		flash.put("idUsuario", idUsuario);
		return "usuarioasignaroles?faces-redirect=true";
	}


}
