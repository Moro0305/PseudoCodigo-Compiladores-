// CAMBIO: Añadidas importaciones para leer de consola
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Leer extends Tupla {
    Token variable;

    public Leer(Token variable, int sv, int sf) {
        super(sv, sf);
        this.variable = variable;
    }

    @Override
    public String toString() {
        return super.toString() + ", [" + variable + "])"; // [cite: 55]
    }

    // CAMBIO: Nuevo método de ejecución
    @Override
    public int ejecutar(TablaSimbolos ts) { // [cite: 57]
        String valor = "0.0"; // [cite: 58]
        try {
            // Pide el valor al usuario
            System.out.print("Da un valor para " + variable.text + ": "); //  (Adaptado de .getNombre() a .text)
            BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in)); //
            valor = entrada.readLine(); // [cite: 62]

            // Resuelve la variable en la tabla de símbolos
            Variable v = (Variable) ts.resolver(variable.text); // [cite: 64] (Adaptado)

            // Asigna el valor
            v.setValor(Float.parseFloat(valor)); // [cite: 66]
        } catch (IOException ex) { // [cite: 63]
            System.out.println("Error de entrada/salida.");
        } catch (NumberFormatException exception) { // [cite: 66-67]
            System.out.println("Error: Número inválido"); // [cite: 68]
        }

        // Devuelve el índice de la siguiente tupla
        return saltoVerdadero; // [cite: 69]
    }
}