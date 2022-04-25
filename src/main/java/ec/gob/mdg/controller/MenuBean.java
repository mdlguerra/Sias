package ec.gob.mdg.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

import ec.gob.mdg.sias.ejb.dao.IMenuDAO;
import ec.gob.mdg.sias.ejb.modelo.Menu;
import ec.gob.mdg.sias.ejb.modelo.Usuario;
import ec.gob.mdg.sias.ejb.modelo.UsuarioRol;
import ec.gob.mdg.sias.ejb.operaciones.OperacionesConUsuario;
import lombok.Data;

@Data
@Named
@SessionScoped
public class MenuBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@EJB
	private IMenuDAO MenuDao;

	@Inject
	private OperacionesConUsuario op;

	Usuario us = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");

	private List<Menu> listaMenu;
	private MenuModel model;

	private List<UsuarioRol> listaUsuarioRol;

	@PostConstruct
	public void init() {

		this.listarMenus();
		model = new DefaultMenuModel();
		this.establecerPermisos();
	}

	public void listarMenus() {
		try {
			listaMenu = op.listaMenuRolporUsuario(us);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void establecerPermisos() {

		for (Menu m : listaMenu) {
			if (m.getTipo().equals("S")) {
				DefaultSubMenu firstSubmenu = new DefaultSubMenu(m.getNombre());

				for (Menu i : listaMenu) {
					Menu submenu = i.getSubmenu();
					if (submenu != null) {
						if (submenu.getId() == m.getId()) {
							DefaultMenuItem item = new DefaultMenuItem(i.getNombre());
							item.setUrl(i.getLink());
							firstSubmenu.addElement(item);
						}
					}
				}
				model.addElement(firstSubmenu);
			} else {
				if (m.getSubmenu() == null) {
					DefaultMenuItem item = new DefaultMenuItem(m.getNombre());
					item.setUrl(m.getLink());
					model.addElement(item);
				}
			}

		}
	}

}
