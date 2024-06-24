package desafios.meus.gerenciadortarefas.dto;

import lombok.*;

@Data
@Builder
public class AnexoDTO {

    private String id;

    private String nome;

    private String s3Key;
}
