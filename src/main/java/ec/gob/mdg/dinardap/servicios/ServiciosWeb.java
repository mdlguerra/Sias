package ec.gob.mdg.dinardap.servicios;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.kernel.xmp.impl.Base64;

import ec.gob.mdg.sias.ejb.utils.Utilitario;
import ec.gob.mdg.dinardap.modelo.AntLicenciaDTO;
import ec.gob.mdg.dinardap.modelo.AntPlacaDTO;
import ec.gob.mdg.dinardap.modelo.RegistroCivilCedulaDTO;
import ec.gob.mdg.dinardap.modelo.SriRucDTO;
import ec.gob.mdg.dinardap.modelo.SriRucDetalleDTO;
import ec.gob.mdg.dinardap.modelo.SuperCiasRucDTO;

public class ServiciosWeb {
	
	//private static final String REST_SERVICE_URL = "http://10.41.1.63:8051/servicios/";
	private static final String REST_SERVICE_URL = "http://186.46.148.233:8051/servicios/";
	//private static final String REST_SERVICE_URL = "http://localhost:8080/servicios/";
	
	private static final String REST_SERVICE_URL_SPRING = "http://localhost:8088/oauth/token";
	
	static RegistroCivilCedulaDTO el_ciudadano = new RegistroCivilCedulaDTO();
	static SriRucDTO la_persona = new SriRucDTO();
	static List<SriRucDetalleDTO> los_establecimientos = new ArrayList<SriRucDetalleDTO>();
	static AntPlacaDTO el_vehiculo = new AntPlacaDTO();
	static AntLicenciaDTO la_licencia = new AntLicenciaDTO();
	static SuperCiasRucDTO la_persona_cias = new SuperCiasRucDTO();
	
	
	public static String obtenerToken(Form forma) throws UnsupportedEncodingException {
		
		Form form = new Form();
		form.param("grant_type", "password");
		form.param("username", "joba1@gmail.com");
		form.param("password", "123");
		
		String Username = "sisalem"; //usuario de la aplicaci�n
		String Password = "sisalem321"; //password de aplicaci�n
        String authStringEnc = Base64.encode(Username + ":" + Password);
        
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(REST_SERVICE_URL_SPRING);
        
        Invocation.Builder solicitud = webTarget.request(MediaType.APPLICATION_JSON).header("Authorization", "Basic " + authStringEnc);
		Response post = solicitud.post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
		
		String token = null;
		if (post.getStatus() == 200) {
			
			String responseJson = post.readEntity(String.class);
			
			JsonReader jsonReader = Json.createReader(new StringReader(responseJson));
	        JsonObject jsonF;
	        jsonF = (JsonObject) jsonReader.read();
	        token = jsonF.getString("access_token");
	        Utilitario.setSessionAttribute("token", token);
		}
        return token;
	}

	
	
	public static RegistroCivilCedulaDTO consultarCiudadanoRegistroCivil(String cedula) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(REST_SERVICE_URL + "registrocivil/{cedula}").resolveTemplate("cedula", cedula);
		String response = target.request(MediaType.APPLICATION_JSON_TYPE).header("X-API-KEY","ministeriodegobierno.gob.ec_ecuador").get(String.class);
		
		Gson g = new Gson();
		el_ciudadano = g.fromJson(response, RegistroCivilCedulaDTO.class);
		return el_ciudadano;
	}
	
	public static SriRucDTO consultarPersonaServicioRentasInternas(String ruc) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(REST_SERVICE_URL + "sri/{ruc}").resolveTemplate("ruc", ruc);
		String response = target.request(MediaType.APPLICATION_JSON_TYPE).header("X-API-KEY","ministeriodegobierno.gob.ec_ecuador").get(String.class);
		
		Gson g = new Gson();
		la_persona = g.fromJson(response, SriRucDTO.class);
		return la_persona;
	}
	
	public static List<SriRucDetalleDTO> consultarEstablecimientosPersonaServicioRentasInternas(String ruc) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(REST_SERVICE_URL + "sriestablecimientos/{ruc}").resolveTemplate("ruc", ruc);
		String response = target.request(MediaType.APPLICATION_JSON_TYPE).header("X-API-KEY","ministeriodegobierno.gob.ec_ecuador").get(String.class);
		
		Gson g = new Gson();
		los_establecimientos = g.fromJson(response, new TypeToken<ArrayList<SriRucDetalleDTO>>() {}.getType());
		return los_establecimientos;
	}
	
	public static AntPlacaDTO consultarVehiculoPlacaAnt(String placa) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(REST_SERVICE_URL + "buscarplaca/{placa}").resolveTemplate("placa", placa);
		String response = target.request(MediaType.APPLICATION_JSON_TYPE).header("X-API-KEY","ministeriodegobierno.gob.ec_ecuador").get(String.class);
		
		Gson g = new Gson();
		el_vehiculo = g.fromJson(response, AntPlacaDTO.class);
		return el_vehiculo;
	}
	
	public static AntLicenciaDTO consultarLicenciaAnt(String cedula) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(REST_SERVICE_URL + "buscarplaca/{cedula}").resolveTemplate("cedula", cedula);
		String response = target.request(MediaType.APPLICATION_JSON_TYPE).header("X-API-KEY","ministeriodegobierno.gob.ec_ecuador").get(String.class);
		
		Gson g = new Gson();
		la_licencia = g.fromJson(response, AntLicenciaDTO.class);
		return la_licencia;
	}
	
	public static SuperCiasRucDTO consultarDatosGeneralesSupercias(String ruc) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(REST_SERVICE_URL + "superciasdatosgenerales/{ruc}").resolveTemplate("ruc", ruc);
		String response = target.request(MediaType.APPLICATION_JSON_TYPE).header("X-API-KEY","ministeriodegobierno.gob.ec_ecuador").get(String.class);
		
		Gson g = new Gson();
		la_persona_cias = g.fromJson(response, SuperCiasRucDTO.class);
		return la_persona_cias;
	}

}
