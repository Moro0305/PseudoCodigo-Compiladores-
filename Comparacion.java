public class Comparacion extends Tupla {
    Token valor1, valor2, operador;

    // (Constructor sin cambios)
    public Comparacion(Token valor1, Token operador, Token valor2, int sv, int sf) {
        super(sv, sf);
        this.valor1 = valor1;
        this.valor2 = valor2;
        this.operador = operador;
    }

    // (toString sin cambios)
    @Override
    public String toString() {
        return super.toString() + ", [" + valor1 + ", " + operador + ", " + valor2 + "])";
    }

    // CAMBIO: Nuevo método de ejecución
    @Override
    public int ejecutar(TablaSimbolos ts) { // [cite: 182]
        float operando1 = 0, operando2 = 0; // [cite: 186-187]

        // Obtiene el valor del operando 1
        if (valor1.type == PseudoLexer.NUM) { // [cite: 188] (Adaptado)
            operando1 = Float.parseFloat(valor1.text); // [cite: 190] (Adaptado)
        } else { // Es un ID
            operando1 = ((Variable) ts.resolver(valor1.text)).getValor(); // [cite: 191] (Adaptado)
        }

        // Obtiene el valor del operando 2
        if (valor2.type == PseudoLexer.NUM) { // [cite: 192] (Adaptado)
            operando2 = Float.parseFloat(valor2.text); // [cite: 194] (Adaptado)
        } else { // Es un ID
            operando2 = ((Variable) ts.resolver(valor2.text)).getValor(); // [cite: 195] (Adaptado)
        }

        // Realiza la comparación y devuelve el salto correspondiente
        switch (operador.text) { // [cite: 196] (Adaptado)
            case "<": // [cite: 198]
                return operando1 < operando2 ? saltoVerdadero : saltoFalso; // [cite: 198-199]
            case "<=": // [cite: 198]
                return operando1 <= operando2 ? saltoVerdadero : saltoFalso; // [cite: 198, 200]
            case ">": // [cite: 201]
                return operando1 > operando2 ? saltoVerdadero : saltoFalso; // [cite: 201-202]
            case ">=": // [cite: 203]
                return operando1 >= operando2 ? saltoVerdadero : saltoFalso; // [cite: 203-204]
            case "=": // Adaptado para '==' del PDF [cite: 205]
            case "==":
                return operando1 == operando2 ? saltoVerdadero : saltoFalso; // [cite: 205-206]
            case "!=": // Adaptado para '!=' del PDF [cite: 207]
                return operando1 != operando2 ? saltoVerdadero : saltoFalso; // [cite: 207]
        }

        return saltoFalso; // Default por si el operador no es reconocido
    }
}