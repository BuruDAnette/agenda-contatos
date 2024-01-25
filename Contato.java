import java.util.ArrayList;
import java.util.List;

public class Contato {
    private Long id;
    private String nome;
    private String sobreNome;
    private List<Telefone> telefones;

    /**
     * Inicia um novo contato
     *
     * @param id        o id
     * @param nome      o nome
     * @param sobreNome o sobrenome
     * @param telefones os telefones
     */
    public Contato(Long id, String nome, String sobreNome, List<Telefone> telefones) {
        this.id = id;
        this.nome = nome;
        this.sobreNome = sobreNome;
        this.telefones = new ArrayList<>();
    }

    // Getters

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getSobreNome() {
        return sobreNome;
    }

    public List<Telefone> getTelefones() {
        return telefones;
    }

    // Setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setSobreNome(String sobreNome) {
        this.sobreNome = sobreNome;
    }

    public void setTelefones(List<Telefone> telefones) {
        this.telefones = telefones;
    }

    public void adicionarTelefone(Telefone telefone) {
        telefones.add(telefone);
    }

    public void removerTelefone(Telefone telefone) {
        telefones.remove(telefone);
    }


    public String toCsv() {
        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append(id).append("|");
        csvBuilder.append(nome).append("|");
        csvBuilder.append(sobreNome);

        for (Telefone telefone : telefones) {
            csvBuilder.append("|").append(telefone.toCsv());
        }

        return csvBuilder.toString();
    }

    
}
