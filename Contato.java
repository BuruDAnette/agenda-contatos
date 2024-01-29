import java.util.ArrayList;
import java.util.List;

public class Contato {

    private Long id;
    private String nome;
    private String sobreNome;
    private List<Telefone> telefones;
    private static Long proximoId = 1L;

    public Contato() {
        this.telefones = new ArrayList<>();
        this.id = proximoId++;
    }

    public Contato(String nome, String sobreNome) {
        this();
        this.nome = nome;
        this.sobreNome = sobreNome;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobreNome() {
        return sobreNome;
    }

    public void setSobreNome(String sobreNome) {
        this.sobreNome = sobreNome;
    }

    public List<Telefone> getTelefones() {
        return telefones;
    }

    public void setTelefones(List<Telefone> telefones) {
        this.telefones = telefones;
    }

    public boolean verificarTelefoneExistente(Telefone telefone) {
        for (Telefone telefoneExistente : telefones) {
            if (telefoneExistente.equals(telefone)) {
                return true;
            }
        }

        return false;
    }

    public static boolean verificarTelefoneExistenteEmTodosOsContatos(Telefone telefone) {
        for (Contato contato : Agenda.contatos) {
            if (contato.verificarTelefoneExistente(telefone)) {
                return true;
            }
        }

        return false;
    }
    public void adicionarTelefone(Telefone telefone) {
        // Verifica se o telefone já está cadastrado em qualquer contato
        try {
            if (verificarTelefoneExistenteEmTodosOsContatos(telefone)) {
                throw new IllegalArgumentException("Telefone já cadastrado em algum contato");
            }
    
            telefones.add(telefone);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            // Exibe o menu novamente
        }
    }

    public static Long proximoId() {
        return proximoId++;
    }
}
