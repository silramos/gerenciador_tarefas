package desafios.meus.gerenciadortarefas.repository;

import desafios.meus.gerenciadortarefas.model.Anexo;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnexosRepository extends ReactiveMongoRepository<Anexo, ObjectId> {
}
