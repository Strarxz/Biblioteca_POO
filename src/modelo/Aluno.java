package modelo;

import java.math.BigDecimal;

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
<<<<<<< HEAD
    public int getLimiteEmprestimos() {
        return 3;
    }

    @Override
    public BigDecimal getTaxaMultaDiaria() {
        return new BigDecimal("1.00");
=======
    public double calcularMulta(int diasAtraso) {
        if(diasAtraso <= 0){
            return 0.0;
        }
        return diasAtraso * taxaAluno;
    }

    @Override
    public int getLimiteEmprestimos() {
        return 3;
>>>>>>> 31e2851fe71a330764d4461022902cd7d8393d48
    }
}
