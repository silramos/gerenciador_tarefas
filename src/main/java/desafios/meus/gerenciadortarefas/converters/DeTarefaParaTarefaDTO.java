package desafios.meus.gerenciadortarefas.converters;

import desafios.meus.gerenciadortarefas.dto.TarefaDTO;
import desafios.meus.gerenciadortarefas.model.Tarefa;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DeTarefaParaTarefaDTO implements Converter<Tarefa, TarefaDTO> {

    @Override
    public TarefaDTO convert(Tarefa fonte) {
        return TarefaDTO.builder()
                .id(fonte.getId() != null ? fonte.getId().toHexString() : null)
                .dataConclusao(fonte.getDataConclusao())
                .dataCriacao(fonte.getDataCriacao())
                .descricao(fonte.getDescricao())
                .prioridade(fonte.getPrioridade())
                .responsavel(fonte.getResponsavel())
                .status(fonte.getStatus())
                .titulo(fonte.getTitulo())
                .build();
    }
}
