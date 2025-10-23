public class Asignacion extends Tupla {
    Token variable, valor1, valor2, operador;

    // Constructor para var = valor [cite: 374-378]
    public Asignacion(Token variable, Token valor, int sv, int sf) {
        super(sv, sf);
        this.variable = variable;
        this.valor1 = valor;
    }

    // Constructor para var = valor1 op valor2 [cite: 379-385]
    public Asignacion(Token variable, Token valor1, Token operador, Token valor2, int sv, int sf) {
        super(sv, sf);
        this.variable = variable;
        this.valor1 = valor1;
        this.valor2 = valor2;
        this.operador = operador;
    }

    @Override
    public String toString() {
        if (operador == null) {
            return super.toString() + ", [\"" + variable + "\", " + valor1 + "\"])"; // [cite: 388-390]
        } else {
            return super.toString() + ", [" + variable + ", " + valor1 + ", " + operador + ", " + valor2 + "])"; // [cite: 397-404]
        }
    }
}