/*
 * GRR20256527
 * CEZAR ANTHONIO
 */
package locadora.ui;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Janela principal do sistema: um {@link JTabbedPane} com uma aba por tela,
 * na ordem Clientes, Incluir veículo, Locar, Devolver e Vender. Ao trocar de
 * aba, a janela chama {@link Atualizavel#atualizar()} do painel mostrado,
 * então cada tela recarrega os dados do repositório (por exemplo, a tabela de
 * Locar some com um veículo vendido na aba Vender).
 *
 * @author Cezar Anthonio
 */
public class JanelaPrincipal extends JFrame {

    /** Abas da janela. */
    private final JTabbedPane abas = new JTabbedPane();

    /** Monta a janela com as cinco abas. */
    public JanelaPrincipal() {
        super("Locadora de Veículos");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        abas.addTab("Clientes", new PainelClientes());
        abas.addTab("Incluir veículo", criarAbaIncluir());
        abas.addTab("Locar", new PainelLocarVeiculo());
        abas.addTab("Devolver", new PainelDevolverVeiculo());
        abas.addTab("Vender", criarAbaVender());

        // ao trocar de aba, a tela mostrada recarrega os seus dados
        abas.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Component atual = abas.getSelectedComponent();
                if (atual instanceof Atualizavel) {
                    ((Atualizavel) atual).atualizar();
                }
            }
        });

        add(abas);
        setPreferredSize(new Dimension(900, 600));
        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Cria a aba Incluir veículo.
     *
     * @return painel da aba
     */
    private Component criarAbaIncluir() {
        return new PainelIncluirVeiculo();
    }

    /**
     * Cria a aba Vender.
     *
     * @return painel da aba
     */
    private Component criarAbaVender() {
        return new PainelVenderVeiculo();
    }

    /**
     * Inicia o sistema.
     *
     * @param args não usados
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new JanelaPrincipal().setVisible(true);
            }
        });
    }
}