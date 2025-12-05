package cl.banco_andino_quinchos.baq.Web;

import cl.banco_andino_quinchos.baq.Usuario.Repository.JPA.UsuarioJPA;
import cl.banco_andino_quinchos.baq.Usuario.Repository.UsuarioJpaRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/perfil")
public class PerfilWebController {

    @Autowired
    private UsuarioJpaRepository usuarioRepo;

    @GetMapping
    public String verPerfil(HttpSession session, Model model) {

        Object idObj = session.getAttribute("usuarioId");
        if (idObj == null) return "redirect:/login";

        Integer idUsuario = (Integer) idObj;
        UsuarioJPA usuario = usuarioRepo.findById(idUsuario).orElse(null);

        if (usuario == null) return "redirect:/home";

        model.addAttribute("usuario", usuario);
        return "Empleado/perfil";
    }

    @PostMapping
    public String actualizarPerfil(HttpSession session,
                                   @RequestParam String nombre,
                                   @RequestParam String password,
                                   RedirectAttributes redirect) {

        Integer idUsuario = (Integer) session.getAttribute("usuarioId");
        if (idUsuario == null) return "redirect:/login";

        UsuarioJPA usuario = usuarioRepo.findById(idUsuario).orElse(null);
        if (usuario == null) return "redirect:/home";

        usuario.setNombre(nombre);
        usuario.setPassword(password);
        usuarioRepo.save(usuario);

        session.setAttribute("usuarioNombre", nombre);

        redirect.addFlashAttribute("msg", "¡Perfil actualizado con éxito!");
        return "redirect:/perfil";
    }

}
