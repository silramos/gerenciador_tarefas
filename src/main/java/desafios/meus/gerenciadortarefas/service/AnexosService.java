package desafios.meus.gerenciadortarefas.service;

import desafios.meus.gerenciadortarefas.config.exceptions.ErroDeValidacao;
import desafios.meus.gerenciadortarefas.converters.DeAnexoDTOParaAnexo;
import desafios.meus.gerenciadortarefas.converters.DeAnexoParaAnexoDTO;
import desafios.meus.gerenciadortarefas.dto.AnexoDTO;
import desafios.meus.gerenciadortarefas.repository.AnexosRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
public class AnexosService {

    AnexosRepository repositorio;

    DeAnexoDTOParaAnexo conversorDeDTO;

    DeAnexoParaAnexoDTO conversorDeAnexo;

    @Autowired
    public AnexosService(AnexosRepository repositorio, DeAnexoDTOParaAnexo deDTO) {
        this.repositorio = repositorio;
        this.conversorDeDTO = deDTO;
    }

    public Mono<AnexoDTO> inserir(AnexoDTO anexoDTO) {
        return repositorio.save(Objects.requireNonNull(conversorDeDTO.convert(anexoDTO)))
                .mapNotNull(conversorDeAnexo::convert);
    }

    public Mono<AnexoDTO> recuperar(String id) {
        return repositorio.findById(new ObjectId(id))
                .switchIfEmpty(Mono.error(new ErroDeValidacao("Não foi encontrado o recurso.")))
                .mapNotNull(conversorDeAnexo::convert);
    }
}
