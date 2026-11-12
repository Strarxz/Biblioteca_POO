package modelo;

import java.math.BigDecimal;

public class Aluno extends Usuario {

    public Aluno(int id, String nome, String matricula) {
        super(id, nome, matricula);
    }

    @Override
    public int getPrazoDias() {
        return 7;
    }

    @Override
    public int getLimiteEmprestimos() {
        return 3;
    }

    @Override
    public BigDecimal getTaxaMultaDiaria() {
        return new BigDecimal("1.00");
    }
}
