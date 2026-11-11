/*
 * GRR20250999
 * OSMAR NUNES JÚNIOR
 */
package locadora.modelo;

import java.util.Calendar;
import java.util.regex.Pattern;

/**
 * Classe abstrata que representa um veículo da locadora. Implementa
 * {@link VeiculoI}; as subclasses {@link Automovel}, {@link Motocicleta} e
 * {@link Van} definem o modelo e a tabela de preços da diária.
 *
 * <p>Conforme o enunciado, a classe não tem um get/set para cada atributo:
 * só existem os métodos da interface {@link VeiculoI}.</p>
 *
 * <p>Ciclo de vida: NOVO ou DISPONIVEL → (locar) → LOCADO → (devolver) →
 * DISPONIVEL → (vender) → VENDIDO. Só um veículo DISPONIVEL pode ser locado
 * ou vendido.</p>
 *
 * @author Osmar Nunes Júnior
 */
public abstract class Veiculo implements VeiculoI {

    /** Formato da placa: três letras maiúsculas, hífen e quatro dígitos. */
    private static final Pattern FORMATO_PLACA = Pattern.compile("[A-Z]{3}-[0-9]{4}");

    /** Percentual de desvalorização por ano de uso, na venda. */
    private static final double DESVALORIZACAO_ANUAL = 0.15;

    /** Fração mínima do valor de compra que o veículo vale na venda. */
    private static final double FRACAO_MINIMA_VENDA = 0.1;

    /** Marca do veículo. */
    private Marca marca;

    /** Estado do veículo. */
    private Estado estado;

    /** Locação atual; é {@code null} quando o estado não é LOCADO. */
    private Locacao locacao;

    /** Categoria do veículo (define o valor da diária). */
    private Categoria categoria;

    /** Valor pago na compra do veículo, em reais. */
    private double valorDeCompra;

    /** Placa no formato XXX-0000. */
    private String placa;

    /** Ano modelo. */
    private int ano;

    /**
     * Cria um veículo com todos os atributos.
     *
     * @param marca         marca
     * @param estado        estado inicial
     * @param locacao       locação atual (deve ser {@code null} se o estado
     *                      não for LOCADO e não nula se for LOCADO)
     * @param categoria     categoria
     * @param valorDeCompra valor de compra, em reais (maior que zero)
     * @param placa         placa no formato XXX-0000 (minúsculas são aceitas
     *                      e convertidas)
     * @param ano           ano modelo
     * @throws IllegalArgumentException se algum dado for inválido
     */
    public Veiculo(Marca marca, Estado estado, Locacao locacao, Categoria categoria,
            double valorDeCompra, String placa, int ano) {
        if (marca == null || estado == null || categoria == null) {
            throw new IllegalArgumentException("Marca, estado e categoria são obrigatórios.");
        }
        if (valorDeCompra <= 0) {
            throw new IllegalArgumentException("O valor de compra deve ser maior que zero.");
        }
        if (placa == null || !FORMATO_PLACA.matcher(placa.trim().toUpperCase()).matches()) {
            throw new IllegalArgumentException("Placa inválida (use o formato XXX-0000): " + placa);
        }
        int anoMaximo = anoAtual() + 1;
        if (ano < 1900 || ano > anoMaximo) {
            throw new IllegalArgumentException("Ano inválido (de 1900 a " + anoMaximo + "): " + ano);
        }
        if ((estado == Estado.LOCADO) != (locacao != null)) {
            throw new IllegalArgumentException(
                    "A locação deve existir somente quando o estado for LOCADO.");
        }
        this.marca = marca;
        this.estado = estado;
        this.locacao = locacao;
        this.categoria = categoria;
        this.valorDeCompra = valorDeCompra;
        this.placa = placa.trim().toUpperCase();
        this.ano = ano;
    }

    /**
     * Aluga o veículo: cria a {@link Locacao} (com o valor da diária vezes o
     * número de dias) e muda o estado para LOCADO.
     *
     * @param dias    número de dias (pelo menos 1)
     * @param data    data da locação
     * @param cliente cliente que está locando
     * @throws IllegalStateException    se o veículo não estiver DISPONIVEL
     * @throws IllegalArgumentException se os dias, a data ou o cliente forem inválidos
     */
    @Override
    public void locar(int dias, Calendar data, Cliente cliente) {
        if (estado != Estado.DISPONIVEL) {
            throw new IllegalStateException(
                    "O veículo " + placa + " não está disponível para locação (estado: " + estado + ").");
        }
        if (dias < 1) {
            throw new IllegalArgumentException("O número de dias deve ser pelo menos 1.");
        }
        if (data == null || cliente == null) {
            throw new IllegalArgumentException("A data e o cliente são obrigatórios.");
        }
        double valor = getValorDiariaLocacao() * dias;
        this.locacao = new Locacao(dias, valor, data, cliente);
        this.estado = Estado.LOCADO;
    }

    /**
     * Vende o veículo: muda o estado para VENDIDO. Depois disso ele não pode
     * mais ser alugado.
     *
     * @throws IllegalStateException se o veículo não estiver DISPONIVEL
     */
    @Override
    public void vender() {
        if (estado != Estado.DISPONIVEL) {
            throw new IllegalStateException(
                    "O veículo " + placa + " não pode ser vendido (estado: " + estado + ").");
        }
        this.estado = Estado.VENDIDO;
    }

    /**
     * Devolve o veículo: a locação é descartada ({@code null}) e o estado
     * volta a ser DISPONIVEL.
     *
     * @throws IllegalStateException se o veículo não estiver LOCADO
     */
    @Override
    public void devolver() {
        if (estado != Estado.LOCADO) {
            throw new IllegalStateException(
                    "O veículo " + placa + " não está locado (estado: " + estado + ").");
        }
        this.locacao = null;
        this.estado = Estado.DISPONIVEL;
    }

    @Override
    public Estado getEstado() {
        return estado;
    }

    @Override
    public Marca getMarca() {
        return marca;
    }

    @Override
    public Categoria getCategoria() {
        return categoria;
    }

    @Override
    public Locacao getLocacao() {
        return locacao;
    }

    @Override
    public String getPlaca() {
        return placa;
    }

    @Override
    public int getAno() {
        return ano;
    }

    /**
     * Calcula o valor para venda usando a idade do veículo no ano atual.
     * Um veículo com ano modelo no futuro conta como idade zero.
     *
     * @return valor para venda, em reais
     */
    @Override
    public double getValorParaVenda() {
        int idade = Math.max(0, anoAtual() - ano);
        return calcularValorParaVenda(valorDeCompra, idade);
    }

    /**
     * Fórmula do valor para venda, separada para poder ser testada com
     * qualquer idade:
     * valorDeCompra - idade * 0,15 * valorDeCompra; se o resultado for menor
     * que 10% do valor de compra (ou negativo), vale 10% do valor de compra.
     *
     * @param valorDeCompra valor de compra, em reais
     * @param idadeEmAnos   idade do veículo, em anos
     * @return valor para venda, em reais
     */
    public static double calcularValorParaVenda(double valorDeCompra, int idadeEmAnos) {
        double valor = valorDeCompra - idadeEmAnos * DESVALORIZACAO_ANUAL * valorDeCompra;
        double minimo = valorDeCompra * FRACAO_MINIMA_VENDA;
        return (valor < minimo) ? minimo : valor;
    }

    /**
     * Valor da diária de locação, conforme a tabela do tipo de veículo e a
     * categoria. Implementado em {@link Automovel}, {@link Motocicleta} e
     * {@link Van}.
     *
     * @return valor da diária, em reais
     */
    @Override
    public abstract double getValorDiariaLocacao();

    /**
     * Retorna o ano atual (de acordo com o relógio do computador).
     *
     * @return ano atual com quatro dígitos
     */
    private static int anoAtual() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " " + placa + " (" + marca + ", " + ano + ", " + estado + ")";
    }
}
