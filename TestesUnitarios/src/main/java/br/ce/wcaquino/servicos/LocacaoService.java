package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import buildermaster.BuilderMaster;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static br.ce.wcaquino.utils.DataUtils.adicionarDias;

public class LocacaoService {

    public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws Exception {
        validarUsuario(usuario);
        validarFilmes(filmes);

        Locacao locacao = new Locacao();
        locacao.setValor(0.0);

        locacao.setFilme(filmes);
        locacao.setUsuario(usuario);
        locacao.setDataLocacao(new Date());

        filmes.stream().distinct().forEach(filme -> locacao.somarValor(filme.getPrecoLocacao()));

        //Entrega no dia seguinte
        Date dataEntrega = new Date();
        dataEntrega = adicionarDias(dataEntrega, 1);
        locacao.setDataRetorno(dataEntrega);

        //Salvando a locacao...
        //TODO adicionar método para salvar

        return locacao;
    }

    private void validarFilmes(List<Filme> filmes) throws FilmeSemEstoqueException, LocadoraException {
        if (Objects.isNull(filmes)) {
            throw new LocadoraException("Filme inexistente.");
        } else if (algumFilmeNaoTemEstoque(filmes))
            throw new FilmeSemEstoqueException("Filme sem estoque.");
    }

    private boolean algumFilmeNaoTemEstoque(List<Filme> filmes) {
        List<Filme> filmesSemEstoque = filmes
                .stream()
                .filter(filme -> filme.getEstoque() == 0)
                .collect(Collectors.toList());

        return filmesSemEstoque.size() > 0;
    }

    private void validarUsuario(Usuario usuario) throws LocadoraException {
        if (Objects.isNull(usuario)) {
            throw new LocadoraException("Usuário inexistente.");
        }
    }

    public static void main(String[] args) {
        new BuilderMaster().gerarCodigoClasse(Locacao.class);
    }
}