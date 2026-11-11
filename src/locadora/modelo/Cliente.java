/*
 * GRR20250999
 * OSMAR NUNES JÚNIOR
 */
package locadora.modelo;

/**
 * Cliente da locadora.
 *
 * @author Osmar Nunes Júnior
 */
public class Cliente {

    /** Nome do cliente. */
    private String nome;

    /** Sobrenome do cliente. */
    private String sobrenome;

    /** RG do cliente. */
    private String rg;

    /** CPF do cliente. */
    private String cpf;

    /** Endereço do cliente. */
    private String endereco;

    /**
     * Cria um cliente.
     *
     * @param nome      nome
     * @param sobrenome sobrenome
     * @param rg        RG
     * @param cpf       CPF
     * @param endereco  endereço
     */
    public Cliente(String nome, String sobrenome, String rg, String cpf, String endereco) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.rg = rg;
        this.cpf = cpf;
        this.endereco = endereco;
    }

    /**
     * Retorna o nome e o sobrenome juntos.
     *
     * @return nome completo
     */
    public String getNomeCompleto() {
        return nome + " " + sobrenome;
    }

    /**
     * Retorna o nome.
     *
     * @return nome do cliente
     */
    public String getNome() {
        return nome;
    }

    /**
     * Altera o nome.
     *
     * @param nome novo nome
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Retorna o sobrenome.
     *
     * @return sobrenome do cliente
     */
    public String getSobrenome() {
        return sobrenome;
    }

    /**
     * Altera o sobrenome.
     *
     * @param sobrenome novo sobrenome
     */
    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    /**
     * Retorna o RG.
     *
     * @return RG do cliente
     */
    public String getRg() {
        return rg;
    }

    /**
     * Altera o RG.
     *
     * @param rg novo RG
     */
    public void setRg(String rg) {
        this.rg = rg;
    }

    /**
     * Retorna o CPF.
     *
     * @return CPF do cliente
     */
    public String getCpf() {
        return cpf;
    }

    /**
     * Altera o CPF.
     *
     * @param cpf novo CPF
     */
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    /**
     * Retorna o endereço.
     *
     * @return endereço do cliente
     */
    public String getEndereco() {
        return endereco;
    }

    /**
     * Altera o endereço.
     *
     * @param endereco novo endereço
     */
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    /**
     * Texto usado nas caixas de seleção e listas: "Nome Sobrenome (CPF)".
     *
     * @return descrição do cliente
     */
    @Override
    public String toString() {
        return getNomeCompleto() + " (" + cpf + ")";
    }
}
