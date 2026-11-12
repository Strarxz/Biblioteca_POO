
package modelo;

public class Professor extends Usuario {

    public Professor(int id, String nome, String matricula) {
        super(id, nome, matricula);
    }

    @Override
    public int getPrazoDias() {
        return 14;
    }

    @Override
    public float calcularMulta(int diasAtraso) {
        if(diasAtraso <= 0){
            return 0.0;
        }
        final float taxaProf = 0.5;
        return diasAtraso * taxaProf;
    }

    @Override
    public int getLimiteEmprestimos() {
        return 5;
    }
}
