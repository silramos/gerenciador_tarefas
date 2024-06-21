package desafios.meus.gerenciadortarefas.enums;

import lombok.Getter;


@Getter
public enum StatusEnum {

    PENDENTE("pendente"),
    CONCLUIDA("concluida");

    private final String nome;

    StatusEnum(String nome) {
        this.nome = nome;
    }

    public static StatusEnum getEnum(String nome) {
        for (StatusEnum statusEnum : StatusEnum.values()) {
            if (statusEnum.getNome().equals(nome)) {
                return statusEnum;
            }
        }

        return null;
    }
}
