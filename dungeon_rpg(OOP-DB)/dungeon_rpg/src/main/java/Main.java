import model.*;
import dao.PersonagemDAO;
import java.util.*;

public class Main implements HabilidadeCallback {
    private static Scanner scanner = new Scanner(System.in);
    private static Random random = new Random();
    private static int combatesVencidos = 0;
    private static Main instance = new Main();
    private static PersonagemDAO personagemDAO = new PersonagemDAO();

    // Listas de nomes para geração aleatória de monstros
    private static String[] nomesMonstros = {
        "Goblin", "Orc", "Esqueleto", "Lobo Sombrio", "Aranha Gigante",
        "Zumbi", "Troll", "Demônio Menor", "Dragão Jovem", "Lich",
        "Minotauro", "Hidra", "Basilisco", "Fênix Negra", "Kraken"
    };

    public static void main(String[] args) {

        // Testar conexão com banco de dados
        personagemDAO.testarConexao();

        System.out.println("=== DUNGEON CRAWLER RPG ===");
        System.out.println("Bem-vindo à masmorra infinita!");

        System.out.print("Digite o nome do seu personagem: ");
        String nome = scanner.nextLine();

        Personagem personagem = new Personagem(nome);
        personagem.setHabilidadeCallback(instance);

        System.out.println("\n" + personagem);
        System.out.println("\nVocê adentra a masmorra escura...\n");

        // Loop principal do jogo
        while (personagem.estaVivo()) {
            Monstro monstro = gerarMonstroAleatorio(personagem.getNivel());
            System.out.println("🐲 Um " + monstro.getNomeCompleto() + " apareceu!");
            System.out.println(monstro);

            // Combate
            boolean vitoria = combate(personagem, monstro);

            if (!vitoria) {
                System.out.println("\n💀 GAME OVER 💀");
                System.out.println("Você foi derrotado na masmorra...");
                System.out.println("Combates vencidos: " + combatesVencidos);

                // Salvar personagem no banco ao morrer
                personagemDAO.salvar(personagem);
                break;
            }

            combatesVencidos++;
            personagem.ganharXp(monstro.getXpRecompensa());

            // A cada 3 combates, ganha uma poção
            if (combatesVencidos % 3 == 0) {
                personagem.ganharPocao();
            }

            // Menu pós-combate
            if (personagem.estaVivo()) {
                menuPosCombate(personagem);
            }
        }

        scanner.close();
    }

    private static Monstro gerarMonstroAleatorio(int nivelPersonagem) {
        String nome = nomesMonstros[random.nextInt(nomesMonstros.length)];

        int hp, ataque, defesa, xpRecompensa;

        // Primeiro monstro é mais fraco para permitir evolução
        if (combatesVencidos == 0) {
            hp = 20 + random.nextInt(10);  // 20-29 HP
            ataque = 5 + random.nextInt(3); // 5-7 ATK
            defesa = 1 + random.nextInt(2); // 1-2 DEF
            xpRecompensa = 30 + random.nextInt(20); // 30-49 XP
        } else {
            // Dificuldade baseada no nível do personagem + variação aleatória
            int multiplicador = nivelPersonagem + random.nextInt(3);

            hp = 35 + (multiplicador * 18) + random.nextInt(20);
            ataque = 15 + (multiplicador * 4) + random.nextInt(5);
            defesa = 2 + (multiplicador * 2) + random.nextInt(3);
// XP é calculado com base nos atributos gerados
            xpRecompensa = (int) Math.round((hp / 5.0) + (ataque * 1.5) + (defesa * 2.0));
        }

        // Gerar elemento aleatório (exceto para o primeiro monstro)
        Elemento elemento = combatesVencidos == 0 ? Elemento.NEUTRO :
            Elemento.values()[random.nextInt(Elemento.values().length)];

        return new Monstro(nome, hp, ataque, defesa, xpRecompensa, elemento);
    }

    private static boolean combate(Personagem personagem, Monstro monstro) {
        System.out.println("\n⚔️ COMBATE INICIADO! ⚔️");

        while (personagem.estaVivo() && monstro.estaVivo()) {
            // Turno do personagem
            System.out.println("\n--- SEU TURNO ---");
            System.out.println("Seu status: " + personagem);
            System.out.println("Inimigo: " + monstro);

            // Mostrar opções de ação
            System.out.println("\nEscolha sua ação:");

            // Mostrar habilidades disponíveis
            List<Habilidade> habilidades = personagem.getHabilidades();
            for (int i = 0; i < habilidades.size(); i++) {
                Habilidade h = habilidades.get(i);
                // Calcular efetividade contra o monstro
                double multiplicador = h.getElemento().getMultiplicadorContra(monstro.getElemento());
                String efetividade = "";
                if (multiplicador > 1.0) efetividade = " ✅";
                else if (multiplicador < 1.0) efetividade = " ❌";

                System.out.println((i + 1) + ". " + h.getNomeCompleto() + " (Dano: " + h.getDanoBase() + ")" + efetividade);
            }

            // Opção de usar poção
            int opcoesCombate = habilidades.size();
            if (personagem.getPocoes() > 0) {
                opcoesCombate++;
                System.out.println(opcoesCombate + ". Usar Poção (" + personagem.getPocoes() + " disponível(is)) (Cura: 50 HP)");
            }

            int escolha = lerEscolha(opcoesCombate);

            // Usar habilidade escolhida
            if (escolha <= habilidades.size()) {
                Habilidade habilidadeEscolhida = habilidades.get(escolha - 1);
                personagem.usarHabilidade(habilidadeEscolhida, monstro);
            } else if (personagem.getPocoes() > 0) {
                // Usar poção
                personagem.usarPocao();
                continue; // Pular turno do monstro se usar poção
            }

            // Verificar se monstro morreu
            if (!monstro.estaVivo()) {
                System.out.println("\n🎉 Você derrotou o " + monstro.getNome() + "! 🎉");
                return true;
            }

            // Turno do monstro
            System.out.println("\n--- TURNO DO INIMIGO ---");
            int danoMonstro = monstro.atacar();
            System.out.println(monstro.getNome() + " ataca!");
            personagem.receberDano(danoMonstro);

            // Pequena pausa para melhor experiência
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        return personagem.estaVivo();
    }

    private static void menuPosCombate(Personagem personagem) {
        System.out.println("\n=== MENU ===");
        System.out.println("1. Continuar explorando");
        System.out.println("2. Ver status completo");
        System.out.println("3. Salvar progresso");
        System.out.println("4. Sair do jogo");

        int escolha = lerEscolha(4);

        switch (escolha) {
            case 1:
                System.out.println("\nVocê segue em frente pela masmorra...");
                break;
            case 2:
                mostrarStatusCompleto(personagem);
                menuPosCombate(personagem); // Voltar ao menu
                break;
            case 3:
                personagemDAO.salvar(personagem);
                menuPosCombate(personagem); // Voltar ao menu
                break;
            case 4:
                System.out.println("\nSalvando progresso...");
                personagemDAO.salvar(personagem);
                System.out.println("Obrigado por jogar!");
                System.out.println("Combates vencidos: " + combatesVencidos);
                System.exit(0);
                break;
        }
    }

    private static void mostrarStatusCompleto(Personagem personagem) {
        System.out.println("\n=== STATUS COMPLETO ===");
        System.out.println(personagem);
        System.out.println("Combates vencidos: " + combatesVencidos);

        if (!personagem.getHabilidades().isEmpty()) {
            System.out.println("\nHabilidades:");
            for (Habilidade h : personagem.getHabilidades()) {
                System.out.println("• " + h.getNome() + " - " + h.getDescricao());
            }
        }
        System.out.println();
    }

    @Override
    public void oferecerEscolhaHabilidade(Personagem personagem) {
        System.out.println("\n🌟 Você pode escolher uma nova habilidade! 🌟");

        // Selecionar 3 habilidades aleatórias sem repetir elementos
        List<Habilidade> opcoes = new ArrayList<>();
        List<Habilidade> habilidadesDisponiveis = filtrarHabilidadesPorNivel(personagem.getNivel());
        Set<Elemento> elementosUsados = new HashSet<>();

        Collections.shuffle(habilidadesDisponiveis);

        for (Habilidade h : habilidadesDisponiveis) {
            if (opcoes.size() >= 3) break;
            if (!elementosUsados.contains(h.getElemento())) {
                opcoes.add(h);
                elementosUsados.add(h.getElemento());
            }
        }

        // Se não conseguir 3 habilidades diferentes, adiciona mais
        if (opcoes.size() < 3) {
            for (Habilidade h : habilidadesDisponiveis) {
                if (opcoes.size() >= 3) break;
                if (!opcoes.contains(h)) {
                    opcoes.add(h);
                }
            }
        }

        System.out.println("Escolha uma habilidade:");
        for (int i = 0; i < opcoes.size(); i++) {
            Habilidade h = opcoes.get(i);
            // Calcular elementos contra os quais é forte
            List<Elemento> fortes = new ArrayList<>();
            for (Elemento e : Elemento.values()) {
                if (h.getElemento().getMultiplicadorContra(e) > 1.0) {
                    fortes.add(e);
                }
            }

            System.out.println((i + 1) + ". " + h.getNomeCompleto() + " - " + h.getDescricao());
            System.out.print("   Bônus: ");
            if (h.getBonusAtaque() > 0) {
                System.out.print("ATK +" + h.getBonusAtaque() + " ");
            }
            System.out.println("Dano: " + h.getDanoBase());

            if (!fortes.isEmpty()) {
                System.out.print("   Forte contra: ");
                for (Elemento e : fortes) {
                    System.out.print(e.getEmoji() + e.getNome() + " ");
                }
                System.out.println();
            }
        }

        int escolha = lerEscolha(opcoes.size());
        personagem.aprenderHabilidade(opcoes.get(escolha - 1));
    }

    private static List<Habilidade> filtrarHabilidadesPorNivel(int nivel) {
        List<Habilidade> habilidades = new ArrayList<>();

        // --- Habilidades de Nível 1+ ---
        // Elementais têm dano base ~12-15, Neutras ~16
        habilidades.add(new Habilidade("Bola de Fogo", "Projétil flamejante", 8, 12, Elemento.FOGO));
        habilidades.add(new Habilidade("Torrente Aquática", "Fluxo d'água poderoso", 6, 14, Elemento.AGUA));
        habilidades.add(new Habilidade("Raio Elétrico", "Descarga elétrica poderosa", 7, 15, Elemento.ELETRICO));
        habilidades.add(new Habilidade("Investida Feroz", "Um ataque direto e poderoso", 12, 16, Elemento.NEUTRO));

        if (nivel >= 3) {
            // --- Habilidades de Nível 3+ ---
            // Elementais têm dano base ~17-19, Neutras ~21
            habilidades.add(new Habilidade("Lança de Luz", "Perfura as trevas", 12, 17, Elemento.LUZ));
            habilidades.add(new Habilidade("Golpe Sombrio", "Ataque das trevas", 12, 17, Elemento.SOMBRA));
            habilidades.add(new Habilidade("Maré Devastadora", "Ondas destruidoras", 10, 18, Elemento.AGUA));
            habilidades.add(new Habilidade("Força Brutal", "Poder físico puro e massivo", 18, 21, Elemento.NEUTRO));
        }

        if (nivel >= 5) {
            // --- Habilidades de Nível 5+ ---
            // Elementais têm dano base ~20-22, Neutras ~25
            habilidades.add(new Habilidade("Fúria Flamejante", "Ataques ígneos devastadores", 15, 20, Elemento.FOGO));
            habilidades.add(new Habilidade("Tempestade", "Múltiplos raios devastadores", 12, 22, Elemento.ELETRICO));
            habilidades.add(new Habilidade("Lâmina das Trevas", "Corte sombrio letal", 14, 22, Elemento.SOMBRA));
            habilidades.add(new Habilidade("Impacto Colossal", "Um golpe com força avassaladora", 20, 25, Elemento.NEUTRO));
        }

        return habilidades;
    }

    private static int lerEscolha(int max) {
        int escolha = 0;
        while (escolha < 1 || escolha > max) {
            System.out.print("Digite sua escolha (1-" + max + "): ");
            try {
                escolha = Integer.parseInt(scanner.nextLine());
                if (escolha < 1 || escolha > max) {
                    System.out.println("Escolha inválida!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, digite um número válido!");
            }
        }
        return escolha;
    }
}
