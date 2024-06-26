package desafios.meus.gerenciadortarefas.service;

import desafios.meus.gerenciadortarefas.config.exceptions.ErroDeValidacao;
import desafios.meus.gerenciadortarefas.converters.DeAnexoDTOParaAnexo;
import desafios.meus.gerenciadortarefas.converters.DeAnexoParaAnexoDTO;
import desafios.meus.gerenciadortarefas.dto.AnexoDTO;
import desafios.meus.gerenciadortarefas.repository.AnexosRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.File;
import java.nio.file.Files;
import java.util.Objects;

@Service
public class AnexosService {

    final AnexosRepository repositorio;

    final DeAnexoDTOParaAnexo conversorDeDTO;

    final DeAnexoParaAnexoDTO conversorDeAnexo;

    private final S3Service s3;

    @Value("${cloud.aws.bucket-name}")
    private String nomeBucket;

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
                            s3.upload(nomeBucket, key, temporario.getPath());
                            return temporario;
                        })
                        .subscribeOn(Schedulers.boundedElastic()))
                .flatMap(temporario -> Mono.fromCallable(() -> {
                            Files.delete(temporario.toPath());
                            return temporario;
                        })
                        .subscribeOn(Schedulers.boundedElastic()))
                .flatMap(ignored -> {
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
