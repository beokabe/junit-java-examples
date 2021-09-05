package br.ce.wcaquino.dados;

import br.ce.wcaquino.entidades.Locacao;

import java.util.List;

public interface LocacaoDAO {

    public void salvar(Locacao locacao);

    List<Locacao> obterLocacoesPendentes();
}
