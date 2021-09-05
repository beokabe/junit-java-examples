package br.ce.wcaquino.builders;

import br.ce.wcaquino.entidades.Filme;

public class FilmeBuilder {

    private Filme filme;

    private FilmeBuilder() {
    }

    public static FilmeBuilder filmeBuilder() {
        FilmeBuilder filmeBuilder = new FilmeBuilder();
        filmeBuilder.filme = new Filme("Filme 1", 1, 1.0);

        return filmeBuilder;
    }

    public Filme agora() {
        return filme;
    }
}
