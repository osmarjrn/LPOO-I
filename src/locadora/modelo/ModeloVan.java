/*
 * GRR20250999
 * OSMAR NUNES JÚNIOR
 */
package locadora.modelo;

/**
 * Modelos de van disponíveis para cadastro.
 *
 * @author Osmar Nunes Júnior
 */
public enum ModeloVan {

    KOMBI("Kombi"),
    SPRINTER("Sprinter"),
    DUCATO("Ducato"),
    MASTER("Master"),
    TRANSIT("Transit");

    /** Nome mostrado ao usuário. */
    private final String descricao;

    /**
     * Cria um modelo.
     *
     * @param descricao nome mostrado ao usuário
     */
    ModeloVan(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
