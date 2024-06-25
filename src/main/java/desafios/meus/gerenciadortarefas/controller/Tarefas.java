package desafios.meus.gerenciadortarefas.controller;

import desafios.meus.gerenciadortarefas.dto.VinculadorAnexosDTO;
import desafios.meus.gerenciadortarefas.dto.TarefaDTO;
import desafios.meus.gerenciadortarefas.enums.StatusEnum;
import desafios.meus.gerenciadortarefas.service.TarefasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/rest/tarefas")
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
    public Mono<TarefaDTO> atualizar(@PathVariable String id, @RequestBody TarefaDTO tarefa) {
        return servico.atualizar(id, tarefa);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> remover(@PathVariable String id) {
        return servico.remover(id);
    }

    @PutMapping("/{id}/anexos")
    public Mono<TarefaDTO> adicionarAnexo(@PathVariable String id, @RequestBody VinculadorAnexosDTO idsAnexos) {
        return servico.recuperar(id)
                .flatMap(tarefa -> {
                    idsAnexos.getIdsAnexos().forEach(tarefa::addAnexo);

                    return servico.atualizar(id, tarefa);
                });
    }
}
