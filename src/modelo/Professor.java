package modelo;

import java.math.BigDecimal;

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
<<<<<<< HEAD
    public int getLimiteEmprestimos() {
        return 5;
    }

    @Override
    public BigDecimal getTaxaMultaDiaria() {
        return new BigDecimal("0.50");
=======
    public double calcularMulta(int diasAtraso) {
        if(diasAtraso <= 0){
            return 0.0;
        }
        return diasAtraso * taxaProf;
    }

    @Override
    public int getLimiteEmprestimos() {
        return 5;
>>>>>>> 31e2851fe71a330764d4461022902cd7d8393d48
    }
}
