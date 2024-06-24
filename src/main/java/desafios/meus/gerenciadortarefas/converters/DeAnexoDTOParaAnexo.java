package desafios.meus.gerenciadortarefas.converters;

import desafios.meus.gerenciadortarefas.dto.AnexoDTO;
import desafios.meus.gerenciadortarefas.model.Anexo;
import org.bson.types.ObjectId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DeAnexoDTOParaAnexo implements Converter<AnexoDTO, Anexo> {

    @Override
    public Anexo convert(AnexoDTO fonte) {
        return Anexo.builder()
                .id(fonte.getId() != null ? new ObjectId(fonte.getId()) : null)
                .nome(fonte.getNome())
                .s3Key(fonte.getS3Key())
                .build();
    }
}
