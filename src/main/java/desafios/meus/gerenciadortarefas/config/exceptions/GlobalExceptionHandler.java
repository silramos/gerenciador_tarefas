package desafios.meus.gerenciadortarefas.config.exceptions;

import org.springframework.boot.autoconfigure.web.reactive.WebFluxProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<ResponseEntity<ProblemDetail>> handleGeneralException(Exception ex) {
        ProblemDetail detalhes = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());

        return Mono.just(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(detalhes));
    }

    @ExceptionHandler(ErroDeValidacao.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ResponseEntity<ProblemDetail>> handleGeneralException(ErroDeValidacao ex) {
        ProblemDetail detalhes = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        detalhes.setTitle("Erro de validação");
        detalhes.setDetail(ex.getMessage());
        detalhes.setType(URI.create("/erros/erro-de-validacao"));

        return Mono.just(ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(detalhes));
    }
}
