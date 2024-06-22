package desafios.meus.gerenciadortarefas.model;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "anexos")
public class Anexo {

    @Id
    private ObjectId id;

    private String nome;

    private String s3Key;
}
