/*
 * GRR20251118
 * CHRISTIAN GUILHERME SOARES SANT'ANA
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
 * Painel de filtros reutilizado nas telas de locação e venda de veículos.
 * Contém três caixas de seleção (Tipo, Marca e Categoria), cada uma com
 * "Todos" como primeira opção.
 *
 * @author Christian Guilherme Soares Sant'ana
 */
public class FiltroVeiculosPanel extends JPanel {

    /** Texto padrão para indicar ausência de filtro. */
    private static final String TODOS = "Todos";

    /** Combo de seleção do tipo de veículo. */
    private final JComboBox<Object> comboTipo;

    /** Combo de seleção da marca do veículo. */
    private final JComboBox<Object> comboMarca;

    /** Combo de seleção da categoria do veículo. */
    private final JComboBox<Object> comboCategoria;

    /**
     * Cria o painel de filtros e registra a ação a ser executada sempre que
     * qualquer opção for alterada.
     *
     * @param aoMudar ação executada quando algum combo muda de valor
     */
    public FiltroVeiculosPanel(final Runnable aoMudar) {
        super(new FlowLayout(FlowLayout.LEFT, 10, 5));
        setBorder(BorderFactory.createTitledBorder("Filtros"));

        comboTipo = criarComboComTodos(TipoVeiculo.values());
        comboMarca = criarComboComTodos(Marca.values());
        comboCategoria = criarComboComTodos(Categoria.values());

        add(new JLabel("Tipo:"));
        add(comboTipo);
        add(new JLabel("Marca:"));
        add(comboMarca);
        add(new JLabel("Categoria:"));
        add(comboCategoria);

        ActionListener ouvinte = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (aoMudar != null) {
                    aoMudar.run();
                }
            }
        };

        comboTipo.addActionListener(ouvinte);
        comboMarca.addActionListener(ouvinte);
        comboCategoria.addActionListener(ouvinte);
    }

    /**
     * Retorna o tipo de veículo selecionado.
     *
     * @return tipo selecionado, ou {@code null} se "Todos" estiver marcado
     */
    public TipoVeiculo getTipo() {
        Object selecionado = comboTipo.getSelectedItem();
        return (selecionado instanceof TipoVeiculo) ? (TipoVeiculo) selecionado : null;
    }

    /**
     * Retorna a marca selecionada.
     *
     * @return marca selecionada, ou {@code null} se "Todos" estiver marcado
     */
    public Marca getMarca() {
        Object selecionado = comboMarca.getSelectedItem();
        return (selecionado instanceof Marca) ? (Marca) selecionado : null;
    }

    /**
     * Retorna a categoria selecionada.
     *
     * @return categoria selecionada, ou {@code null} se "Todos" estiver marcado
     */
    public Categoria getCategoria() {
        Object selecionado = comboCategoria.getSelectedItem();
        return (selecionado instanceof Categoria) ? (Categoria) selecionado : null;
    }

    /**
     * Monta um JComboBox contendo "Todos" na primeira posição seguido dos
     * valores informados.
     *
     * @param itens valores do enum a adicionar
     * @return combo preenchido
     */
    private static JComboBox<Object> criarComboComTodos(Object[] itens) {
        JComboBox<Object> combo = new JComboBox<>();
        combo.addItem(TODOS);
        for (Object item : itens) {
            combo.addItem(item);
        }
        return combo;
    }
}