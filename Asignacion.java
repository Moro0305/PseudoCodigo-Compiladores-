public class Asignacion extends Tupla {
    Token variable, valor1, valor2, operador;

    // (Constructores sin cambios)
    public Asignacion(Token variable, Token valor, int sv, int sf) {
        super(sv, sf);
        this.variable = variable;
        this.valor1 = valor;
    }
    public Asignacion(Token variable, Token valor1, Token operador, Token valor2, int sv, int sf) {
        super(sv, sf);
        this.variable = variable;
        this.valor1 = valor1;
        this.valor2 = valor2;
        this.operador = operador;
    }

    // (toString sin cambios)
    @Override
    public String toString() {
        if (operador == null) {
            return super.toString() + ", [\"" + variable + "\", " + valor1 + "\"])";
        } else {
            return super.toString() + ", [" + variable + ", " + valor1 + ", " + operador + ", " + valor2 + "])";
        }
    }

    // CAMBIO: Nuevo método de ejecución
    @Override
    public int ejecutar(TablaSimbolos ts) { // [cite: 133]
        // Resuelve la variable donde se guardará el resultado
        Variable v = (Variable) ts.resolver(variable.text); // [cite: 134] (Adaptado)
        float operando1 = 0, operando2 = 0; // [cite: 134]

        // Obtiene el valor del operando 1
        if (valor1.type == PseudoLexer.NUM) { // [cite: 135] (Adaptado)
            operando1 = Float.parseFloat(valor1.text); // [cite: 137] (Adaptado)
        } else { // Es un ID
            operando1 = ((Variable) ts.resolver(valor1.text)).getValor(); // [cite: 138] (Adaptado)
        }

        // Si hay un segundo valor, obtiene el operando 2
        if (valor2 != null) { // [cite: 139]
            if (valor2.type == PseudoLexer.NUM) { // [cite: 140] (Adaptado)
                operando2 = Float.parseFloat(valor2.text); // [cite: 142] (Adaptado)
            } else { // Es un ID
                operando2 = ((Variable) ts.resolver(valor2.text)).getValor(); // [cite: 143] (Adaptado)
            }
        }

        // Realiza la operación de asignación
        if (operador == null) {
            // Asignación simple: var = valor1
            v.setValor(operando1); // [cite: 144, 146] (Corregido del PDF)
        } else {
            // Asignación compleja: var = valor1 op valor2
            switch (operador.text) { // [cite: 147] (Adaptado)
                case "+": // [cite: 148]
                    v.setValor(operando1 + operando2); // [cite: 149]
                    break;
                case "-": // [cite: 151]
                    v.setValor(operando1 - operando2); // [cite: 152-153]
                    break;
                case "*": // [cite: 155]
                    v.setValor(operando1 * operando2); // [cite: 156-157]
                    break;
                case "/": // [cite: 159]
                    if (operando2 != 0) { // [cite: 160]
                        v.setValor(operando1 / operando2); // [cite: 161]
                    } else {
                        System.out.println("Error: División entre cero"); // [cite: 163]
                        System.exit(1); // [cite: 164]
                    }
                    break;
            }
        }
        return saltoVerdadero; // [cite: 168]
    }
}