/*
 * GRR20250999
 * OSMAR NUNES JÚNIOR
 */
package locadora.modelo;

/**
 * Marca de um veículo (automóvel, motocicleta ou van).
 *
 * @author Osmar Nunes Júnior
 */
public enum Marca {

    VW("VW"),
    GM("GM"),
    FIAT("Fiat"),
    HONDA("Honda"),
    MERCEDES("Mercedes"),
    FORD("Ford"),
    TOYOTA("Toyota"),
    RENAULT("Renault"),
    YAMAHA("Yamaha"),
    SUZUKI("Suzuki"),
    BMW("BMW");

    /** Nome mostrado ao usuário (por exemplo, nas caixas de seleção). */
    private final String descricao;

    /**
     * Cria uma marca.
     *
     * @param descricao nome mostrado ao usuário
     */
    Marca(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
