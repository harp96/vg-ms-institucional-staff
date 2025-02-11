package pe.edu.vallegrande.vgmsinstitucionalstaff.domain.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import pe.edu.vallegrande.vgmsinstitucionalstaff.domain.model.InstitucionalStaff;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface InstitucionalStaffRepository extends ReactiveMongoRepository<InstitucionalStaff, String>{

    Flux<InstitucionalStaff> findByStatus(String status);
    Mono<Long> countByStatus(String status);
    Flux<InstitucionalStaff> findByStatusAndRol(String status, String rol);
}