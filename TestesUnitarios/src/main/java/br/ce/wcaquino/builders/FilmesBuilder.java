package br.ce.wcaquino.builders;

import br.ce.wcaquino.entidades.Filme;

import java.util.List;

public class FilmesBuilder {

    private List<Filme> filmes;

    private FilmesBuilder() {
    }

    public static FilmesBuilder filmesBuilder() {
        FilmesBuilder builder = new FilmesBuilder();
        builder.filmes.add(new Filme("Filme 1", 10, 20.0));
        builder.filmes.add(new Filme("Filme 2", 10, 30.0));
        builder.filmes.add(new Filme("Filme 3", 10, 40.0));
        return builder;
    }

    public FilmesBuilder semEstoqueNoPrimeiroIndex() {
        filmes.get(0).setEstoque(0);
        return this;
    }

    public List<Filme> getFilmes() {
        return filmes;
    }
}
