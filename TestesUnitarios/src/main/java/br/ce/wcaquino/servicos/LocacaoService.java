package br.ce.wcaquino.servicos;

import br.ce.wcaquino.dados.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;
import buildermaster.BuilderMaster;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static br.ce.wcaquino.utils.DataUtils.adicionarDias;

public class LocacaoService {

    private LocacaoDAO locacaoDAO;
    private SPCService spcService;
    private EmailService emailService;

    public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws Exception {
        validarUsuario(usuario);
        validarSPC(usuario);
        validarFilmes(filmes);

        Locacao locacao = new Locacao();
        locacao.setValor(0.0);

        locacao.setFilme(filmes);
        locacao.setUsuario(usuario);
        locacao.setDataLocacao(Calendar.getInstance().getTime());

        calcularValorLocacao(filmes, locacao);

        //Entrega no dia seguinte
        Date dataEntrega = Calendar.getInstance().getTime();
        dataEntrega = adicionarDias(dataEntrega, 1);
        locacao.setDataRetorno(dataEntrega);

        //Salvando a locacao...
        locacaoDAO.salvar(locacao);

        return locacao;
    }

    private boolean calcularValorLocacao(List<Filme> filmes, Locacao locacao) {
        filmes.stream().distinct().forEach(filme -> locacao.somarValor(filme.getPrecoLocacao()));
        return true; //apenas para teste
    }

    private void validarUsuario(Usuario usuario) throws LocadoraException {
        if (Objects.isNull(usuario)) {
            throw new LocadoraException("Usuário inexistente.");
        }
    }

    private void validarSPC(Usuario usuario) throws LocadoraException {
        try {
            if (spcService.possuiNegativacao(usuario)) {
                throw new LocadoraException("Usuário está negativado no SPC");
            }
        } catch (Exception e) {
            throw new LocadoraException("Problemas com SPC, tente novamente.");
        }
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

    public void notificarAtrasos() {
        List<Locacao> locacoes = locacaoDAO.obterLocacoesPendentes();
        Date date = new Date();

        for (Locacao locacao : locacoes) {
            if (locacao.getDataLocacao().getYear() < date.getYear() &&
                    locacao.getDataLocacao().getDay() < date.getDay()) {

                emailService.notificarAtraso(locacao.getUsuario());
            }
        }
    }

    public void setLocacaoDAO(LocacaoDAO dao) {
        this.locacaoDAO = dao;
    }

    public void prorrogarLocacao(Locacao locacao, int dias) {
        Locacao novaLocacao = new Locacao();
        novaLocacao.setUsuario(locacao.getUsuario());
        novaLocacao.setFilme(locacao.getFilme());
        novaLocacao.setDataLocacao(new Date());
        novaLocacao.setDataRetorno(DataUtils.obterDataComDiferencaDias(dias));
        novaLocacao.setValor(locacao.getValor() * dias);
        locacaoDAO.salvar(novaLocacao);
    }

    public void setSpcService(SPCService spc) {
        this.spcService = spc;
    }

    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    public static void main(String[] args) {
        new BuilderMaster().gerarCodigoClasse(Locacao.class);
    }
}