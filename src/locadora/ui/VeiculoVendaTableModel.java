/*
 * GRR20251118
 * CHRISTIAN GUILHERME SOARES SANT'ANA
 */
package locadora.ui;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import locadora.modelo.Veiculo;
import locadora.modelo.VeiculoUtil;
import locadora.util.Formatador;

/**
 * Modelo da tabela de veículos disponíveis para venda (tela Vender).
 * Colunas: Placa, Marca, Modelo, Ano e Preço para venda.
 *
 * @author Christian Guilherme Soares Sant'ana
 */
public class VeiculoVendaTableModel extends AbstractTableModel {

    /** Nomes das colunas exibidas na tabela. */
    private static final String[] COLUNAS = {
        "Placa", "Marca", "Modelo", "Ano", "Preço para venda"
    };

    /** Lista de veículos exibidos na tabela. */
    private List<Veiculo> veiculos = new ArrayList<>();

    /**
     * Atualiza a lista de veículos exibidos e notifica a JTable.
     *
     * @param novosVeiculos lista de veículos disponíveis
     */
    public void setVeiculos(List<Veiculo> novosVeiculos) {
        this.veiculos = new ArrayList<>(novosVeiculos);
        fireTableDataChanged();
    }

    /**
     * Retorna o veículo correspondente à linha selecionada.
     *
     * @param linha índice da linha
     * @return veículo da linha
     */
    public Veiculo getVeiculo(int linha) {
        return veiculos.get(linha);
    }

    @Override
    public int getRowCount() {
        return veiculos.size();
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
        Veiculo v = veiculos.get(linha);
        switch (coluna) {
            case 0:
                return v.getPlaca();
            case 1:
                return v.getMarca();
            case 2:
                return VeiculoUtil.descricaoModelo(v);
            case 3:
                return Formatador.ano(v.getAno());
            case 4:
                return Formatador.moeda(v.getValorParaVenda());
            default:
                throw new IllegalArgumentException("Coluna inválida: " + coluna);
        }
    }
}