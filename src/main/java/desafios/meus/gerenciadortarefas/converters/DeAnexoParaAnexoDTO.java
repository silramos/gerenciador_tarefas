package desafios.meus.gerenciadortarefas.converters;

import desafios.meus.gerenciadortarefas.dto.AnexoDTO;
import desafios.meus.gerenciadortarefas.model.Anexo;
import org.bson.types.ObjectId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DeAnexoParaAnexoDTO implements Converter<Anexo, AnexoDTO> {

    @Override
    public AnexoDTO convert(Anexo fonte) {
        return AnexoDTO.builder()
                .id(fonte.getId() != null ? fonte.getId().toHexString() : null)
                .nome(fonte.getNome())
                .s3Key(fonte.getS3Key())
                .build();
    }
}
