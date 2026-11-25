package locadora.ui;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

public class JanelaTemporaria {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame janela = new JFrame("Teste das telas");
            JTabbedPane abas = new JTabbedPane();
            abas.addTab("Locar", new PainelLocarVeiculo());
            abas.addTab("Devolver", new PainelDevolverVeiculo());
            abas.addChangeListener(e ->
                ((Atualizavel) abas.getSelectedComponent()).atualizar());
            janela.add(abas);
            janela.setSize(1000, 620);
            janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            janela.setVisible(true);
        });
    }
}