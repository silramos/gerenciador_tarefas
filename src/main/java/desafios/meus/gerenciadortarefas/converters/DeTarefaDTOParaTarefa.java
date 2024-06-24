package desafios.meus.gerenciadortarefas.converters;

import desafios.meus.gerenciadortarefas.dto.TarefaDTO;
import desafios.meus.gerenciadortarefas.model.Tarefa;
import org.bson.types.ObjectId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class DeTarefaDTOParaTarefa implements Converter<TarefaDTO, Tarefa> {

    @Override
    public Tarefa convert(TarefaDTO fonte) {
        return Tarefa.builder()
                .id(fonte.getId() != null ? new ObjectId(fonte.getId()) : null)
                .dataConclusao(fonte.getDataConclusao())
                .dataCriacao(fonte.getDataCriacao())
                .descricao(fonte.getDescricao())
                .prioridade(fonte.getPrioridade())
                .responsavel(fonte.getResponsavel())
                .status(fonte.getStatus() != null ? fonte.getStatus().getNome() : null)
                .titulo(fonte.getTitulo())
                .anexos(Optional.ofNullable(fonte.getAnexos()).orElseGet(HashSet::new)
                        .stream()
                        .filter(Objects::nonNull)
                        .map(ObjectId::new)
                        .collect(Collectors.toSet()))
                .build();
    }
}
