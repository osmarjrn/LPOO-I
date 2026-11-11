/*
 * GRR20250999
 * OSMAR NUNES JÚNIOR
 */
package locadora.modelo;

/**
 * Van. Diária por categoria: POPULAR R$ 200,00, INTERMEDIARIO R$ 400,00 e
 * LUXO R$ 600,00.
 *
 * @author Osmar Nunes Júnior
 */
public class Van extends Veiculo {

    /** Modelo da van. */
    private ModeloVan modelo;

    /**
     * Cria uma van com todos os atributos.
     *
     * @param marca         marca
     * @param estado        estado inicial
     * @param locacao       locação atual (null se não estiver LOCADO)
     * @param categoria     categoria
     * @param valorDeCompra valor de compra, em reais
     * @param placa         placa no formato XXX-0000
     * @param ano           ano modelo
     * @param modelo        modelo da van
     */
    public Van(Marca marca, Estado estado, Locacao locacao, Categoria categoria,
            double valorDeCompra, String placa, int ano, ModeloVan modelo) {
        super(marca, estado, locacao, categoria, valorDeCompra, placa, ano);
        if (modelo == null) {
            throw new IllegalArgumentException("O modelo é obrigatório.");
        }
        this.modelo = modelo;
    }

    /**
     * Retorna o modelo.
     *
     * @return modelo da van
     */
    public ModeloVan getModelo() {
        return modelo;
    }

    @Override
    public double getValorDiariaLocacao() {
        switch (getCategoria()) {
            case POPULAR:
                return 200.00;
            case INTERMEDIARIO:
                return 400.00;
            case LUXO:
                return 600.00;
            default:
                throw new IllegalStateException("Categoria desconhecida: " + getCategoria());
        }
    }
}
