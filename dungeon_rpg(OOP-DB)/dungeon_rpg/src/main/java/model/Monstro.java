package model;

public class Monstro {
    private String nome;
    private int hp;
    private int hpMaximo;
    private int ataque;
    private int defesa;
    private int xpRecompensa;
    private Elemento elemento;

    public Monstro(String nome, int hp, int ataque, int defesa, int xpRecompensa) {
        this.nome = nome;
        this.hp = hp;
        this.hpMaximo = hp;
        this.ataque = ataque;
        this.defesa = defesa;
        this.xpRecompensa = xpRecompensa;
        this.elemento = Elemento.NEUTRO;
    }

    public Monstro(String nome, int hp, int ataque, int defesa, int xpRecompensa, Elemento elemento) {
        this.nome = nome;
        this.hp = hp;
        this.hpMaximo = hp;
        this.ataque = ataque;
        this.defesa = defesa;
        this.xpRecompensa = xpRecompensa;
        this.elemento = elemento;
    }

    public int getDefesa() {
        return defesa;
    }

    public int getAtaque() {
        return ataque;
    }

    public int getHp() {
        return hp;
    }

    public int getXpRecompensa() {
        return xpRecompensa;
    }

    public boolean estaVivo() {
        return hp > 0;
    }

    public void receberDano(int dano) {
        this.hp -= dano;
        if (this.hp < 0) this.hp = 0;
    }

    public int atacar() {
        return this.ataque;
    }

    public String getNome() {
        return nome;
    }

    public Elemento getElemento() {
        return elemento;
    }

    public String getNomeCompleto() {
        return nome + " " + elemento.getEmoji();
    }

    @Override
    public String toString() {
        return getNomeCompleto() + " (HP: " + hp + "/" + hpMaximo + ", ATK: " + ataque + ", DEF: " + defesa + ")";
    }
}
