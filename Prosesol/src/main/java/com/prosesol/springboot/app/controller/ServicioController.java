package com.prosesol.springboot.app.controller;

import com.prosesol.springboot.app.entity.Beneficio;
import com.prosesol.springboot.app.entity.CentroContacto;
import com.prosesol.springboot.app.entity.Servicio;
import com.prosesol.springboot.app.entity.rel.RelServicioBeneficio;
import com.prosesol.springboot.app.exception.CustomUserException;
import com.prosesol.springboot.app.exception.CustomUserExceptionHandler;
import com.prosesol.springboot.app.service.*;
import mx.openpay.client.Plan;
import mx.openpay.client.core.OpenpayAPI;
import mx.openpay.client.enums.PlanRepeatUnit;
import mx.openpay.client.enums.PlanStatusAfterRetry;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@SessionAttributes("servicio")
@RequestMapping("/servicios")
public class ServicioController {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private IServicioService servicioService;

    @Autowired
    private IBeneficioService beneficioService;

    @Autowired
    private ICentroContactoService centroContactoService;

    @Autowired
    private IRelServicioBeneficioService relServicioBeneficioService;

    @Autowired
    private IPlanService planService;

    @Value("${openpay.pk}")
    private String privateKey;

    @Value("${openpay.url}")
    private String openpayURL;

    @Value("${openpay.id}")
    private String merchantId;

    private Long idServicioGeneral;

    /**
     * Método para la creación de un Servicio
     *
     * @param model
     * @return
     */

    @Secured({"ROLE_ADMINISTRADOR", "ROLE_USUARIO"})
    @RequestMapping(value = "/crear")
    public String crear(Map<String, Object> model) {

        Servicio servicio = new Servicio();
        boolean titular = false;
        boolean beneficiario = false;

        model.put("servicio", servicio);
        model.put("titulo", "Crear Servicio");
        model.put("titular", titular);
        model.put("beneficiario", beneficiario);

        logger.info("Id servicio desde el método de crear: " + servicio.getId());

        return "catalogos/servicios/crear";

    }

    /**
     * Método para la visualización del Servicio
     *
     * @param model
     * @return
     */

    @Secured({"ROLE_ADMINISTRADOR", "ROLE_USUARIO"})
    @RequestMapping(value = "/ver", method = RequestMethod.GET)
    public String ver(Model model) {

        try {
            model.addAttribute("titulo", "Membresía");
            model.addAttribute("servicios", servicioService.findAll());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "catalogos/servicios/ver";
    }

    /**
     * Método para el detalle del Servicio
     *
     * @param id
     * @param model
     * @param redirect
     * @return
     */

    @Secured({"ROLE_ADMINISTRADOR", "ROLE_USUARIO"})
    @RequestMapping(value = "/detalle/{id}")
    public String detalle(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes redirect) {

        Servicio servicio = null;
        List<RelServicioBeneficio> listaServicioBeneficio = relServicioBeneficioService
                .getRelServicioBeneficioByIdServicio(id);

        List<Beneficio> beneficios = new ArrayList<Beneficio>();

        if (id > 0) {
            servicio = servicioService.findById(id);

            if (servicio == null) {
                redirect.addFlashAttribute("error", "El servicio no existe en la base de datos");
                return "redirect:/servicios/ver";
            }
        } else {
            redirect.addFlashAttribute("error", "El id del servicio no puede ser cero");
            return "redirect:/servicios/ver";
        }

        for (RelServicioBeneficio listabeneficio : listaServicioBeneficio) {
            Beneficio beneficio = new Beneficio();

            beneficio = beneficioService.findById(listabeneficio.getBeneficio().getId());
            beneficios.add(beneficio);
        }

        model.put("servicio", servicio);
        model.put("listaBeneficios", beneficios);

        return "catalogos/servicios/detalle";

    }

    /**
     * Almacena el servicio con sus beneficios dentro de la base de datos
     * Verifica si el servicio se cnvertirá en plan
     *
     * @param servicio
     * @param result
     * @param redirect
     * @param status
     * @param idBeneficio
     * @param descripcion
     * @param beneficiario
     * @param titular
     * @param beneDescripcion
     * @return
     * @throws Exception
     */

    @Secured({"ROLE_ADMINISTRADOR", "ROLE_USUARIO"})
    @RequestMapping(value = "/crear", method = RequestMethod.POST, params = "action=save")
    public String guardar(@Valid Servicio servicio, BindingResult result, RedirectAttributes redirect,
                          SessionStatus status, @RequestParam(name = "beneficio[]", required = false) List<Long> idBeneficio,
                          @RequestParam(name = "descripcion[]", required = false) List<String> descripcion,
                          @RequestParam(name = "beneficiario[]", required = false) List<Long> beneficiario,
                          @RequestParam(name = "titular[]", required = false) List<Long> titular,
                          @RequestParam(name = "beneDescripcion[]", required = false) List<Long> beneDescripcion,
                          @RequestParam(value = "isPlan", required = false) String isPlan,
                          @RequestParam(value = "periodosOpenpay", required = false) String periodosOpenpay,
                          Model model) {

        logger.info("Entra al método para guardar o modificar el servicio");

        String flashMessage = "";
        RelServicioBeneficio relServicioBeneficio = new RelServicioBeneficio();

        try {

            if (result.hasErrors()) {
                redirect.addFlashAttribute("error", "Campos incompletos");
                return "catalogos/servicios/crear";
            }

            // Verifica si se necesita editar el servicio o se deberá de crear

            if (servicio.getId() != null) {

                servicio.setEstatus(true);
                // Se creará un plan por el servicio si se selecciona el checkbox isValid

                if (isPlan != null) {
                    if (servicio.getCostoTitular() > 0) {
                        servicio.setIsPlan(true);
                        servicioService.save(servicio);
                        guardarPlan(servicio, periodosOpenpay);
                    } else {
                        model.addAttribute("error", "El servicio no cuenta con costo " +
                                "de servicio");
                        return "/catalogos/servicios/crear";
                    }

                } else if(servicio.getIsPlan()){
                    servicioService.save(servicio);
                }else{
                    servicio.setIsPlan(false);
                    servicioService.save(servicio);
                }

                // Verifica si el servicio se editará con todo y beneficios

                if (idBeneficio != null && idBeneficio.size() > 0) {
                    editarServiciosConBeneficios(servicio, idBeneficio, descripcion, titular, beneficiario);
                }else {
                	List<RelServicioBeneficio> listRelServicioBeneficio = getRelServicioBeneficioByIdServicio(servicio.getId());
                	if(listRelServicioBeneficio.size()>0 || listRelServicioBeneficio!=null) {
                		relServicioBeneficioService.deleteAllRelServicioBeneficioByIdServicio(servicio.getId());
                	}
                }

                flashMessage = "Servicio editado correctamente";

            } else {

                // Verifica si el servicio se creará con todo y beneficios

                if (idBeneficio != null && idBeneficio.size() > 0) {

                    //descripcion.removeAll(Arrays.asList(" ",""));

                    servicio.setEstatus(true);

                    // Se creará un plan por el servicio si se selecciona el checkbox isValid

                    if (isPlan != null) {
                        if (servicio.getCostoTitular() > 0) {
                            servicio.setIsPlan(true);
                            servicioService.save(servicio);
                            guardarPlan(servicio, periodosOpenpay);
                        } else {
                            model.addAttribute("error", "El servicio no cuenta con costo " +
                                    "de servicio");
                            return "/catalogos/servicios/crear";
                        }

                    } else {
                        servicio.setIsPlan(false);
                        servicioService.save(servicio);
                    }

                    int dIndex = 0;
                    int tIndex = 0;
                    int bIndex = 0;
                    int countTitular = 1;
                    int countBeneficiario = 1;
                    String guardoBeneficiario = null;
                    String guardoTitular = null;
                    ArrayList<String> lB = new ArrayList<String>();
                    ArrayList<String> lT = new ArrayList<String>();
                    for (Long beneficio : idBeneficio) {

                        Beneficio nBeneficio = beneficioService.findById(beneficio);

                        relServicioBeneficio.setServicio(servicio);
                        relServicioBeneficio.setBeneficio(nBeneficio);
                        
                        for (Long idbeneficio : beneDescripcion) {
                            if (idbeneficio == beneficio) {
                                                       		
                                if (descripcion.get(dIndex) == " ") {
                                    	relServicioBeneficio.setDescripcion(descripcion.get(dIndex).trim());
                                	} else {
                                    	relServicioBeneficio.setDescripcion(descripcion.get(dIndex).trim());
                                	}
                                	dIndex=0;
                                	break;
                            }
                            	dIndex++;
                            
                            
                        }
                         relServicioBeneficioService.save(relServicioBeneficio);

                    }

                    flashMessage = "Servicio creado correctamente";

                } else { // Solamente se inserta el servicio

                    servicio.setEstatus(true);

                    // Se creará un plan por el servicio si se selecciona el checkbox isValid

                    if (isPlan != null) {
                        if (servicio.getCostoTitular() > 0) {
                            servicio.setIsPlan(true);
                            servicioService.save(servicio);
                            guardarPlan(servicio, periodosOpenpay);
                        } else {
                            model.addAttribute("error", "El servicio no cuenta con costo " +
                                    "de servicio");
                            return "/catalogos/servicios/crear";
                        }

                    } else {
                        servicio.setIsPlan(false);
                        servicioService.save(servicio);
                    }

                    flashMessage = "Servicio creado correctamente";
                }
            }

            status.setComplete();
            redirect.addFlashAttribute("success", flashMessage);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        logger.info("Id servicio desde el método de guardar: " + servicio.getId());

        return "redirect:/servicios/ver";

    }

    /**
     * Método para la edición del Servicio
     *
     * @param idServicio
     * @param model
     * @param redirect
     * @return
     */

    @Secured({"ROLE_ADMINISTRADOR", "ROLE_USUARIO"})
    @RequestMapping(value = "/editar/{id}")
    public String editar(@PathVariable(value = "id") Long idServicio, Model model, RedirectAttributes redirect) {

        Servicio servicio = null;
        List<RelServicioBeneficio> relServicioBeneficios = getRelServicioBeneficioByIdServicio(idServicio);
        List<Beneficio> beneficios = beneficioService.findAll();
        List<RelServicioBeneficio> nRelServicioBeneficio = new ArrayList<RelServicioBeneficio>();

        try {
            if (idServicio > 0) {
                servicio = servicioService.findById(idServicio);
                if (servicio == null) {
                    redirect.addFlashAttribute("error", "El id del servicio no existe");
                    return "redirect:/servicios/ver";
                }

                int countSB = 0;

                for (Beneficio beneficio : beneficios) {
                    if (relServicioBeneficios.size() > countSB
                            && beneficio.getId() == relServicioBeneficios.get(countSB).getBeneficio().getId()) {

                        RelServicioBeneficio relServicioBeneficio = new RelServicioBeneficio(
                                relServicioBeneficios.get(countSB).getServicio(),
                                relServicioBeneficios.get(countSB).getBeneficio(),
                                //relServicioBeneficios.get(countSB).getTitular(),
                                //relServicioBeneficios.get(countSB).getBeneficiario(),
                                relServicioBeneficios.get(countSB).getDescripcion());

                        nRelServicioBeneficio.add(relServicioBeneficio);

                        countSB++;
                    } else {

                        RelServicioBeneficio relServicioBeneficio = new RelServicioBeneficio(servicio, beneficio, null);

                        nRelServicioBeneficio.add(relServicioBeneficio);
                    }

                }

            } else {
                redirect.addFlashAttribute("error", "El id del servicio no puede ser cero");
                return "redirect:/servicios/ver";
            }
        } catch (Exception ex) {
            redirect.addFlashAttribute("error", "Ocurrió un error en el sistema, contacte al administrador");
            ex.printStackTrace();
            return "redirect:/servicios/ver";
        }

        idServicioGeneral = idServicio;

        model.addAttribute("servicio", servicio);
        model.addAttribute("relServicioBeneficios", nRelServicioBeneficio);

        return "catalogos/servicios/editar";

    }

    /**
     * Método para borrar el Servicio
     *
     * @param id
     * @param redirect
     * @return
     */

    @Secured({"ROLE_ADMINISTRADOR", "ROLE_USUARIO"})
    @RequestMapping(value = "/eliminar/{id}")
    public String borrar(@PathVariable(value = "id") Long id, RedirectAttributes redirect) {

        try {
            if (id > 0) {
                servicioService.delete(id);
                redirect.addFlashAttribute("success", "Registro eliminado correctamente");
            }
        } catch (Exception e) {
            logger.error("Ocurrió un error al momento de eliminar el registro", e);
            redirect.addFlashAttribute("error", "El servicio no se puede eliminar porque está asignado a un Afiliado");
        }

        return "redirect:/servicios/ver";
    }

    /**
     * Método para borrar los beneficios en una lista
     *
     * @param beneficios
     * @param model
     * @param redirect
     * @return
     */

    @Secured({"ROLE_ADMINISTRADOR", "ROLE_USUARIO"})
    @RequestMapping(value = "/crear", method = RequestMethod.POST, params = "action=delete")
    public String borrarBeneficios(@RequestParam(name = "beneficio[]", required = false) List<Long> beneficios,
                                   RedirectAttributes redirect) {
        try {

            for (Long beneficio : beneficios) {
                relServicioBeneficioService.removeBeneficiobyIdBeneficio(beneficio);
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Ocurrió un error al momento de realizar la eliminación de los beneficios", e);
            return "redirect:/servicios/ver";
        }

        redirect.addFlashAttribute("success", "Beneficios eliminados correctamente");

        return "redirect:/servicios/ver/";
    }

    /**
     * Cátalogo de beneficios para la vista de creación de servicios
     *
     * @return
     */

    @ModelAttribute("beneficios")
    public List<Beneficio> getAllBeneficios() {
        return beneficioService.findAll();
    }

    /**
     * Método que obtiene la lista de los beneficios que pertenecen a cada Servicio
     *
     * @param idServicio
     * @return
     */

    public List<RelServicioBeneficio> getRelServicioBeneficioByIdServicio(Long idServicio) {
        return relServicioBeneficioService.getRelServicioBeneficioByIdServicio(idServicio);
    }

    /**
     * Método que obtiene la lista de los centros de asistencia por cada servicio
     *
     * @return
     */

    @ModelAttribute("centros")
    public List<CentroContacto> getAllCentroContacto() {
        return centroContactoService.findAll();
    }

    /**
     * Método que edita o inserta beneficios
     *
     * @param servicio
     * @param idBeneficio
     * @param descripcion
     * @param titular'
     * @param beneficiario
     */

    public void editarServiciosConBeneficios(Servicio servicio, List<Long> idBeneficio, List<String> descripcion,
                                             List<Long> titular, List<Long> beneficiario) {

        RelServicioBeneficio relServicioBeneficio = null;
        List<Beneficio> beneficios = beneficioService.findAll();
        Map<Long, String> beneficioDescripcion = new HashMap<Long, String>();
        Boolean valida = false;
        int countDescripcion = 0;

        for (Beneficio b : beneficios) {
            beneficioDescripcion.put(b.getId(), descripcion.get(countDescripcion));
            countDescripcion++;
        }
        //actualiza los campos relServiciosBeneficios
        for (Long id : idBeneficio) {
        	
        	
        	RelServicioBeneficio rel = relServicioBeneficioService.getRelServicioBeneficioByIdServcioAndIdBeneficio(servicio.getId(), id);
        		if(rel ==null || id == rel.getBeneficio().getId()) {
        		
        			Beneficio beneficio = beneficioService.findById(id);
                    for (Map.Entry<Long, String> entry : beneficioDescripcion.entrySet()) {
                        if (entry.getKey() == id) {
                            relServicioBeneficio = new RelServicioBeneficio(servicio, beneficio,entry.getValue());
                            relServicioBeneficioService.save(relServicioBeneficio);
                            break;
                        }
                    }
                    
        		}
        	}		
        //elimina los id unchecked relServiciosBeneficios	
        List<RelServicioBeneficio> listRelServicioBeneficio = getRelServicioBeneficioByIdServicio(servicio.getId());
        for(RelServicioBeneficio rel : listRelServicioBeneficio) {
        	valida = false;
        		for (Long id : idBeneficio) {
        			if(rel.getBeneficio().getId() == id ) {
        				valida=true;
        				break;
        			}
        		}
        		if(!valida) {
        			relServicioBeneficioService.deleteBeneficioByIdBeneficioAndIdServicio(servicio.getId(), rel.getBeneficio().getId());
        		}
        	}
        
         }

    @ModelAttribute("listaTipoPrivacidad")
    public Map getTipoPrivacidad() {
        Map<Boolean, String> tipoPrivacidad = new HashMap<Boolean, String>();

        tipoPrivacidad.put(true, "Privado");
        tipoPrivacidad.put(false, "Público");

        return tipoPrivacidad;
    }

    /**
     * Se guarda el servicio como plan
     *
     * @param servicio
     */

    private void guardarPlan(Servicio servicio, String periodosOpenpay) throws ServiceUnavailableException,
            OpenpayServiceException{

        OpenpayAPI api = new OpenpayAPI(openpayURL, privateKey, merchantId);
        Plan plan = new Plan();

        plan.name(servicio.getNombre());
        plan.amount(BigDecimal.valueOf(servicio.getCostoTitular()));

        switch (periodosOpenpay){
            case "SEMANAL":
                plan.repeatEvery(1, PlanRepeatUnit.WEEK);
                break;
            case "MENSUAL":
                plan.repeatEvery(1, PlanRepeatUnit.MONTH);
                break;
            case "ANUAL":
                plan.repeatEvery(1, PlanRepeatUnit.YEAR);
                break;
        }
        plan.retryTimes(3);
        plan.statusAfterRetry(PlanStatusAfterRetry.UNPAID);
        plan.trialDays(30);

        plan = api.plans().create(plan);

        if (plan.getStatus().equals("active")) {
            logger.info("Activada");
            com.prosesol.springboot.app.entity.Plan planProsesol =
                    new com.prosesol.springboot.app.entity.Plan(servicio, servicio.getNombre(),
                            plan.getId());

            planService.save(planProsesol);
        }
    }
}
