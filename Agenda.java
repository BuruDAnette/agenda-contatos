import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Tipo Agenda.
 */
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

    /**
     * Leitor arquivo.
     */
    public void leitor_arquivo() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(FILE_NAME))) {
            String linha;

            while ((linha = bufferedReader.readLine()) != null) {
                if (linha.equals("=====================================")) {
                    continue;
                }

                String[] partes = linha.split("\\|");
                Long id = Long.parseLong(partes[0].trim());
                String[] nomeSobrenome = partes[1].trim().split(" ");
                String nome = nomeSobrenome[0];
                String sobrenome = nomeSobrenome[1];

                Contato contato = new Contato(nome, sobrenome);
                contato.setId(id);

                linha = bufferedReader.readLine();

                
                String ddd = linha.substring(linha.indexOf("(") + 1, linha.indexOf(")"));
                String numeroStr = linha.substring(linha.indexOf(")") + 1).trim();
                Long numero = Long.parseLong(numeroStr);

                Telefone telefone = new Telefone(ddd, numero);
                contato.adicionar_telefone(telefone);

                while (!(linha = bufferedReader.readLine()).equals("=====================================")) {
                    ddd = linha.substring(linha.indexOf("(") + 1, linha.indexOf(")"));
                    numeroStr = linha.substring(linha.indexOf(")") + 1).trim();
                    numero = Long.parseLong(numeroStr);

                    telefone = new Telefone(ddd, numero);
                    contato.adicionar_telefone(telefone);
                }

                contatos.add(contato);
            }

            System.out.println(GREEN_BOLD + "ARQUIVO LIDO COM SUCESSO!" + RESET);
        } catch (Exception e) {
            System.err.println(RED_BOLD + "ERRO: " + e.getMessage() + RESET);
        }
    }


    /**
     * Escritor arquivo.
     */
    public void escritor_arquivo() {
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
    
    private void criar_contato(Scanner scanner) {
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
                if (Contato.verificar_telefone_em_todos_contatos(telefone)) {
                    throw new IllegalArgumentException(TELEFONE_JA_EXISTE);
                }

                novoContato.adicionar_telefone(telefone);
            }
    
            contatos.add(novoContato);
            System.out.println(MENSAGEM_CONTATO_CRIADO);
    
            escritor_arquivo();
    
        } catch (IllegalArgumentException e) {
            System.out.println(RED_BOLD + "ERRO: " + e.getMessage() + RESET);
        }
    }
    

    private void remover_contato(Scanner scanner) {
        if (contatos.isEmpty()) {
            System.out.println(RED_BOLD +"AGENDA VAZIA!" + RESET + "\n");
        } else {
            listar_contatos();

            System.out.print("DIGITE O " + BLACK_BOLD + "ID " + RESET + "DO CONTATO A SER EXCLUIDO: ");
            try {
                Long id_remover = scanner.nextLong();
        
                Contato contato_remover = null;
                for (Contato contato : contatos) {
                    if (contato.getId().equals(id_remover)) {
                        contato_remover = contato;
                        break;
                    }
                }
        
                if (contato_remover != null) {
                    contatos.remove(contato_remover);
                    System.out.println(MENSAGEM_CONTATO_REMOVIDO);
                    
                    escritor_arquivo();
                } else {
                    System.out.println(ID_NAO_ENCONTRADO);
                }
        
            } catch (InputMismatchException e) {
                System.out.println(MENSAGEM_OPCAO_INVALIDA);
                scanner.nextLine();
            }
        }
    }

    private void editar_contato(Scanner scanner) {
        if (contatos.isEmpty()) {
            System.out.println(RED_BOLD +"AGENDA VAZIA!" + RESET + "\n");
        } else {
            listar_contatos();
    
            System.out.print("DIGITE O " + BLACK_BOLD + "ID " + RESET + "DO CONTATO A SER EDITADO: ");
            Long id_editar = scanner.nextLong();
            
            Contato contato_editar = null;
            for (Contato contato : contatos) {
                if (contato.getId().equals(id_editar)) {
                contato_editar = contato;
                break;
                }
            }
            
            if (contato_editar != null) {
                System.out.println(BLACK_BOLD + "     COMANDOS PARA EDITAR    \n");
                System.out.println(" 1  " + RESET + " NOME");
                System.out.println(BLACK_BOLD + " 2  " + RESET + " SOBRENOME");
                System.out.println(BLACK_BOLD + " 3  " + RESET + " TELEFONES");
                System.out.print("\nDIGITE O " + BLACK_BOLD + "NUMERO" + RESET + " DO COMANDO: ");
                int comando_editar = scanner.nextInt();
            
                switch (comando_editar) {
                    case 1:
                        System.out.println(PURPLE_BACKGROUND + "         EDITAR NOME         " + RESET + "\n");
                        System.out.println("DIGITE UM NOVO " + BLACK_BOLD + "NOME " + RESET + "PARA ESSE CONTATO: ");
                        String novo_nome = scanner.next();
                        contato_editar.setNome(novo_nome);
                        System.out.println(MENSAGEM_CONTATO_EDITADO);
                        break;
                    case 2:
                        System.out.println(PURPLE_BACKGROUND + "       EDITAR SOBRENOME      " + RESET + "\n");
                        System.out.println("DIGITE UM NOVO " + BLACK_BOLD + "SOBRENOME " + RESET + "PARA ESSE CONTATO: ");
                        String novo_sobrenome = scanner.next();
                        contato_editar.setSobreNome(novo_sobrenome);
                        System.out.println(MENSAGEM_CONTATO_EDITADO);
                        break;
                    case 3:
                        System.out.println(PURPLE_BACKGROUND + "       EDITAR TELEFONES      " + RESET + "\n");        
                        System.out.print("DIGITE A " + BLACK_BOLD + "POSICAO " + RESET + "DO TELEFONE A SER EDITADO: ");
                        try {
                            int telefone_editar = scanner.nextInt();
                            scanner.nextLine();
        
                            if (telefone_editar >= 1 && telefone_editar <= contato_editar.getTelefones().size()) {
                                Telefone telefone_editado = editar_telefone(scanner, contato_editar.getTelefones().get(telefone_editar - 1));
                                if (Contato.verificar_telefone_em_todos_contatos(telefone_editado)) {
                                    throw new IllegalArgumentException(TELEFONE_JA_EXISTE);
                                }
                                contato_editar.getTelefones().set(telefone_editar - 1, telefone_editado);
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
                escritor_arquivo();
                
            } else {
            System.out.println(ID_NAO_ENCONTRADO);
            }
        }
    }

    private Telefone editar_telefone(Scanner scanner, Telefone telefoneExistente) {
        boolean input = false;
    
        while (!input) {
            System.out.println("TELEFONE A SER EDITADO: " + BLACK_BOLD + "(" + telefoneExistente.getDdd() + ") " + telefoneExistente.getNumero() + RESET);
            System.out.println(BLACK_BOLD + "     COMANDOS PARA EDITAR    \n");
            System.out.println(" 1  " + RESET + " DDD");
            System.out.println(BLACK_BOLD + " 2  " + RESET + " NUMERO");
            System.out.print("\nDIGITE O " + BLACK_BOLD + "NUMERO" + RESET + " DO COMANDO: ");
    
            try {
                int comando_editar2 = scanner.nextInt();
                scanner.nextLine(); // Consumir a quebra de linha
    
                switch (comando_editar2) {
                    case 1:
                        System.out.println(PURPLE_BACKGROUND + "         EDITAR DDD          " + RESET + "\n");
                        System.out.print("DIGITE UM NOVO " + BLACK_BOLD + "DDD " + RESET + "PARA ESSE TELEFONE: ");
                        String novo_ddd = scanner.next();
                        telefoneExistente.setDdd(novo_ddd);
                        input = true;
                        break;
                    case 2:
                        System.out.println(PURPLE_BACKGROUND + "         EDITAR NUMERO       " + RESET + "\n");
                        System.out.print("DIGITE UM NOVO " + BLACK_BOLD + "NUMERO " + RESET + "PARA ESSE TELEFONE: ");
                        Long novo_numero = scanner.nextLong();
                        telefoneExistente.setNumero(novo_numero);
                        input = true;
                        break;
                    default:
                        System.out.println(MENSAGEM_OPCAO_INVALIDA);
                }
            } catch (InputMismatchException e) {
                System.out.println(MENSAGEM_OPCAO_INVALIDA); 
            }
        }
        return telefoneExistente;
    }


    /**
     * Listar contatos.
     */
    public void listar_contatos() {
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
    

    private static void imprimir_menu() {
        System.out.println(" ");
        System.out.println(BLACK_BOLD + "       MENU DE COMANDOS      \n");
        System.out.println(" 1  " + RESET + " ADICIONAR CONTATO");
        System.out.println(BLACK_BOLD + " 2  " + RESET + " REMOVER CONTATO");
        System.out.println(BLACK_BOLD + " 3  " + RESET + " EDITAR CONTATO");
        //System.out.println(BLACK_BOLD + " 4  " + RESET + " LISTAR CONTATOS");
        System.out.println(BLACK_BOLD + " 4  " + RESET + " SAIR");
        System.out.print("\nDIGITE O " + BLACK_BOLD + "NUMERO" + RESET + " DO COMANDO: ");
    }

    /**
     * O ponto de entrada da aplicacao.
     *
     * @param args argumentos do input
     * @throws IOException exceção
     */
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        Agenda agenda = new Agenda();

        System.out.println(MENSAGEM_BEM_VINDO);

        agenda.leitor_arquivo();

        while (true) {
            imprimir_menu();

            int comando = scanner.nextInt();

            switch (comando) {
                case 1:
                    System.out.println(PURPLE_BACKGROUND + "      ADICIONAR CONTATO      " + RESET + "\n");
                    agenda.criar_contato(scanner);
                    break;
                case 2:
                    System.out.println(PURPLE_BACKGROUND + "       REMOVER CONTATO       " + RESET + "\n");
                    agenda.remover_contato(scanner);
                    break;
                case 3:
                    System.out.println(PURPLE_BACKGROUND + "       EDITAR CONTATO        " + RESET + "\n");
                    agenda.editar_contato(scanner);
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
