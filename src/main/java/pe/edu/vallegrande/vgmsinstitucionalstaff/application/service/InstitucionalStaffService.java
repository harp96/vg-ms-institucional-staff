package pe.edu.vallegrande.vgmsinstitucionalstaff.application.service;

import pe.edu.vallegrande.vgmsinstitucionalstaff.domain.dto.InstitucionalStaffDto;
import pe.edu.vallegrande.vgmsinstitucionalstaff.domain.dto.Ubigeo;
import pe.edu.vallegrande.vgmsinstitucionalstaff.domain.model.InstitucionalStaff;
import pe.edu.vallegrande.vgmsinstitucionalstaff.domain.repository.InstitucionalStaffRepository;

import org.springframework.stereotype.Service;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
public class InstitucionalStaffService {

    private InstitucionalStaffRepository institucionalStaffRepository;
    private ExternalService externalService;
    private FirebaseService firebaseService;

    public InstitucionalStaffService(InstitucionalStaffRepository institucionalStaffRepository,
            ExternalService externalService, FirebaseService firebaseService) {
        this.institucionalStaffRepository = institucionalStaffRepository;
        this.externalService = externalService;
        this.firebaseService = firebaseService;
    }

    public Flux<InstitucionalStaffDto> findByStatus(String status) {
        log.info("Listdo de personal educativo con estado: " + status);
        return institucionalStaffRepository.findByStatus(status)
                .flatMap(this::converTo)
                .collectList()
                .flatMapMany(Flux::fromIterable);
    }

    public Mono<InstitucionalStaff> create(InstitucionalStaff institucionalStaff) {
        log.info("Inicio del proceso de creación del personal institucional");
    
        institucionalStaff.setStatus("A");
        log.info("Estado del personal institucional configurado a: " + institucionalStaff.getStatus());
    
        // Guardar primero en MongoDB
        return institucionalStaffRepository.save(institucionalStaff)
            .flatMap(savedStaff -> {
                log.info("Personal institucional guardado exitosamente en MongoDB con ID: " + savedStaff.getId());
    
                // Si el guardado en MongoDB es exitoso, proceder a crear el usuario en Firebase
                return Mono.fromCallable(() -> {
                    try {
                        log.info("Creando usuario en Firebase con email: " + savedStaff.getEmail());
                        UserRecord firebaseUser = firebaseService.createFirebaseUser(savedStaff);
                        log.info("Usuario creado en Firebase con UID: " + firebaseUser.getUid());
                        log.info("Asignando rol en Firebase: " + savedStaff.getRol());
                        firebaseService.assignRoleToUser(firebaseUser.getUid(), savedStaff.getRol());
                    } catch (FirebaseAuthException e) {
                        log.error("Error creando usuario en Firebase: ", e);
                        throw new RuntimeException(e);
                    }
                    return savedStaff;
                }).subscribeOn(Schedulers.boundedElastic());
            })
            .doOnError(error -> log.error("Error durante el proceso de creación: ", error));
    }
    
    

    public Mono<InstitucionalStaff> update(String id, InstitucionalStaff institucionalStaff) {
        return institucionalStaffRepository.findById(id)
                .flatMap(staff -> {
                    staff.setName(institucionalStaff.getName());
                    staff.setFatherLastname(institucionalStaff.getFatherLastname());
                    staff.setMotherLastname(institucionalStaff.getMotherLastname());
                    staff.setDocumentType(institucionalStaff.getDocumentType());
                    staff.setDocumentNumber(institucionalStaff.getDocumentNumber());
                    staff.setSex(institucionalStaff.getSex());
                    staff.setCountry(institucionalStaff.getCountry());
                    staff.setEmail(institucionalStaff.getEmail());
                    staff.setPhone(institucionalStaff.getPhone());
                    staff.setCivilStatus(institucionalStaff.getCivilStatus());
                    staff.setInstructionGrade(institucionalStaff.getInstructionGrade());
                    staff.setDisabilityType(institucionalStaff.getDisabilityType());
                    staff.setDisability(institucionalStaff.getDisability());
                    staff.setWorkCondition(institucionalStaff.getWorkCondition());
                    staff.setOccupation(institucionalStaff.getOccupation());
                    staff.setNativeLanguage(institucionalStaff.getNativeLanguage());
                    staff.setAddress(institucionalStaff.getAddress());
                    staff.setRol(institucionalStaff.getRol());
                    staff.setStatus("A");
                    log.info("Actualizando personal educativo: " + staff.getName());
                    return institucionalStaffRepository.save(staff);
                });
    }

    public Mono<InstitucionalStaff> changeStatus(String id, String status) {
        log.info("Cambiando estado de personal educativo con ID: " + id);
        return institucionalStaffRepository.findById(id)
                .flatMap(st -> {
                    st.setStatus(status);
                    return institucionalStaffRepository.save(st);
                });
    }

    public Mono<InstitucionalStaff> getById(String id_institucional_staff) {
        log.info("Obteniendo staff con ID: " + id_institucional_staff);
        return institucionalStaffRepository.findById(id_institucional_staff)
                .switchIfEmpty(
                        Mono.error(new RuntimeException("No se encontró staff con ID: " + id_institucional_staff)));
    }

    public Flux<InstitucionalStaffDto> findByStatusAndRolDocente(String status) {
        log.info("Buscando docentes con estado: " + status);
        return institucionalStaffRepository.findByStatusAndRol(status, "docente")
                .flatMap(this::converTo)
                .collectList()
                .flatMapMany(Flux::fromIterable);
    }

    public Flux<Ubigeo> findAllUbigeo() {
        log.info("Listando todos los ubigeos");
        return externalService.findAllUbigeo();
    }

    private Mono<InstitucionalStaffDto> converTo(InstitucionalStaff staff) {
        InstitucionalStaffDto dto = new InstitucionalStaffDto();
        dto.setId(staff.getId());
        dto.setFatherLastname(staff.getFatherLastname());
        dto.setMotherLastname(staff.getMotherLastname());
        dto.setName(staff.getName());
        dto.setDocumentType(staff.getDocumentType());
        dto.setDocumentNumber(staff.getDocumentNumber());
        dto.setBirthdate(staff.getBirthdate());
        dto.setSex(staff.getSex());
        dto.setCountry(staff.getCountry());
        dto.setEmail(staff.getEmail());
        dto.setPhone(staff.getPhone());
        dto.setCivilStatus(staff.getCivilStatus());
        dto.setInstructionGrade(staff.getInstructionGrade());
        dto.setDisabilityType(staff.getDisabilityType());
        dto.setDisability(staff.getDisability());
        dto.setWorkCondition(staff.getWorkCondition());
        dto.setOccupation(staff.getOccupation());
        dto.setNativeLanguage(staff.getNativeLanguage());
        dto.setAddress(staff.getAddress());
        dto.setRol(staff.getRol());
        dto.setStatus(staff.getStatus());

        Mono<Ubigeo> ubigeoMono = externalService.findById(staff.getUbigeo())
                .doOnNext(ubigeo -> {
                    if (ubigeo == null) {
                        System.out.println("Ubigeo no encontrado");
                    }
                });
        return Mono.zip(ubigeoMono, Mono.just(staff.getId()), (ubigeo, id) -> {
            dto.setUbigeo(ubigeo);
            dto.setId(id);
            return dto;
        });

    }

}
