public class Telefone {
    private Long id;
    private String ddd;
    private Long numero;

    /**
     * Incinializa um novo telefone
     *
     * @param id     the id
     * @param ddd    the ddd
     * @param numero the numero
     */
    public Telefone(Long id, String ddd, Long numero) {
        this.id = id;
        this.ddd = ddd;
        this.numero = numero;
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public String getDdd() {
        return ddd;
    }

    public Long getNumero() {
        return numero;
    }

    public String toCsv() {
        return id + "|" + ddd + "|" + numero;
    }
}
