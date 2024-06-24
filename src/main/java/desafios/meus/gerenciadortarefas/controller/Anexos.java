package desafios.meus.gerenciadortarefas.controller;

import desafios.meus.gerenciadortarefas.dto.AnexoDTO;
import desafios.meus.gerenciadortarefas.service.AnexosService;
import desafios.meus.gerenciadortarefas.service.S3Service;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/rest/anexos")
public class Anexos {

    private final AnexosService servico;

    private final S3Service s3;

    private static final String NOME_BUCKET = "gerenciador-tarefas220122";

    public Anexos(AnexosService servico, S3Service s3) {
        this.servico = servico;
        this.s3 = s3;
    }

    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public Flux<AnexoDTO> uploadFiles(@RequestParam("anexo") List<MultipartFile> anexos) {
        return Flux.fromIterable(anexos)
                .flatMap(servico::upload);
    }

    @GetMapping("/{id}/download")
    public Mono<ResponseEntity<Flux<DataBuffer>>> downloadFile(@PathVariable String id) {
        return servico.recuperar(id)
                .flatMap(anexo -> {
                    String key = anexo.getS3Key();
                    Flux<DataBuffer> fileStream = s3.download(NOME_BUCKET, key);

                    return Mono.just(ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + anexo.getNome() + "\"")
                            .contentType(MediaType.APPLICATION_OCTET_STREAM)
                            .body(fileStream));
                });
    }
}
