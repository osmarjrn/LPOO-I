/*
 * GRR20252934
 * GABRIEL DE ALMEIDA
 */
package locadora.ui;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import locadora.modelo.Veiculo;
import locadora.modelo.VeiculoUtil;
import locadora.util.Formatador;

/**
 * Modelo da tabela de veículos disponíveis para locação (tela Locar).
 * Colunas: Placa, Marca, Modelo, Ano e Preço da diária.
 *
 * <p>A tabela guarda uma lista de {@link Veiculo} (qualquer tipo) e usa o
 * {@link VeiculoUtil} para mostrar o modelo, sem precisar saber se o veículo
 * é automóvel, moto ou van (polimorfismo).</p>
 *
 * @author Gabriel de Almeida
 */
public class VeiculoLocacaoTableModel extends AbstractTableModel {

    /** Títulos das colunas, na ordem em que aparecem. */
    private static final String[] COLUNAS = {"Placa", "Marca", "Modelo", "Ano", "Preço da diária"};

    /** Veículos mostrados, um por linha. */
    private List<Veiculo> veiculos = new ArrayList<>();

    /**
     * Troca a lista de veículos mostrada e avisa a tabela para se redesenhar.
     *
     * @param novos veículos que devem aparecer (uma cópia é guardada)
     */
    public void setVeiculos(List<Veiculo> novos) {
        this.veiculos = new ArrayList<>(novos);
        fireTableDataChanged();
    }

    /**
     * Retorna o veículo de uma linha.
     *
     * @param linha índice da linha no modelo
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
                return Formatador.moeda(v.getValorDiariaLocacao());
            default:
                throw new IllegalArgumentException("Coluna inválida: " + coluna);
        }
    }
}
