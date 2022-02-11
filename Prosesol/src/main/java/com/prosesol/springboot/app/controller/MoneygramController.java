package com.prosesol.springboot.app.controller;

import com.prosesol.springboot.app.entity.*;
import com.prosesol.springboot.app.entity.rel.RelAfiliadoMoneygram;
import com.prosesol.springboot.app.entity.rel.RelUsuarioPromotor;
import com.prosesol.springboot.app.service.*;
import com.prosesol.springboot.app.services.EmailService;
import com.prosesol.springboot.app.services.IdMoneygramService;
import com.prosesol.springboot.app.util.GenerarClave;
import com.prosesol.springboot.app.util.Paises;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequestMapping("/moneygram")
@SessionAttributes({"afiliado", "relAfiliadoMoneygram", "usuarioPromotor"})
public class MoneygramController {

    protected static final Log logger = LogFactory.getLog(MoneygramController.class);

    private final static int ID_TEMPLATE_BA = 3053146;
    

	@Value("${app.clave}")
	private String clave;

    @Autowired
    private IRelAfiliadoMoneygramService relAfiliadoMoneygramService;

    @Autowired
    private IPeriodicidadService periodicidadService;

    @Autowired
    private IAfiliadoService afiliadoService;

    @Autowired
    private IServicioService servicioService;

    @Autowired
    private ICuentaService cuentaService;

    @Autowired
    private IPromotorService promotorService;

    @Autowired
    private IParametroService parametroService;

    @Autowired
    private IEmpresaService empresaService;

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private  IRelUsuarioPromotorService usuarioPromotorService;

    @Autowired
    private EmailService emailService;
    
    @Autowired
   	private IBeneficioService BeneficioService;
    
    @Autowired
	private GenerarClave generarClave;
    
    @Autowired
	private IdMoneygramService moneygramService;

    @Secured("ROLE_PROMOTOR")
    @GetMapping(value = "/home")
    public String home() {
        return "moneygram/home";
    }

    @Secured({"ROLE_PROMOTOR"})
    @RequestMapping(value = "/crear")
    public String crear(Model model){

        Afiliado afiliado = new Afiliado();
        RelAfiliadoMoneygram relAfiliadoMoneygram = new RelAfiliadoMoneygram();

        model.addAttribute("afiliado", afiliado);
        model.addAttribute("relAfiliadoMoneygram", relAfiliadoMoneygram);
        return "moneygram/crear";
    }

    /**
     * Método para afiliar a un contratante al programa de moneygram
     * @param afiliado
     * @param relAfiliadoMoneygram
     * @param redirect
     * @param status
     * @return
     */

    @Secured({"ROLE_PROMOTOR"})
    @RequestMapping(value = "/crear", method = RequestMethod.POST)
    public String guardar(@ModelAttribute(name = "afiliado") Afiliado afiliado,
                          @ModelAttribute(name = "relAfiliadoMoneygram") RelAfiliadoMoneygram relAfiliadoMoneygram,
                          @ModelAttribute(name = "usuarioPromotor") Promotor promotor, RedirectAttributes redirect,
                          SessionStatus status, Authentication authentication){

        String emailAfiliado = afiliado.getEmail();
        Empresa empresa = empresaService.findById(promotor.getEmpresa().getId());
        String emailContratante = relAfiliadoMoneygram.getEmailContratante();
        List<String> correos = new ArrayList<>();
		Map<String, String> modelo = new LinkedHashMap<>();
		JSONArray ABeneficioD = new JSONArray();
		 List<String> correoContratante = new ArrayList<>();
        
        
        try{
           
            String idMoneygram = moneygramService.generaIdMoneygram(afiliado,promotor);
            
            if(idMoneygram == null) {
            	redirect.addFlashAttribute("error", "No se pudo crear el ID MONEYGRAM");
                return "redirect:/moneygram/crear";
            }
            String moneygramSplit = null;
            String moneygramSplitValid = null;
          //set validacion
            String valid = idMoneygram.substring(10, 11);
            String posicionLeft = idMoneygram.substring(0, 10);
            String posicionRight = idMoneygram.substring(11, 20);
           
            
            //validacion 0
            idMoneygram = posicionLeft + "0" + posicionRight;
            moneygramSplit =  splitMoneygram(idMoneygram);
           
           
           //validacion 1
           idMoneygram = posicionLeft + "1" + posicionRight;
           moneygramSplitValid = splitMoneygram(idMoneygram);
           
            afiliado.setClave(generarClave.getClave(clave));
            afiliado.setEmail(emailAfiliado);
            afiliado.setPromotor(promotor);
            afiliadoService.save(afiliado);
            relAfiliadoMoneygram.setAfiliado(afiliado);
            relAfiliadoMoneygram.setIdMoneygram(idMoneygram);
            relAfiliadoMoneygram.setEmailContratante(emailContratante);
            relAfiliadoMoneygramService.save(relAfiliadoMoneygram);

            status.setComplete();
          
            //costos servicio
           
            Double costoTitularInscripcion = afiliado.getServicio().getCostoTitular() + afiliado.getServicio().getInscripcionTitular() ;
         // Envío email bienvenida
            String valida = "true"; 
         			if (afiliado.getEmail()!=null) {
         				
                        modelo.put("afiliado", afiliado.getNombre() + " " + afiliado.getApellidoPaterno() +
                                " " + afiliado.getApellidoMaterno());
                        modelo.put("servicio", afiliado.getServicio().getNombre());
                        modelo.put("rfc", afiliado.getRfc());
                        modelo.put("proveedor", afiliado.getServicio().getNombreProveedor());
                        //modelo.put("telefono", afiliado.getServicio().getTelefono());
                        modelo.put("correo", afiliado.getServicio().getCorreo());
                        modelo.put("nota", afiliado.getServicio().getNota());
                        modelo.put("id",afiliado.getClave());
                        modelo.put("idMoneygram",moneygramSplit);
                        modelo.put("idMoneygramValid", moneygramSplitValid);
                        modelo.put("contratante", relAfiliadoMoneygram.getNombreContratante());
                        modelo.put("costoServicio", afiliado.getServicio().getCostoTitular().toString());
                        modelo.put("costoSIncripcion", costoTitularInscripcion.toString());
                        correos.add(emailAfiliado);
                        correoContratante.add(relAfiliadoMoneygram.getEmailContratante());
                        List<Beneficio> relServcioBeneficio = BeneficioService.getBeneficiosByIdServicio(afiliado.getServicio().getId());
                        		for(Beneficio bene : relServcioBeneficio) {
                        			ABeneficioD.put(getBeneficios(bene.getNombre(), bene.getDescripcion()));
                        		}                                                          
         			}
         			//email afiliado
         			emailService.sendMailJet(modelo,ID_TEMPLATE_BA,correos,ABeneficioD,valida);
         			//email contratante
                    emailService.sendMailJet(modelo,ID_TEMPLATE_BA,correoContratante,ABeneficioD,valida);

        } catch (Exception e){
            e.printStackTrace();
            redirect.addFlashAttribute("error", "Ocurrió un problema en el sistema, contacte al administrador");

            return "redirect:/moneygram/crear";
        }

        return "redirect:/moneygram/ver";
    }

    @Secured({"ROLE_PROMOTOR"})
    @GetMapping(value = "/ver")
    public String ver(Model model, Authentication authentication){

        String username = authentication.getName();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        authorities.forEach(authority ->{
            if(authority.getAuthority().equals("ROLE_PROMOTOR")){
                model.addAttribute("afiliadoMoneygram", relAfiliadoMoneygramService.getAfiliadosByUsername(username));
            }
        });



        return "moneygram/ver";
    }

    /**
     * Método para mostrar los periodos Dentro del list box de crear afiliados
     *
     * @param(name ="periodos")
     */

    @ModelAttribute("periodos")
    public List<Periodicidad> listaPeriodos() {
        return periodicidadService.findAll();
    }

    /**
     * Método para mostrar los estados Dentro del list box de crear afiliados
     *
     * @param(name = "estados")
     */

    @ModelAttribute("estados")
    public List<String> getAllEstados() {
        return afiliadoService.getAllEstados();
    }

    /**
     * Método para mostrar los servicios Dentro del list box de crear afiliados
     *
     * @param(name = "servicios")
     */

    @ModelAttribute("servicios")
    public List<Servicio> getAllServicios() {
        return servicioService.findAll();
    }

    /**
     * Método para obtener el nombre del promotor logeado
     * @param authentication
     * @return
     */

    @ModelAttribute("usuarioPromotor")
    public Promotor getPromotor(Authentication authentication) {
        Promotor promotor = new Promotor();

        String usuarioPromotor = authentication.getName();

        try{
            Usuario usuario = usuarioService.findByUsername(usuarioPromotor);

            if(usuario == null){
                throw new Exception("Usuario no encontrado");
            }

            RelUsuarioPromotor relUsuarioPromotor = usuarioPromotorService.getPromotorByIdUsuario(usuario);
            promotor = relUsuarioPromotor.getPromotor();

        }catch(Exception e){
            e.printStackTrace();
        }

        return promotor;

    }

    /**
     * Método para mostrar los servicios Dentro del list box de crear afiliados
     *
     * @param(name = "cuentas")
     */

    @ModelAttribute("cuentas")
    public List<Cuenta> getAllCuentas() {
        return cuentaService.findAll();
    }

    /**
     * Método para mostrar los países Dentro del list box de crear afiliados
     *
     * @param(name = "paises")
     */

    @ModelAttribute("paises")
    public List<Paises> getAllPaises() {
        return afiliadoService.getAllPaises();
    }
    
public JSONObject getBeneficios(String name, String descripcion) {
		
		JSONObject OBeneficioD = new JSONObject();
			OBeneficioD.put("nombre",name);
			OBeneficioD.put("descripcion",descripcion);
		   return OBeneficioD ;
	}

public String splitMoneygram(String idMoneygram) {
	
	String moneygramSplit = null;
	 String []array = idMoneygram.split("(?<=\\G....)"); 
     if(array.length == 5) {
  	   moneygramSplit = array[0] +"-"+array[1]+"-"+array[2]+"-"+array[3]+"-"+array[4];
     }else {
  	   moneygramSplit = idMoneygram;
     }
	return moneygramSplit;
}

}
