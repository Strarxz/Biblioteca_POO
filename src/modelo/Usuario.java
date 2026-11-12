
package modelo;

public abstract class Usuario {

    private String nome;
    private String matricula;
    private int id;

    public Usuario(int id, String nome, String matricula) {
        this.id = id;
        this.nome = nome;
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public String getMatricula() {
        return matricula;
    }

    public abstract int getPrazoDias();

    public abstract double calcularMulta(int diasAtraso);

    public abstract int getLimiteEmprestimos();
}
