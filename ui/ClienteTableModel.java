/*
 * GRR20256527
 * CEZAR ANTHONIO
 */
package locadora.ui;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import locadora.modelo.Cliente;

/**
 * Modelo da tabela de clientes. Colunas: Nome, Sobrenome, RG, CPF e Endereço.
 *
 * <p>Estende {@link AbstractTableModel} (e não DefaultTableModel, como pede o
 * enunciado): implementa {@code getRowCount}, {@code getColumnCount},
 * {@code getColumnName} e {@code getValueAt}, e avisa a tabela com
 * {@code fireTableDataChanged()} quando a lista muda.</p>
 *
 * @author Cezar Anthonio
 */
public class ClienteTableModel extends AbstractTableModel {

    /** Títulos das colunas, na ordem em que aparecem. */
    private static final String[] COLUNAS = {"Nome", "Sobrenome", "RG", "CPF", "Endereço"};

    /** Clientes mostrados, um por linha. */
    private List<Cliente> clientes = new ArrayList<>();

    /**
     * Troca a lista de clientes mostrada e avisa a tabela para se redesenhar.
     *
     * @param novos clientes que devem aparecer (uma cópia é guardada)
     */
    public void setClientes(List<Cliente> novos) {
        this.clientes = new ArrayList<>(novos);
        fireTableDataChanged();
    }

    /**
     * Retorna o cliente de uma linha.
     *
     * @param linha índice da linha no modelo
     * @return cliente da linha
     */
    public Cliente getCliente(int linha) {
        return clientes.get(linha);
    }

    /**
     * Procura a linha de um cliente.
     *
     * @param cliente cliente procurado
     * @return índice da linha, ou -1 se o cliente não estiver na tabela
     */
    public int indexOf(Cliente cliente) {
        return clientes.indexOf(cliente);
    }

    @Override
    public int getRowCount() {
        return clientes.size();
    }

    @Override
    public int getColumnCount() {
        return COLUNAS.length;
    }

    @Override
    public String getColumnName(int coluna) {
        return COLUNAS[coluna];
    }

    @Override
    public Object getValueAt(int linha, int coluna) {
        Cliente c = clientes.get(linha);
        switch (coluna) {
            case 0:
                return c.getNome();
            case 1:
                return c.getSobrenome();
            case 2:
                return c.getRg();
            case 3:
                return c.getCpf();
            case 4:
                return c.getEndereco();
            default:
                throw new IllegalArgumentException("Coluna inválida: " + coluna);
        }
    }
}
