package modelo;

import excecoes.LimiteEmprestimosException;
import excecoes.LivroIndisponivelException;
import excecoes.OperacaoInvalidaException;
import excecoes.RegistroNaoEncontradoException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Biblioteca {

    private List<Livro> livros = new ArrayList<>();
    private List<Usuario> usuarios = new ArrayList<>();
    private List<Emprestimo> emprestimos = new ArrayList<>();
    private int proximoIdEmprestimo = 1;

    public void cadastrarLivro(Livro livro) {
        livros.add(livro);
    }

    public void cadastrarUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }

    public Emprestimo emprestar(Usuario usuario, Livro livro)
            throws LimiteEmprestimosException, LivroIndisponivelException {

        if (contarEmprestimosAbertos(usuario) >= usuario.getLimiteEmprestimos()) {
            throw new LimiteEmprestimosException(usuario.getNome()
                    + " já atingiu o limite de " + usuario.getLimiteEmprestimos() + " empréstimos.");
        }

        livro.emprestarExemplar();

        Emprestimo emprestimo = new Emprestimo(proximoIdEmprestimo, usuario, livro, LocalDate.now());
        proximoIdEmprestimo++;
        emprestimos.add(emprestimo);
        return emprestimo;
    }

    public double devolver(Emprestimo emprestimo, LocalDate dataAtual) throws OperacaoInvalidaException {
        emprestimo.finalizarDevolucao(dataAtual);
        emprestimo.getLivro().devolverExemplar();
        return emprestimo.getValorMulta();
    }

    public List<Livro> buscar(String titulo) {
        List<Livro> resultado = new ArrayList<>();
        for (Livro livro : livros) {
            if (contem(livro.getTitulo(), titulo)) {
                resultado.add(livro);
            }
        }
        return resultado;
    }

    public List<Livro> buscar(String titulo, String autor) {
        List<Livro> resultado = new ArrayList<>();
        for (Livro livro : buscar(titulo)) {
            if (contem(livro.getAutor(), autor)) {
                resultado.add(livro);
            }
        }
        return resultado;
    }

    public void removerLivro(int id) throws RegistroNaoEncontradoException, OperacaoInvalidaException {
        Livro livro = null;
        for (Livro l : livros) {
            if (l.getId() == id) {
                livro = l;
            }
        }
        if (livro == null) {
            throw new RegistroNaoEncontradoException("Não existe livro com o id " + id + ".");
        }

        for (Emprestimo e : emprestimos) {
            if (e.getLivro() == livro && e.getStatus().equals(Emprestimo.ABERTO)) {
                throw new OperacaoInvalidaException("O livro \"" + livro.getTitulo()
                        + "\" está emprestado e não pode ser removido.");
            }
        }
        livros.remove(livro);
    }

    public List<Livro> getLivros() {
        return Collections.unmodifiableList(livros);
    }

    public List<Usuario> getUsuarios() {
        return Collections.unmodifiableList(usuarios);
    }

    public List<Emprestimo> getEmprestimos() {
        return Collections.unmodifiableList(emprestimos);
    }

    private int contarEmprestimosAbertos(Usuario usuario) {
        int total = 0;
        for (Emprestimo e : emprestimos) {
            if (e.getUsuario() == usuario && e.getStatus().equals(Emprestimo.ABERTO)) {
                total++;
            }
        }
        return total;
    }

    private boolean contem(String texto, String procurado) {
        return texto.toLowerCase().contains(procurado.toLowerCase());
    }
}
