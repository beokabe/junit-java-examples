package br.ce.wcaquino.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class NomeFilmeMatcher extends TypeSafeMatcher<String> {

    private String nomeFilme;

    public NomeFilmeMatcher(String nomeFilme) {
        this.nomeFilme = nomeFilme;
    }

    @Override
    protected boolean matchesSafely(String nomeFilme) { //verificação do matcher
        return this.nomeFilme.equalsIgnoreCase(nomeFilme);
    }

    @Override
    public void describeTo(Description description) { //adiciona uma descrição na mensagem de erro
        description.appendText("espera alguma coisa");
    }
}
