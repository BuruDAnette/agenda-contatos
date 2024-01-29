import java.util.Objects;

/**
 * Tipo Telefone.
 */
public class Telefone {
    private Long id;
    private String ddd;
    private Long numero;

    public Telefone() {
    }

    /**
     *
     * @param ddd    o ddd
     * @param numero o numero
     */
    public Telefone(String ddd, Long numero) {
        this.ddd = ddd;
        this.numero = numero;
    }

    public Long getId() {
        return id;
    }

    public String getDdd() {
        return ddd;
    }

    public void setDdd(String ddd) {
        this.ddd = ddd;
    }

    public Long getNumero() {
        return numero;
    }

    public void setNumero(Long numero) {
        this.numero = numero;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Telefone telefone = (Telefone) obj;
        return Objects.equals(ddd, telefone.ddd) && Objects.equals(numero, telefone.numero);
    }
}
