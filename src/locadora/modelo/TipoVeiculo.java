/*
 * GRR20250999
 * OSMAR NUNES JÚNIOR
 */
package locadora.modelo;

/**
 * Tipo de veículo. É usado nos filtros das telas (automóvel, van ou
 * motocicleta) e para escolher qual lista de modelos mostrar.
 *
 * @author Osmar Nunes Júnior
 */
public enum TipoVeiculo {

    AUTOMOVEL("Automóvel"),
    VAN("Van"),
    MOTOCICLETA("Motocicleta");

    /** Nome mostrado ao usuário. */
    private final String descricao;

    /**
     * Cria um tipo de veículo.
     *
     * @param descricao nome mostrado ao usuário
     */
    TipoVeiculo(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
