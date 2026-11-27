/*
 * GRR20256527
 * CEZAR ANTHONIO
 */
package locadora.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
 * <p>Esta versão só depende das Partes 1, 2 e 4. As abas "Incluir veículo" e
 * "Vender" (Parte 3, do Christian) usam painéis provisórios; quando as telas
 * existirem, troque {@code criarAbaIncluir()} e {@code criarAbaVender()} por
 * {@code return new PainelIncluirVeiculo();} e
 * {@code return new PainelVenderVeiculo();}.</p>
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
     * Cria a aba Incluir veículo. <b>Parte 3:</b> troque por
     * {@code return new PainelIncluirVeiculo();} quando a tela existir.
     *
     * @return painel da aba
     */
    private Component criarAbaIncluir() {
        return new PainelProvisorio("Tela de incluir veículo (Parte 3) ainda não integrada.");
    }

    /**
     * Cria a aba Vender. <b>Parte 3:</b> troque por
     * {@code return new PainelVenderVeiculo();} quando a tela existir.
     *
     * @return painel da aba
     */
    private Component criarAbaVender() {
        return new PainelProvisorio("Tela de vender veículo (Parte 3) ainda não integrada.");
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

    /** Painel temporário que ocupa o lugar de uma tela que ainda não existe. */
    private static class PainelProvisorio extends JPanel implements Atualizavel {

        /**
         * Cria o painel provisório.
         *
         * @param texto mensagem mostrada no centro
         */
        PainelProvisorio(String texto) {
            super(new GridBagLayout());
            add(new JLabel(texto));
        }

        @Override
        public void atualizar() {
            // nada a recarregar
        }
    }
}
