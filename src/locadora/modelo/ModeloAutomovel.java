/*
 * GRR20250999
 * OSMAR NUNES JÚNIOR
 */
package locadora.modelo;

/**
 * Modelos de automóvel disponíveis para cadastro.
 *
 * @author Osmar Nunes Júnior
 */
public enum ModeloAutomovel {

    GOL("Gol"),
    CELTA("Celta"),
    PALIO("Palio"),
    UNO("Uno"),
    ONIX("Onix"),
    KA("Ka"),
    SANDERO("Sandero"),
    CIVIC("Civic"),
    COROLLA("Corolla"),
    CLASSE_C("Classe C");

    /** Nome mostrado ao usuário. */
    private final String descricao;

    /**
     * Cria um modelo.
     *
     * @param descricao nome mostrado ao usuário
     */
    ModeloAutomovel(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
