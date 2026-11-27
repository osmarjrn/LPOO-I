/*
 * GRR20251118
 * CHRISTIAN GUILHERME SOARES SANT'ANA
 */
package locadora.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;
import locadora.dados.Repositorio;
import locadora.modelo.Automovel;
import locadora.modelo.Categoria;
import locadora.modelo.Estado;
import locadora.modelo.Marca;
import locadora.modelo.ModeloAutomovel;
import locadora.modelo.ModeloMotocicleta;
import locadora.modelo.ModeloVan;
import locadora.modelo.Motocicleta;
import locadora.modelo.TipoVeiculo;
import locadora.modelo.Van;
import locadora.modelo.Veiculo;
import locadora.modelo.VeiculoUtil;

/**
 * Tela de cadastro (compra) de novos veículos na locadora.
 * Permite escolher o tipo de veículo (Automóvel, Van ou Motocicleta),
 * atualiza dinamicamente os modelos disponíveis e valida placa, ano e valor
 * antes de incluir no repositório.
 *
 * @author Christian Guilherme Soares Sant'ana
 */
public class PainelIncluirVeiculo extends JPanel implements Atualizavel {

    /** Repositório compartilhado em memória. */
    private final Repositorio repositorio = Repositorio.getInstance();

    /** Seletor do tipo de veículo (define qual subclasse será instanciada). */
    private final JComboBox<TipoVeiculo> comboTipo = new JComboBox<>(TipoVeiculo.values());

    /** Seletor da marca do veículo. */
    private final JComboBox<Marca> comboMarca = new JComboBox<>(Marca.values());

    /** Seletor do estado inicial (apenas NOVO e DISPONIVEL). */
    private final JComboBox<Estado> comboEstado = new JComboBox<>(
            new Estado[]{Estado.DISPONIVEL, Estado.NOVO});

    /** Seletor da categoria do veículo. */
    private final JComboBox<Categoria> comboCategoria = new JComboBox<>(Categoria.values());

    /** Seletor do modelo (recarregado conforme o tipo selecionado). */
    private final JComboBox<Object> comboModelo = new JComboBox<>();

    /** Campo formatado para o ano com 4 dígitos. */
    private JFormattedTextField campoAno;

    /** Campo formatado para o valor de compra em reais. */
    private JFormattedTextField campoValorCompra;

    /** Campo formatado para a placa no padrão UUU-####. */
    private JFormattedTextField campoPlaca;

    /** Botão para confirmar a inclusão do veículo. */
    private final JButton botaoIncluir = new JButton("Incluir");

    /** Botão para limpar os campos do formulário. */
    private final JButton botaoLimpar = new JButton("Limpar");

    /** Monta a interface gráfica da tela de inclusão de veículos. */
    public PainelIncluirVeiculo() {
        setLayout(new BorderLayout());

        inicializarCamposFormatados();

        JPanel formulario = new JPanel(new GridBagLayout());
        adicionarLinha(formulario, 0, "Tipo de Veículo:", comboTipo);
        adicionarLinha(formulario, 1, "Marca:", comboMarca);
        adicionarLinha(formulario, 2, "Estado:", comboEstado);
        adicionarLinha(formulario, 3, "Categoria:", comboCategoria);
        adicionarLinha(formulario, 4, "Modelo:", comboModelo);
        adicionarLinha(formulario, 5, "Ano:", campoAno);
        adicionarLinha(formulario, 6, "Valor de Compra (R$):", campoValorCompra);
        adicionarLinha(formulario, 7, "Placa:", campoPlaca);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelBotoes.add(botaoIncluir);
        painelBotoes.add(botaoLimpar);

        JPanel containerSuperior = new JPanel(new BorderLayout());
        containerSuperior.add(formulario, BorderLayout.CENTER);
        containerSuperior.add(painelBotoes, BorderLayout.SOUTH);

        add(containerSuperior, BorderLayout.NORTH);

        comboTipo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizarModelos();
            }
        });

        botaoIncluir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                incluirVeiculo();
            }
        });

        botaoLimpar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limparFormulario();
            }
        });

        atualizarModelos();
    }

    /** Inicializa os campos com suas respectivas máscaras de entrada. */
    private void inicializarCamposFormatados() {
        try {
            MaskFormatter mascaraPlaca = new MaskFormatter("UUU-####");
            mascaraPlaca.setPlaceholderCharacter('_');
            campoPlaca = new JFormattedTextField(mascaraPlaca);
            campoPlaca.setColumns(10);

            MaskFormatter mascaraAno = new MaskFormatter("####");
            mascaraAno.setPlaceholderCharacter('_');
            campoAno = new JFormattedTextField(mascaraAno);
            campoAno.setColumns(6);
        } catch (ParseException e) {
            campoPlaca = new JFormattedTextField();
            campoPlaca.setColumns(10);
            campoAno = new JFormattedTextField();
            campoAno.setColumns(6);
        }

        DecimalFormatSymbols simbolos = new DecimalFormatSymbols(Locale.forLanguageTag("pt-BR"));
        DecimalFormat formatoMoeda = new DecimalFormat("#,##0.00", simbolos);
        NumberFormatter formatadorValor = new NumberFormatter(formatoMoeda);
        formatadorValor.setValueClass(Double.class);
        formatadorValor.setMinimum(0.0);
        formatadorValor.setAllowsInvalid(false);

        campoValorCompra = new JFormattedTextField(new DefaultFormatterFactory(formatadorValor));
        campoValorCompra.setColumns(12);
    }

    /** Recarrega o combo de modelos de acordo com o tipo de veículo escolhido. */
    private void atualizarModelos() {
        TipoVeiculo tipo = (TipoVeiculo) comboTipo.getSelectedItem();
        comboModelo.removeAllItems();
        if (tipo != null) {
            for (Object modelo : VeiculoUtil.modelosDoTipo(tipo)) {
                comboModelo.addItem(modelo);
            }
        }
    }

    /** Valida os campos, instancia o veículo via polimorfismo e adiciona ao repositório. */
    private void incluirVeiculo() {
        TipoVeiculo tipo = (TipoVeiculo) comboTipo.getSelectedItem();
        Marca marca = (Marca) comboMarca.getSelectedItem();
        Estado estado = (Estado) comboEstado.getSelectedItem();
        Categoria categoria = (Categoria) comboCategoria.getSelectedItem();
        Object modelo = comboModelo.getSelectedItem();

        if (tipo == null || marca == null || estado == null || categoria == null || modelo == null) {
            aviso("Selecione todas as opções nas caixas de seleção.");
            return;
        }

        String textoAno = campoAno.getText().replace("_", "").trim();
        if (textoAno.length() != 4) {
            aviso("Informe o ano com 4 dígitos.");
            return;
        }
        int ano;
        try {
            ano = Integer.parseInt(textoAno);
        } catch (NumberFormatException e) {
            aviso("Ano inválido.");
            return;
        }

        try {
            campoValorCompra.commitEdit();
        } catch (ParseException e) {
            aviso("Informe um valor de compra válido.");
            return;
        }

        Object valorObj = campoValorCompra.getValue();
        if (!(valorObj instanceof Number)) {
            aviso("Informe o valor de compra do veículo.");
            return;
        }
        double valorDeCompra = ((Number) valorObj).doubleValue();
        if (valorDeCompra <= 0) {
            aviso("O valor de compra deve ser maior que zero.");
            return;
        }

        String placa = campoPlaca.getText().trim();
        if (placa.contains("_") || placa.length() != 8) {
            aviso("Informe a placa completa no formato XXX-0000.");
            return;
        }

        try {
            // Polimorfismo: a variável veiculo referencia qualquer subclasse concreta
            Veiculo veiculo;
            switch (tipo) {
                case AUTOMOVEL:
                    veiculo = new Automovel(marca, estado, null, categoria,
                            valorDeCompra, placa, ano, (ModeloAutomovel) modelo);
                    break;
                case MOTOCICLETA:
                    veiculo = new Motocicleta(marca, estado, null, categoria,
                            valorDeCompra, placa, ano, (ModeloMotocicleta) modelo);
                    break;
                case VAN:
                    veiculo = new Van(marca, estado, null, categoria,
                            valorDeCompra, placa, ano, (ModeloVan) modelo);
                    break;
                default:
                    throw new IllegalArgumentException("Tipo de veículo inválido.");
            }

            repositorio.adicionarVeiculo(veiculo);
            limparFormulario();
            JOptionPane.showMessageDialog(this, "Veículo " + veiculo.getPlaca() + " incluído com sucesso!");
        } catch (IllegalArgumentException e) {
            aviso(e.getMessage());
        }
    }

    /** Restaura o formulário para os valores iniciais. */
    private void limparFormulario() {
        comboTipo.setSelectedIndex(0);
        comboMarca.setSelectedIndex(0);
        comboEstado.setSelectedItem(Estado.DISPONIVEL);
        comboCategoria.setSelectedIndex(0);
        atualizarModelos();
        campoAno.setValue(null);
        campoAno.setText("");
        campoValorCompra.setValue(null);
        campoValorCompra.setText("");
        campoPlaca.setValue(null);
        campoPlaca.setText("");
    }

    @Override
    public void atualizar() {
        // Tela apenas de cadastro; não depende de listagens externas
    }

    /** Exibe uma caixa de diálogo de aviso ao usuário. */
    private void aviso(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Atenção", JOptionPane.WARNING_MESSAGE);
    }

    /** Adiciona um par rótulo/componente em uma linha do GridBagLayout. */
    private static void adicionarLinha(JPanel painel, int linha, String textoRotulo, JComponent campo) {
        GridBagConstraints gbcRotulo = new GridBagConstraints();
        gbcRotulo.gridx = 0;
        gbcRotulo.gridy = linha;
        gbcRotulo.anchor = GridBagConstraints.EAST;
        gbcRotulo.insets = new Insets(4, 12, 4, 6);
        painel.add(new JLabel(textoRotulo), gbcRotulo);

        GridBagConstraints gbcCampo = new GridBagConstraints();
        gbcCampo.gridx = 1;
        gbcCampo.gridy = linha;
        gbcCampo.anchor = GridBagConstraints.WEST;
        gbcCampo.insets = new Insets(4, 6, 4, 12);
        painel.add(campo, gbcCampo);
    }
}