package desafios.meus.gerenciadortarefas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

    @Bean
    public S3Client s3Client() {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(
                "AKIAYS2NWB563VLQQNAF",
                "RVm1VtZIB21x1HayAArPkJub5pel0q1PuQzRxmtH");

        return S3Client.builder()
                .region(Region.US_EAST_2)  // Set your region
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
    }
}
