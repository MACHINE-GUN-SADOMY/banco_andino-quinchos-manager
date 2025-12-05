package cl.banco_andino_quinchos.baq.Web;

import cl.banco_andino_quinchos.baq.Quincho.Repository.JPA.QuinchoJPA;
import cl.banco_andino_quinchos.baq.Quincho.Service.QuinchoService;
import cl.banco_andino_quinchos.baq.Reserva.Repository.JPA.ReservaJPA;
import cl.banco_andino_quinchos.baq.Reserva.Service.ReservaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/reservas")
public class ReservaWebController {

    private final ReservaService reservaService;
    private final QuinchoService quinchoService;

    public ReservaWebController(ReservaService reservaService, QuinchoService quinchoService) {
        this.reservaService = reservaService;
        this.quinchoService = quinchoService;
    }

    @GetMapping("/nueva")
    public String mostrarFormularioNuevaReserva(
            HttpSession session,
            @RequestParam(value = "idQuinchoSel", required = false) Integer idQuinchoSel,
            Model model
    ) {
        Object usuarioId = session.getAttribute("usuarioId");
        if (usuarioId == null) {
            return "redirect:/login";
        }

        // Lista de quinchos activos
        List<QuinchoJPA> quinchos = quinchoService.listarActivos();
        model.addAttribute("quinchos", quinchos);

        // Buscamos el quincho seleccionado (si viene idQuinchoSel)
        QuinchoJPA quinchoSeleccionado = null;
        if (idQuinchoSel != null) {
            for (QuinchoJPA q : quinchos) {
                if (q.getId().equals(idQuinchoSel)) {
                    quinchoSeleccionado = q;
                    break;
                }
            }
        }

        model.addAttribute("quinchoSeleccionado", quinchoSeleccionado);
        model.addAttribute("idQuinchoSel", idQuinchoSel);

        return "Empleado/nueva";
    }


    @PostMapping("/nueva")
    public String crearReserva(HttpSession session,
                               @RequestParam Integer idQuincho,
                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin,
                               @RequestParam(required = false) String motivo,
                               Model model) {

        Object usuarioIdObj = session.getAttribute("usuarioId");
        if (usuarioIdObj == null) {
            return "redirect:/login";
        }
        Integer idUsuario = (Integer) usuarioIdObj;

        try {
            reservaService.crearReserva(idUsuario, idQuincho, fechaInicio, fechaFin, motivo);
            return "redirect:/reservas/mis";
        } catch (RuntimeException e) {
            List<QuinchoJPA> quinchos = quinchoService.listarActivos();
            model.addAttribute("quinchos", quinchos);
            model.addAttribute("error", e.getMessage());
            return "Empleado/nueva";
        }
    }

    @GetMapping("/mis")
    public String verMisReservas(HttpSession session, Model model) {
        Object usuarioIdObj = session.getAttribute("usuarioId");
        if (usuarioIdObj == null) {
            return "redirect:/login";
        }
        Integer idUsuario = (Integer) usuarioIdObj;

        List<ReservaJPA> reservas = reservaService.listarPorUsuario(idUsuario);
        model.addAttribute("reservas", reservas);

        return "Empleado/mis";
    }

    @PostMapping("/mis/{id}/cancelar")
    public String cancelarDesdeMis(@PathVariable Integer id,
                                   HttpSession session) {

        Object usuarioIdObj = session.getAttribute("usuarioId");
        if (usuarioIdObj == null) {
            return "redirect:/login";
        }

        Integer idUsuario = (Integer) usuarioIdObj;

        reservaService.cancelarPorUsuario(id, idUsuario);

        return "redirect:/reservas/mis";
    }

}
