/*
 * GRR20256527
 * CEZAR ANTHONIO
 */
package locadora.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import locadora.dados.Repositorio;
import locadora.modelo.Cliente;

/**
 * Tela de clientes: incluir, atualizar, excluir e listar. Clicar em uma linha
 * da tabela preenche os campos para o usuário alterar ou excluir o cliente.
 *
 * <p>Regras: todos os campos são obrigatórios; o CPF precisa ser válido e
 * único (com ou sem pontos, é o mesmo CPF); um cliente com veículo locado
 * não pode ser excluído, e a tela avisa o usuário.</p>
 *
 * @author Cezar Anthonio
 */
public class PainelClientes extends JPanel implements Atualizavel {

    private Repositorio repositorio = Repositorio.getInstance();

    private JTextField campoNome = new JTextField(20);
    private JTextField campoSobrenome = new JTextField(20);
    private JTextField campoRg = new JTextField(15);
    private JTextField campoCpf = new JTextField(15);
    private JTextField campoEndereco = new JTextField(35);

    private JButton botaoIncluir = new JButton("Incluir");
    private JButton botaoAtualizar = new JButton("Atualizar");
    private JButton botaoExcluir = new JButton("Excluir");
    private JButton botaoLimpar = new JButton("Limpar");

    private ClienteTableModel modeloTabela = new ClienteTableModel();
    private JTable tabela = new JTable(modeloTabela);

    /** Cliente escolhido na tabela; {@code null} se nenhum estiver escolhido. */
    private Cliente selecionado;

    /** Vale {@code true} enquanto a tabela é recarregada, para ignorar a seleção. */
    private boolean recarregando;

    /** Monta a tela. */
    public PainelClientes() {
        setLayout(new BorderLayout());

        // em cima: formulário e botões
        JPanel formulario = new JPanel(new GridBagLayout());
        adicionarLinha(formulario, 0, "Nome:", campoNome);
        adicionarLinha(formulario, 1, "Sobrenome:", campoSobrenome);
        adicionarLinha(formulario, 2, "RG:", campoRg);
        adicionarLinha(formulario, 3, "CPF:", campoCpf);
        adicionarLinha(formulario, 4, "Endereço:", campoEndereco);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoes.add(botaoIncluir);
        botoes.add(botaoAtualizar);
        botoes.add(botaoExcluir);
        botoes.add(botaoLimpar);

        JPanel norte = new JPanel(new BorderLayout());
        norte.add(formulario, BorderLayout.CENTER);
        norte.add(botoes, BorderLayout.SOUTH);
        add(norte, BorderLayout.NORTH);

        // no meio: tabela de clientes
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        // eventos
        tabela.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && !recarregando) {
                    aoSelecionarLinha();
                }
            }
        });
        botaoIncluir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                incluir();
            }
        });
        botaoAtualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizarCliente();
            }
        });
        botaoExcluir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                excluir();
            }
        });
        botaoLimpar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpar();
            }
        });

        atualizar();
    }

    /** Recarrega a tabela; a janela principal chama ao abrir a aba. */
    @Override
    public void atualizar() {
        Cliente manter = selecionado;
        recarregando = true;
        modeloTabela.setClientes(repositorio.listarClientes());
        int linha = (manter == null) ? -1 : modeloTabela.indexOf(manter);
        if (linha >= 0) {
            tabela.setRowSelectionInterval(linha, linha); // mantém o cliente escolhido
        } else {
            tabela.clearSelection();
        }
        recarregando = false;
        if (manter != null && linha < 0) {
            limpar(); // o cliente escolhido não existe mais
        }
    }

    /** Preenche os campos com o cliente da linha escolhida. */
    private void aoSelecionarLinha() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            selecionado = null;
            return;
        }
        selecionado = modeloTabela.getCliente(linha);
        campoNome.setText(selecionado.getNome());
        campoSobrenome.setText(selecionado.getSobrenome());
        campoRg.setText(selecionado.getRg());
        campoCpf.setText(selecionado.getCpf());
        campoEndereco.setText(selecionado.getEndereco());
    }

    /** Valida os campos e cadastra um novo cliente. */
    private void incluir() {
        if (!camposValidos()) {
            return;
        }
        Cliente cliente = new Cliente(campoNome.getText().trim(), campoSobrenome.getText().trim(),
                campoRg.getText().trim(), formatarCpf(campoCpf.getText()),
                campoEndereco.getText().trim());
        if (!repositorio.adicionarCliente(cliente)) {
            aviso("Já existe um cliente cadastrado com este CPF.");
            return;
        }
        limpar();
        atualizar();
        JOptionPane.showMessageDialog(this, "Cliente incluído.");
    }

    /** Valida os campos e altera os dados do cliente escolhido na tabela. */
    private void atualizarCliente() {
        if (selecionado == null) {
            aviso("Selecione um cliente na tabela para atualizar.");
            return;
        }
        if (!camposValidos()) {
            return;
        }
        // o cliente escolhido é ignorado, para o CPF dele não conflitar consigo mesmo
        if (repositorio.cpfEmUso(campoCpf.getText(), selecionado)) {
            aviso("Outro cliente já usa este CPF.");
            return;
        }
        selecionado.setNome(campoNome.getText().trim());
        selecionado.setSobrenome(campoSobrenome.getText().trim());
        selecionado.setRg(campoRg.getText().trim());
        selecionado.setCpf(formatarCpf(campoCpf.getText()));
        selecionado.setEndereco(campoEndereco.getText().trim());
        atualizar();
        JOptionPane.showMessageDialog(this, "Cliente atualizado.");
    }

    /**
     * Exclui o cliente escolhido. Cliente com veículo locado não pode ser
     * excluído: a tela mostra uma mensagem e não exclui.
     */
    private void excluir() {
        if (selecionado == null) {
            aviso("Selecione um cliente na tabela para excluir.");
            return;
        }
        if (repositorio.clientePossuiVeiculoLocado(selecionado)) {
            aviso("O cliente " + selecionado.getNomeCompleto()
                    + " não pode ser excluído porque possui veículo locado.");
            return;
        }
        int resposta = JOptionPane.showConfirmDialog(this,
                "Excluir o cliente " + selecionado.getNomeCompleto() + "?",
                "Confirmar exclusão", JOptionPane.YES_NO_OPTION);
        if (resposta != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            repositorio.removerCliente(selecionado);
        } catch (IllegalStateException | IllegalArgumentException e) {
            aviso(e.getMessage()); // segunda barreira: o repositório também recusa
            return;
        }
        limpar();
        atualizar();
    }

    /** Esvazia os campos e tira a escolha da tabela. */
    private void limpar() {
        selecionado = null;
        campoNome.setText("");
        campoSobrenome.setText("");
        campoRg.setText("");
        campoCpf.setText("");
        campoEndereco.setText("");
        tabela.clearSelection();
    }

    /**
     * Confere se todos os campos estão preenchidos e se o CPF é válido,
     * mostrando uma mensagem quando algo está errado.
     *
     * @return {@code true} se os dados podem ser usados
     */
    private boolean camposValidos() {
        if (campoNome.getText().trim().isEmpty() || campoSobrenome.getText().trim().isEmpty()
                || campoRg.getText().trim().isEmpty() || campoCpf.getText().trim().isEmpty()
                || campoEndereco.getText().trim().isEmpty()) {
            aviso("Preencha todos os campos.");
            return false;
        }
        if (!cpfValido(campoCpf.getText())) {
            aviso("CPF inválido. Informe os 11 dígitos de um CPF válido.");
            return false;
        }
        return true;
    }

    /**
     * Confere um CPF: 11 dígitos (pontos e traço são aceitos), não todos
     * iguais, e com os dois dígitos verificadores corretos.
     *
     * @param cpf texto digitado
     * @return {@code true} se o CPF é válido
     */
    public static boolean cpfValido(String cpf) {
        String d = (cpf == null) ? "" : cpf.replaceAll("\\D", "");
        if (d.length() != 11 || d.matches("(\\d)\\1{10}")) {
            return false;
        }
        return d.charAt(9) - '0' == digitoVerificador(d, 9)
                && d.charAt(10) - '0' == digitoVerificador(d, 10);
    }

    /**
     * Calcula um dígito verificador do CPF.
     *
     * @param d       os dígitos do CPF
     * @param tamanho quantos dígitos entram na conta (9 para o primeiro
     *                verificador, 10 para o segundo)
     * @return dígito verificador (0 a 9)
     */
    private static int digitoVerificador(String d, int tamanho) {
        int soma = 0;
        for (int i = 0; i < tamanho; i++) {
            soma += (d.charAt(i) - '0') * (tamanho + 1 - i);
        }
        int resto = (soma * 10) % 11;
        return resto == 10 ? 0 : resto;
    }

    /**
     * Escreve um CPF válido no formato 000.000.000-00.
     *
     * @param cpf CPF com ou sem pontuação
     * @return CPF formatado
     */
    private static String formatarCpf(String cpf) {
        String d = cpf.replaceAll("\\D", "");
        return d.substring(0, 3) + "." + d.substring(3, 6) + "." + d.substring(6, 9)
                + "-" + d.substring(9);
    }

    /** Mostra uma mensagem de aviso. */
    private void aviso(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Atenção", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Coloca um rótulo e um campo em uma linha do formulário.
     *
     * @param painel painel do formulário
     * @param linha  número da linha
     * @param texto  texto do rótulo
     * @param campo  campo de texto
     */
    private static void adicionarLinha(JPanel painel, int linha, String texto, JTextField campo) {
        GridBagConstraints rotulo = new GridBagConstraints();
        rotulo.gridx = 0;
        rotulo.gridy = linha;
        rotulo.anchor = GridBagConstraints.EAST;
        rotulo.insets = new Insets(4, 12, 4, 6);
        painel.add(new JLabel(texto), rotulo);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = linha;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(4, 6, 4, 6);
        painel.add(campo, c);
    }
}
