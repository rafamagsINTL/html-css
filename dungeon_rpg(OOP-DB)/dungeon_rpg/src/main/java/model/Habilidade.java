package model;

public class Habilidade {
    private String nome;
    private String descricao;
    private int bonusAtaque;
    private int danoBase;
    private Elemento elemento;

    public Habilidade(String nome, String descricao, int bonusAtaque, int danoBase, Elemento elemento) {
        this.nome = nome;
        this.descricao = descricao;
        this.bonusAtaque = bonusAtaque;
        this.danoBase = danoBase;
        this.elemento = elemento;
    }

    public Habilidade(String nome, String descricao, int bonusAtaque) {
        this.nome = nome;
        this.descricao = descricao;
        this.bonusAtaque = bonusAtaque;
        this.danoBase = 10; // Dano padrão das habilidades
        this.elemento = Elemento.NEUTRO;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getBonusAtaque() {
        return bonusAtaque;
    }

    public int getDanoBase() {
        return danoBase;
    }

    public Elemento getElemento() {
        return elemento;
    }

    public String getNomeCompleto() {
        return nome + " " + elemento.getEmoji();
    }
}
