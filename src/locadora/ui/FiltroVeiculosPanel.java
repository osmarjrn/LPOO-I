/*
 * GRR20252934
 * GABRIEL DE ALMEIDA
 */
package locadora.ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import locadora.modelo.Categoria;
import locadora.modelo.Marca;
import locadora.modelo.TipoVeiculo;

/**
 * STUB PROVISÓRIO do FiltroVeiculosPanel (Parte 3, do Christian), só para a
 * tela de locar poder ser compilada e testada antes da entrega oficial.
 * Segue exatamente o contrato do roteiro. Quando a versão do Christian
 * existir, apague este arquivo e use a dele.
 *
 * <p>Três combos (Tipo, Marca e Categoria), cada um com "Todos" como
 * primeiro item. {@code aoMudar.run()} é chamado sempre que algum combo
 * mudar; os getters devolvem {@code null} quando "Todos" está
 * selecionado.</p>
 *
 * @author Gabriel de Almeida
 */
public class FiltroVeiculosPanel extends JPanel {

    /** Texto do primeiro item de cada combo. */
    private static final String TODOS = "Todos";

    /** Combo do tipo de veículo. */
    private final JComboBox<Object> comboTipo = criarCombo(TipoVeiculo.values());

    /** Combo da marca. */
    private final JComboBox<Object> comboMarca = criarCombo(Marca.values());

    /** Combo da categoria. */
    private final JComboBox<Object> comboCategoria = criarCombo(Categoria.values());

    /**
     * Cria o filtro.
     *
     * @param aoMudar ação chamada sempre que algum combo mudar
     */
    public FiltroVeiculosPanel(Runnable aoMudar) {
        super(new FlowLayout(FlowLayout.LEFT, 10, 5));
        setBorder(BorderFactory.createTitledBorder("Filtros"));
        add(new JLabel("Tipo:"));
        add(comboTipo);
        add(new JLabel("Marca:"));
        add(comboMarca);
        add(new JLabel("Categoria:"));
        add(comboCategoria);
        // o ouvinte entra depois de montar os combos, para não disparar na criação
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aoMudar.run();
            }
        };
        comboTipo.addActionListener(listener);
        comboMarca.addActionListener(listener);
        comboCategoria.addActionListener(listener);
    }

    /**
     * Retorna o tipo escolhido.
     *
     * @return tipo, ou {@code null} se estiver em "Todos"
     */
    public TipoVeiculo getTipo() {
        Object item = comboTipo.getSelectedItem();
        return (item instanceof TipoVeiculo) ? (TipoVeiculo) item : null;
    }

    /**
     * Retorna a marca escolhida.
     *
     * @return marca, ou {@code null} se estiver em "Todos"
     */
    public Marca getMarca() {
        Object item = comboMarca.getSelectedItem();
        return (item instanceof Marca) ? (Marca) item : null;
    }

    /**
     * Retorna a categoria escolhida.
     *
     * @return categoria, ou {@code null} se estiver em "Todos"
     */
    public Categoria getCategoria() {
        Object item = comboCategoria.getSelectedItem();
        return (item instanceof Categoria) ? (Categoria) item : null;
    }

    /**
     * Cria um combo com "Todos" na frente dos valores.
     *
     * @param valores valores do enum
     * @return combo preenchido, com "Todos" selecionado
     */
    private static JComboBox<Object> criarCombo(Object[] valores) {
        JComboBox<Object> combo = new JComboBox<>();
        combo.addItem(TODOS);
        for (Object valor : valores) {
            combo.addItem(valor);
        }
        return combo;
    }
}
