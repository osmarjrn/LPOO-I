/*
 * GRR20250999
 * OSMAR NUNES JÚNIOR
 */
package locadora.ui;

/**
 * Implementada por todas as telas (painéis) do sistema. A janela principal
 * chama {@link #atualizar()} sempre que o usuário abre ou volta para a tela,
 * para ela recarregar os dados do repositório (por exemplo, uma tabela de
 * veículos disponíveis depois de uma venda feita em outra tela).
 *
 * @author Osmar Nunes Júnior
 */
public interface Atualizavel {

    /** Recarrega os dados mostrados pela tela. */
    void atualizar();
}
