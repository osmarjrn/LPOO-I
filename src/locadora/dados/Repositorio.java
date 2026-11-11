/*
 * GRR20250999
 * OSMAR NUNES JÚNIOR
 */
package locadora.dados;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import locadora.modelo.Categoria;
import locadora.modelo.Cliente;
import locadora.modelo.Estado;
import locadora.modelo.Marca;
import locadora.modelo.TipoVeiculo;
import locadora.modelo.Veiculo;
import locadora.modelo.VeiculoUtil;

/**
 * "Banco de dados" em memória da locadora: guarda os clientes e os veículos
 * e concentra as regras que envolvem mais de um objeto (CPF e placa únicos,
 * exclusão de cliente, um veículo locado por cliente, filtros das tabelas).
 *
 * <p>Todas as telas usam a mesma instância, obtida com
 * {@link #getInstance()}. Os métodos de listagem devolvem cópias das listas:
 * para incluir ou remover é preciso chamar os métodos próprios.</p>
 *
 * @author Osmar Nunes Júnior
 */
public class Repositorio {

    /** Instância única usada pelo sistema. */
    private static final Repositorio INSTANCIA = new Repositorio();

    /** Clientes cadastrados. */
    private final List<Cliente> clientes = new ArrayList<>();

    /** Veículos cadastrados (de qualquer estado). */
    private final List<Veiculo> veiculos = new ArrayList<>();

    /**
     * Cria um repositório vazio. O sistema usa {@link #getInstance()}; este
     * construtor existe para os testes poderem criar repositórios isolados.
     */
    public Repositorio() {
        // começa vazio
    }

    /**
     * Retorna o repositório único do sistema.
     *
     * @return repositório compartilhado por todas as telas
     */
    public static Repositorio getInstance() {
        return INSTANCIA;
    }

    // ------------------------------------------------------------ clientes

    /**
     * Lista todos os clientes.
     *
     * @return cópia da lista de clientes
     */
    public List<Cliente> listarClientes() {
        return new ArrayList<>(clientes);
    }

    /**
     * Cadastra um cliente, desde que o CPF ainda não esteja em uso.
     *
     * @param cliente cliente a cadastrar
     * @return {@code true} se foi cadastrado; {@code false} se o CPF já existe
     */
    public boolean adicionarCliente(Cliente cliente) {
        if (cpfEmUso(cliente.getCpf(), null)) {
            return false;
        }
        clientes.add(cliente);
        return true;
    }

    /**
     * Informa se um CPF já pertence a algum cliente (compara só os dígitos,
     * então "123.456.789-09" e "12345678909" são o mesmo CPF).
     *
     * @param cpf    CPF a procurar
     * @param ignorar cliente que deve ser ignorado na procura (use ao
     *                atualizar um cliente, para ele não conflitar consigo
     *                mesmo); pode ser {@code null}
     * @return {@code true} se outro cliente já usa esse CPF
     */
    public boolean cpfEmUso(String cpf, Cliente ignorar) {
        String procurado = soDigitos(cpf);
        for (Cliente c : clientes) {
            if (c != ignorar && soDigitos(c.getCpf()).equals(procurado)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Informa se o cliente está com algum veículo locado no momento.
     *
     * @param cliente cliente a verificar
     * @return {@code true} se existe veículo LOCADO em nome do cliente
     */
    public boolean clientePossuiVeiculoLocado(Cliente cliente) {
        for (Veiculo v : veiculos) {
            if (v.getEstado() == Estado.LOCADO && v.getLocacao().getCliente() == cliente) {
                return true;
            }
        }
        return false;
    }

    /**
     * Exclui um cliente. Um cliente com veículo locado não pode ser excluído.
     *
     * @param cliente cliente a excluir
     * @throws IllegalStateException    se o cliente possuir veículo locado
     * @throws IllegalArgumentException se o cliente não estiver cadastrado
     */
    public void removerCliente(Cliente cliente) {
        if (!clientes.contains(cliente)) {
            throw new IllegalArgumentException("Cliente não cadastrado.");
        }
        if (clientePossuiVeiculoLocado(cliente)) {
            throw new IllegalStateException(
                    "O cliente " + cliente.getNomeCompleto()
                    + " não pode ser excluído: possui veículo locado.");
        }
        clientes.remove(cliente);
    }

    /**
     * Procura clientes pelo nome, sobrenome ou CPF (sem diferenciar maiúsculas
     * de minúsculas; basta o termo aparecer em qualquer parte do campo).
     *
     * @param termo texto procurado; vazio devolve todos os clientes
     * @return clientes encontrados
     */
    public List<Cliente> buscarClientes(String termo) {
        String texto = (termo == null) ? "" : termo.trim().toLowerCase();
        String digitos = soDigitos(texto);
        List<Cliente> resultado = new ArrayList<>();
        for (Cliente c : clientes) {
            boolean acha = texto.isEmpty()
                    || c.getNome().toLowerCase().contains(texto)
                    || c.getSobrenome().toLowerCase().contains(texto)
                    || c.getNomeCompleto().toLowerCase().contains(texto)
                    || c.getCpf().toLowerCase().contains(texto)
                    || (!digitos.isEmpty() && soDigitos(c.getCpf()).contains(digitos));
            if (acha) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    // ------------------------------------------------------------ veículos

    /**
     * Informa se já existe um veículo com a placa.
     *
     * @param placa placa a procurar (maiúsculas ou minúsculas)
     * @return {@code true} se a placa já está cadastrada
     */
    public boolean placaJaCadastrada(String placa) {
        for (Veiculo v : veiculos) {
            if (v.getPlaca().equalsIgnoreCase(placa.trim())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Armazena um veículo em memória.
     *
     * @param veiculo veículo a incluir
     * @throws IllegalArgumentException se a placa já estiver cadastrada
     */
    public void adicionarVeiculo(Veiculo veiculo) {
        if (placaJaCadastrada(veiculo.getPlaca())) {
            throw new IllegalArgumentException(
                    "Já existe um veículo com a placa " + veiculo.getPlaca() + ".");
        }
        veiculos.add(veiculo);
    }

    /**
     * Lista todos os veículos, de qualquer estado.
     *
     * @return cópia da lista de veículos
     */
    public List<Veiculo> listarVeiculos() {
        return new ArrayList<>(veiculos);
    }

    /**
     * Lista os veículos no estado DISPONIVEL, com filtros opcionais. É a
     * consulta usada pelas telas de locar e de vender.
     *
     * @param tipo      tipo do veículo, ou {@code null} para não filtrar
     * @param marca     marca, ou {@code null} para não filtrar
     * @param categoria categoria, ou {@code null} para não filtrar
     * @return veículos disponíveis que atendem aos filtros
     */
    public List<Veiculo> listarDisponiveis(TipoVeiculo tipo, Marca marca, Categoria categoria) {
        List<Veiculo> resultado = new ArrayList<>();
        for (Veiculo v : veiculos) {
            boolean passa = v.getEstado() == Estado.DISPONIVEL
                    && (tipo == null || VeiculoUtil.tipoDe(v) == tipo)
                    && (marca == null || v.getMarca() == marca)
                    && (categoria == null || v.getCategoria() == categoria);
            if (passa) {
                resultado.add(v);
            }
        }
        return resultado;
    }

    /**
     * Lista os veículos que estão locados (para a tela de devolução).
     *
     * @return veículos no estado LOCADO
     */
    public List<Veiculo> listarLocados() {
        List<Veiculo> resultado = new ArrayList<>();
        for (Veiculo v : veiculos) {
            if (v.getEstado() == Estado.LOCADO) {
                resultado.add(v);
            }
        }
        return resultado;
    }

    /**
     * Loca um veículo para um cliente, respeitando a regra "um cliente pode
     * locar um veículo por vez". Depois de conferir a regra, chama o método
     * {@link Veiculo#locar(int, Calendar, Cliente)} do veículo.
     *
     * @param veiculo veículo a locar (deve estar DISPONIVEL)
     * @param dias    número de dias (pelo menos 1)
     * @param data    data da locação
     * @param cliente cliente que está locando
     * @throws IllegalStateException    se o cliente já tiver um veículo locado
     *                                  ou se o veículo não estiver disponível
     * @throws IllegalArgumentException se os dados forem inválidos
     */
    public void locar(Veiculo veiculo, int dias, Calendar data, Cliente cliente) {
        if (cliente != null && clientePossuiVeiculoLocado(cliente)) {
            throw new IllegalStateException(
                    "O cliente " + cliente.getNomeCompleto() + " já possui um veículo locado.");
        }
        veiculo.locar(dias, data, cliente);
    }

    /**
     * Deixa só os dígitos de um texto.
     *
     * @param texto texto qualquer
     * @return o texto sem nenhum caractere que não seja dígito
     */
    private static String soDigitos(String texto) {
        return texto == null ? "" : texto.replaceAll("\\D", "");
    }
}
