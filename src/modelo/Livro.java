package modelo;

import excecoes.LivroIndisponivelException;

public class Livro {

<<<<<<< HEAD
    private final int id;
    private final String titulo;
    private final String autor;
    private final String isbn;
    private final int quantidadeTotalExemplares;
    private int quantidadeDeExemplares;

    public Livro(int id, String titulo, String autor, String isbn, int quantidadeDeExemplares) {
        if (id <= 0) {
            throw new IllegalArgumentException("O id do livro deve ser maior que zero.");
        }
=======
    private int id;
    private String titulo;
    private String autor;
    private String isbn;
    private int quantidadeExemplares;  

    public Livro(int id, String titulo, String autor, String isbn, int quantidadeExemplares) {
>>>>>>> 31e2851fe71a330764d4461022902cd7d8393d48
        this.id = id;
        this.titulo = validarTexto(titulo, "título");
        this.autor = validarTexto(autor, "autor");
        this.isbn = validarTexto(isbn, "ISBN");
        if (quantidadeDeExemplares < 0) {
            throw new IllegalArgumentException("A quantidade de exemplares não pode ser negativa.");
        }
        this.quantidadeTotalExemplares = quantidadeDeExemplares;
        this.quantidadeDeExemplares = quantidadeDeExemplares;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getQuantidadeTotalExemplares() {
        return quantidadeTotalExemplares;
    }

    public int getQuantidadeDeExemplares() {
        return quantidadeDeExemplares;
    }

<<<<<<< HEAD
    public void emprestarExemplar() {
        if (quantidadeDeExemplares <= 0) {
            throw new EstoqueInsuficienteException("Não há exemplares disponíveis para empréstimo.");
        }
        quantidadeDeExemplares--;
=======
    
    public void emprestarExemplar() throws LivroIndisponivelException {
        if (quantidadeExemplares == 0) {
            throw new LivroIndisponivelException("O livro \"" + titulo + "\" não tem exemplar disponível.");
        }
        quantidadeExemplares--;
>>>>>>> 31e2851fe71a330764d4461022902cd7d8393d48
    }

    
    public void devolverExemplar() {
        if (quantidadeDeExemplares >= quantidadeTotalExemplares) {
            throw new RegraNegocioException(
                    "A devolução excederia a quantidade total de exemplares cadastrada.");
        }
        quantidadeDeExemplares++;
    }

    private static String validarTexto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("O " + campo + " do livro é obrigatório.");
        }
        return valor.trim();
    }

    

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getQuantidadeExemplares() {
        return quantidadeExemplares;
    }


    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    @Override
    public String toString() {
        return "[" + id + "] " + titulo + " - " + autor + " (" + quantidadeExemplares + " disponível(is))";
    }
}
