
package modelo;

public class Aluno extends Usuario {

    private static final double taxaAluno = 1.0;

    public Aluno(int id, String nome, String matricula) {
        super(id, nome, matricula);
    }


    @Override
    public int getPrazoDias() {
        return 7;
    }

    @Override
    public double calcularMulta(int diasAtraso) {
        if(diasAtraso <= 0){
            return 0.0;
        }
        return diasAtraso * taxaAluno;
    }

    @Override
    public int getLimiteEmprestimos() {
        return 3;
    }
}
