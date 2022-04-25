package ec.gob.mdg.controller;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import ec.gob.mdg.sias.ejb.modelo.TipoDetalle;
import ec.gob.mdg.sias.ejb.modelo.Usuario;
import ec.gob.mdg.sias.ejb.modelo.UsuarioSesiones;
import ec.gob.mdg.sias.ejb.operaciones.OperacionesConUsuario;
import ec.gob.mdg.sias.ejb.service.ITipoDetalleService;
import ec.gob.mdg.sias.ejb.service.IUsuarioService;
import ec.gob.mdg.sias.ejb.service.IUsuarioSesionesService;
import ec.gob.mdg.sias.ejb.utils.UtilsDate;
import lombok.Data;

@Data
@Named
@ViewScoped
public class IndexBean implements Serializable {

	private static final long serialVersionUID = 992085370965349203L;

	@Inject
	private IUsuarioService serviceUsuario;

	@Inject
	private IUsuarioSesionesService serviceUsuarioSesiones;

	@Inject
	private OperacionesConUsuario serviceOpUsuarios;
	
//	@Inject
//	private ITipoDetalleService serviceTipoDetalle;

	private Usuario us;
	private UsuarioSesiones usuarioSesiones;
	private TipoDetalle estado;
	Date fechaActual;
	Integer contador;

	@PostConstruct
	public void init() {
		fechaActual = UtilsDate.timestamp();
		this.us = new Usuario();
		contador = 0;
	}

	@Transactional
	public String login() throws Exception {
		System.out.println(" LOGIN");
		String redireccion = "";
		// us.setUsername(us.getUsername());
		if (us.getUsername() != null) {
			System.out.println("entra EN LOGIN");
			boolean respuesta = serviceOpUsuarios.rucEstaRegistrado(us.getUsername());
			boolean respuestaB = serviceOpUsuarios.usuarioBloqueado(us);
			System.out.println(" respuesta " + respuesta);
			System.out.println(" respuestaB " + respuestaB);

			if (respuesta == false) {
				try {
					System.out.println("entra em usuario no existe");
					grabaSesion("Usuario no Existe", "Fallido");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Credenciales incorrectas, Usuario no existe ", "Aviso, "));
				} catch (Exception e) {
					System.out.println("error no existe null " + e);
					e.printStackTrace();
				}
			} else if (respuesta == true) {
				if (respuestaB == true) {
					System.out.println("entra em usuario usuario bloqueado");
					grabaSesion("Usuario Bloqueado", "Fallido");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
							"El usuario ha sido bloqueado, enviar correo al Administrador Funcional de la Dirección",
							"Aviso"));
				} else if (respuestaB == false) {
					Usuario usuario = serviceUsuario.login(us);

					if (usuario != null && usuario.getIdTipoEstado()== 7) {
						
						if (usuario.getFechaCambioClave() == null) {
							System.out.println("entra para cambiar clave 1 vez");
							FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario",
									usuario);
							redireccion = "/cambioClave?faces-redirect=true";
						} 
						else if (usuario.getFechaReinicioClave()!= null) {
							System.out.println("entra para cambiar clave reinicio");
							FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario",
									usuario);
							redireccion = "/cambioClave?faces-redirect=true";
						}
						else if (usuario.getFechaCambioClave() != null) {
							System.out.println("entra si ya cambio la clave");
							
//							LocalDateTime ldt = usuario.getFechaCambioClave();
//							ZonedDateTime zdt = ldt.atZone(ZoneId.systemDefault());
//							Date fecha = Date.from(zdt.toInstant());
							Date fecha = usuario.getFechaCambioClave();
							Calendar calendar = Calendar.getInstance();
							calendar.setTime(fecha);
							calendar.add(Calendar.DAY_OF_YEAR, 90);
//     						Date fechaS = Date.from(zdt.toInstant());
							Date fechaS = calendar.getTime();	
							System.out.println("Imprime fechaS " + fechaS);
							System.out.println("Imprime fechaActual " + fechaActual);

							if (fechaS.compareTo(fechaActual) < 0) {
								System.out.println("Entra  a comparar fechas" );
								FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario",
										usuario);
								redireccion = "/cambioClave?faces-redirect=true";
							} else {
								System.out.println("Entra a principal" );
								FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario",
										usuario);
								redireccion = "/protegido/principal?faces-redirect=true";
								System.out.println("--- sale de principal --- " );
							}
						}
						grabaSesion("Usuario Correcto","Exitoso");

					} else {
					
						contador++;
						grabaSesion( "Contraseña Incorrecta","Fallido");

						if (contador < 3) {

							FacesContext.getCurrentInstance().addMessage(null,
									new FacesMessage(FacesMessage.SEVERITY_WARN,
											"Credenciales incorrectas, " + contador
													+ " de 3 intentos, al tercer intento el usuario será bloqueado",
											"Aviso, "));
					
						} else {
							System.out.println("ENTRA MAS DE 3");
							
//							estado = serviceTipoDetalle.estado(2); //Bloqueado
							us.setIdTipoEstado(8);
							serviceUsuario.modificar(us);

							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
									FacesMessage.SEVERITY_WARN,
									"Credenciales incorrectas, el usuario fue bloqueado, comuníquese con la Dirección Financiera",
									"Aviso"));
						}
					}
				}
			}
		}
		return redireccion;
	}

	// almacena tabla usuariosesiones
	@Transactional
	public void grabaSesion(String Mensaje, String Tipo) {
		
		System.out.println("entra grabaSesion");
		UsuarioSesiones usuarioSesiones = new UsuarioSesiones();

		usuarioSesiones.setFecha(fechaActual);
		usuarioSesiones.setMensaje(Mensaje);
		usuarioSesiones.setTipo(Tipo);
		usuarioSesiones.setUsername(us.getUsername());
		try {
			serviceUsuarioSesiones.registrar(usuarioSesiones);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("sale grabaSesion");
	}

}
