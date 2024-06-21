package desafios.meus.gerenciadortarefas.config.exceptions;

public class ErroDeValidacao extends RuntimeException {

    public ErroDeValidacao (String mensagem) {
        super(mensagem);
    }
}
