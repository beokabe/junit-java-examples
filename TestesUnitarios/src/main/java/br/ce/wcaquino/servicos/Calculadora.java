package br.ce.wcaquino.servicos;

import br.ce.wcaquino.exceptions.CalculadoraDivisaoPorZeroException;

import java.util.Objects;

public class Calculadora {

    public int somar(int n1, int n2) {
        return n1 + n2;
    }

    public int subtrair(int n1, int n2) {
        return n1 - n2;
    }

    public int dividir(int n1, int divisor) throws CalculadoraDivisaoPorZeroException {
        if (Objects.equals(divisor, 0)) {
            throw new CalculadoraDivisaoPorZeroException("O divisor não pode ser 0.");
        }
        return n1 / divisor;
    }
}
