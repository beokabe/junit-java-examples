package br.ce.wcaquino.servicos;

import br.ce.wcaquino.builders.FilmesBuilder;
import br.ce.wcaquino.builders.UsuarioBuilder;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class UsuarioServiceTest {

    Locacao locacao = new Locacao();

    LocacaoService locacaoService;

    @Test
    public void alugarFilmeTest() throws Exception {
        List<Filme> filmes = FilmesBuilder.filmesBuilder().getFilmes();
        Usuario usuarioBuilder = UsuarioBuilder.umUsuario().getUmUsuario();

        locacao = locacaoService.alugarFilme(usuarioBuilder, filmes);

        Assert.assertThat(locacao.getValor(), is(equalTo(90.0)));
    }
}
