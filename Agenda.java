import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Agenda {

    static List<Contato> contatos;

    private static final String FILE_NAME = "agenda.txt";
    private static final String MENSAGEM_BEM_VINDO = "Olá!";
    private static final String MENSAGEM_CONTATO_CRIADO = "Contato criado com sucesso.";
    private static final String MENSAGEM_CONTATO_REMOVIDO = "Contato removido com sucesso.";
    private static final String MENSAGEM_CONTATO_EDITADO = "Contato editado com sucesso.";
    private static final String MENSAGEM_OPCAO_INVALIDA = "Entrada inválida. Por favor, tente novamente.";
    private static final String TELEFONE_JA_EXISTE = "Entrada inválida. Telefone já existe.";
    private static final String ID_NAO_ENCONTRADO = "Entrada inválida. ID não encontrado.";

    public Agenda() {
        Agenda.contatos = new ArrayList<>();
    }

    public void escreverArquivo() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Contato contato : contatos) {
                bufferedWriter.write(contato.getId() + " | " + contato.getNome() + " " + contato.getSobreNome());
    
                for (Telefone telefone : contato.getTelefones()) {
                    bufferedWriter.write(" | (" + telefone.getDdd() + ") " + telefone.getNumero() + " || ");
                }
    
                bufferedWriter.write("\n");
            }
        } catch (IOException e) {
            System.err.println("ERRO: " + e.getMessage());
        }
    }
    
    private void criarContato(Scanner scanner) {
        //-------------------------NOME-E-SOBRENOME------------------------//
        System.out.print("DIGITE UM NOME PARA ESSE CONTATO: ");
        String nome = scanner.next();
    
        System.out.print("DIGITE UM SOBRENOME PARA ESSE CONTATO: ");
        String sobreNome = scanner.next();
    
        Contato novoContato = new Contato(nome, sobreNome);
    
        //----------------------------TELEFONES----------------------------//
        System.out.print("QUANTOS TELEFONES DESEJA ADICIONAR? ");
        try {
            int numTelefones = scanner.nextInt();
            for (int i = 0; i < numTelefones; i++) {
                System.out.print("DIGITE O DDD DO TELEFONE " + (i + 1) + ":");
                String ddd = scanner.next();
    
                System.out.print("DIGITE O NÚMERO DO TELEFONE " + (i + 1) + ":");
                Long numero = scanner.nextLong();
    
                Telefone telefone = new Telefone(ddd, numero);
                // Verifica se o telefone já existe em qualquer contato
                if (Contato.verificarTelefoneExistenteEmTodosOsContatos(telefone)) {
                    throw new IllegalArgumentException(TELEFONE_JA_EXISTE);
                }

                novoContato.adicionarTelefone(telefone);
            }
    
            contatos.add(novoContato);
            System.out.println(MENSAGEM_CONTATO_CRIADO);
    
            // Grava os dados imediatamente após adicionar um novo contato
            escreverArquivo();
    
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            // Exibe o menu novamente
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
                
                // Grava os dados imediatamente após remover o contato
                escreverArquivo();
            } else {
                System.out.println(ID_NAO_ENCONTRADO);
            }
    
        } catch (InputMismatchException e) {
            System.out.println(MENSAGEM_OPCAO_INVALIDA);
            scanner.nextLine(); // Consumir a entrada inválida
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
                    System.out.println("QUANTOS TELEFONES DESEJA EDITAR? ");
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
                            // Adiciona o telefone editado na lista de telefones
                            contatoEditar.getTelefones().add(telefone);
                            System.out.println(MENSAGEM_CONTATO_EDITADO);
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                        // Exibe o menu novamente
                    }
            
                    break;
                default:
                System.out.println(MENSAGEM_OPCAO_INVALIDA);
            }
        
        
            // Grava os dados imediatamente após editar o contato
            escreverArquivo();
            
        } else {
        System.out.println(ID_NAO_ENCONTRADO);
        }
    }
    
    public void listarContatos() {
        if (contatos.isEmpty()) {
            System.out.println("AGENDA VAZIA!");
        } else {
            System.out.println("=====================================");
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
    
                System.out.println("=====================================");
            }
        }
    }
    

    private static void imprimirMenu() {
        System.out.println("MENU DE COMANDOS");
        System.out.println("1 - ADICIONAR CONTATO");
        System.out.println("2 - REMOVER CONTATO");
        System.out.println("3 - EDITAR CONTATO");
        System.out.println("4 - LISTAR CONTATOS");
        System.out.println("5 - SAIR");
        System.out.print("DIGITE O NUMERO DO COMANDO: ");
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
                    agenda.criarContato(scanner);
                    break;
                case 2:
                    agenda.removerContato(scanner);
                    break;
                case 3:
                    agenda.editarContato(scanner);
                    break;
                case 4:
                    agenda.listarContatos();
                    break;
                case 5:
                    System.out.println("Até logo!");
                    System.exit(0);
                    scanner.close();
                default:
                    System.out.println(MENSAGEM_OPCAO_INVALIDA);
            }
        }  
    }
}
