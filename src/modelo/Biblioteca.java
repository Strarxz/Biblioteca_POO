package modelo;

import excecoes.LimiteEmprestimosException;
import excecoes.LivroIndisponivelException;
import excecoes.OperacaoInvalidaException;
import excecoes.RegistroNaoEncontradoException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * O "balcão" da biblioteca: guarda as listas e aplica as regras
 * que envolvem mais de uma classe ao mesmo tempo.
 */
public class Biblioteca {

    private List<Livro> livros = new ArrayList<>();
    private List<Usuario> usuarios = new ArrayList<>();
    private List<Emprestimo> emprestimos = new ArrayList<>();
    private int proximoIdEmprestimo = 1;   // gera o id de cada novo empréstimo

    // ---------- Cadastros ----------

    public void cadastrarLivro(Livro livro) {
        livros.add(livro);
    }

    public void cadastrarUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }

    // ---------- Empréstimo e devolução ----------

    /**
     * Empresta um livro para um usuário (RF1.3).
     * Confere o limite do usuário (RN1.3) e se o livro tem exemplar livre (RN1.4).
     * Se algo der errado, lança a exceção e nada é alterado.
     */
    public Emprestimo emprestar(Usuario usuario, Livro livro)
            throws LimiteEmprestimosException, LivroIndisponivelException {

        if (contarEmprestimosAbertos(usuario) >= usuario.getLimiteEmprestimos()) {
            throw new LimiteEmprestimosException(usuario.getNome()
                    + " já atingiu o limite de " + usuario.getLimiteEmprestimos() + " empréstimos.");
        }

        livro.emprestarExemplar();   // lança LivroIndisponivelException se não houver exemplar

        Emprestimo emprestimo = new Emprestimo(proximoIdEmprestimo, usuario, livro, LocalDate.now());
        proximoIdEmprestimo++;
        emprestimos.add(emprestimo);
        return emprestimo;
    }

    /**
     * Registra a devolução (RF1.4) e devolve o valor da multa para mostrar ao usuário.
     */
    public double devolver(Emprestimo emprestimo, LocalDate dataAtual) throws OperacaoInvalidaException {
        emprestimo.finalizarDevolucao(dataAtual);       // lança exceção se já foi devolvido
        emprestimo.getLivro().devolverExemplar();
        return emprestimo.getValorMulta();
    }

    // ---------- Busca (sobrecarga) ----------

    /** Busca livros cujo título contém o texto informado (RF1.5). */
    public List<Livro> buscar(String titulo) {
        List<Livro> resultado = new ArrayList<>();
        for (Livro livro : livros) {
            if (contem(livro.getTitulo(), titulo)) {
                resultado.add(livro);
            }
        }
        return resultado;
    }

    /** Busca livros pelo título e pelo autor ao mesmo tempo (RF1.5). */
    public List<Livro> buscar(String titulo, String autor) {
        List<Livro> resultado = new ArrayList<>();
        for (Livro livro : buscar(titulo)) {
            if (contem(livro.getAutor(), autor)) {
                resultado.add(livro);
            }
        }
        return resultado;
    }

    // ---------- Remoção ----------

    /**
     * Remove um livro do acervo.
     * Não deixa remover se o id não existir ou se o livro estiver emprestado (RN1.5).
     */
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

    // ---------- Consultas para listar no console ----------

    public List<Livro> getLivros() {
        return Collections.unmodifiableList(livros);   // só leitura: ninguém altera a lista por fora
    }

    public List<Usuario> getUsuarios() {
        return Collections.unmodifiableList(usuarios);
    }

    public List<Emprestimo> getEmprestimos() {
        return Collections.unmodifiableList(emprestimos);
    }

    // ---------- Métodos auxiliares (privados) ----------

    /** Conta quantos livros o usuário está segurando agora. */
    private int contarEmprestimosAbertos(Usuario usuario) {
        int total = 0;
        for (Emprestimo e : emprestimos) {
            if (e.getUsuario() == usuario && e.getStatus().equals(Emprestimo.ABERTO)) {
                total++;
            }
        }
        return total;
    }

    /** Compara textos ignorando maiúsculas e minúsculas. */
    private boolean contem(String texto, String procurado) {
        return texto.toLowerCase().contains(procurado.toLowerCase());
    }
}
