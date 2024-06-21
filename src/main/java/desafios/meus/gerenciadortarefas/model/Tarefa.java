package desafios.meus.gerenciadortarefas.model;

import desafios.meus.gerenciadortarefas.enums.StatusEnum;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(value = "tarefa")
public class Tarefa {

    @Id
    private ObjectId id;

    private String titulo;

    private String descricao;

    private LocalDateTime dataCriacao;

    private LocalDateTime dataConclusao;

    private String status;

    private String prioridade;

    private String responsavel;

    public StatusEnum getStatus() {
        return StatusEnum.getEnum(this.status);
    }

    public void setStatus(StatusEnum status) {
        this.status = status.getNome();
    }
}
