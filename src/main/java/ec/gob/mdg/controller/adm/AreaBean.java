package ec.gob.mdg.controller.adm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ec.gob.mdg.sias.ejb.modelo.Area;
import ec.gob.mdg.sias.ejb.modelo.Canton;
import ec.gob.mdg.sias.ejb.modelo.Institucion;
import ec.gob.mdg.sias.ejb.modelo.Provincia;
import ec.gob.mdg.sias.ejb.modelo.TipoDetalle;
import ec.gob.mdg.sias.ejb.service.IAreaService;
import ec.gob.mdg.sias.ejb.service.ICantonService;
import ec.gob.mdg.sias.ejb.service.IInstitucionService;
import ec.gob.mdg.sias.ejb.service.IProvinciaService;
import ec.gob.mdg.sias.ejb.service.ITipoDetalleService;
//import lombok.Data;

@Named
@ViewScoped
public class AreaBean implements Serializable {

	private static final long serialVersionUID = -1880117470518909108L;

	@Inject
	private IAreaService serviceArea;

	@Inject
	private IInstitucionService serviceInstitucion;

	@Inject
	private IProvinciaService serviceProvincia;

	@Inject
	private ICantonService serviceCanton;

	@Inject
	private ITipoDetalleService serviceTipoArea;

	private List<Institucion> listaIntitucion;
	private List<Provincia> listaProvincia;
	private List<Canton> listaCanton;
	private List<Area> listaArea;
	private List<TipoDetalle> listaTipoArea = new ArrayList<TipoDetalle>();

	private String tipoDialog;
	private String tipoArea;
	private Area area;

	@PostConstruct
	public void init() {
		this.area = new Area();
		this.tipoDialog = "Nuevo";
		listarInstitucion();
		listarProvincia();
		listarCanton();
		listarTipoArea();
		listarArea();
	}

	public void operar(String accion) {
		try {
			if (accion.equalsIgnoreCase("R")) {
				System.out.println("Entra en R");
				area.setNombre(area.getNombre().toUpperCase());
				area.setNombreCorto(area.getNombreCorto().toUpperCase());
				area.setDireccion(area.getDireccion().toUpperCase());
				area.setSiglas(area.getSiglas().toUpperCase());

				this.serviceArea.registrar(this.area);
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Se grabó con éxito", "Satisfactoriamente"));
			} else if (accion.equalsIgnoreCase("M")) {
				System.out.println("Entra en M");
				area.setNombre(area.getNombre().toUpperCase());
				area.setNombreCorto(area.getNombreCorto().toUpperCase());
				area.setDireccion(area.getDireccion().toUpperCase());
				area.setSiglas(area.getSiglas().toUpperCase());

				this.serviceArea.modificar(this.area);
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Se Modificó éxito", "Satisfactoriamente"));
			}
			this.listarArea();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void mostrarData(Area i) {
		this.area = i;
		this.tipoDialog = "Modificar Area";
	}

	public void limpiarControles() {
		this.area = new Area();
		this.tipoDialog = "Nuevo Area";
	}

	public void listarInstitucion() {
		try {
			this.listaIntitucion = serviceInstitucion.listar();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void listarProvincia() {
		try {
			listaProvincia = serviceProvincia.listar();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void listarCanton() {
		try {
			listaCanton = serviceCanton.listar();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void listarTipoArea() {
		try {
			listaTipoArea = serviceTipoArea.listaTipoDetallePorIdTipo(4);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void listarArea() {
		try {
			listaArea = serviceArea.listar();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String tipoArea(Integer id) {
		tipoArea = serviceTipoArea.traeDescripcion(id).getNombre();
		return tipoArea;
	}

//	getters & setters

	public List<Institucion> getListaIntitucion() {
		return listaIntitucion;
	}

	public void setListaIntitucion(List<Institucion> listaIntitucion) {
		this.listaIntitucion = listaIntitucion;
	}

	public List<Provincia> getListaProvincia() {
		return listaProvincia;
	}

	public void setListaProvincia(List<Provincia> listaProvincia) {
		this.listaProvincia = listaProvincia;
	}

	public List<Canton> getListaCanton() {
		return listaCanton;
	}

	public void setListaCanton(List<Canton> listaCanton) {
		this.listaCanton = listaCanton;
	}

	public List<Area> getListaArea() {
		return listaArea;
	}

	public void setListaArea(List<Area> listaArea) {
		this.listaArea = listaArea;
	}

	public List<TipoDetalle> getListaTipoArea() {
		return listaTipoArea;
	}

	public void setListaTipoArea(List<TipoDetalle> listaTipoArea) {
		this.listaTipoArea = listaTipoArea;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public String getTipoDialog() {
		return tipoDialog;
	}

	public void setTipoDialog(String tipoDialog) {
		this.tipoDialog = tipoDialog;
	}

//	public List<TipoDetalle> getlistaTipoArea() {
//		return listaTipoArea= serviceTipoArea.listaTipoDetallePorIdTipo(4);
//	}
//	

}
