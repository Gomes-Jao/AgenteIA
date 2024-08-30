import java.util.*;

public class Inferencia {
    private BaseConhecimento baseConhecimento;
    private Scanner scanner;

    public Inferencia() {
        baseConhecimento = new BaseConhecimento();
        scanner = new Scanner(System.in);
    }

    public void adicionarRegra(List<String> antecedentes, String consequente) {
        Regra regra = new Regra(antecedentes, consequente);
        baseConhecimento.adicionarRegra(regra);
        System.out.println("Regra adicionada com sucesso!");
    }

    public void listarRegras() {
        List<Regra> regras = baseConhecimento.obterRegras();
        if (regras.isEmpty()) {
            System.out.println("Nenhuma regra cadastrada.");
        } else {
            System.out.println("Regras cadastradas:");
            for (int i = 0; i < regras.size(); i++) {
                System.out.println((i + 1) + ": " + regras.get(i));
            }
        }
    }

    public void removerRegra(int indice) {
        List<Regra> regras = baseConhecimento.obterRegras();
        if (indice >= 0 && indice < regras.size()) {
            baseConhecimento.removerRegra(indice);
            System.out.println("Regra removida com sucesso!");
        } else {
            System.out.println("Índice inválido. A regra não foi removida.");
        }
    }

    public void encadeamentoHibrido(List<String> objetivos) {
        List<String> fatosIniciais = new ArrayList<>();
        List<String> objetivosAlcancados = new ArrayList<>();
        Map<String, List<String>> regrasUsadasPorObjetivo = new HashMap<>();
        Map<String, String> antecedentesRespostas = new HashMap<>(); // Mapear antecedentes para suas respostas

        for (Regra regra : baseConhecimento.obterRegras()) {
            for (String antecedente : regra.getAntecedentes()) {
                if (!antecedentesRespostas.containsKey(antecedente)) {
                    String respostaAntecedente = perguntarAntecedente(antecedente, antecedentesRespostas);
                    if (respostaAntecedente.equalsIgnoreCase("V")) {
                        fatosIniciais.add(antecedente); // Adiciona antecedente aos fatos iniciais se a resposta for "V"
                    }
                }
            }
        }

        for (String objetivo : objetivos) {
            List<String> fatosDerivados = new ArrayList<>(fatosIniciais);
            List<String> regrasUsadasParaObjetivo = new ArrayList<>();

            while (!fatosDerivados.contains(objetivo)) {
                boolean regraAplicada = false;

                for (Regra regra : baseConhecimento.obterRegras()) {
                    if (fatosDerivados.containsAll(regra.getAntecedentes()) && !fatosDerivados.contains(regra.getConsequente())) {
                        fatosDerivados.add(regra.getConsequente());
                        String regraUtilizada = String.join(", ", regra.getAntecedentes()) + " => " + regra.getConsequente();
                        regrasUsadasParaObjetivo.add(regraUtilizada);
                        regraAplicada = true;
                    }
                }

                if (!regraAplicada) {
                    break;
                }
            }

            if (fatosDerivados.contains(objetivo)) {
                objetivosAlcancados.add(objetivo);
                regrasUsadasPorObjetivo.put(objetivo, regrasUsadasParaObjetivo);
            }
        }

        if (!objetivosAlcancados.isEmpty()) {
            for (String objetivoAlcancado : objetivosAlcancados) {
                System.out.println("\nObjetivo alcançado: " + objetivoAlcancado);
                System.out.println("COMO eu alcancei? usei as seguintes regras para concluir isto: " + objetivoAlcancado + ":");
                List<String> regrasUsadas = regrasUsadasPorObjetivo.get(objetivoAlcancado);
                for (String regraUsada : regrasUsadas) {
                    System.out.println(regraUsada);
                }
            }
        } else {
            System.out.println("Nenhum dos objetivos foi alcançado.");
        }
    }

    private String perguntarAntecedente(String antecedente, Map<String, String> antecedentesRespostas) {
        while (true) {
            System.out.print("O atributo '" + antecedente + "' é verdadeiro (V), falso (F) ou por que (PQ)? ");
            String resposta = scanner.nextLine();

            if (resposta.equalsIgnoreCase("V") || resposta.equalsIgnoreCase("F")) {
                antecedentesRespostas.put(antecedente, resposta);
                return resposta; // Retorna a resposta dada pelo usuário
            } else if (resposta.equalsIgnoreCase("PQ")) {
                listarObjetivosComAntecedente(antecedente);
            } else {
                System.out.println("Resposta inválida. Tente novamente.");
            }
        }
    }

    private void listarObjetivosComAntecedente(String antecedente) {
        List<String> objetivosComAntecedente = new ArrayList<>();
        for (Regra regra : baseConhecimento.obterRegras()) {
            if (regra.getAntecedentes().contains(antecedente)) {
                objetivosComAntecedente.add(regra.getConsequente());
            }
        }

        if (!objetivosComAntecedente.isEmpty()) {
            System.out.println("Pois com '" + antecedente + "' eu estou tentando concluir:");
            for (String objetivo : objetivosComAntecedente) {
                System.out.println(objetivo);
            }
        } else {
            System.out.println("Nenhum objetivo tem '" + antecedente + "' como antecedente.");
        }
    }

    public static void main(String[] args) {
        Inferencia sistema = new Inferencia();
        List<String> varobj = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1- Adicionar regra");
            System.out.println("2- Listar regras");
            System.out.println("3- Remover regra");
            System.out.println("4- Inferência lógica");
            System.out.println("5- Finalizar código");

            System.out.print("Escolha uma opção: ");
            int opcao = scanner.nextInt();
            scanner.nextLine();  // Limpar o buffer

            switch (opcao) {
                case 1:
                    System.out.println("Digite os antecedentes da regra separados por vírgula:");
                    String antecedentesStr = scanner.nextLine();
                    List<String> antecedentes = Arrays.asList(antecedentesStr.split(", "));

                    System.out.println("Digite o consequente da regra:");
                    String consequente = scanner.nextLine();

                    System.out.print("Este consequente é um objetivo (S/N)? ");
                    String resposta = scanner.nextLine().trim();

                    if (resposta.equalsIgnoreCase("S")) {
                        varobj.add(consequente);
                        System.out.println("Consequente adicionado à lista de objetivos.");
                    }

                    sistema.adicionarRegra(antecedentes, consequente);
                    break;

                case 2:
                    sistema.listarRegras();
                    break;

                case 3:
                    System.out.print("Digite o índice da regra a ser removida: ");
                    int indice = scanner.nextInt();
                    sistema.removerRegra(indice - 1); // Subtrai 1 do índice para corresponder ao índice base 0 na lista
                    break;

                case 4:
                    System.out.println("Objetivos: " + varobj);

                    // Encadeamento híbrido para cada objetivo
                    System.out.println("\nRealizando encadeamento híbrido para cada objetivo:");
                    sistema.encadeamentoHibrido(varobj);
                    break;

                case 5:
                    System.out.println("Finalizando o código.");
                    scanner.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Opção inválida. Tente novamente.");
                    break;
            }
        }
    }
}
