package model;

import java.util.ArrayList;
import java.util.List;

public class Personagem {
    private String nome;
    private int nivel;
    private int xp;
    private int xpParaProximoNivel;
    private int hp;
    private int hpMaximo;
    private int ataque;
    private int defesa;
    private int pocoes;
    private List<Habilidade> habilidades;
    private HabilidadeCallback habilidadeCallback;

    public Personagem(String nome) {
        this.nome = nome;
        this.nivel = 1;
        this.xp = 0;
        this.xpParaProximoNivel = 100;
        this.hp = 100;
        this.hpMaximo = 100;
        this.ataque = 10;
        this.defesa = 5;
        this.pocoes = 0;
        this.habilidades = new ArrayList<>();

        // Habilidade inicial
        Habilidade habilidadeInicial = new Habilidade("Golpe Básico", "Um ataque simples mas eficaz", 0);
        this.habilidades.add(habilidadeInicial);
    }

    public void setHabilidadeCallback(HabilidadeCallback callback) {
        this.habilidadeCallback = callback;
    }

    public void usarHabilidade(Habilidade habilidade, Monstro m) {
        int danoBase = habilidade.getDanoBase() + this.ataque;

        // Calcular multiplicador elemental
        double multiplicadorElemental = habilidade.getElemento().getMultiplicadorContra(m.getElemento());

        int dano = (int) Math.round((danoBase * multiplicadorElemental) - m.getDefesa());
        dano = Math.max(1, dano); // Dano mínimo é 1

        m.receberDano(dano);

        String mensagem = nome + " usou " + habilidade.getNomeCompleto() + " em " + m.getNomeCompleto() + " causando " + dano + " de dano!";

        // Mostrar efetividade elemental
        if (multiplicadorElemental > 1.0) {
            mensagem += " ✅ Super eficaz!";
        } else if (multiplicadorElemental < 1.0) {
            mensagem += " ❌ Pouco eficaz...";
        }

        System.out.println(mensagem);
    }

    public void receberDano(int dano) {
        int danoReal = Math.max(1, dano - this.defesa);
        this.hp -= danoReal;
        if (this.hp < 0) this.hp = 0;
        System.out.println(nome + " recebeu " + danoReal + " de dano!");
    }

    public void ganharXp(int xpGanho) {
        this.xp += xpGanho;
        System.out.println("+" + xpGanho + " XP! Total: " + this.xp + "/" + this.xpParaProximoNivel);

        // Verificar múltiplos level ups
        while (this.xp >= this.xpParaProximoNivel) {
            int xpSobrando = this.xp - this.xpParaProximoNivel;
            subirNivel();
            this.xp = xpSobrando; // Manter XP que sobrou para o próximo nível
        }
    }

    private void subirNivel() {
        this.nivel++;
        this.xpParaProximoNivel = (int)(this.xpParaProximoNivel * 1.5);

        // Melhorar atributos
        int hpAntigo = this.hpMaximo;
        this.hpMaximo += 20;
        this.ataque += 5;
        this.defesa += 3;
        this.hp = this.hpMaximo; // Cura completa ao subir de nível

        System.out.println("\n🎉 LEVEL UP! 🎉");
        System.out.println("Nível: " + this.nivel);
        System.out.println("HP: " + hpAntigo + " → " + this.hpMaximo);
        System.out.println("Ataque: " + (this.ataque - 5) + " → " + this.ataque);
        System.out.println("Defesa: " + (this.defesa - 3) + " → " + this.defesa);
        System.out.println("Próximo nível: " + this.xpParaProximoNivel + " XP");

        // Chamar escolha de habilidade através do callback
        if (habilidadeCallback != null) {
            habilidadeCallback.oferecerEscolhaHabilidade(this);
        }
    }

    public void usarPocao() {
        if (pocoes > 0) {
            pocoes--;
            int cura = 50;
            int hpAntigo = this.hp;
            this.hp = Math.min(this.hpMaximo, this.hp + cura);
            System.out.println("Você usou uma poção! HP: " + hpAntigo + " → " + this.hp);
            System.out.println("Poções restantes: " + pocoes);
        } else {
            System.out.println("Você não tem poções!");
        }
    }

    public void ganharPocao() {
        pocoes++;
        System.out.println("Você ganhou uma poção de vida! Total: " + pocoes);
    }

    public void aprenderHabilidade(Habilidade habilidade) {
        habilidades.add(habilidade);
        this.ataque += habilidade.getBonusAtaque();

        System.out.println("\n✨ Nova habilidade aprendida: " + habilidade.getNome() + " ✨");
        System.out.println(habilidade.getDescricao());
        if (habilidade.getBonusAtaque() > 0) {
            System.out.println("Ataque +" + habilidade.getBonusAtaque());
        }
    }

    public boolean estaVivo() {
        return hp > 0;
    }

    // Getters essenciais
    public String getNome() {
        return nome;
    }

    public int getNivel() {
        return nivel;
    }

    public int getXp() {
        return xp;
    }

    public int getHp() {
        return hp;
    }

    public int getAtaque() {
        return ataque;
    }

    public int getDefesa() {
        return defesa;
    }

    public int getPocoes() {
        return pocoes;
    }

    public List<Habilidade> getHabilidades() {
        return habilidades;
    }

    @Override
    public String toString() {
        return nome + " - Nível " + nivel + " (HP: " + hp + "/" + hpMaximo +
               ", ATK: " + ataque + ", DEF: " + defesa + ", Poções: " + pocoes +
               ", XP: " + xp + "/" + xpParaProximoNivel + ")";
    }
}
