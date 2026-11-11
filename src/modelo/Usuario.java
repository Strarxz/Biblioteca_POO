
package modelo;

public abstract class Usuario {

    private String nome;
    private String matricula;
    private int id;

    public Usuario(String nome, String matricula, int id) {
        this.nome = nome;
        this.matricula = matricula;
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public String getMatricula() {
        return matricula;
    }

    public abstract int getPrazoDias();

    public abstract float calcularMulta(int diasAtraso);

    public abstract int getLimiteEmprestimos();
}
