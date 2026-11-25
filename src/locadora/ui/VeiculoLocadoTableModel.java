/*
 * GRR20252934
 * GABRIEL DE ALMEIDA
 */
package locadora.ui;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import locadora.modelo.Locacao;
import locadora.modelo.Veiculo;
import locadora.modelo.VeiculoUtil;
import locadora.util.Formatador;

/**
 * Modelo da tabela de veículos locados (tela Devolver). Colunas: Nome do
 * Cliente, Placa, Marca, Modelo, Ano, Data Locação, Preço Diária,
 * Quantidade de dias locado e Valor Locação.
 *
 * <p>Os veículos da lista devem estar LOCADOS, ou seja, ter uma
 * {@link Locacao}; é isso que o {@code Repositorio.listarLocados()}
 * devolve.</p>
 *
 * @author Gabriel de Almeida
 */
public class VeiculoLocadoTableModel extends AbstractTableModel {

    /** Títulos das colunas, na ordem em que aparecem. */
    private static final String[] COLUNAS = {
        "Nome do Cliente", "Placa", "Marca", "Modelo", "Ano",
        "Data Locação", "Preço Diária", "Quantidade de dias locado", "Valor Locação"
    };

    /** Veículos locados mostrados, um por linha. */
    private List<Veiculo> veiculos = new ArrayList<>();

    /**
     * Troca a lista de veículos mostrada e avisa a tabela para se redesenhar.
     *
     * @param novos veículos locados que devem aparecer (uma cópia é guardada)
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
        Locacao locacao = v.getLocacao();
        switch (coluna) {
            case 0:
                return locacao.getCliente().getNomeCompleto();
            case 1:
                return v.getPlaca();
            case 2:
                return v.getMarca();
            case 3:
                return VeiculoUtil.descricaoModelo(v);
            case 4:
                return Formatador.ano(v.getAno());
            case 5:
                return Formatador.data(locacao.getData());
            case 6:
                return Formatador.moeda(v.getValorDiariaLocacao());
            case 7:
                return String.valueOf(locacao.getDias());
            case 8:
                return Formatador.moeda(locacao.getValor());
            default:
                throw new IllegalArgumentException("Coluna inválida: " + coluna);
        }
    }
}
