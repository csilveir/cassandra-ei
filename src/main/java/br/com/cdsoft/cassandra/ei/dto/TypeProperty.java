package br.com.cdsoft.cassandra.ei.dto;

public enum TypeProperty {

    CONTACORRENTE(1, "Conta Corrente"),
    CREDITO(2, "Crédito");
    private int valor;

    public int getValor() {
        return valor;
    }


    public String getDescricao() {
        return descricao;
    }

    private String descricao;

    private TypeProperty(final int valor, final String descricao) {

        this.valor = valor;
        this.descricao = descricao;

    }
}
