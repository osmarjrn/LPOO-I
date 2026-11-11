/*
 * GRR20250999
 * OSMAR NUNES JÚNIOR
 */
package locadora.modelo;

import java.util.Calendar;

/**
 * Locação de um veículo. É criada quando o veículo é locado e descartada
 * (o veículo volta a ter locação {@code null}) quando ele é devolvido.
 *
 * @author Osmar Nunes Júnior
 */
public class Locacao {

    /** Número de dias da locação. */
    private final int dias;

    /** Valor total da locação, em reais. */
    private final double valor;

    /** Data da locação. */
    private final Calendar data;

    /** Cliente que locou o veículo. */
    private final Cliente cliente;

    /**
     * Cria uma locação.
     *
     * @param dias    número de dias
     * @param valor   valor total em reais
     * @param data    data da locação
     * @param cliente cliente da locação
     */
    public Locacao(int dias, double valor, Calendar data, Cliente cliente) {
        this.dias = dias;
        this.valor = valor;
        this.data = (Calendar) data.clone();
        this.cliente = cliente;
    }

    /**
     * Retorna o número de dias (necessário para a tabela de devolução).
     *
     * @return número de dias da locação
     */
    public int getDias() {
        return dias;
    }

    /**
     * Retorna o valor da locação.
     *
     * @return valor total em reais
     */
    public double getValor() {
        return valor;
    }

    /**
     * Retorna a data da locação (uma cópia, para ninguém alterá-la por fora).
     *
     * @return data da locação
     */
    public Calendar getData() {
        return (Calendar) data.clone();
    }

    /**
     * Retorna o cliente da locação.
     *
     * @return cliente que locou o veículo
     */
    public Cliente getCliente() {
        return cliente;
    }
}
