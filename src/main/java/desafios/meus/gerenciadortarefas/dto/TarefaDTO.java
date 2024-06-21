package desafios.meus.gerenciadortarefas.dto;

import desafios.meus.gerenciadortarefas.enums.StatusEnum;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Builder
public class TarefaDTO {

    private String id;

    private String titulo;

    private String descricao;

    private LocalDateTime dataCriacao;

    private LocalDateTime dataConclusao;

    private StatusEnum status;

    private String prioridade;

    private String responsavel;
}
