package cl.banco_andino_quinchos.baq.Bitacora.Controller;

import cl.banco_andino_quinchos.baq.Bitacora.Repository.JPA.BitacoraJPA;
import cl.banco_andino_quinchos.baq.Bitacora.Service.BitacoraService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bitacora")
public class BitacoraController {
    private final BitacoraService bitacoraService;

    public BitacoraController(BitacoraService bitacoraService) {
        this.bitacoraService = bitacoraService;
    }

    @GetMapping
    public List<BitacoraJPA> listarTodo() {
        return bitacoraService.listarTodo();
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<BitacoraJPA>> obtenerAccionesPorUsuario(@PathVariable Integer idUsuario) {
        List<BitacoraJPA> acciones = bitacoraService.listarPorUsuario(idUsuario);
        return ResponseEntity.ok(acciones);
    }
}
