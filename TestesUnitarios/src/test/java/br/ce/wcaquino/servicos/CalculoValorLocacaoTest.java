package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTest {

    @Parameterized.Parameter
    public List<Filme> filmes;

    @Parameterized.Parameter(value=1)
    public Double valorLocacao;

    @Parameterized.Parameter(value=2)
    public String nomeDosTestes;

    private LocacaoService service;

    private static Filme filme1 = new Filme("Filme 1", 1, 10.0);
    private static Filme filme2 = new Filme("Filme 2", 2, 20.0);
    private static Filme filme3 = new Filme("Filme 2", 3, 30.0);
    private static Filme filme4 = new Filme("Filme 4", 4, 40.0);
    private static Filme filme5 = new Filme("Filme 5", 5, 50.0);
    private static Filme filme6 = new Filme("Filme 6", 6, 60.0);

    @Before
    public void setup() {
        service = new LocacaoService();
    }

    //Esse método faz com que inicialize as variáveis de teste e todos os cenários da matriz sejame executados no método alugarFilmeTest
    @Parameterized.Parameters(name = "Teste {index} = {1} - {2}")
    public static Collection<Object[]> getParametros() {
        return Arrays.asList(new Object[][]{
                {Arrays.asList(filme1, filme2, filme3), 60.0, "Teste 1"},
                {Arrays.asList(filme1, filme2, filme3, filme4), 100.0, "Teste 2"},
                {Arrays.asList(filme1, filme2, filme3, filme5), 110.0, "Teste 3"},
                {Arrays.asList(filme1, filme2, filme3, filme5, filme6), 170.0, "Teste 4"},
        });
    }

    @Test
    public void alugarFilmeTest() throws Exception {
        //cenario
        Usuario usuario = new Usuario("Usuario 1");

        //acao
        Locacao resultado = service.alugarFilme(usuario, filmes);

        //verificacao
        Assert.assertThat(resultado.getValor(), is(valorLocacao));
    }
}
