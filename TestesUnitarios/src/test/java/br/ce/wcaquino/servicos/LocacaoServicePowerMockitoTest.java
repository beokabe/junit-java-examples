package br.ce.wcaquino.servicos;

import br.ce.wcaquino.builders.FilmeBuilder;
import br.ce.wcaquino.dados.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.matchers.MatchersProprios;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import static org.hamcrest.CoreMatchers.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
// Powermock não está funcionando por conta do JDK. Quando se utiliza Java 1.8, provavelmente ele funcionará.
//@RunWith(PowerMockRunner.class)
//@PrepareForTest(LocacaoService.class)
public class LocacaoServicePowerMockitoTest {

    @InjectMocks
    LocacaoService locacaoService;

    @Mock
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
    public void run() {

    }

    //    @Test
//    public void alugarFilmeTest() throws Exception
//      Serve para retornar uma data fixa quando a classe Date for criada sem inicialização do objeto
//        PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(29, 4, 2017));
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.DAY_OF_MONTH, 29);
//        calendar.set(Calendar.MONTH, 2);
//        calendar.set(Calendar.YEAR, 2018);

//        PowerMockito.mockStatic(Calendar.class)
//        PowerMockito.when(Calendar.getInstance()).thenReturn(calendar)
//        init(10, usuario, false);
//
//        Assert.assertEquals(1050, locacao.getValor(), 0.0);
//        Assert.assertTrue(isMesmaData(locacao.getDataLocacao(), new Date()));
//        Assert.assertTrue(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)));
//
//        Assert.assertThat(locacao.getValor(), is(equalTo(1050.0)));
//        Assert.assertThat(locacao.getValor(), is(not(25.0)));
//        Assert.assertThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
//        Assert.assertThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
//    }

//    @Test
//    public void deveCalcularValorLocacao() throws Exception {
//        List<Filme> filmes = Arrays.asList(FilmeBuilder.filmeBuilder().agora());
//
//    Class<LocacaoService> clazz = LocacaoService.class;
//    Method method = clazz.getDeclaredMethod("calcularValorLocacao", List.class, Locacao.class);
//    method.setAcessible(true);
//    method.invoke(service, filmes, locacao);
//        Boolean valor = (Boolean) Whitebox.invokeMethod(locacaoService, "calcularValorLocacao", filmes, locacao);
//
//        Assert.assertThat(valor, is(true));
//    }
}
