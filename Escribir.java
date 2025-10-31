public class Escribir extends Tupla {
    Token cadena, variable;

    // (Constructores sin cambios)
    public Escribir(Token variableCadena, int sv, int sf) {
        super(sv, sf);
        if (variableCadena.type == PseudoLexer.CADENA) {
            this.cadena = variableCadena;
        } else {
            this.variable = variableCadena;
        }
    }

    public Escribir(Token cadena, Token variable, int sv, int sf) {
        super(sv, sf);
        this.cadena = cadena;
        this.variable = variable;
    }

    // (toString sin cambios)
    @Override
    public String toString() {
        if (variable == null) {
            return super.toString() + ", [" + cadena + "])";
        }
        if (cadena == null) {
            return super.toString() + ", [" + variable + "])";
        }
        return super.toString() + ", [" + cadena + ", " + variable + "])";
    }

    // CAMBIO: Nuevo método de ejecución
    @Override
    public int ejecutar(TablaSimbolos ts) { // [cite: 94]
        if (cadena == null) {
            // Caso: escribir variable
            Variable v = (Variable) ts.resolver(variable.text); // [cite: 96] (Adaptado)
            float valor = v.getValor(); // [cite: 97]
            System.out.println(valor); // [cite: 98]
        } else if (variable == null) {
            // Caso: escribir "cadena"
            System.out.println(cadena.text); // [cite: 100] (Adaptado)
        } else {
            // Caso: escribir "cadena", variable
            Variable v = (Variable) ts.resolver(variable.text); // [cite: 103] (Adaptado)
            float valor = v.getValor(); // [cite: 103]
            System.out.println(cadena.text + valor); // [cite: 104] (Adaptado)
        }
        return saltoVerdadero; // [cite: 105]
    }
}