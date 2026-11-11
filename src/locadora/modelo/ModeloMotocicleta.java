/*
 * GRR20250999
 * OSMAR NUNES JÚNIOR
 */
package locadora.modelo;

/**
 * Modelos de motocicleta disponíveis para cadastro.
 *
 * @author Osmar Nunes Júnior
 */
public enum ModeloMotocicleta {

    CG_125("CG 125"),
    BIZ_125("Biz 125"),
    CBR_500("CBR 500"),
    XRE_300("XRE 300"),
    FAZER_250("Fazer 250"),
    NINJA_400("Ninja 400");

    /** Nome mostrado ao usuário. */
    private final String descricao;

    /**
     * Cria um modelo.
     *
     * @param descricao nome mostrado ao usuário
     */
    ModeloMotocicleta(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
