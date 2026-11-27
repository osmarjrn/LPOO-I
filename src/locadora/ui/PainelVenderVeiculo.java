/*
 * GRR20251118
 * CHRISTIAN GUILHERME SOARES SANT'ANA
 */
package locadora.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import locadora.dados.Repositorio;
import locadora.modelo.Veiculo;
import locadora.util.Formatador;

/**
 * Tela de venda de veículos. Exibe apenas veículos no estado DISPONIVEL,
 * permite filtrar por tipo, marca e categoria e efetiva a venda do veículo
 * selecionado.
 *
 * @author Christian Guilherme Soares Sant'ana
 */
public class PainelVenderVeiculo extends JPanel implements Atualizavel {

    /** Repositório compartilhado em memória. */
    private final Repositorio repositorio = Repositorio.getInstance();

    /** Painel superior com os combos de filtro. */
    private final FiltroVeiculosPanel painelFiltros;

    /** Modelo da tabela de veículos disponíveis para venda. */
    private final VeiculoVendaTableModel modeloTabela = new VeiculoVendaTableModel();

    /** Tabela visual de veículos. */
    private final JTable tabela = new JTable(modeloTabela);

    /** Botão para concretizar a venda do veículo selecionado. */
    private final JButton botaoVender = new JButton("Vender");

    /** Monta a tela de venda de veículos. */
    public PainelVenderVeiculo() {
        setLayout(new BorderLayout());

        painelFiltros = new FiltroVeiculosPanel(new Runnable() {
            @Override
            public void run() {
                atualizar();
            }
        });
        add(painelFiltros, BorderLayout.NORTH);

        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel painelInferior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelInferior.add(botaoVender);
        add(painelInferior, BorderLayout.SOUTH);

        botaoVender.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                venderSelecionado();
            }
        });

        atualizar();
    }

    /** Recarrega a tabela aplicando os filtros selecionados. */
    @Override
    public void atualizar() {
        List<Veiculo> disponiveis = repositorio.listarDisponiveis(
                painelFiltros.getTipo(),
                painelFiltros.getMarca(),
                painelFiltros.getCategoria());
        modeloTabela.setVeiculos(disponiveis);
    }

    /** Vende o veículo selecionado na tabela e atualiza a listagem. */
    private void venderSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um veículo na tabela para vender.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Veiculo veiculo = modeloTabela.getVeiculo(linha);
        double valorVenda = veiculo.getValorParaVenda();

        int confirmacao = JOptionPane.showConfirmDialog(this,
                "Confirmar a venda do veículo " + veiculo.getPlaca()
                + " por " + Formatador.moeda(valorVenda) + "?",
                "Confirmar venda", JOptionPane.YES_NO_OPTION);

        if (confirmacao != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            veiculo.vender();
            atualizar();
            JOptionPane.showMessageDialog(this,
                    "Veículo " + veiculo.getPlaca() + " vendido por " + Formatador.moeda(valorVenda) + "!");
        } catch (IllegalStateException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    "Atenção", JOptionPane.WARNING_MESSAGE);
        }
    }
}