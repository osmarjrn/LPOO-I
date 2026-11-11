/*
 * GRR20250999
 * OSMAR NUNES JÚNIOR
 */
package locadora.modelo;

/**
 * Categoria de um veículo. Define o valor da diária de uma locação (cada
 * tipo de veículo tem a sua tabela de preços).
 *
 * @author Osmar Nunes Júnior
 */
public enum Categoria {

    POPULAR("Popular"),
    INTERMEDIARIO("Intermediário"),
    LUXO("Luxo");

    /** Nome mostrado ao usuário. */
    private final String descricao;

    /**
     * Cria uma categoria.
     *
     * @param descricao nome mostrado ao usuário
     */
    Categoria(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
