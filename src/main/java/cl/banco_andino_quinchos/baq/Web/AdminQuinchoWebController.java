package cl.banco_andino_quinchos.baq.Web;

import cl.banco_andino_quinchos.baq.Bitacora.Service.BitacoraService;
import cl.banco_andino_quinchos.baq.Quincho.Repository.JPA.QuinchoJPA;
import cl.banco_andino_quinchos.baq.Quincho.Service.QuinchoService;
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
@RequestMapping("/admin/quinchos")
public class AdminQuinchoWebController {
    @Autowired
    private UsuarioJpaRepository usuarioRepository;

    private final QuinchoService quinchoService;
    private final BitacoraService bitacoraService;

    public AdminQuinchoWebController(QuinchoService quinchoService, BitacoraService bitacoraService) {
        this.quinchoService = quinchoService;
        this.bitacoraService = bitacoraService;
    }

    private boolean esAdmin(HttpSession session) {
        Object rolObj = session.getAttribute("usuarioRol");
        return rolObj != null && "ADMIN".equals(rolObj);
    }

    @GetMapping
    public String listar(HttpSession session, Model model) {
        if (!esAdmin(session)) {
            return "redirect:/home";
        }

        List<QuinchoJPA> quinchos = quinchoService.listarTodos();
        model.addAttribute("quinchos", quinchos);
        return "Admin/quinchos";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(HttpSession session, Model model) {
        if (!esAdmin(session)) {
            return "redirect:/home";
        }
        return "Admin/quinchos_nuevo";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id,
                         HttpSession session,
                         Model model) {
        if (!esAdmin(session)) return "redirect:/home";

        QuinchoJPA q = quinchoService.obtenerPorId(id);
        model.addAttribute("quincho", q);
        return "Admin/quinchos_editar";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id,
                                          HttpSession session,
                                          Model model,
                                          RedirectAttributes redirect) {

        if (!esAdmin(session)) return "redirect:/home";

        QuinchoJPA quincho = quinchoService.obtenerPorId(id);

        if (quincho == null) {
            redirect.addFlashAttribute("error", "Quincho no encontrado.");
            return "redirect:/admin/quinchos";
        }

        model.addAttribute("quincho", quincho);
        return "Admin/quinchos_editar";
    }

    @PostMapping("/nuevo")
    public String crear(HttpSession session,
                        @RequestParam String nombre,
                        @RequestParam Integer capacidad,
                        @RequestParam String ubicacion,
                        @RequestParam(required = false) Boolean disponibilidad,
                        Model model) {
        if (!esAdmin(session)) {
            return "redirect:/home";
        }

        Integer idAdmin = (Integer) session.getAttribute("usuarioId");

        QuinchoJPA q = new QuinchoJPA();
        q.setNombre(nombre);
        q.setCapacidad(capacidad);
        q.setDisponibilidad(disponibilidad != null ? disponibilidad : true);
        q.setUbicacion(ubicacion);

        quinchoService.crear(q, idAdmin);

        return "redirect:/admin/quinchos";
    }

    @PostMapping("/editar/{id}")
    public String procesarEdicion(@PathVariable Long id,
                                  @RequestParam String nombre,
                                  @RequestParam Integer capacidad,
                                  @RequestParam String ubicacion,
                                  HttpSession session,
                                  RedirectAttributes redirect) {

        if (!esAdmin(session)) return "redirect:/home";

        QuinchoJPA actual = quinchoService.obtenerPorId(id);

        if (actual == null) {
            redirect.addFlashAttribute("error", "Quincho no encontrado.");
            return "redirect:/admin/quinchos";
        }

        QuinchoJPA datosActualizados = new QuinchoJPA();
        datosActualizados.setNombre(nombre);
        datosActualizados.setCapacidad(capacidad);
        datosActualizados.setDisponibilidad(actual.getDisponibilidad());
        datosActualizados.setUbicacion(ubicacion);

        quinchoService.actualizar(id, datosActualizados);

        redirect.addFlashAttribute("msg", "Quincho actualizado correctamente.");
        return "redirect:/admin/quinchos";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminarQuincho(@PathVariable Integer id,
                                  HttpSession session,
                                  RedirectAttributes redirect) {

        if (!esAdmin(session)) {
            return "redirect:/home";
        }

        boolean eliminado = quinchoService.eliminarSiNoTieneReservas(id);

        if (!eliminado) {
            redirect.addFlashAttribute(
                    "error",
                    "No se puede eliminar este quincho porque ya posee reservas activas."
            );
        } else {
            redirect.addFlashAttribute(
                    "msg",
                    "Quincho eliminado correctamente."
            );
        }

        return "redirect:/admin/quinchos";
    }

    @PostMapping("/{id}/estado")
    public String cambiarEstado(@PathVariable Long id,
                                @RequestParam("activo") Boolean activo,
                                HttpSession session,
                                RedirectAttributes redirect) {

        if (!esAdmin(session)) return "redirect:/home";

        Integer idUsuario = (Integer) session.getAttribute("usuarioId");
        UsuarioJPA usuario = usuarioRepository.findById(idUsuario).orElse(null);

        quinchoService.cambiarEstado(id, activo);

        bitacoraService.registrarAccion(
                "CAMBIO ESTADO",
                "CAMBIO DE DISPONIBILIDAD DE QUINCHO",
                "Estado nuevo: " + (activo ? "Disponible" : "No disponible"),
                usuario
        );

        redirect.addFlashAttribute("msg", "Estado actualizado correctamente.");
        return "redirect:/admin/quinchos";
    }
}
