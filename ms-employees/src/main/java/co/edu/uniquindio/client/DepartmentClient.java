package co.edu.uniquindio.client;


import co.edu.uniquindio.dto.DepartmentDTO;
import co.edu.uniquindio.exception.DepartmentNotFoundException;
import co.edu.uniquindio.exception.DepartmentUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class DepartmentClient {

    private final WebClient webClient;

    public DepartmentClient(WebClient departmentWebClient){
        this.webClient = departmentWebClient;
    }

    @CircuitBreaker(name = "departmentService", fallbackMethod = "fallback")
    @Retry(name = "departmentService")
    public DepartmentDTO getDepartmentById(Long id){
        return webClient.get()
                .uri("/api/departments/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.error(new DepartmentNotFoundException(id)))
                .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.error(new DepartmentUnavailableException(id)))
                .bodyToMono(DepartmentDTO.class)
                .block();
    }

    public DepartmentDTO fallback(Long id, Throwable ex){
        if (ex instanceof DepartmentNotFoundException) {
            throw (DepartmentNotFoundException) ex;
        }

        throw new DepartmentUnavailableException(id);
    }



}
