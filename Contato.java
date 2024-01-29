import java.util.ArrayList;
import java.util.List;

public class Contato {

    private static Long proximoId = 1L;
    private Long id;
    private String nome;
    private String sobreNome;
    private List<Telefone> telefones;

    public Contato() {
        this.id = proximoId++;
        this.telefones = new ArrayList<>();
    }

    public Contato(String nome, String sobreNome) {
        this.id = proximoId++;
        this.nome = nome;
        this.sobreNome = sobreNome;
        this.telefones = new ArrayList<>();
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
    public void setId(Long id) {
        this.id = id;
    }

    public List<Telefone> getTelefones() {
        return telefones;
    }

    public void setTelefones(List<Telefone> telefones) {
        this.telefones = telefones;
    }

    public static boolean verificar_telefone_em_todos_contatos(Telefone telefone) {
        for (Contato contato : Agenda.contatos) {
            if (contato.verificar_telefone_existe(telefone)) {
                return true;
            }
        }
        return false;
    }

    public boolean verificar_telefone_existe(Telefone telefone) {
        for (Telefone telefoneExistente : telefones) {
            if (telefoneExistente.equals(telefone)) {
                return true;
            }
        }
        return false;
    }

    
    public void adicionar_telefone(Telefone telefone) {
        try {
            if (verificar_telefone_em_todos_contatos(telefone)) {
                throw new IllegalArgumentException("Telefone já cadastrado em algum contato");
            }
    
            if (verificar_telefone_em_todos_contatos(telefone)) {
                throw new IllegalArgumentException("Telefone já cadastrado neste contato");
            }
    
            telefones.add(telefone);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
