package br.ce.wcaquino.matchers;

import br.ce.wcaquino.utils.DataUtils;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.Date;

public class DataMatcher extends TypeSafeMatcher<Date> {

    private int dias;

    public DataMatcher(Integer dias) {
        this.dias = dias;
    }

    @Override
    protected boolean matchesSafely(Date date) { //verificação do matcher
        return DataUtils.isMesmaData(date, DataUtils.obterDataComDiferencaDias(dias));
    }

    @Override
    public void describeTo(Description description) { //adiciona uma descrição na mensagem de erro
        description.appendText("espera algum dia");
    }
}
