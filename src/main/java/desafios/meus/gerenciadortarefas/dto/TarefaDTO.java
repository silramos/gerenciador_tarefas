package desafios.meus.gerenciadortarefas.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import desafios.meus.gerenciadortarefas.enums.StatusEnum;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;


@Data
@Builder
public class TarefaDTO {

    private String id;

    private String titulo;

    private String descricao;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataCriacao;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataConclusao;

    private StatusEnum status;

    private String prioridade;

    private String responsavel;

    @JsonIgnore
    private Set<String> anexos;

    @JsonProperty("anexos")
    public Set<String> getAnexos() {
        return anexos;
    }

    public void addAnexo(String anexo) {
        this.anexos.add(anexo);
    }
}
