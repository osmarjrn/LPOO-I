/*
 * GRR20250999
 * OSMAR NUNES JÚNIOR
 */
package locadora.modelo;

/**
 * Funções de apoio para as telas manipularem veículos de qualquer tipo por
 * meio da superclasse {@link Veiculo} (polimorfismo), sem precisar de um
 * get para o modelo na própria interface do enunciado.
 *
 * @author Osmar Nunes Júnior
 */
public final class VeiculoUtil {

    /** Impede a criação de instâncias (classe só com métodos estáticos). */
    private VeiculoUtil() {
    }

    /**
     * Descobre o tipo de um veículo.
     *
     * @param veiculo veículo (não nulo)
     * @return {@link TipoVeiculo#AUTOMOVEL}, {@link TipoVeiculo#VAN} ou
     *         {@link TipoVeiculo#MOTOCICLETA}
     * @throws IllegalArgumentException se o tipo do veículo for desconhecido
     */
    public static TipoVeiculo tipoDe(Veiculo veiculo) {
        if (veiculo instanceof Automovel) {
            return TipoVeiculo.AUTOMOVEL;
        }
        if (veiculo instanceof Van) {
            return TipoVeiculo.VAN;
        }
        if (veiculo instanceof Motocicleta) {
            return TipoVeiculo.MOTOCICLETA;
        }
        throw new IllegalArgumentException("Tipo de veículo desconhecido: " + veiculo);
    }

    /**
     * Retorna o texto do modelo de um veículo (para a coluna "Modelo" das
     * tabelas), qualquer que seja o seu tipo.
     *
     * @param veiculo veículo (não nulo)
     * @return nome do modelo, como "Gol", "CG 125" ou "Sprinter"
     */
    public static String descricaoModelo(Veiculo veiculo) {
        if (veiculo instanceof Automovel) {
            return ((Automovel) veiculo).getModelo().toString();
        }
        if (veiculo instanceof Van) {
            return ((Van) veiculo).getModelo().toString();
        }
        if (veiculo instanceof Motocicleta) {
            return ((Motocicleta) veiculo).getModelo().toString();
        }
        throw new IllegalArgumentException("Tipo de veículo desconhecido: " + veiculo);
    }

    /**
     * Retorna os modelos que o usuário pode escolher para um tipo de
     * veículo (para preencher a caixa de seleção de modelo).
     *
     * @param tipo tipo do veículo
     * @return vetor com os valores do enum de modelos do tipo
     */
    public static Object[] modelosDoTipo(TipoVeiculo tipo) {
        switch (tipo) {
            case AUTOMOVEL:
                return ModeloAutomovel.values();
            case VAN:
                return ModeloVan.values();
            case MOTOCICLETA:
                return ModeloMotocicleta.values();
            default:
                throw new IllegalArgumentException("Tipo desconhecido: " + tipo);
        }
    }
}
