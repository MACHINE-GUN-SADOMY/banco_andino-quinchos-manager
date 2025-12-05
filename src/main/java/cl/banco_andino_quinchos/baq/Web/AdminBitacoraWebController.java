package cl.banco_andino_quinchos.baq.Web;

import cl.banco_andino_quinchos.baq.Bitacora.Repository.JPA.BitacoraJPA;
import cl.banco_andino_quinchos.baq.Bitacora.Service.BitacoraService;
import cl.banco_andino_quinchos.baq.Usuario.Repository.JPA.UsuarioJPA;
import cl.banco_andino_quinchos.baq.Usuario.Repository.UsuarioJpaRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/bitacora")
public class AdminBitacoraWebController {
    @Autowired
    private BitacoraService bitacoraService;

    @Autowired
    private UsuarioJpaRepository usuarioRepository;

    // MISMO CHECK DE ADMIN QUE EN OTROS CONTROLADORES
    private boolean esAdmin(HttpSession session) {
        Object rolObj = session.getAttribute("usuarioRol");
        return rolObj != null && "ADMIN".equals(rolObj);
    }

    @GetMapping("/admin/bitacora")
    public String verBitacora(HttpSession session, Model model) {
        if (!esAdmin(session)) return "redirect:/home";

        model.addAttribute("registros", bitacoraService.listarTodo());
        return "Admin/bitacora_nuevo";
    }

    // LISTADO GENERAL
    @GetMapping
    public String listar(HttpSession session, Model model) {

        if (!esAdmin(session)) {
            return "redirect:/home";
        }

        List<BitacoraJPA> registros = bitacoraService.listarTodo();
        model.addAttribute("registros", registros);

        return "Admin/bitacora";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(HttpSession session, Model model) {
        if (!esAdmin(session)) return "redirect:/home";

        model.addAttribute("usuarios", usuarioRepository.findAll());

        return "Admin/bitacora_nuevo";
    }

    // PROCESAR REGISTRO MANUAL
    @PostMapping("/nuevo")
    public String registrarAccionManual(@RequestParam String tipoAccion,
                                  @RequestParam String accion,
                                  @RequestParam(required = false) String detalle,
                                  HttpSession session,
                                  RedirectAttributes redirect) {

        if (!esAdmin(session)) {
            return "redirect:/home";
        }

        Integer idUsuario = (Integer) session.getAttribute("usuarioId");
        UsuarioJPA usuario = null;

        if (idUsuario != null) {
            usuario = usuarioRepository.findById(idUsuario).orElse(null);
        }

        bitacoraService.registrarAccion(
                tipoAccion,
                accion,
                detalle,
                usuario
        );

        redirect.addFlashAttribute("msg", "Acción registrada en la bitácora correctamente.");
        return "redirect:/admin/bitacora";
    }
}
