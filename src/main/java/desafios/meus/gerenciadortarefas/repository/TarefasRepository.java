package desafios.meus.gerenciadortarefas.repository;

import desafios.meus.gerenciadortarefas.model.Tarefa;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
public interface TarefasRepository extends ReactiveMongoRepository<Tarefa, ObjectId> {

    Flux<Tarefa> findByStatus(String status);

    Mono<Boolean> existsByIdOrTitulo(ObjectId id, String titulo);

    Mono<Boolean> existsByTitulo(String titulo);

    Mono<Boolean> existsByTituloAndIdNot(String titulo, ObjectId id);
}
