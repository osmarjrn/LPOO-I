/*
 * GRR20250999
 * OSMAR NUNES JÚNIOR
 */
package locadora.modelo;

/**
 * Motocicleta. Diária por categoria: POPULAR R$ 70,00, INTERMEDIARIO
 * R$ 200,00 e LUXO R$ 350,00.
 *
 * @author Osmar Nunes Júnior
 */
public class Motocicleta extends Veiculo {

    /** Modelo da motocicleta. */
    private ModeloMotocicleta modelo;

    /**
     * Cria uma motocicleta com todos os atributos.
     *
     * @param marca         marca
     * @param estado        estado inicial
     * @param locacao       locação atual (null se não estiver LOCADO)
     * @param categoria     categoria
     * @param valorDeCompra valor de compra, em reais
     * @param placa         placa no formato XXX-0000
     * @param ano           ano modelo
     * @param modelo        modelo da motocicleta
     */
    public Motocicleta(Marca marca, Estado estado, Locacao locacao, Categoria categoria,
            double valorDeCompra, String placa, int ano, ModeloMotocicleta modelo) {
        super(marca, estado, locacao, categoria, valorDeCompra, placa, ano);
        if (modelo == null) {
            throw new IllegalArgumentException("O modelo é obrigatório.");
        }
        this.modelo = modelo;
    }

    /**
     * Retorna o modelo.
     *
     * @return modelo da motocicleta
     */
    public ModeloMotocicleta getModelo() {
        return modelo;
    }

    @Override
    public double getValorDiariaLocacao() {
        switch (getCategoria()) {
            case POPULAR:
                return 70.00;
            case INTERMEDIARIO:
                return 200.00;
            case LUXO:
                return 350.00;
            default:
                throw new IllegalStateException("Categoria desconhecida: " + getCategoria());
        }
    }
}
