package desafios.meus.gerenciadortarefas.controller;

import desafios.meus.gerenciadortarefas.dto.TarefaDTO;
import desafios.meus.gerenciadortarefas.enums.StatusEnum;
import desafios.meus.gerenciadortarefas.model.Tarefa;
import desafios.meus.gerenciadortarefas.service.TarefasService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/tarefas")
public class Tarefas {

    private final TarefasService servico;

    @Autowired
    public Tarefas(TarefasService servico) {
        this.servico = servico;
    }

    @GetMapping
    public Flux<TarefaDTO> obterTodas(@RequestParam(required = false) StatusEnum status) {
        return servico.obterTodasPor(status);
    }

    @PostMapping
    public Mono<TarefaDTO> inserir(@RequestBody TarefaDTO tarefa) {
        return servico.inserir(tarefa);
    }

    @PutMapping("/{id}")
    public Mono<TarefaDTO> atualizar(@PathVariable ObjectId id, @RequestBody TarefaDTO tarefa) {
        return servico.atualizar(id, tarefa);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> remover(@PathVariable ObjectId id) {
        return servico.remover(id);
    }
}
