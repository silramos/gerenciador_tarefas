package desafios.meus.gerenciadortarefas.repository;

import desafios.meus.gerenciadortarefas.model.Anexo;
import desafios.meus.gerenciadortarefas.model.Tarefa;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface AnexosRepository extends ReactiveMongoRepository<Anexo, ObjectId> {
}
