public class Escribir extends Tupla {
    Token cadena, variable;

    // Constructor para 'escribir variable' o 'escribir "cadena"' [cite: 333]
    public Escribir(Token variableCadena, int sv, int sf) {
        super(sv, sf);
        // Asume que las clases están en el mismo paquete
        if (variableCadena.type == PseudoLexer.CADENA) {
            this.cadena = variableCadena;
        } else {
            this.variable = variableCadena;
        }
    }

    // Constructor para 'escribir "cadena", variable' [cite: 341]
    public Escribir(Token cadena, Token variable, int sv, int sf) {
        super(sv, sf);
        this.cadena = cadena;
        this.variable = variable;
    }

    @Override
    public String toString() {
        if (variable == null) {
            return super.toString() + ", [" + cadena + "])"; // [cite: 350, 354]
        }
        if (cadena == null) {
            return super.toString() + ", [" + variable + "])"; // [cite: 353, 355]
        }
        return super.toString() + ", [" + cadena + ", " + variable + "])"; // [cite: 359]
    }
}