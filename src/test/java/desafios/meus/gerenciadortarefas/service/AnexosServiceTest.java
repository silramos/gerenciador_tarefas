package desafios.meus.gerenciadortarefas.service;

import static org.junit.jupiter.api.Assertions.*;

import desafios.meus.gerenciadortarefas.converters.DeAnexoDTOParaAnexo;
import desafios.meus.gerenciadortarefas.converters.DeAnexoParaAnexoDTO;
import desafios.meus.gerenciadortarefas.dto.AnexoDTO;
import desafios.meus.gerenciadortarefas.model.Anexo;
import desafios.meus.gerenciadortarefas.repository.AnexosRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnexosServiceTest {

    @Mock
    private AnexosRepository repositorioMock;

    @Mock
    private DeAnexoDTOParaAnexo deDTOConverterMock;

    @Mock
    private DeAnexoParaAnexoDTO deAnexoConverterMock;

    @Mock
    private S3Service s3ServiceMock;

    private AnexosService servico;

    private static final ObjectId UM_ID = new ObjectId("667a46489527fe6f78156731");

    private static final String UM_NOME_ARQUIVO_TXT = "arquivo-teste.txt";

    private static final String UMA_KEY_S3 = "abcd1234";

    private AnexoDTO resultado;

    @BeforeEach
    public void setUp() {
        servico = new AnexosService(repositorioMock, deDTOConverterMock, deAnexoConverterMock, s3ServiceMock);

        resultado = null;
    }

    @Nested
    class Upload {

        @BeforeEach
        void setUp() {
            MultipartFile arquivo = new MockMultipartFile(
                    UM_NOME_ARQUIVO_TXT, UM_NOME_ARQUIVO_TXT, "", "".getBytes());

            doNothing().when(s3ServiceMock).upload(any(), any(), any());

            Anexo anexoConvertido = Anexo.builder().build();
            when(deDTOConverterMock.convert(any())).thenReturn(anexoConvertido);

            Anexo anexoSalvo = Anexo.builder()
                    .id(UM_ID)
                    .s3Key(UMA_KEY_S3)
                    .nome(arquivo.getOriginalFilename())
                    .build();
            when(repositorioMock.save(any())).thenReturn(Mono.just(anexoSalvo));

            AnexoDTO anexoDTOConvertido = AnexoDTO.builder()
                    .id(anexoSalvo.getId().toHexString())
                    .s3Key(anexoSalvo.getS3Key())
                    .nome(anexoSalvo.getNome())
                    .build();
            when(deAnexoConverterMock.convert(any())).thenReturn(anexoDTOConvertido);

            resultado = servico.upload(arquivo).block();
        }

        @Test
        void testNotNull() {
            assertNotNull(resultado);
        }

        @Test
        void testNomeArquivo() {
            assertEquals(UM_NOME_ARQUIVO_TXT, Objects.requireNonNull(resultado).getNome());
        }

        @Test
        void testChamouS3() {
            verify(s3ServiceMock, times(1)).upload(anyString(), any(), any());
        }

        @Test
        void testSalvou() {
            verify(repositorioMock, times(1)).save(any());
        }
    }

    @Nested
    class Inserir {

        @BeforeEach
        void setUp() {
            AnexoDTO anexoASerInserido = AnexoDTO.builder()
                    .nome(UM_NOME_ARQUIVO_TXT)
                    .s3Key(UMA_KEY_S3)
                    .build();

            Anexo anexoASerInseridoConvertido = Anexo.builder()
                    .nome(anexoASerInserido.getNome())
                    .s3Key(anexoASerInserido.getS3Key())
                    .build();
            when(deDTOConverterMock.convert(any())).thenReturn(anexoASerInseridoConvertido);

            Anexo anexoSalvo = Anexo.builder()
                    .id(UM_ID)
                    .nome(anexoASerInseridoConvertido.getNome())
                    .s3Key(anexoASerInseridoConvertido.getS3Key())
                    .build();
            when(repositorioMock.save(any())).thenReturn(Mono.just(anexoSalvo));

            AnexoDTO anexoSalvoConvertido = AnexoDTO.builder()
                    .id(anexoSalvo.getId().toHexString())
                    .nome(anexoSalvo.getNome())
                    .s3Key(anexoSalvo.getS3Key())
                    .build();
            when(deAnexoConverterMock.convert(any())).thenReturn(anexoSalvoConvertido);

            resultado = servico.inserir(anexoASerInserido).block();
        }

        @Test
        void testNotNull() {
            assertNotNull(resultado);
        }

        @Test
        void testSalvou() {
            verify(repositorioMock, times(1)).save(any());
        }

        @Test
        void testId() {
            assertEquals(UM_ID.toHexString(), resultado.getId());
        }
    }

    @Nested
    class Recuperar {

        @BeforeEach
        void setUp() {
            Anexo anexoEncontrado = Anexo.builder()
                    .id(UM_ID)
                    .nome(UM_NOME_ARQUIVO_TXT)
                    .s3Key(UMA_KEY_S3)
                    .build();
            when(repositorioMock.findById(any(ObjectId.class))).thenReturn(Mono.just(anexoEncontrado));

            AnexoDTO anexoEncontradoConvertido = AnexoDTO.builder()
                    .id(anexoEncontrado.getId().toHexString())
                    .nome(anexoEncontrado.getNome())
                    .s3Key(anexoEncontrado.getS3Key())
                    .build();
            when(deAnexoConverterMock.convert(any())).thenReturn(anexoEncontradoConvertido);

            resultado = servico.recuperar(UM_ID.toHexString()).block();
        }

        @Test
        void testNotNull() {
            assertNotNull(resultado);
        }

        @Test
        void testConsultou() {
            verify(repositorioMock, times(1)).findById(any(ObjectId.class));
        }

        @Test
        void testId() {
            assertEquals(UM_ID.toHexString(), resultado.getId());
        }
    }

    @Test
    void testRepositorioNotNull() {
        assertNotNull(repositorioMock);
    }

    @Test
    void testDeAnexoDTOParaAnexoNotNull() {
        assertNotNull(deDTOConverterMock);
    }

    @Test
    void testDeAnexoParaAnexoDTONotNull() {
        assertNotNull(deAnexoConverterMock);
    }

    @Test
    void testDeS3NotNull() {
        assertNotNull(s3ServiceMock);
    }
}
