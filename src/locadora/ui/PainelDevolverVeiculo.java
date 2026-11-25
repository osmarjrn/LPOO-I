/*
 * GRR20252934
 * GABRIEL DE ALMEIDA
 */
package locadora.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import locadora.dados.Repositorio;
import locadora.modelo.Veiculo;

/**
 * Tela de devolução. Mostra os veículos locados; o usuário escolhe uma linha
 * e clica em "Devolver veículo".
 *
 * @author Gabriel de Almeida
 */
public class PainelDevolverVeiculo extends JPanel implements Atualizavel {

    private Repositorio repositorio = Repositorio.getInstance();
    private VeiculoLocadoTableModel modeloTabela = new VeiculoLocadoTableModel();
    private JTable tabela = new JTable(modeloTabela);
    private JButton botaoDevolver = new JButton("Devolver veículo");

    /** Monta a tela. */
    public PainelDevolverVeiculo() {
        setLayout(new BorderLayout());

        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel baixo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        baixo.add(botaoDevolver);
        add(baixo, BorderLayout.SOUTH);

        botaoDevolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                devolver();
            }
        });

        atualizar();
    }

    /** Recarrega a tabela; a janela principal chama ao abrir a aba. */
    @Override
    public void atualizar() {
        modeloTabela.setVeiculos(repositorio.listarLocados());
    }

    /** Devolve o veículo da linha selecionada. */
    private void devolver() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um veículo na tabela.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Veiculo veiculo = modeloTabela.getVeiculo(linha);
        veiculo.devolver(); // a locação vira null e o estado volta a DISPONIVEL
        atualizar();        // o veículo devolvido sai da tabela
        JOptionPane.showMessageDialog(this, "Veículo " + veiculo.getPlaca() + " devolvido.");
    }
}
