package modelo;

/**
 * Classe abstrata que representa qualquer pessoa que pode pegar livros emprestados.
 * Não dá para criar um "Usuario" direto: só um Aluno ou um Professor.
 */
public abstract class Usuario {

    private int prazoDias;     // quantos dias o usuário pode ficar com o livro
    private int id;
    private String nome;
    private String matricula;

    /**
     * Construtor chamado pelas filhas (Aluno e Professor) através do super(...).
     * Cada filha informa o seu próprio prazo.
     */
    public Usuario(int id, String nome, String matricula, int prazoDias) {
        this.id = id;
        this.nome = nome;
        this.matricula = matricula;
        this.prazoDias = prazoDias;
    }

    // ---------- Métodos abstratos: cada filha responde do seu jeito ----------

    /** Quantos livros o usuário pode ter emprestados ao mesmo tempo (RN1.3). */
    public abstract int getLimiteEmprestimos();

    /** Quanto o usuário paga por dia de atraso (RN1.2). */
    public abstract double getTaxaMultaDiaria();

    // ---------- Getters ----------

    public int getPrazoDias() {
        return prazoDias;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getMatricula() {
        return matricula;
    }

    // ---------- Setters (usados na edição do cadastro, RF1.2) ----------

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    @Override
    public String toString() {
        return "[" + id + "] " + nome + " (matrícula " + matricula + ")";
    }
}
