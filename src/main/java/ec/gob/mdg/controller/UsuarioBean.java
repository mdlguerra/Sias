
package ec.gob.mdg.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.mindrot.jbcrypt.BCrypt;

import ec.gob.mdg.dinardap.modelo.RegistroCivilCedulaDTO;
import ec.gob.mdg.dinardap.servicios.ServiciosWeb;
import ec.gob.mdg.sias.ejb.modelo.Institucion;
import ec.gob.mdg.sias.ejb.modelo.TipoDetalle;
import ec.gob.mdg.sias.ejb.modelo.Usuario;
import ec.gob.mdg.sias.ejb.service.IInstitucionService;
import ec.gob.mdg.sias.ejb.service.ITipoDetalleService;
import ec.gob.mdg.sias.ejb.service.IUsuarioService;
import ec.gob.mdg.sias.ejb.utils.CedulaRuc;
import lombok.Data;

@Data
@Named
@ViewScoped
public class UsuarioBean implements Serializable {

	private static final long serialVersionUID = -6912327508466146927L;

	@Inject
	private IUsuarioService serviceUsuario;

	@Inject
	private IInstitucionService serviceInstitusion;

	@Inject
	private ITipoDetalleService serviceTipoDetalle;

	private Institucion institucion;
	private Usuario usuario;
	private TipoDetalle estado;
	
	private List<TipoDetalle> listaTituloAcademico = new ArrayList<TipoDetalle>();
	private List<TipoDetalle> listaTipoCertificado = new ArrayList<TipoDetalle>();
	private List<TipoDetalle> listaEstado= new ArrayList<TipoDetalle>();
	
	RegistroCivilCedulaDTO usuarioRegCivil = new RegistroCivilCedulaDTO();
	
	boolean estadeshabilitado;
	boolean estadeshabilitado_ap = true;
	Integer idusuario = 0;
	boolean valida = false;
	String claveNueva = null;
	boolean validador;
	boolean render = false;
	
	@PostConstruct
	public void init() {
		this.usuario = new Usuario();
//	   usuario.setUsername("0502384431");
		claveNueva = generarSecuenciaAleatoria();
		desactivarAP();
		listarTitulos();
		listarTipoCertificado();
		listarEstado();
		listarEstadoActivo();
		
		
	//	consultaIdentificacion("0502384431");
	}

	/// Consultar por cédula en el WS de Registro Civil

	public void consultaIdentificacion(String id) {
		System.out.println("entra al método consulta identificación  " + id.length());
		if (id.length() == 0) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ingrese el usuario", "Datos incompletos"));
		} else {
			System.out.println("entra ELSE " + id);
			validaIdentificacion(id);
			usuarioRegCivil = ServiciosWeb.consultarCiudadanoRegistroCivil(id);

			if (!(usuarioRegCivil.getNombre() == null)) {
				usuario.setNombre(usuarioRegCivil.getNombre());
				usuario.setGenero(usuarioRegCivil.getGenero());
				render = true;
				System.out.println("Nombre de la identificación" + usuarioRegCivil.getNombre());
			}
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "biennnnn", "ok"));
		}

	}
	

	// Registrar usuario
	public void registrar() {
		System.out.println("Entra en registrar");

		try {
			String clave = claveNueva;// this.usuario.getContrasena();
			String claveHash = BCrypt.hashpw(clave, BCrypt.gensalt());

			Calendar calendar = Calendar.getInstance();
			Date fechaS = calendar.getTime();			
//
//			usuario.setNombre(usuario.getNombre().toUpperCase());
    		usuario.setGenero(usuario.getGenero().toUpperCase());
//			usuario.setIdTituloAcademico(usuario.getIdTituloAcademico());
//			usuario.setTelefono(usuario.getTelefono());
//			usuario.setCorreoElectronico(usuario.getCorreoElectronico());
			
			usuario.setIdTipoEstado(estado.getId());
			usuario.setPassword(claveHash);
			// usuario.setFechaRegistro(LocalDateTime.now());
			usuario.setFechaRegistro(fechaS);
			usuario.setCantidadEntradas(1);

			this.serviceUsuario.registrar(usuario);
			estadeshabilitado = true;
			valida = true;
			activarAP();

			System.out.println("ANTES DE ENVIAR " + usuario.getNombre() + " - " + claveNueva);
			enviarContrasenia(usuario.getUsername(), claveNueva);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// VALIDADOR DE CEDULA-RUC
	public void validaIdentificacion(String id) {
		if (id.length() == 10) {
			System.out.println("entra a valida identifciación " + id);
			String validaIdentificacion = CedulaRuc.comprobacion(id);
			System.out.println("validaIdentificacion " + validaIdentificacion);
			if (validaIdentificacion.equals("T")) {
				validador = false;
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Identificación correcta", validaIdentificacion));
			} else {
				System.out.println("entra a diferente de T: " + validaIdentificacion);
				validador = true;
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Tipo de identificacion erronea", validaIdentificacion));
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Identificacion erronea", "Error"));
		}
	}

	// NUEVO REGISTR0
	public Boolean nuevo() {
		estadeshabilitado = false;
		return estadeshabilitado;
	}

	//// ACTIVAR EL BOTON DE ASIGNAR PUNTO
	public Boolean activarAP() {
		estadeshabilitado_ap = false;
		return estadeshabilitado_ap;
	}

	//// DESACTIVAR EL BOTON DE ASIGNAR PUNTO
	public Boolean desactivarAP() {
		estadeshabilitado_ap = true;
		return estadeshabilitado_ap;
	}

	
	public void listarTitulos() {
		listaTituloAcademico = serviceTipoDetalle.listaTipoDetallePorIdTipo(1);
	}
	
	public void listarEstado() {
		listaEstado= serviceTipoDetalle.listaTipoDetallePorIdTipo(3);
	}
	
	public void listarEstadoActivo() {
		try {
			System.out.println("Entra en get listar estado");
			estado = serviceTipoDetalle.traeDescripcion(7); //Activo
			System.out.println("Imprime estado " + estado.getNombre());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void listarTipoCertificado() {
		listaTipoCertificado = serviceTipoDetalle.listaTipoDetallePorIdTipo(2);
	}
	public static String generarSecuenciaAleatoria() {
		String sec = "";
		do {
			if (generarNumeroAleatorios(0, 1) == 0)
				sec += generarNumeroAleatorios(0, 9);
			else
				sec += (char) generarNumeroAleatorios(65, 90);
		} while (sec.length() < 10);
		return sec;
	}

	public static int generarNumeroAleatorios(int minimo, int maximo) {
		return (int) Math.floor(Math.random() * (maximo - minimo + 1) + (minimo));
	}

	public void enviarContrasenia(String cedula, String clave) {
		System.out.println("entra a enviar contraseña 1");
		eviarCorreo(cedula, claveNueva);
	}

	public boolean eviarCorreo(String ci, String clave) {

//		usuario = serviceUsuario.muestraUsuarioPor(ci);
		institucion = serviceInstitusion.institucionActiva();
			
		Properties props = System.getProperties();
		props.put("mail.smtp.host", institucion.getMailSmtpHost()); // El servidor SMTP de Google
		props.put("mail.smtp.user", institucion.getMailEmisor());
		props.put("mail.smtp.clave", institucion.getMailPasswordEmisor()); // La clave de la cuenta
		props.put("mail.smtp.auth", institucion.getMailSmtpAuth()); // Usar autenticacin mediante usuario y clave
		props.put("mail.smtp.starttls.enable", institucion.getMailSmtpStartTlsEnable()); // Para conectar de manera segura al servidor SMTP
		props.put("mail.smtp.port", institucion.getMailSmtpPort()); // El puerto SMTP seguro de Google
		props.put("mail.smtp.ssl.trust", "*");
	
		
//		@SuppressWarnings("resource")
//		Formatter obj = new Formatter();
		String asuntoMensaje = "MDG - Sistema SIAS - Credenciales del Usuario "
				+ usuario.getNombre() ;

		String cuerpoMensaje  = "<html><head><title></title></head><body>" + "Estimado(a) Usuario (a): "+ usuario.getNombre() ;

		cuerpoMensaje += "<br><br>Le informamos que se ha realizado la creación de su usuario para el Sistema SIAS, su usuario es:  "
		              + usuario.getUsername()
				      + ", con la clave: "
				      + clave 
		              + "<br><br>Atentamente,<br>"
					  + institucion.getNombre() +" - Sistema SIAS <br><br>"+ "</body></html>";
		
		Session session = Session.getInstance(props, null);
		session.setDebug(true);

		try {
			MimeBodyPart textoMensaje = new MimeBodyPart();
			textoMensaje.setContent(cuerpoMensaje,"text/html");
			
			MimeMultipart multiParte = new MimeMultipart();
			multiParte.addBodyPart(textoMensaje);

			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(institucion.getMailEmisor(),institucion.getNombre()));
			message.addRecipients(Message.RecipientType.TO, usuario.getCorreoElectronico()); 
			message.setSubject(asuntoMensaje);
			message.setContent(multiParte);
			Transport transport = session.getTransport("smtp");
			System.out.println("4 ");			
			transport.connect(institucion.getMailSmtpHost(), institucion.getMailEmisor(),
					institucion.getMailPasswordEmisor());
			System.out.println("5 ");
			transport.sendMessage(message, message.getAllRecipients());
			System.out.println("6 ");
			transport.close();
			
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuario registrado y se remite las credenciales al email registrado", "Aviso"));
			return true;
		} catch (Exception e) {
			
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR: No fue enviado el correo", "ERROR"));
			return false;
		}
	}
	
	

}
