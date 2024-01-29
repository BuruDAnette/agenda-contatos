import java.io.BufferedWriter;
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

    public void escreverArquivo() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Contato contato : contatos) {
                bufferedWriter.write(contato.getId() + " | " + contato.getNome() + " " + contato.getSobreNome());
    
                for (Telefone telefone : contato.getTelefones()) {
                    bufferedWriter.write("\n(" + telefone.getDdd() + ") " + telefone.getNumero());
                }
    
                bufferedWriter.write(PURPLE_BOLD +"\n=====================================" + RESET + "\n"); 
            }
        } catch (IOException e) {
            System.err.println("ERRO: " + e.getMessage());
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
                System.out.println("DIGITE O " + BLACK_BOLD + "DDD " + RESET + "DO TELEFONE " + BLACK_BOLD + (i + 1) + RESET +":");
                String ddd = scanner.next();
    
                System.out.print("DIGITE O " + BLACK_BOLD + "NÚMERO " + RESET + "DO TELEFONE " + BLACK_BOLD + (i + 1) + RESET + ":");
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
            System.out.println(e.getMessage());
        }
    }
    

    private void removerContato(Scanner scanner) {
        System.out.print("DIGITE O ID DO CONTATO A SER EXCLUIDO: ");
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

    private void editarContato(Scanner scanner) {
        System.out.print("DIGITE O ID DO CONTATO A SER EDITADO: ");
        Long idEditar = scanner.nextLong();
        
        Contato contatoEditar = null;
        for (Contato contato : contatos) {
            if (contato.getId().equals(idEditar)) {
            contatoEditar = contato;
            break;
            }
        }
        
        if (contatoEditar != null) {
            System.out.println("COMANDOS PARA EDITAR");
            System.out.println("1 - NOME");
            System.out.println("2 - SOBRENOME");
            System.out.println("3 - TELEFONES");
            System.out.print("DIGITE O NUMERO DO COMANDO PARA EDITAR: ");
            int opcaoEditar = scanner.nextInt();
        
            switch (opcaoEditar) {
                case 1:
                    System.out.print("DIGITE UM NOVO NOME PARA ESSE CONTATO: ");
                    String novoNome = scanner.next();
                    contatoEditar.setNome(novoNome);
                break;
                case 2:
                    System.out.print("DIGITE UM NOVO SOBRENOME PARA ESSE CONTATO: ");
                    String novoSobrenome = scanner.next();
                    contatoEditar.setSobreNome(novoSobrenome);
                break;
                case 3:        
                    System.out.println(BLACK_BOLD + "QUANTOS " + RESET + "TELEFONES DESEJA EDITAR? ");
                    try {
                        int numTelefones = scanner.nextInt();
                        for (int i = 0; i < numTelefones; i++) {
                            System.out.println("Digite o DDD do telefone " + (i + 1) + ":");
                            String ddd = scanner.next();
                
                            System.out.println("Digite o número do telefone " + (i + 1) + ":");
                            Long numero = scanner.nextLong();
                
                            Telefone telefone = new Telefone(ddd, numero);
                            if (Contato.verificarTelefoneExistenteEmTodosOsContatos(telefone)) {
                                throw new IllegalArgumentException(TELEFONE_JA_EXISTE);
                            }
                            contatoEditar.getTelefones().add(telefone);
                            System.out.println(MENSAGEM_CONTATO_EDITADO);
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
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
    
    public void listarContatos() {
        if (contatos.isEmpty()) {
            System.out.println(RED_BOLD +"AGENDA VAZIA!" + RESET + "\n");
        } else {
            System.out.println(BLACK_BOLD + "          SUA AGENDA         " + RESET);
            for (Contato contato : contatos) {
                System.out.print("ID: " + contato.getId() + " |");
                System.out.print(contato.getNome() + " ");
                System.out.print(contato.getSobreNome() + " | ");
                System.out.println(" ");
    
                for (Telefone telefone : contato.getTelefones()) {
                    System.out.print("(" + telefone.getDdd() + ") ");
                    System.out.print(telefone.getNumero());
                    System.out.println(" ");
                }
    
                System.out.println(PURPLE_BOLD + "=============================" + RESET);
                System.out.println(MENSAGEM_CONTATOS_LISTADOS);
            }
        }
    }
    

    private static void imprimirMenu() {
        System.out.println(BLACK_BOLD + "       MENU DE COMANDOS      \n");
        System.out.println(" 1  " + RESET + " ADICIONAR CONTATO");
        System.out.println(BLACK_BOLD + " 2  " + RESET + " REMOVER CONTATO");
        System.out.println(BLACK_BOLD + " 3  " + RESET + " EDITAR CONTATO");
        System.out.println(BLACK_BOLD + " 4  " + RESET + " LISTAR CONTATOS");
        System.out.println(BLACK_BOLD + " 5  " + RESET + " SAIR");
        System.out.print("\nDIGITE O " + BLACK_BOLD + "NUMERO" + RESET + " DO COMANDO: ");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Agenda agenda = new Agenda();

        System.out.println(MENSAGEM_BEM_VINDO);

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
                case 4:
                    System.out.println(PURPLE_BACKGROUND + "       LISTAR CONTATOS       " + RESET + "\n");
                    agenda.listarContatos();
                    break;
                case 5:
                    System.out.println(PURPLE_BACKGROUND + BLACK_BOLD + "         ATÉ, USER!          " + RESET + "\n");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println(MENSAGEM_OPCAO_INVALIDA);
            }
        }  
    }
}
