package desafios.meus.gerenciadortarefas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Paths;

@Service
public class S3Service {

    private final S3Client s3Client;

    private static final int BUFFER_SIZE = 4096;

    @Autowired
    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public void upload(String bucketName, String key, String filePath) {
        s3Client.putObject(PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build(),
                RequestBody.fromFile(Paths.get(filePath)));
    }

    public Flux<DataBuffer> download(String bucketName, String key) {
        return Mono.fromCallable(() -> s3Client.getObject(GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build()))
                .flatMapMany(responseS3 -> {
                    ReadableByteChannel channel = Channels.newChannel(responseS3);
                    DataBufferFactory bufferFactory = new DefaultDataBufferFactory();

                    return DataBufferUtils.readByteChannel(() -> channel, bufferFactory, BUFFER_SIZE);
                });
    }
}
