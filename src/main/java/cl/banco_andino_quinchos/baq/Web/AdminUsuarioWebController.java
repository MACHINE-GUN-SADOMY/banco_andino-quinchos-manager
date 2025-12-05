package cl.banco_andino_quinchos.baq.Web;

import cl.banco_andino_quinchos.baq.Usuario.Repository.JPA.UsuarioJPA;
import cl.banco_andino_quinchos.baq.Usuario.Service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/usuarios")
public class AdminUsuarioWebController {

    private final UsuarioService usuarioService;

    public AdminUsuarioWebController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
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

        List<UsuarioJPA> usuarios = usuarioService.listarTodos();
        model.addAttribute("usuarios", usuarios);

        return "Admin/usuarios";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(HttpSession session, Model model) {
        if (!esAdmin(session)) {
            return "redirect:/home";
        }
        return "admin/usuario_nuevo";
    }


    @PostMapping("/nuevo")
    public String crear(HttpSession session,
                        @RequestParam String nombre,
                        @RequestParam String correo,
                        @RequestParam String password,
                        @RequestParam String rol,
                        @RequestParam(required = false) Boolean activo) {
        if (!esAdmin(session)) {
            return "redirect:/home";
        }

        UsuarioJPA u = new UsuarioJPA();
        u.setNombre(nombre);
        u.setCorreo(correo);
        u.setPassword(password);
        u.setRol(rol);
        u.setActivo(activo != null ? activo : true);

        usuarioService.crear(u);

        return "redirect:/admin/usuarios";
    }

    @PostMapping("/{id}/estado")
    public String cambiarEstado(@PathVariable Integer id,
                                @RequestParam boolean activo,
                                HttpSession session) {
        if (!esAdmin(session)) {
            return "redirect:/home";
        }

        if (activo) {
            usuarioService.activar(id);
        } else {
            usuarioService.desactivar(id);
        }

        return "redirect:/admin/usuarios";
    }

    @GetMapping("/editar/{id}")
    public String editarUsuario(@PathVariable Integer id, Model model, HttpSession session) {
        if (!esAdmin(session)) return "redirect:/home";

        UsuarioJPA usuario = usuarioService.obtenerPorId(id);
        model.addAttribute("usuario", usuario);

        return "Admin/usuario_editar";
    }

    @PostMapping("/editar/{id}")
    public String guardarEdicion(@PathVariable Integer id,
                                 @RequestParam String nombre,
                                 @RequestParam(required = false) String password,
                                 HttpSession session,
                                 RedirectAttributes redirect) {

        if (!esAdmin(session)) return "redirect:/home";

        UsuarioJPA usuario = usuarioService.obtenerPorId(id);
        usuario.setNombre(nombre);

        if (password != null && !password.isBlank()) {
            usuario.setPassword(password);
        }

        usuarioService.crear(usuario);

        redirect.addFlashAttribute("msg", "Usuario editado correctamente.");
        return "redirect:/admin/usuarios";
    }
}
