package cl.banco_andino_quinchos.baq.Web;

import cl.banco_andino_quinchos.baq.Reserva.Repository.JPA.ReservaJPA;
import cl.banco_andino_quinchos.baq.Reserva.Service.ReservaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/reservas")
public class AdminReservaWebController {

    private final ReservaService reservaService;

    public AdminReservaWebController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @GetMapping
    public String listarTodas(HttpSession session, Model model) {
        Object rolObj = session.getAttribute("usuarioRol");
        if (rolObj == null || !"ADMIN".equals(rolObj)) {
            return "redirect:/home";
        }

        List<ReservaJPA> reservas = reservaService.listarTodas();
        model.addAttribute("reservas", reservas);

        return "Admin/reservas";
    }

    @PostMapping("/{idReserva}/estado")
    public String cambiarEstado(@PathVariable Integer idReserva,
                                @RequestParam String nuevoEstado,
                                HttpSession session) {

        Object adminIdObj = session.getAttribute("usuarioId");
        Object rolObj = session.getAttribute("usuarioRol");
        if (adminIdObj == null || !"ADMIN".equals(rolObj)) {
            return "redirect:/home";
        }
        Integer idAdmin = (Integer) adminIdObj;

        boolean aprobado = "APROBADA".equalsIgnoreCase(nuevoEstado);

        reservaService.cambiarEstado(idReserva, nuevoEstado, aprobado, idAdmin);

        return "redirect:/admin/reservas";
    }
}
