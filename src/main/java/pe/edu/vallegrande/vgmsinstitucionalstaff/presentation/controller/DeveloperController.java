package pe.edu.vallegrande.vgmsinstitucionalstaff.presentation.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import pe.edu.vallegrande.vgmsinstitucionalstaff.application.service.InstitucionalStaffService;
import pe.edu.vallegrande.vgmsinstitucionalstaff.domain.dto.InstitucionalStaffDto;
import pe.edu.vallegrande.vgmsinstitucionalstaff.domain.dto.Ubigeo;
import pe.edu.vallegrande.vgmsinstitucionalstaff.domain.model.InstitucionalStaff;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/developer/${api.version}/institucional-staff")
public class DeveloperController {

    private final InstitucionalStaffService institucionalStaffService;

    public DeveloperController(InstitucionalStaffService institucionalStaffService) {
        this.institucionalStaffService = institucionalStaffService;
    }

    @PostMapping("/create")
    public Mono<InstitucionalStaff> create(@RequestBody InstitucionalStaff institucionalStaff) {
        // Imprimir el objeto recibido para depurar
        System.out.println("Datos recibidos en el controlador: " + institucionalStaff);
        
        // Verificar si el email es nulo o vacío antes de proceder
        if (institucionalStaff.getEmail() == null || institucionalStaff.getEmail().isEmpty()) {
            System.out.println("Error: El email está nulo o vacío en el controlador.");
            throw new IllegalArgumentException("El correo electrónico no puede ser nulo o vacío");
        }
        
        // Llamar al servicio para crear el registro
        return institucionalStaffService.create(institucionalStaff);
    }
    

    @PutMapping("/update/{id}")
    public Mono<InstitucionalStaff> update(@PathVariable String id , @RequestBody InstitucionalStaff institucionalStaff) {
        return institucionalStaffService.update(id, institucionalStaff);
    }

    @PutMapping("/activate/{id}")
    public Mono<InstitucionalStaff> activate(@PathVariable String id) {
        return institucionalStaffService.changeStatus(id, "A");
    }

    @PutMapping("/deactivate/{id}")
    public Mono<InstitucionalStaff> deactivate(@PathVariable String id) {
        return institucionalStaffService.changeStatus(id, "I");
    }

    @GetMapping("/get/{id}")
    public Mono<InstitucionalStaff> getById(@PathVariable String id) {
        return institucionalStaffService.getById(id);
    }

    @GetMapping("/list/active")
    public Flux<InstitucionalStaffDto> getAllActive() {
        return institucionalStaffService.findByStatus("A");
    }

    @GetMapping("/list/inactive")
    public Flux<InstitucionalStaffDto> getAllInactive() {
        return institucionalStaffService.findByStatus("I");
    }

    @GetMapping("/list/docente")
    public Flux<InstitucionalStaffDto> getAllDocentes() {
        return institucionalStaffService.findByStatusAndRolDocente("A");
    }

    @GetMapping("/list/ubigeo")
    public Flux<Ubigeo> getAllUbigeo() {
        return institucionalStaffService.findAllUbigeo();
    }


}
