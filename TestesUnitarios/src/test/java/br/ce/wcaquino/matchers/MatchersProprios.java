package br.ce.wcaquino.matchers;

public class MatchersProprios {

    public static NomeFilmeMatcher nomeEhIgual(String nome) {
        return new NomeFilmeMatcher(nome);
    }

    public static DataMatcher ehHojeComDiferencaDias(int dias) {
        return new DataMatcher(dias);
    }
}
