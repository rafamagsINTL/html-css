package model;

public enum Elemento {

    FOGO("🔥", "Fogo"),
    AGUA("💧", "Água"),
    ELETRICO("⚡", "Elétrico"),
    LUZ("✨", "Luz"),
    SOMBRA("🌑", "Sombra"),
    NEUTRO("⚪", "Neutro");

    private final String emoji;
    private final String nome;

    Elemento(String emoji, String nome) {
        this.emoji = emoji;
        this.nome = nome;
    }

    public String getEmoji() {
        return emoji;
    }

    public String getNome() {
        return nome;
    }

    // Sistema de vantagens/desvantagens elementais
    public double getMultiplicadorContra(Elemento outro) {
        if (this == NEUTRO || outro == NEUTRO) return 1.0;

        switch (this) {
            case FOGO:
                if (outro == AGUA) return 0.7;
                if (outro == ELETRICO) return 1.5;
                break;
            case AGUA:
                if (outro == FOGO) return 1.5;
                if (outro == ELETRICO) return 0.7;
                break;
            case ELETRICO:
                if (outro == AGUA) return 1.5;
                if (outro == FOGO) return 0.7;
                break;
            case LUZ:
                if (outro == SOMBRA) return 2.0;
                break;
            case SOMBRA:
                if (outro == LUZ) return 2.0;
                break;
        }
        return 1.0; // Neutro
    }

    @Override
    public String toString() {
        return emoji + nome;
    }
}
