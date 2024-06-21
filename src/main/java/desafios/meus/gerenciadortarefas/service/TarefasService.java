package desafios.meus.gerenciadortarefas.service;

import desafios.meus.gerenciadortarefas.config.exceptions.ErroDeValidacao;
import desafios.meus.gerenciadortarefas.converters.DeTarefaDTOParaTarefa;
import desafios.meus.gerenciadortarefas.converters.DeTarefaParaTarefaDTO;
import desafios.meus.gerenciadortarefas.dto.TarefaDTO;
import desafios.meus.gerenciadortarefas.enums.StatusEnum;
import desafios.meus.gerenciadortarefas.repository.TarefasRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Optional;

@Service
public class TarefasService {

    private final TarefasRepository repositorio;

    private final DeTarefaParaTarefaDTO conversor;

    private final DeTarefaDTOParaTarefa conversorDeDTO;

    @Autowired
    public TarefasService(TarefasRepository repositorio, DeTarefaParaTarefaDTO conversor, DeTarefaDTOParaTarefa conversorDeDTO) {
        this.repositorio = repositorio;
        this.conversor = conversor;
        this.conversorDeDTO = conversorDeDTO;
    }

    public Flux<TarefaDTO> obterTodasPor(StatusEnum status) {
        return Flux.defer(() ->
                Optional.ofNullable(status)
                        .map(s -> repositorio.findByStatus(s.getNome()))
                        .orElseGet(repositorio::findAll)
                        .mapNotNull(conversor::convert)
        );
    }

    public Mono<TarefaDTO> inserir(TarefaDTO tarefa) {
        Optional<String> idOptional = Optional.ofNullable(tarefa.getId());

        return idOptional.map(id -> repositorio.existsByIdOrTitulo(new ObjectId(id), tarefa.getTitulo())
                .flatMap(existe -> {
                    if (existe) {
                        return Mono.error(new ErroDeValidacao("Essa tarefa já existe."));
                    } else {
                        return salvar(tarefa);
                    }
                })).orElseGet(() -> repositorio.existsByTitulo(tarefa.getTitulo())
                .flatMap(existe -> {
                    if (existe) {
                        return Mono.error(new ErroDeValidacao("Já existe uma tarefa com esse título"));
                    } else {
                        return salvar(tarefa);
                    }
                }));
    }

    private Mono<TarefaDTO> salvar(TarefaDTO tarefa) {
        return repositorio.save(Objects.requireNonNull(conversorDeDTO.convert(tarefa)))
                .mapNotNull(conversor::convert);
    }

    public Mono<TarefaDTO> atualizar(ObjectId id, TarefaDTO tarefa) {
        if (tarefa.getId() != null && !id.toHexString().equals(tarefa.getId())) {
            throw new ErroDeValidacao("O ID do recurso e do payload deveriam ser iguais.");
        }

        return repositorio.findById(id)
                .switchIfEmpty(Mono.error(new ErroDeValidacao("Não foi encontrado o recurso.")))
                .flatMap(encontrada -> {
                    encontrada.setTitulo(tarefa.getTitulo());
                    encontrada.setDescricao(tarefa.getDescricao());
                    encontrada.setStatus(tarefa.getStatus());
                    encontrada.setPrioridade(tarefa.getPrioridade());
                    encontrada.setDataCriacao(tarefa.getDataCriacao());
                    encontrada.setDataConclusao(tarefa.getDataConclusao());
                    encontrada.setResponsavel(tarefa.getResponsavel());

                    return repositorio.save(encontrada);
                })
                .mapNotNull(conversor::convert);
    }

    public Mono<Void> remover(ObjectId id) {
        return repositorio.findById(id)
                .switchIfEmpty(Mono.error(new ErroDeValidacao("Não foi encontrado o recurso.")))
                .flatMap(tarefa -> repositorio.deleteById(id));
    }
}
