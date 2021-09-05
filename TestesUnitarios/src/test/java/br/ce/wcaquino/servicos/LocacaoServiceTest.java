package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.matchers.MatchersProprios;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.*;

public class LocacaoServiceTest {

    Usuario usuario = new Usuario("Beatriz");
    List<Filme> filmes;
    Locacao locacao = new Locacao();
    LocacaoService locacaoService;

    int contador = 0;
    private static int contadorEstatico = 0;  //Esse contador será incrementado no Before ou After, pois ele é estático

    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    @Rule
    public ExpectedException expected = ExpectedException.none();

    private void init(int estoque, Usuario usuario, boolean filmesIsNull) throws Exception {
        Filme filmeAnabelle3 = new Filme("Anabelle 3", estoque, 50.0);
        Filme filmeEdnaldoPereira = new Filme("Você não vale nada", estoque, 1000.0);

        if (!filmesIsNull) {
            filmes = new ArrayList<>();
            filmes.add(filmeAnabelle3);
            filmes.add(filmeEdnaldoPereira);
        }

        locacaoService = new LocacaoService();
        locacao = locacaoService.alugarFilme(usuario, filmes);

//        Assert.assertThat(filmeAnabelle3.getNome(), new NomeFilmeMatcher("anabelle 3")); -> não recomendado
        Assert.assertThat(filmeAnabelle3.getNome(), MatchersProprios.nomeEhIgual("anabelle 3"));
    }

    //Quando trabalho com Before, o JUnit reinicializa todas as variáveis, então esse contador++ só será incrementado uma vez
    @Before
    public void setup() {
        contador++;
        contadorEstatico++;
    }

    @After
    public void tearDown() {
        System.out.println("Contador estático " + contador);
        System.out.println("Contador global " + contadorEstatico);
    }


    @Test
    public void alugarFilmeTest() throws Exception {
        init(10, usuario, false);

        Assert.assertEquals(1050, locacao.getValor(), 0.0);
        Assert.assertTrue(isMesmaData(locacao.getDataLocacao(), new Date()));
        Assert.assertTrue(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)));

        //Método assertThat tem uma leitura diferente do assertEquals,
        // mas tem a mesma lógica que assertEquals ou assertNotEquals:
        // "Verifique que .... é igual a ...." ou "Verifique que .... não é igual a ..... ".
        Assert.assertThat(locacao.getValor(), is(equalTo(1050.0)));
        Assert.assertThat(locacao.getValor(), is(not(25.0)));
        Assert.assertThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
        Assert.assertThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
    }

    @Ignore
    public void alugarFilmeComErrorCollectorTest() throws Exception {
        init(10, usuario, false);

        //Mapeia todas as falhas (exceções esperadas no teste) num teste e as aponta, facilitando a identificação de falha no primeiro teste
        errorCollector.checkThat(locacao.getValor(), is(equalTo(1050.0)));
        errorCollector.checkThat(locacao.getValor(), is(not(20.0)));
        errorCollector.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
        errorCollector.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
        errorCollector.checkThat(locacao.getDataRetorno(), MatchersProprios.ehHojeComDiferencaDias(1));
    }

    @Test(expected = FilmeSemEstoqueException.class)
    public void alugarFilmeSemEstoqueComTratamentoDeErroEleganteTest() throws Exception {
        init(0, usuario, false);

        Assert.assertEquals(1050.0, locacao.getValor(), 0.0);
        Assert.assertTrue(isMesmaData(locacao.getDataLocacao(), new Date()));
        Assert.assertTrue(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)));
    }

    @Test
    public void alugarFilmeSemEstoqueComTratamentoDeErroComControleTest() {
        try {
            init(0, usuario, false);
        } catch (Exception e) {
            Assert.assertThat(e.getMessage(), is("Filme sem estoque."));
        }
    }

    @Ignore //Esse teste deve falhar, por isso foi ignorado.
    public void alugarFilmeSemEstoqueQueEsperaQueUmErroOcorraMasEleNaoOcorreTest() {
        try {
            init(10, usuario, false);
            Assert.fail("Espera-se que o teste retorne uma exceção.");
        } catch (Exception e) {
            Assert.assertThat(e.getMessage(), is("Filme sem estoque."));
        }
    }

    @Test
    public void alugarFilmeSemEstoqueComTratamentoDeErroExpectedExceptionTest() throws Exception {
        //ExpectedException deve ser declarada antes da Exception ser estourada
        expected.expect(FilmeSemEstoqueException.class);
        expected.expectMessage("Filme sem estoque.");

        init(0, usuario, false);
    }

    @Test
    public void alugarFilmeComUsuarioNuloTestException() throws Exception {
        try {
            init(1, null, false);
            Assert.fail();
        } catch (LocadoraException locadoraException) {
            Assert.assertThat(locadoraException.getMessage(), is("Usuário inexistente."));
        }
    }

    @Test
    public void alugarFilmeComFilmeNuloTestException() throws Exception {
        try {
            init(1, usuario, true);
        } catch (LocadoraException locadoraException) {
            Assert.assertThat(locadoraException.getMessage(), is("Filme inexistente."));
        }
    }

    @Test
    public void alugarFilmeComFilmeNuloExpectedExceptionTest() throws Exception {
        expected.expect(LocadoraException.class);
        expected.expectMessage("Filme inexistente.");

        init(1, usuario, true);
    }
}
