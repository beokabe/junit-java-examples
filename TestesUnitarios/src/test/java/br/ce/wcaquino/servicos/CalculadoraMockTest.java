package br.ce.wcaquino.servicos;

import br.ce.wcaquino.exceptions.CalculadoraDivisaoPorZeroException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

public class CalculadoraMockTest {

    @Mock
    private Calculadora calcMock;

    @Spy
    private Calculadora calcSpy;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void devoMostrarDiferencaEntreMockSpy() throws CalculadoraDivisaoPorZeroException {
        Mockito.when(calcMock.somar(1, 2)).thenReturn(8);
        // Mockito.when(calcSpy.somar(1, 2)).thenReturn(8);

        //Ordem de execução dessa linha é diferente, mas o objetivo é o mesmo. Isso pode evitar erros nos testes.
        Mockito.doReturn(8).when(calcSpy).somar(1, 2);

        System.out.println("Mock: " + calcMock.somar(1, 5));

        //Quando não tem um comportamento pré-definido para o spy, ele executa o método real, por isso ele apenas funciona com classes concretas.
        System.out.println("Spy:  " + calcSpy.somar(1, 5));

        //Obs: o spy executa métodos voids enquanto o mock não.

        //Quando não quiser que o mockito não faça nada, utilize a seguinte sintaxe
        Mockito.doNothing().when(calcSpy).dividir(1, 1);

        Mockito.when(calcMock.somar(2, 2)).thenCallRealMethod();
        System.out.println("Mock chama método real se as condições são satisfatórias: " + calcMock.somar(2, 2));
    }

    @Test
    public void calculadoraMockTest() {
        Calculadora mock = Mockito.mock(Calculadora.class);

        Mockito.when(mock.somar(1, 2)).thenReturn(5);

        Assert.assertEquals(mock.somar(1, 2), 5);

        Mockito.when(mock.somar(Matchers.anyInt(), Matchers.anyInt())).thenReturn(10);

        Assert.assertEquals(mock.somar(50, 50), 10);

        //Mockito.eq fixa um valor para o paramêtro
        Mockito.when(mock.somar(Mockito.eq(1), Matchers.anyInt())).thenReturn(100);

        Assert.assertEquals(mock.somar(50, 50), 10);
        Assert.assertEquals(mock.somar(1, 50), 100);
    }
}
