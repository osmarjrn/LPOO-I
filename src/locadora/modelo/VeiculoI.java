/*
 * GRR20250999
 * OSMAR NUNES JÚNIOR
 */
package locadora.modelo;

import java.util.Calendar;

/**
 * Interface exigida pelo enunciado, implementada por {@link Veiculo}.
 *
 * @author Osmar Nunes Júnior
 */
public interface VeiculoI {

    /**
     * Muda o estado para LOCADO. Cria uma instância de {@link Locacao} e a
     * guarda no atributo locacao. Chama {@link #getValorDiariaLocacao()} para
     * calcular o valor da locação.
     *
     * @param dias    número de dias da locação
     * @param data    data da locação
     * @param cliente cliente que está locando
     */
    public void locar(int dias, Calendar data, Cliente cliente);

    /** Muda o estado para VENDIDO. O veículo não pode mais ser alugado. */
    public void vender();

    /** Muda o estado para DISPONIVEL. */
    public void devolver();

    /**
     * Retorna o estado do veículo.
     *
     * @return estado atual
     */
    public Estado getEstado();

    /**
     * Retorna a marca do veículo.
     *
     * @return marca
     */
    public Marca getMarca();

    /**
     * Retorna a categoria do veículo.
     *
     * @return categoria
     */
    public Categoria getCategoria();

    /**
     * Retorna a locação atual.
     *
     * @return locação, ou {@code null} se o veículo não estiver locado
     */
    public Locacao getLocacao();

    /**
     * Retorna a placa do veículo.
     *
     * @return placa no formato XXX-0000
     */
    public String getPlaca();

    /**
     * Retorna o ano modelo do veículo.
     *
     * @return ano modelo
     */
    public int getAno();

    /**
     * Calcula o valor para venda:
     * valorParaVenda = valorDeCompra - idadeEmAnos * 0,15 * valorDeCompra.
     * Se o resultado for menor que 10% do valorDeCompra (ou negativo),
     * o valor para venda é valorDeCompra * 0,1.
     *
     * @return valor para venda
     */
    public double getValorParaVenda();

    /**
     * Valor da diária de locação. É abstrato em {@link Veiculo}: cada tipo
     * de veículo tem a sua tabela de preços por categoria.
     *
     * @return valor da diária, em reais
     */
    public double getValorDiariaLocacao();
}
