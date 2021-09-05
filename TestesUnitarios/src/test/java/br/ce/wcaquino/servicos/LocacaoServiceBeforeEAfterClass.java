package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.*;

public class LocacaoServiceBeforeEAfterClass {

    Usuario usuario = new Usuario("Beatriz");
    List<Filme> filmes = new ArrayList<>();
    Locacao locacao;
    LocacaoService locacaoService;

    private void init() throws Exception {
        Filme filmeAnabelle3 = new Filme("Anabelle 3", 10, 50.0);
        Filme filmeEdnaldoPereira = new Filme("Você não vale nada", 10, 1000.0);

        if (Objects.nonNull(filmes)) {
            filmes = new ArrayList<>();
            filmes.add(filmeAnabelle3);
            filmes.add(filmeEdnaldoPereira);
        }

        locacaoService = new LocacaoService();
        locacao = locacaoService.alugarFilme(usuario, filmes);
    }

    //BeforeClass e AfterClass só são executados depois que a classe é instaciada, por isso deve-se declará-los como estáticos
    //BeforeClass é o primeiro a ser executado - só executa uma vez
    //AfterClass é o último a ser executado - só executa uma vez
    @BeforeClass
    public static void setupClass() {
        System.out.println("Before Class");
    }

    @AfterClass
    public static void tearDownClass() {
        System.out.println("After Class");
    }

    @Test
    public void alugarFilmeTest() throws Exception {
        init();

        Assert.assertEquals(1050.0, locacao.getValor(), 0.0);
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
}
