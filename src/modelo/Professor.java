package modelo;

import java.math.BigDecimal;

public class Professor extends Usuario {

    public Professor(int id, String nome, String matricula) {
        super(id, nome, matricula);
    }

    @Override
    public int getPrazoDias() {
        return 15;
    }

    @Override
    public int getLimiteEmprestimos() {
        return 5;
    }

    @Override
    public BigDecimal getTaxaMultaDiaria() {
        return new BigDecimal("0.50");
    }
}
