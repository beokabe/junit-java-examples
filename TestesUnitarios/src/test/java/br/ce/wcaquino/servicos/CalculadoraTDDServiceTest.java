package br.ce.wcaquino.servicos;

import br.ce.wcaquino.exceptions.CalculadoraDivisaoPorZeroException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CalculadoraTDDServiceTest {

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Test
    public void deveSomarDoisValoresTest() {
        //cenario
        int n1 = 50;
        int n2 = 50;

        Calculadora calculadora = new Calculadora();

        int resultado = calculadora.somar(n1, n2);

        //Verificacao
        Assert.assertEquals(100, resultado);
    }

    @Test
    public void deveSubtrairDoisValoresTest() {
        int n1 = 50;
        int n2 = 100;

        Calculadora calculadora = new Calculadora();

        int resultado = calculadora.subtrair(n1, n2);

        Assert.assertEquals(-50, resultado);
    }

    @Test
    public void deveDividirTest() throws CalculadoraDivisaoPorZeroException {
        int n1 = 50;
        int divisor = 2;

        Calculadora calculadora = new Calculadora();

        int resultado = calculadora.dividir(n1, divisor);

        Assert.assertEquals(25, resultado);
    }

    @Test
    public void calculadoraDeveLancarExcecaoQuandoDividePorZeroTest() throws CalculadoraDivisaoPorZeroException {
        expected.expect(CalculadoraDivisaoPorZeroException.class);

        int n1 = 50;
        int divisor = 0;

        Calculadora calculadora = new Calculadora();

        int resultado = calculadora.dividir(n1, divisor);

        Assert.assertEquals(25, resultado);
    }
}
