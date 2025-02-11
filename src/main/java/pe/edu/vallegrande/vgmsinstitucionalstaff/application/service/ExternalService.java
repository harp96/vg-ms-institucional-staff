package pe.edu.vallegrande.vgmsinstitucionalstaff.application.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;
import pe.edu.vallegrande.vgmsinstitucionalstaff.domain.dto.Ubigeo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class ExternalService {

    @Value("${services.student.url}")
    private String ubigeoServiceUrl;

    private final WebClient.Builder webClientBuilder;

    public ExternalService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public Mono<Ubigeo> findById(String ubigeoReniec) {
        return fetchData(ubigeoServiceUrl + "/list/ubigeoReniec/", 
                ubigeoReniec, Ubigeo.class);
    }

    public Flux<Ubigeo> findAllUbigeo(){
        return fetchDataList(ubigeoServiceUrl + "/list",
                 Ubigeo.class);
    }

    private <T> Mono<T> fetchData(String baseUrl, String ubigeoReniec, Class<T> responseType) {
        return webClientBuilder.build()
                .get()
                .uri(baseUrl + ubigeoReniec)
                .retrieve()
                .bodyToMono(responseType)
                .onErrorResume(e -> {
                    log.error("Error fetching data: ", e);
                    return Mono.empty();
                });
    }

    private <T> Flux<T> fetchDataList(String baseUrl, Class<T> responseType) {
        return webClientBuilder.build()
                .get()
                .uri(baseUrl) 
                .retrieve()
                .bodyToFlux(responseType) 
                .onErrorResume(e -> {
                    log.error("Error fetching data: ", e);
                    return Flux.empty(); 
                });
    }

}
