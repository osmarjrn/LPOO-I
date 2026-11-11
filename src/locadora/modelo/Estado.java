/*
 * GRR20250999
 * OSMAR NUNES JÚNIOR
 */
package locadora.modelo;

/**
 * Estado de um veículo no ciclo de vida da locadora.
 *
 * <ul>
 *   <li>{@link #NOVO}: recém-comprado;</li>
 *   <li>{@link #DISPONIVEL}: pode ser locado ou vendido;</li>
 *   <li>{@link #LOCADO}: está alugado (tem uma {@link Locacao});</li>
 *   <li>{@link #VENDIDO}: já foi vendido e não pode mais ser alugado.</li>
 * </ul>
 *
 * @author Osmar Nunes Júnior
 */
public enum Estado {

    NOVO("Novo"),
    LOCADO("Locado"),
    DISPONIVEL("Disponível"),
    VENDIDO("Vendido");

    /** Nome mostrado ao usuário. */
    private final String descricao;

    /**
     * Cria um estado.
     *
     * @param descricao nome mostrado ao usuário
     */
    Estado(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
