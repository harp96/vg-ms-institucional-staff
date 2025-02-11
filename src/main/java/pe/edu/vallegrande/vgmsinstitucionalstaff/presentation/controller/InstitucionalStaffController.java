package pe.edu.vallegrande.vgmsinstitucionalstaff.presentation.controller;

import pe.edu.vallegrande.vgmsinstitucionalstaff.application.service.InstitucionalStaffService;
import pe.edu.vallegrande.vgmsinstitucionalstaff.domain.dto.InstitucionalStaffDto;
import pe.edu.vallegrande.vgmsinstitucionalstaff.domain.dto.Ubigeo;
import pe.edu.vallegrande.vgmsinstitucionalstaff.domain.model.InstitucionalStaff;

import org.springframework.web.bind.annotation.*;


import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT})
@RequestMapping("/management/${api.version}/institucional-staff")
public class InstitucionalStaffController {

    private final InstitucionalStaffService institucionalStaffService;

    public InstitucionalStaffController(InstitucionalStaffService institucionalStaffService) {
        this.institucionalStaffService = institucionalStaffService;
    }

    @PostMapping("/create")
    public Mono<InstitucionalStaff> create(@RequestBody InstitucionalStaff institucionalStaff) {
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
