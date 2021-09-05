package br.ce.wcaquino.suites;

import br.ce.wcaquino.servicos.CalculadoraTDDServiceTest;
import br.ce.wcaquino.servicos.CalculoValorLocacaoTest;
import br.ce.wcaquino.servicos.LocacaoServiceBeforeEAfterClass;
import br.ce.wcaquino.servicos.LocacaoServiceTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

//Suite de classes executa todos os métodos de uma vez só. Porém a suite acaba duplicando a bateria de teste quando todos os testes são executados.

@Ignore
@RunWith(Suite.class)
@Suite.SuiteClasses({
        CalculadoraTDDServiceTest.class,
        CalculoValorLocacaoTest.class,
        LocacaoServiceBeforeEAfterClass.class,
        LocacaoServiceTest.class
})
public class SuiteExecucao {

    @BeforeClass
    public static void before() {
        System.out.println("Configuração inicial.");
    }

    @AfterClass
    public static void after() {
        System.out.println("Configuração final.");
    }
}
