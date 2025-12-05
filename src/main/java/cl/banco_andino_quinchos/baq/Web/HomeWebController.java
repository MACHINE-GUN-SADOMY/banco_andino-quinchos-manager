package cl.banco_andino_quinchos.baq.Web;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeWebController {

    @GetMapping({"/", "/home"})
    public String home(HttpSession session, Model model) {

        Object usuarioId = session.getAttribute("usuarioId");
        if (usuarioId == null) {
            return "redirect:/login";
        }

        String nombre = (String) session.getAttribute("usuarioNombre");
        String rol = (String) session.getAttribute("usuarioRol");

        model.addAttribute("nombre", nombre);
        model.addAttribute("rol", rol);

        return "home";
    }
}
