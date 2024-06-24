package desafios.meus.gerenciadortarefas.service;

import desafios.meus.gerenciadortarefas.config.exceptions.ErroDeValidacao;
import desafios.meus.gerenciadortarefas.converters.DeAnexoDTOParaAnexo;
import desafios.meus.gerenciadortarefas.converters.DeAnexoParaAnexoDTO;
import desafios.meus.gerenciadortarefas.dto.AnexoDTO;
import desafios.meus.gerenciadortarefas.repository.AnexosRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.File;
import java.nio.file.Files;
import java.util.Objects;

@Service
public class AnexosService {

    AnexosRepository repositorio;

    DeAnexoDTOParaAnexo conversorDeDTO;

    DeAnexoParaAnexoDTO conversorDeAnexo;

    private final S3Service s3;

    private static final String NOME_BUCKET = "gerenciador-tarefas220122";

    @Autowired
    public AnexosService(AnexosRepository repositorio, DeAnexoDTOParaAnexo deDTO, DeAnexoParaAnexoDTO deAnexo, S3Service s3Service) {
        this.repositorio = repositorio;
        this.conversorDeDTO = deDTO;
        this.conversorDeAnexo = deAnexo;
        this.s3 = s3Service;
    }

    public Mono<AnexoDTO> upload(MultipartFile anexo) {
        String key = System.currentTimeMillis() + "_" + anexo.getOriginalFilename();

        return Mono.fromCallable(() -> {
                    File temporario = File.createTempFile("upload", null);
                    anexo.transferTo(temporario);
                    return temporario;
                })
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(temporario -> Mono.fromCallable(() -> {
                            s3.upload(NOME_BUCKET, key, temporario.getPath());
                            return temporario;
                        })
                        .subscribeOn(Schedulers.boundedElastic()))
                .flatMap(temporario -> Mono.fromCallable(() -> {
                            Files.delete(temporario.toPath());
                            return temporario;
                        })
                        .subscribeOn(Schedulers.boundedElastic()))
                .flatMap(temporario -> {
                    AnexoDTO dto = AnexoDTO.builder()
                            .nome(anexo.getOriginalFilename())
                            .s3Key(key)
                            .build();

                    return inserir(dto);
                });
    }

    public Mono<AnexoDTO> inserir(AnexoDTO anexoDTO) {
        return repositorio
                .save(Objects.requireNonNull(conversorDeDTO.convert(anexoDTO)))
                .mapNotNull(conversorDeAnexo::convert);
    }

    public Mono<AnexoDTO> recuperar(String id) {
        return repositorio.findById(new ObjectId(id))
                .switchIfEmpty(Mono.error(new ErroDeValidacao("Não foi encontrado o recurso.")))
                .mapNotNull(conversorDeAnexo::convert);
    }
}
