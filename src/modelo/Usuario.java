package modelo;

public abstract class Usuario {

    private int prazoDias;
    private int id;
    private String nome;
    private String matricula;

    public Usuario(int id, String nome, String matricula, int prazoDias) {
        this.id = id;
        this.nome = nome;
        this.matricula = matricula;
        this.prazoDias = prazoDias;
    }

    public abstract int getLimiteEmprestimos();

    public abstract double getTaxaMultaDiaria();

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
