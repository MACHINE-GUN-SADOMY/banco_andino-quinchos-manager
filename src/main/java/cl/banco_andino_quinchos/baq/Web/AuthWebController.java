package cl.banco_andino_quinchos.baq.Web;

import cl.banco_andino_quinchos.baq.Usuario.Repository.JPA.UsuarioJPA;
import cl.banco_andino_quinchos.baq.Usuario.Service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthWebController {

    private final UsuarioService usuarioService;

    public AuthWebController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    public String mostrarLogin(@RequestParam(required = false) String error,
                               Model model) {
        model.addAttribute("error", error);
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam String email,
                                @RequestParam String password,
                                HttpSession session) {
        try {
            UsuarioJPA usuario = usuarioService.login(email, password);

            session.setAttribute("usuarioId", usuario.getId());
            session.setAttribute("usuarioNombre", usuario.getNombre());
            session.setAttribute("usuarioRol", usuario.getRol());

            return "redirect:/home";
        } catch (RuntimeException e) {
            return "redirect:/login?error=Credenciales%20inv%C3%A1lidas";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
