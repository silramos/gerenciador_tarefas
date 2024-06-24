package desafios.meus.gerenciadortarefas.config.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Arrays;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ProblemDetail> lidarComExcecaoDesconhecida(Exception ex) {
        log.error("Erro desconhecido: {}", ex.getMessage(), ex);

        ProblemDetail detalhes = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(detalhes);
    }

    @ExceptionHandler(ErroDeValidacao.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ProblemDetail> lidarComErroDeValidacao(ErroDeValidacao ex) {
        log.error("Erro de validação: {}", ex.getMessage(), ex);

        ProblemDetail detalhes = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        detalhes.setTitle("Erro de validação");
        detalhes.setDetail(ex.getMessage());
        detalhes.setType(URI.create("/erros/erro-de-validacao"));

        return ResponseEntity
                .badRequest()
                .body(detalhes);
    }
}
