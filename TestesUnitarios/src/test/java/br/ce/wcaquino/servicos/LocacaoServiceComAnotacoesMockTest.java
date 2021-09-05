package br.ce.wcaquino.servicos;

import br.ce.wcaquino.builders.FilmeBuilder;
import br.ce.wcaquino.builders.LocacaoBuilder;
import br.ce.wcaquino.builders.UsuarioBuilder;
import br.ce.wcaquino.dados.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.matchers.MatchersProprios;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.when;

public class LocacaoServiceComAnotacoesMockTest {

    @InjectMocks //injeta os mocks na classe
    LocacaoService locacaoService;

    @Mock //mock da classe locacaoService
    private LocacaoDAO locacaoDAO;

    @Mock
    private SPCService spcService;

    @Mock
    private EmailService email;

    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    @Rule
    public ExpectedException expected = ExpectedException.none();

    Usuario usuario = new Usuario("Beatriz");
    List<Filme> filmes;
    Locacao locacao = new Locacao();

    @Before
    public void setup() {
        //Injeta os mocks em Locação Service
        MockitoAnnotations.initMocks(this);
    }

    private void init(int estoque, Usuario usuario, boolean filmesIsNull) throws Exception {
        Filme filmeAnabelle3 = new Filme("Anabelle 3", estoque, 50.0);
        Filme filmeEdnaldoPereira = new Filme("Você não vale nada", estoque, 1000.0);

        if (!filmesIsNull) {
            filmes = new ArrayList<>();
            filmes.add(filmeAnabelle3);
            filmes.add(filmeEdnaldoPereira);
        }

        locacao = locacaoService.alugarFilme(usuario, filmes);

        Assert.assertThat(filmeAnabelle3.getNome(), MatchersProprios.nomeEhIgual("anabelle 3"));
    }

    @Test
    public void alugarFilmeTest() throws Exception {
        init(10, usuario, false);

        Assert.assertEquals(1050, locacao.getValor(), 0.0);
        Assert.assertTrue(isMesmaData(locacao.getDataLocacao(), new Date()));
        Assert.assertTrue(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)));

        Assert.assertThat(locacao.getValor(), is(equalTo(1050.0)));
        Assert.assertThat(locacao.getValor(), is(not(25.0)));
        Assert.assertThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
        Assert.assertThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
    }

    @Ignore
    public void alugarFilmeComErrorCollectorTest() throws Exception {
        init(10, usuario, false);

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

    @Test
    public void alugarFilmeSemEstoqueComTratamentoDeErroExpectedExceptionTest() throws Exception {
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

    @Test
    public void deveTratarErroSPCTest() throws Exception {
        //Cenário
        Usuario usuario = UsuarioBuilder.umUsuario().getUmUsuario();
        List<Filme> filmes = Arrays.asList(FilmeBuilder.filmeBuilder().agora());

        when(spcService.possuiNegativacao(usuario)).thenThrow(new Exception("Falha catastrófica"));

        //Verificação
        expected.expect(LocadoraException.class);
        expected.expectMessage("Problemas com SPC, tente novamente.");

        //Ação
        locacaoService.alugarFilme(usuario, filmes);
    }

    @Test
    public void deveProrrogarUmaLocacao() {
        Locacao locacao = LocacaoBuilder.umLocacao().agora();

        locacaoService.prorrogarLocacao(locacao, 3);

        ArgumentCaptor<Locacao> argumentCaptor = ArgumentCaptor.forClass(Locacao.class);
        Mockito.verify(locacaoDAO).salvar(argumentCaptor.capture());
        Locacao locacaoRetornada = argumentCaptor.getValue();

        errorCollector.checkThat(locacaoRetornada.getValor(), is(3.0));
    }
}
