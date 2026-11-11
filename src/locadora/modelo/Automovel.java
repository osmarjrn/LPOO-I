/*
 * GRR20250999
 * OSMAR NUNES JÚNIOR
 */
package locadora.modelo;

/**
 * Automóvel. Diária por categoria: POPULAR R$ 100,00, INTERMEDIARIO
 * R$ 300,00 e LUXO R$ 450,00.
 *
 * @author Osmar Nunes Júnior
 */
public class Automovel extends Veiculo {

    /** Modelo do automóvel. */
    private ModeloAutomovel modelo;

    /**
     * Cria um automóvel com todos os atributos.
     *
     * @param marca         marca
     * @param estado        estado inicial
     * @param locacao       locação atual (null se não estiver LOCADO)
     * @param categoria     categoria
     * @param valorDeCompra valor de compra, em reais
     * @param placa         placa no formato XXX-0000
     * @param ano           ano modelo
     * @param modelo        modelo do automóvel
     */
    public Automovel(Marca marca, Estado estado, Locacao locacao, Categoria categoria,
            double valorDeCompra, String placa, int ano, ModeloAutomovel modelo) {
        super(marca, estado, locacao, categoria, valorDeCompra, placa, ano);
        if (modelo == null) {
            throw new IllegalArgumentException("O modelo é obrigatório.");
        }
        this.modelo = modelo;
    }

    /**
     * Retorna o modelo.
     *
     * @return modelo do automóvel
     */
    public ModeloAutomovel getModelo() {
        return modelo;
    }

    @Override
    public double getValorDiariaLocacao() {
        switch (getCategoria()) {
            case POPULAR:
                return 100.00;
            case INTERMEDIARIO:
                return 300.00;
            case LUXO:
                return 450.00;
            default:
                throw new IllegalStateException("Categoria desconhecida: " + getCategoria());
        }
    }
}
