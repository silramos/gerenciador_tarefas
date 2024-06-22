package desafios.meus.gerenciadortarefas.controller;

import desafios.meus.gerenciadortarefas.dto.AnexoDTO;
import desafios.meus.gerenciadortarefas.service.AnexosService;
import desafios.meus.gerenciadortarefas.service.S3Service;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.nio.file.Files;

@RestController
@RequestMapping("/anexos")
public class Anexos {

    private final AnexosService servico;

    private final S3Service s3;

    private static final String NOME_BUCKET = "gerenciador-tarefas220122";

    public Anexos(AnexosService servico, S3Service s3) {
        this.servico = servico;
        this.s3 = s3;
    }

    @PostMapping("/upload")
    public Mono<AnexoDTO> uploadFile(@RequestPart("anexo") FilePart anexoBin) {
        String key = System.currentTimeMillis() + "_" + anexoBin.filename();

        return Mono.fromCallable(() -> Files.createTempFile("upload", null))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(anexoTemp -> anexoBin.transferTo(anexoTemp)
                        .then(Mono.fromCallable(() -> {
                            s3.upload(NOME_BUCKET, key, anexoTemp.toString());
                            Files.deleteIfExists(anexoTemp);
                            return anexoTemp;
                        }).subscribeOn(Schedulers.boundedElastic()))
                )
                .flatMap(anexoTemp -> {
                    AnexoDTO dto = AnexoDTO.builder()
                            .nome(anexoBin.filename())
                            .s3Key(key)
                            .build();
                    return servico.inserir(dto);
                });
    }

    @GetMapping("/download/{id}")
    public Mono<Void> downloadFile(@PathVariable String id, ServerHttpResponse resposta) {
        return servico.recuperar(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Recurso não encontrado")))
                .flatMap(anexo -> {
                    String key = anexo.getS3Key();

                    resposta.getHeaders().set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + anexo.getNome() + "\"");
                    resposta.getHeaders().setContentType(MediaType.APPLICATION_OCTET_STREAM);

                    return resposta.writeWith(s3.download(NOME_BUCKET, key));
                });
    }
}
