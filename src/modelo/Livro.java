package modelo;

import excecoes.LivroIndisponivelException;

public class Livro {

    private int id;
    private String titulo;
    private String autor;
    private String isbn;
    private int quantidadeExemplares;  

    public Livro(int id, String titulo, String autor, String isbn, int quantidadeExemplares) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.quantidadeExemplares = quantidadeExemplares;
    }

    
    public void emprestarExemplar() throws LivroIndisponivelException {
        if (quantidadeExemplares == 0) {
            throw new LivroIndisponivelException("O livro \"" + titulo + "\" não tem exemplar disponível.");
        }
        quantidadeExemplares--;
    }

    
    public void devolverExemplar() {
        quantidadeExemplares++;
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
