import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Agenda {
    static List<Contato> contatos;

    //------------------------------CORES------------------------------//
    public static final String RESET = "\033[0m";
    public static final String RED_BOLD = "\033[1;31m"; 
    public static final String GREEN_BOLD = "\033[1;32m";
    public static final String BLACK_BOLD = "\033[1;30m";
    public static final String PURPLE_BOLD = "\033[1;35m";
    public static final String PURPLE_BACKGROUND = "\033[45m";

    private static final String FILE_NAME = "agenda.txt";
    private static final String MENSAGEM_BEM_VINDO = "\n" + PURPLE_BACKGROUND + BLACK_BOLD + "          OLÁ USER!          " + RESET;
    private static final String MENSAGEM_CONTATO_CRIADO = GREEN_BOLD + "CONTATO CRIADO COM SUCESSO!\n" + RESET;
    private static final String MENSAGEM_CONTATO_REMOVIDO = GREEN_BOLD + "CONTATO REMOVIDO COM SUCESSO!\n" + RESET;
    private static final String MENSAGEM_CONTATO_EDITADO = GREEN_BOLD + "CONTATO EDITADO COM SUCESSO!\n" + RESET;
    private static final String MENSAGEM_CONTATOS_LISTADOS = GREEN_BOLD + "CONTATOS LISTADOS COM SUCESSO!\n" + RESET;
    private static final String MENSAGEM_OPCAO_INVALIDA = RED_BOLD + "ENTRADA INVALIDA! TENTE NOVAMENTE.\n" + RESET;
    private static final String TELEFONE_JA_EXISTE = RED_BOLD + "ENTRADA INVALIDA! TELEFONE JA EXISTE.\n" + RESET;
    private static final String ID_NAO_ENCONTRADO = RED_BOLD + "ENTRADA INVALIDA! ID NAO ENCONTRADO.\n" + RESET;

    public Agenda() {
        Agenda.contatos = new ArrayList<>();
    }

    public void lerArquivo() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(FILE_NAME))) {
            String linha;

            while ((linha = bufferedReader.readLine()) != null) {
                if (linha.equals("=====================================")) {
                    continue; // Ignora linhas de separação
                }

                String[] partes = linha.split("\\|");
                Long id = Long.parseLong(partes[0].trim());
                String[] nomeSobrenome = partes[1].trim().split(" ");
                String nome = nomeSobrenome[0];
                String sobrenome = nomeSobrenome[1];

                Contato contato = new Contato(nome, sobrenome);
                contato.setId(id);

                linha = bufferedReader.readLine(); // Pular linha em branco

                // Tratar a primeira linha de telefone
                String ddd = linha.substring(linha.indexOf("(") + 1, linha.indexOf(")"));
                String numeroStr = linha.substring(linha.indexOf(")") + 1).trim();
                Long numero = Long.parseLong(numeroStr);

                Telefone telefone = new Telefone(ddd, numero);
                contato.adicionarTelefone(telefone);

                // Continuar a leitura dos telefones
                while (!(linha = bufferedReader.readLine()).equals("=====================================")) {
                    ddd = linha.substring(linha.indexOf("(") + 1, linha.indexOf(")"));
                    numeroStr = linha.substring(linha.indexOf(")") + 1).trim();
                    numero = Long.parseLong(numeroStr);

                    telefone = new Telefone(ddd, numero);
                    contato.adicionarTelefone(telefone);
                }

                contatos.add(contato);
            }

            System.out.println(GREEN_BOLD + "ARQUIVO LIDO COM SUCESSO!" + RESET);
        } catch (Exception e) {
            System.err.println(RED_BOLD + "ERRO: " + e.getMessage() + RESET);
        }
    }


    public void escreverArquivo() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Contato contato : contatos) {
                bufferedWriter.write(contato.getId() + " | " + contato.getNome() + " " + contato.getSobreNome());
    
                for (Telefone telefone : contato.getTelefones()) {
                    bufferedWriter.write("\n(" + telefone.getDdd() + ") " + telefone.getNumero());
                }
    
                bufferedWriter.write("\n=====================================\n"); 
            }

            System.out.println(GREEN_BOLD + "ARQUIVO GRAVADO COM SUCESSO!" + RESET);
        } catch (IOException e) {
            System.err.println(RED_BOLD + "ERRO: " + e.getMessage() + RESET);
        }
    }
    
    private void criarContato(Scanner scanner) {
        //-------------------------NOME-E-SOBRENOME------------------------//
        System.out.println("DIGITE UM " + BLACK_BOLD + "NOME " + RESET + "PARA ESSE CONTATO: ");
        String nome = scanner.next();
    
        System.out.println("DIGITE UM " + BLACK_BOLD + "SOBRENOME " + RESET + "PARA ESSE CONTATO: ");
        String sobreNome = scanner.next();
    
        Contato novoContato = new Contato(nome, sobreNome);
    
        //----------------------------TELEFONES----------------------------//
        System.out.print(BLACK_BOLD + "QUANTOS " + RESET + "TELEFONES DESEJA ADICIONAR? ");
        try {
            int numTelefones = scanner.nextInt();
            for (int i = 0; i < numTelefones; i++) {
                System.out.println("DIGITE O " + BLACK_BOLD + "DDD " + RESET + "DO TELEFONE " + BLACK_BOLD + (i + 1) + RESET +": ");
                String ddd = scanner.next();
    
                System.out.println("DIGITE O " + BLACK_BOLD + "NÚMERO " + RESET + "DO TELEFONE " + BLACK_BOLD + (i + 1) + RESET + ": ");
                Long numero = scanner.nextLong();
    
                Telefone telefone = new Telefone(ddd, numero);
                if (Contato.verificarTelefoneExistenteEmTodosOsContatos(telefone)) {
                    throw new IllegalArgumentException(TELEFONE_JA_EXISTE);
                }

                novoContato.adicionarTelefone(telefone);
            }
    
            contatos.add(novoContato);
            System.out.println(MENSAGEM_CONTATO_CRIADO);
    
            escreverArquivo();
    
        } catch (IllegalArgumentException e) {
            System.out.println(RED_BOLD + "ERRO: " + e.getMessage() + RESET);
        }
    }
    

    private void removerContato(Scanner scanner) {
        if (contatos.isEmpty()) {
            System.out.println(RED_BOLD +"AGENDA VAZIA!" + RESET + "\n");
        } else {
            listarContatos();

            System.out.print("DIGITE O " + BLACK_BOLD + "ID " + RESET + "DO CONTATO A SER EXCLUIDO: ");
            try {
                Long idRemover = scanner.nextLong();
        
                Contato contatoRemover = null;
                for (Contato contato : contatos) {
                    if (contato.getId().equals(idRemover)) {
                        contatoRemover = contato;
                        break;
                    }
                }
        
                if (contatoRemover != null) {
                    contatos.remove(contatoRemover);
                    System.out.println(MENSAGEM_CONTATO_REMOVIDO);
                    
                    escreverArquivo();
                } else {
                    System.out.println(ID_NAO_ENCONTRADO);
                }
        
            } catch (InputMismatchException e) {
                System.out.println(MENSAGEM_OPCAO_INVALIDA);
                scanner.nextLine();
            }
        }
    }

    private void editarContato(Scanner scanner) {
        if (contatos.isEmpty()) {
            System.out.println(RED_BOLD +"AGENDA VAZIA!" + RESET + "\n");
        } else {
            listarContatos();
    
            System.out.print("DIGITE O " + BLACK_BOLD + "ID " + RESET + "DO CONTATO A SER EDITADO: ");
            Long idEditar = scanner.nextLong();
            
            Contato contatoEditar = null;
            for (Contato contato : contatos) {
                if (contato.getId().equals(idEditar)) {
                contatoEditar = contato;
                break;
                }
            }
            
            if (contatoEditar != null) {
                System.out.println(BLACK_BOLD + "     COMANDOS PARA EDITAR    \n");
                System.out.println(" 1  " + RESET + " NOME");
                System.out.println(BLACK_BOLD + " 2  " + RESET + " SOBRENOME");
                System.out.println(BLACK_BOLD + " 3  " + RESET + " TELEFONES");
                System.out.print("\nDIGITE O " + BLACK_BOLD + "NUMERO" + RESET + " DO COMANDO: ");
                int opcaoEditar = scanner.nextInt();
            
                switch (opcaoEditar) {
                    case 1:
                        System.out.println(PURPLE_BACKGROUND + "         EDITAR NOME         " + RESET + "\n");
                        System.out.println("DIGITE UM NOVO " + BLACK_BOLD + "NOME " + RESET + "PARA ESSE CONTATO: ");
                        String novoNome = scanner.next();
                        contatoEditar.setNome(novoNome);
                        System.out.println(MENSAGEM_CONTATO_EDITADO);
                        break;
                    case 2:
                        System.out.println(PURPLE_BACKGROUND + "       EDITAR SOBRENOME      " + RESET + "\n");
                        System.out.println("DIGITE UM NOVO " + BLACK_BOLD + "SOBRENOME " + RESET + "PARA ESSE CONTATO: ");
                        String novoSobrenome = scanner.next();
                        contatoEditar.setSobreNome(novoSobrenome);
                        System.out.println(MENSAGEM_CONTATO_EDITADO);
                        break;
                    case 3:
                        System.out.println(PURPLE_BACKGROUND + "       EDITAR TELEFONES      " + RESET + "\n");        
                        System.out.print("DIGITE A " + BLACK_BOLD + "POSICAO " + RESET + "DO TELEFONE A SER EDITADO: ");
                        try {
                            int escolhaTelefone = scanner.nextInt();
                            scanner.nextLine();
                            
                            if (escolhaTelefone >= 1 && escolhaTelefone <= contatoEditar.getTelefones().size()) {
                                Telefone telefoneEditado = editarTelefone(scanner, contatoEditar.getTelefones().get(escolhaTelefone - 1));
                                contatoEditar.getTelefones().set(escolhaTelefone - 1, telefoneEditado);
                                System.out.println(MENSAGEM_CONTATO_EDITADO);
                            } else {
                                System.out.println(MENSAGEM_OPCAO_INVALIDA);
                            }
                        } catch (InputMismatchException e) {
                            System.out.println(MENSAGEM_OPCAO_INVALIDA);
                            scanner.nextLine();
                        }
                        break;
                    default:
                    System.out.println(MENSAGEM_OPCAO_INVALIDA);
                }
                escreverArquivo();
                
            } else {
            System.out.println(ID_NAO_ENCONTRADO);
            }
        }
    }

    private Telefone editarTelefone(Scanner scanner, Telefone telefoneExistente) {
        System.out.println("TELEFONE A SER EDITADO: (" + telefoneExistente.getDdd() + ") " + telefoneExistente.getNumero());
    
        System.out.println(BLACK_BOLD + "     COMANDOS PARA EDITAR    \n");
        System.out.println(" 1  " + RESET + " DDD");
        System.out.println(BLACK_BOLD + " 2  " + RESET + " NUMERO");
        System.out.print("\nDIGITE O " + BLACK_BOLD + "NUMERO" + RESET + " DO COMANDO: ");
    
        try {
            int escolhaEdicao = scanner.nextInt();
            scanner.nextLine(); // Consumir a quebra de linha
    
            switch (escolhaEdicao) {
                case 1:
                    System.out.println(PURPLE_BACKGROUND + "         EDITAR DDD          " + RESET + "\n");
                    System.out.print("DIGITE UM NOVO " + BLACK_BOLD + "DDD " + RESET + "PARA ESSE TELEFONE: ");
                    String novoDdd = scanner.next();
                    telefoneExistente.setDdd(novoDdd);
                    break;
                case 2:
                    System.out.println(PURPLE_BACKGROUND + "         EDITAR NUMERO       " + RESET + "\n");
                    System.out.print("DIGITE UM NOVO " + BLACK_BOLD + "NUMERO " + RESET + "PARA ESSE TELEFONE: ");
                    Long novoNumero = scanner.nextLong();
                    telefoneExistente.setNumero(novoNumero);
                    break;
                default:
                    System.out.println(MENSAGEM_OPCAO_INVALIDA);
            }
    
        } catch (InputMismatchException e) {
            System.out.println(MENSAGEM_OPCAO_INVALIDA);
            scanner.nextLine(); // Consumir a entrada inválida
        }
    
        return telefoneExistente;
    }
    
    
    public void listarContatos() {
        System.out.println(BLACK_BOLD + "          SUA AGENDA         " + RESET);
        for (Contato contato : contatos) {
            System.out.print("ID: " + contato.getId() + " | ");
            System.out.print(contato.getNome() + " ");
            System.out.print(contato.getSobreNome() + " | ");
            System.out.println(" ");
    
            for (Telefone telefone : contato.getTelefones()) {
                System.out.print("(" + telefone.getDdd() + ") ");
                System.out.print(telefone.getNumero());
                System.out.println(" ");
            }
    
            System.out.println(PURPLE_BOLD + "=============================" + RESET);
        }
        System.out.println(MENSAGEM_CONTATOS_LISTADOS);
    }
    

    private static void imprimirMenu() {
        System.out.println(" ");
        System.out.println(BLACK_BOLD + "       MENU DE COMANDOS      \n");
        System.out.println(" 1  " + RESET + " ADICIONAR CONTATO");
        System.out.println(BLACK_BOLD + " 2  " + RESET + " REMOVER CONTATO");
        System.out.println(BLACK_BOLD + " 3  " + RESET + " EDITAR CONTATO");
        //System.out.println(BLACK_BOLD + " 4  " + RESET + " LISTAR CONTATOS");
        System.out.println(BLACK_BOLD + " 4  " + RESET + " SAIR");
        System.out.print("\nDIGITE O " + BLACK_BOLD + "NUMERO" + RESET + " DO COMANDO: ");
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        Agenda agenda = new Agenda();

        System.out.println(MENSAGEM_BEM_VINDO);

        agenda.lerArquivo();

        while (true) {
            imprimirMenu();

            int opcao = scanner.nextInt();

            switch (opcao) {
                case 1:
                    System.out.println(PURPLE_BACKGROUND + "      ADICIONAR CONTATO      " + RESET + "\n");
                    agenda.criarContato(scanner);
                    break;
                case 2:
                    System.out.println(PURPLE_BACKGROUND + "       REMOVER CONTATO       " + RESET + "\n");
                    agenda.removerContato(scanner);
                    break;
                case 3:
                    System.out.println(PURPLE_BACKGROUND + "       EDITAR CONTATO        " + RESET + "\n");
                    agenda.editarContato(scanner);
                    break;
                //case 4:
                    //System.out.println(PURPLE_BACKGROUND + "       LISTAR CONTATOS       " + RESET + "\n");
                    //agenda.listarContatos();
                    //break;
                case 4:
                    System.out.println(PURPLE_BACKGROUND + BLACK_BOLD + "         ATÉ, USER!          " + RESET + "\n");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println(MENSAGEM_OPCAO_INVALIDA);
            }
        }  
    }
}
