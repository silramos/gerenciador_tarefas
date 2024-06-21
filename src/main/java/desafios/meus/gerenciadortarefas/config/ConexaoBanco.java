package desafios.meus.gerenciadortarefas.config;

import com.mongodb.lang.NonNull;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;

@Configuration
public class ConexaoBanco extends AbstractReactiveMongoConfiguration {

    public @Bean MongoClient mongoClient() {
        return MongoClients.create();
    }

    @Override
    @NonNull
    protected String getDatabaseName() {
        return "gerenciador_tarefas";
    }
}
