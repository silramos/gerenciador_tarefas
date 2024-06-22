package desafios.meus.gerenciadortarefas.dto;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
public class AnexoDTO {

    private String id;

    private String nome;

    private String s3Key;
}
