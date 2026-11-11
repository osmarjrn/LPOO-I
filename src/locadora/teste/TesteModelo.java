/*
 * GRR20250999
 * OSMAR NUNES JÚNIOR
 */
package locadora.teste;

import java.util.Calendar;
import java.util.List;
import locadora.dados.Repositorio;
import locadora.modelo.Automovel;
import locadora.modelo.Categoria;
import locadora.modelo.Cliente;
import locadora.modelo.Estado;
import locadora.modelo.Marca;
import locadora.modelo.ModeloAutomovel;
import locadora.modelo.ModeloMotocicleta;
import locadora.modelo.ModeloVan;
import locadora.modelo.Motocicleta;
import locadora.modelo.TipoVeiculo;
import locadora.modelo.Van;
import locadora.modelo.Veiculo;
import locadora.modelo.VeiculoUtil;
import locadora.util.Formatador;

/**
 * Testes do modelo de domínio e do repositório, sem biblioteca externa
 * (basta rodar {@code java locadora.teste.TesteModelo}). Cada linha mostra
 * [OK] ou [ERRO]; no final há o total.
 *
 * @author Osmar Nunes Júnior
 */
public class TesteModelo {

    /** Quantidade de testes que passaram. */
    private static int ok = 0;

    /** Quantidade de testes que falharam. */
    private static int erro = 0;

    /** Ano atual, usado para montar veículos com idade conhecida. */
    private static final int ANO = Calendar.getInstance().get(Calendar.YEAR);

    /**
     * Registra o resultado de um teste.
     *
     * @param descricao o que está sendo testado
     * @param condicao  {@code true} se passou
     */
    private static void conferir(String descricao, boolean condicao) {
        System.out.println((condicao ? "[OK]   " : "[ERRO] ") + descricao);
        if (condicao) {
            ok++;
        } else {
            erro++;
        }
    }

    /**
     * Confere se uma operação lança uma exceção do tipo esperado.
     *
     * @param descricao descrição do teste
     * @param esperada  tipo da exceção esperada
     * @param operacao  operação que deve falhar
     */
    private static void lanca(String descricao, Class<? extends Exception> esperada, Runnable operacao) {
        try {
            operacao.run();
            conferir(descricao + " (deveria lançar " + esperada.getSimpleName() + ")", false);
        } catch (Exception e) {
            conferir(descricao + " -> " + esperada.getSimpleName(), esperada.isInstance(e));
        }
    }

    /**
     * Cria um automóvel DISPONIVEL de teste.
     *
     * @param placa placa do veículo
     * @param ano   ano modelo
     * @return automóvel Gol, popular, comprado por R$ 40.000,00
     */
    private static Automovel gol(String placa, int ano) {
        return new Automovel(Marca.VW, Estado.DISPONIVEL, null, Categoria.POPULAR, 40000, placa, ano, ModeloAutomovel.GOL);
    }

    /**
     * Executa todos os testes.
     *
     * @param args não utilizado
     */
    public static void main(String[] args) {
        testarDiarias();
        testarValorParaVenda();
        testarEstados();
        testarPlaca();
        testarLocacao();
        testarRepositorio();
        testarFormatacao();
        System.out.println("\nTotal: " + ok + " OK, " + erro + " ERRO");
        if (erro > 0) {
            System.exit(1);
        }
    }

    /** Tabelas de valor da diária de cada tipo de veículo. */
    private static void testarDiarias() {
        System.out.println("--- Diárias (tabela do enunciado)");
        double[][] esperado = {{100, 300, 450}, {70, 200, 350}, {200, 400, 600}};
        Categoria[] cats = Categoria.values();
        for (int i = 0; i < cats.length; i++) {
            Veiculo a = new Automovel(Marca.GM, Estado.DISPONIVEL, null, cats[i], 1000, "AAA-0001", ANO, ModeloAutomovel.ONIX);
            Veiculo m = new Motocicleta(Marca.HONDA, Estado.DISPONIVEL, null, cats[i], 1000, "AAA-0002", ANO, ModeloMotocicleta.CG_125);
            Veiculo v = new Van(Marca.MERCEDES, Estado.DISPONIVEL, null, cats[i], 1000, "AAA-0003", ANO, ModeloVan.SPRINTER);
            conferir("Automóvel " + cats[i] + " = " + esperado[0][i], a.getValorDiariaLocacao() == esperado[0][i]);
            conferir("Motocicleta " + cats[i] + " = " + esperado[1][i], m.getValorDiariaLocacao() == esperado[1][i]);
            conferir("Van " + cats[i] + " = " + esperado[2][i], v.getValorDiariaLocacao() == esperado[2][i]);
        }
    }

    /** Fórmula do valor para venda. */
    private static void testarValorParaVenda() {
        System.out.println("--- Valor para venda");
        conferir("idade 0 -> valor de compra", gol("BBB-0001", ANO).getValorParaVenda() == 40000);
        conferir("idade 2 -> 70% (40000 - 2*0,15*40000 = 28000)",
                Math.abs(gol("BBB-0002", ANO - 2).getValorParaVenda() - 28000) < 1e-6);
        conferir("idade 6 -> mínimo de 10% (4000)",
                Math.abs(gol("BBB-0003", ANO - 6).getValorParaVenda() - 4000) < 1e-6);
        conferir("idade 10 (resultado negativo) -> 10% (4000)",
                Math.abs(gol("BBB-0004", ANO - 10).getValorParaVenda() - 4000) < 1e-6);
        conferir("ano modelo do ano que vem -> conta como idade 0",
                gol("BBB-0005", ANO + 1).getValorParaVenda() == 40000);
        conferir("fórmula direta: compra 1000, idade 3 -> 550",
                Math.abs(Veiculo.calcularValorParaVenda(1000, 3) - 550) < 1e-6);
        conferir("fórmula direta: compra 1000, idade 7 -> 100",
                Math.abs(Veiculo.calcularValorParaVenda(1000, 7) - 100) < 1e-6);
    }

    /** Transições de estado de um veículo. */
    private static void testarEstados() {
        System.out.println("--- Estados");
        Cliente c = new Cliente("Ana", "Lima", "1234567", "111.444.777-35", "Rua A, 10");
        Calendar hoje = Calendar.getInstance();

        Veiculo v = gol("CCC-0001", ANO);
        conferir("começa DISPONIVEL e sem locação", v.getEstado() == Estado.DISPONIVEL && v.getLocacao() == null);
        v.locar(3, hoje, c);
        conferir("locar -> LOCADO com locação criada", v.getEstado() == Estado.LOCADO && v.getLocacao() != null);
        conferir("valor da locação = diária (100) x 3 dias = 300", v.getLocacao().getValor() == 300);
        conferir("locação guarda dias, cliente e data",
                v.getLocacao().getDias() == 3 && v.getLocacao().getCliente() == c
                && Formatador.data(v.getLocacao().getData()).equals(Formatador.data(hoje)));
        lanca("locar um veículo já locado", IllegalStateException.class, () -> v.locar(1, hoje, c));
        lanca("vender um veículo locado", IllegalStateException.class, v::vender);
        v.devolver();
        conferir("devolver -> DISPONIVEL e locação null", v.getEstado() == Estado.DISPONIVEL && v.getLocacao() == null);
        lanca("devolver um veículo que não está locado", IllegalStateException.class, v::devolver);
        v.vender();
        conferir("vender -> VENDIDO", v.getEstado() == Estado.VENDIDO);
        lanca("locar um veículo vendido", IllegalStateException.class, () -> v.locar(1, hoje, c));
        lanca("vender duas vezes", IllegalStateException.class, v::vender);

        Veiculo novo = new Van(Marca.FIAT, Estado.NOVO, null, Categoria.LUXO, 90000, "CCC-0002", ANO, ModeloVan.DUCATO);
        lanca("locar veículo NOVO (só DISPONIVEL pode ser locado)", IllegalStateException.class, () -> novo.locar(1, hoje, c));
    }

    /** Validação da placa e dos demais dados do construtor. */
    private static void testarPlaca() {
        System.out.println("--- Dados do veículo");
        conferir("placa ABC-1234 é aceita", gol("ABC-1234", ANO).getPlaca().equals("ABC-1234"));
        conferir("placa em minúsculas é convertida", gol("abc-1234", ANO).getPlaca().equals("ABC-1234"));
        for (String ruim : new String[] {"ABC1234", "AB-12345", "ABCD-123", "123-ABCD", "ABC-12A4", "", "  "}) {
            lanca("placa inválida \"" + ruim + "\"", IllegalArgumentException.class, () -> gol(ruim, ANO));
        }
        lanca("placa null", IllegalArgumentException.class, () -> gol(null, ANO));
        lanca("valor de compra zero", IllegalArgumentException.class,
                () -> new Automovel(Marca.VW, Estado.DISPONIVEL, null, Categoria.POPULAR, 0, "DDD-0001", ANO, ModeloAutomovel.GOL));
        lanca("ano 1800", IllegalArgumentException.class, () -> gol("DDD-0002", 1800));
        lanca("modelo null", IllegalArgumentException.class,
                () -> new Automovel(Marca.VW, Estado.DISPONIVEL, null, Categoria.POPULAR, 1000, "DDD-0003", ANO, null));
        lanca("estado LOCADO sem locação", IllegalArgumentException.class,
                () -> new Automovel(Marca.VW, Estado.LOCADO, null, Categoria.POPULAR, 1000, "DDD-0004", ANO, ModeloAutomovel.GOL));
    }

    /** Tipo e modelo vistos pela superclasse. */
    private static void testarLocacao() {
        System.out.println("--- Polimorfismo (VeiculoUtil)");
        Veiculo[] todos = {
            gol("EEE-0001", ANO),
            new Motocicleta(Marca.YAMAHA, Estado.DISPONIVEL, null, Categoria.INTERMEDIARIO, 20000, "EEE-0002", ANO, ModeloMotocicleta.FAZER_250),
            new Van(Marca.MERCEDES, Estado.DISPONIVEL, null, Categoria.LUXO, 150000, "EEE-0003", ANO, ModeloVan.SPRINTER)
        };
        conferir("tipoDe: Automóvel, Motocicleta, Van",
                VeiculoUtil.tipoDe(todos[0]) == TipoVeiculo.AUTOMOVEL
                && VeiculoUtil.tipoDe(todos[1]) == TipoVeiculo.MOTOCICLETA
                && VeiculoUtil.tipoDe(todos[2]) == TipoVeiculo.VAN);
        conferir("descricaoModelo: Gol, Fazer 250, Sprinter",
                VeiculoUtil.descricaoModelo(todos[0]).equals("Gol")
                && VeiculoUtil.descricaoModelo(todos[1]).equals("Fazer 250")
                && VeiculoUtil.descricaoModelo(todos[2]).equals("Sprinter"));
        conferir("modelosDoTipo devolve a lista certa para cada tipo",
                VeiculoUtil.modelosDoTipo(TipoVeiculo.AUTOMOVEL).length == ModeloAutomovel.values().length
                && VeiculoUtil.modelosDoTipo(TipoVeiculo.VAN).length == ModeloVan.values().length
                && VeiculoUtil.modelosDoTipo(TipoVeiculo.MOTOCICLETA).length == ModeloMotocicleta.values().length);
    }

    /** Regras do repositório. */
    private static void testarRepositorio() {
        System.out.println("--- Repositório");
        Repositorio r = new Repositorio();
        Cliente ana = new Cliente("Ana", "Lima", "1111111", "111.444.777-35", "Rua A");
        Cliente bia = new Cliente("Beatriz", "Souza Lima", "2222222", "52998224725", "Rua B");
        Calendar hoje = Calendar.getInstance();

        conferir("adicionar cliente", r.adicionarCliente(ana) && r.adicionarCliente(bia) && r.listarClientes().size() == 2);
        conferir("CPF repetido é recusado (mesmo com máscara diferente)",
                !r.adicionarCliente(new Cliente("X", "Y", "3", "111.444.777-35", "R")) && !r.adicionarCliente(new Cliente("X", "Y", "3", "11144477735", "R")));
        conferir("cpfEmUso ignora o próprio cliente", !r.cpfEmUso("111.444.777-35", ana) && r.cpfEmUso("111.444.777-35", bia));
        conferir("buscar por nome", r.buscarClientes("ana").size() == 1 && r.buscarClientes("ana").get(0) == ana);
        conferir("buscar por sobrenome (Lima aparece em 2)", r.buscarClientes("lima").size() == 2);
        conferir("buscar por CPF com ou sem máscara", r.buscarClientes("52998224725").size() == 1 && r.buscarClientes("529.982").size() == 1);
        conferir("busca vazia lista todos", r.buscarClientes("").size() == 2);
        conferir("busca sem resultado", r.buscarClientes("zzz").isEmpty());

        Veiculo gol = gol("FFF-0001", ANO);
        Veiculo moto = new Motocicleta(Marca.HONDA, Estado.DISPONIVEL, null, Categoria.POPULAR, 15000, "FFF-0002", ANO, ModeloMotocicleta.CG_125);
        Veiculo van = new Van(Marca.MERCEDES, Estado.DISPONIVEL, null, Categoria.LUXO, 150000, "FFF-0003", ANO, ModeloVan.SPRINTER);
        Veiculo novo = new Automovel(Marca.FIAT, Estado.NOVO, null, Categoria.POPULAR, 30000, "FFF-0004", ANO, ModeloAutomovel.UNO);
        r.adicionarVeiculo(gol);
        r.adicionarVeiculo(moto);
        r.adicionarVeiculo(van);
        r.adicionarVeiculo(novo);
        lanca("placa repetida", IllegalArgumentException.class, () -> r.adicionarVeiculo(gol("fff-0001", ANO)));
        conferir("placaJaCadastrada", r.placaJaCadastrada("FFF-0002") && !r.placaJaCadastrada("ZZZ-9999"));

        List<Veiculo> disp = r.listarDisponiveis(null, null, null);
        conferir("disponíveis sem filtro: 3 (o NOVO não aparece)", disp.size() == 3 && !disp.contains(novo));
        conferir("filtro por tipo Van", r.listarDisponiveis(TipoVeiculo.VAN, null, null).size() == 1);
        conferir("filtro por marca Honda", r.listarDisponiveis(null, Marca.HONDA, null).get(0) == moto);
        conferir("filtro por categoria Luxo", r.listarDisponiveis(null, null, Categoria.LUXO).get(0) == van);
        conferir("filtros combinados (Automóvel + VW + Popular)", r.listarDisponiveis(TipoVeiculo.AUTOMOVEL, Marca.VW, Categoria.POPULAR).get(0) == gol);
        conferir("filtros combinados sem resultado (Van + Honda)", r.listarDisponiveis(TipoVeiculo.VAN, Marca.HONDA, null).isEmpty());

        r.locar(gol, 2, hoje, ana);
        conferir("locar tira o veículo da lista de disponíveis", r.listarDisponiveis(null, null, null).size() == 2);
        conferir("listarLocados mostra o veículo locado", r.listarLocados().size() == 1 && r.listarLocados().get(0) == gol);
        conferir("cliente com veículo locado", r.clientePossuiVeiculoLocado(ana) && !r.clientePossuiVeiculoLocado(bia));
        lanca("segundo veículo para o mesmo cliente", IllegalStateException.class, () -> r.locar(moto, 1, hoje, ana));
        lanca("excluir cliente com veículo locado", IllegalStateException.class, () -> r.removerCliente(ana));
        conferir("excluir cliente sem locação", r.listarClientes().contains(bia));
        r.removerCliente(bia);
        conferir("cliente excluído saiu da lista", !r.listarClientes().contains(bia) && r.listarClientes().size() == 1);
        lanca("excluir cliente que não existe", IllegalArgumentException.class, () -> r.removerCliente(bia));

        gol.devolver();
        conferir("depois de devolver, o cliente pode ser excluído", !r.clientePossuiVeiculoLocado(ana));
        r.removerCliente(ana);
        conferir("lista de clientes vazia", r.listarClientes().isEmpty());

        van.vender();
        conferir("vendido não aparece mais como disponível", !r.listarDisponiveis(null, null, null).contains(van));
        conferir("listarVeiculos mantém todos (4)", r.listarVeiculos().size() == 4);
        conferir("listagens devolvem cópias", r.listarVeiculos() != r.listarVeiculos());
    }

    /** Formatação de dinheiro, ano e data. */
    private static void testarFormatacao() {
        System.out.println("--- Formatador");
        conferir("moeda 100 -> R$100,00", Formatador.moeda(100).equals("R$100,00"));
        conferir("moeda 450.5 -> R$450,50", Formatador.moeda(450.5).equals("R$450,50"));
        conferir("moeda 45000 -> R$45.000,00", Formatador.moeda(45000).equals("R$45.000,00"));
        conferir("ano com 4 dígitos", Formatador.ano(2021).equals("2021"));
        Calendar d = Formatador.converterData("05/11/2026");
        conferir("converterData e data (ida e volta)", d != null && Formatador.data(d).equals("05/11/2026"));
        conferir("data inexistente 31/02/2026 -> null", Formatador.converterData("31/02/2026") == null);
        conferir("data fora do formato -> null", Formatador.converterData("2026-11-05") == null && Formatador.converterData("5/11/2026") == null
                && Formatador.converterData("") == null && Formatador.converterData(null) == null);
    }
}
