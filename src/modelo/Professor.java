
package modelo;

public class Professor extends Usuario {

    private static final taxaProf = 0.5;

    public Professor(int id, String nome, String matricula) {
        super(id, nome, matricula);
    }

    @Override
    public int getPrazoDias() {
        return 15;
    }

    @Override
    public double calcularMulta(int diasAtraso) {
        if(diasAtraso <= 0){
            return 0.0;
        }
        return diasAtraso * taxaProf;
    }

    @Override
    public int getLimiteEmprestimos() {
        return 5;
    }
}
