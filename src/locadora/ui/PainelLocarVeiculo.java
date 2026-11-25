/*
 * GRR20252934
 * GABRIEL DE ALMEIDA
 */
package locadora.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import locadora.dados.Repositorio;
import locadora.modelo.Cliente;
import locadora.modelo.Veiculo;
import locadora.util.Formatador;

/**
 * Tela de locação. O usuário busca e escolhe o cliente, filtra os veículos,
 * escolhe um na tabela, informa dias e data e clica em Locar.
 *
 * @author Gabriel de Almeida
 */
public class PainelLocarVeiculo extends JPanel implements Atualizavel {

    private Repositorio repositorio = Repositorio.getInstance();

    // cliente
    private JTextField campoBusca = new JTextField(20);
    private JButton botaoBuscar = new JButton("Buscar");
    private DefaultListModel<Cliente> modeloClientes = new DefaultListModel<>();
    private JList<Cliente> listaClientes = new JList<>(modeloClientes);

    // veículos
    private FiltroVeiculosPanel filtro;
    private VeiculoLocacaoTableModel modeloTabela = new VeiculoLocacaoTableModel();
    private JTable tabela = new JTable(modeloTabela);

    // locação
    private JTextField campoDias = new JTextField(5);
    private JTextField campoData = new JTextField(9);
    private JButton botaoLocar = new JButton("Locar");

    /** Monta a tela. */
    public PainelLocarVeiculo() {
        setLayout(new BorderLayout());

        // parte de cima: busca de cliente e lista
        JPanel busca = new JPanel(new FlowLayout(FlowLayout.LEFT));
        busca.add(new JLabel("Cliente (nome, sobrenome ou CPF):"));
        busca.add(campoBusca);
        busca.add(botaoBuscar);
        listaClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaClientes.setVisibleRowCount(4);
        JPanel painelCliente = new JPanel(new BorderLayout());
        painelCliente.add(busca, BorderLayout.NORTH);
        painelCliente.add(new JScrollPane(listaClientes), BorderLayout.CENTER);
        add(painelCliente, BorderLayout.NORTH);

        // meio: filtro e tabela
        filtro = new FiltroVeiculosPanel(new Runnable() {
            @Override
            public void run() {
                recarregarVeiculos(); // chamado quando algum combo do filtro muda
            }
        });
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JPanel painelVeiculos = new JPanel(new BorderLayout());
        painelVeiculos.add(filtro, BorderLayout.NORTH);
        painelVeiculos.add(new JScrollPane(tabela), BorderLayout.CENTER);
        add(painelVeiculos, BorderLayout.CENTER);

        // parte de baixo: dias, data e botão
        campoData.setText(Formatador.data(Calendar.getInstance())); // hoje
        JPanel painelLocacao = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelLocacao.add(new JLabel("Dias:"));
        painelLocacao.add(campoDias);
        painelLocacao.add(new JLabel("Data (dd/MM/aaaa):"));
        painelLocacao.add(campoData);
        painelLocacao.add(botaoLocar);
        add(painelLocacao, BorderLayout.SOUTH);

        // eventos
        // o mesmo listener serve para o botão Buscar e para a tecla Enter no campo
        ActionListener aoBuscar = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                recarregarClientes();
            }
        };
        botaoBuscar.addActionListener(aoBuscar);
        campoBusca.addActionListener(aoBuscar);

        botaoLocar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                locar();
            }
        });

        atualizar();
    }

    /** Recarrega clientes e veículos; a janela principal chama ao abrir a aba. */
    @Override
    public void atualizar() {
        recarregarClientes();
        recarregarVeiculos();
    }

    /** Preenche a lista com os clientes que combinam com o texto da busca. */
    private void recarregarClientes() {
        modeloClientes.clear();
        for (Cliente c : repositorio.buscarClientes(campoBusca.getText())) {
            modeloClientes.addElement(c);
        }
    }

    /** Preenche a tabela com os veículos DISPONIVEL que passam no filtro. */
    private void recarregarVeiculos() {
        if (filtro == null) {
            return; // o filtro pode avisar uma mudança ainda na criação da tela
        }
        modeloTabela.setVeiculos(repositorio.listarDisponiveis(
                filtro.getTipo(), filtro.getMarca(), filtro.getCategoria()));
    }

    /** Valida os dados (cliente, veículo, dias, data) e loca o veículo. */
    private void locar() {
        Cliente cliente = listaClientes.getSelectedValue();
        if (cliente == null) {
            aviso("Selecione um cliente.");
            return;
        }

        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            aviso("Selecione um veículo na tabela.");
            return;
        }
        Veiculo veiculo = modeloTabela.getVeiculo(linha);

        int dias;
        try {
            dias = Integer.parseInt(campoDias.getText().trim());
        } catch (NumberFormatException e) {
            aviso("Informe o número de dias como um número inteiro.");
            return;
        }
        if (dias < 1) {
            aviso("O número de dias deve ser pelo menos 1.");
            return;
        }

        Calendar data = Formatador.converterData(campoData.getText());
        if (data == null) {
            aviso("Data inválida. Use dd/MM/aaaa com uma data que exista.");
            return;
        }

        try {
            repositorio.locar(veiculo, dias, data, cliente);
        } catch (IllegalStateException | IllegalArgumentException e) {
            // por exemplo: o cliente já tem um veículo locado
            aviso(e.getMessage());
            recarregarVeiculos();
            return;
        }

        recarregarVeiculos(); // o veículo locado sai da tabela
        campoDias.setText("");
        JOptionPane.showMessageDialog(this, "Veículo locado! Valor da locação: "
                + Formatador.moeda(veiculo.getLocacao().getValor()));
    }

    /** Mostra uma mensagem de aviso. */
    private void aviso(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Atenção", JOptionPane.WARNING_MESSAGE);
    }
}
