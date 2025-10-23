import java.util.ArrayList;

public class PseudoGenerador {
    private ArrayList<Tupla> tuplas = new ArrayList<>();
    // A diferencia del PDF, no necesitamos los tokens aquí
    // si el Parser le pasa los tokens correctos.

    public PseudoGenerador() {
        // Constructor vacío
    }

    public ArrayList<Tupla> getTuplas() {
        return tuplas;
    }

    // --- Métodos de Creación de Tuplas ---

    public void crearTuplaAsignacion(Token variable, Token valor) {
        tuplas.add(new Asignacion(variable, valor, tuplas.size() + 1, tuplas.size() + 1));
        // [cite: 441, 444] adaptado
    }

    public void crearTuplaAsignacion(Token variable, Token valor1, Token op, Token valor2) {
        tuplas.add(new Asignacion(variable, valor1, op, valor2, tuplas.size() + 1, tuplas.size() + 1));
        // [cite: 447-451] adaptado
    }

    public void crearTuplaLeer(Token variable) {
        tuplas.add(new Leer(variable, tuplas.size() + 1, tuplas.size() + 1));
        // [cite: 453, 455] adaptado
    }

    // Para 'escribir var' o 'escribir "cadena"'
    public void crearTuplaEscribir(Token variableCadena) {
        tuplas.add(new Escribir(variableCadena, tuplas.size() + 1, tuplas.size() + 1));
        // [cite: 459, 461] adaptado
    }

    // Para 'escribir "cadena", var'
    public void crearTuplaEscribir(Token cadena, Token variable) {
        tuplas.add(new Escribir(cadena, variable, tuplas.size() + 1, tuplas.size() + 1));
        // [cite: 463, 466] adaptado
    }

    public void crearTuplaComparacion(Token valor1, Token op, Token valor2) {
        // Los saltos se dejan pendientes para el backpatching
        tuplas.add(new Comparacion(valor1, op, valor2, tuplas.size() + 1, -1)); // Salto falso pendiente
        // [cite: 470-473] adaptado
    }

    public void crearTuplaFinPrograma() {
        tuplas.add(new FinPrograma());
        // [cite: 476]
    }

    // --- Métodos de Conexión (Backpatching) ---

    public int getIndiceSiguienteTupla() {
        return tuplas.size();
    }

    // Conecta el 'saltoFalso' de una tupla 'si' al final del bloque
    public void conectarSi(int indiceTuplaComparacion) {
        if (indiceTuplaComparacion >= tuplas.size()) return;
        tuplas.get(indiceTuplaComparacion).setSaltoFalso(tuplas.size()); // Salta al final del bloque 'si'
        // [cite: 478-482] adaptado
    }

    // Conecta una estructura 'mientras'
    public void conectarMientras(int indiceTuplaComparacion) {
        int indiceTuplaFinal = tuplas.size() - 1; // La última tupla *dentro* del bucle
        if (indiceTuplaComparacion >= tuplas.size() || indiceTuplaComparacion > indiceTuplaFinal) return;

        // El 'saltoFalso' de la comparación sale del bucle (a la sig. tupla)
        tuplas.get(indiceTuplaComparacion).setSaltoFalso(tuplas.size()); // [cite: 487]

        // La última tupla del bucle debe saltar de vuelta a la comparación
        tuplas.get(indiceTuplaFinal).setSaltoVerdadero(indiceTuplaComparacion); // [cite: 487]
        tuplas.get(indiceTuplaFinal).setSaltoFalso(indiceTuplaComparacion); // [cite: 487]

        // (El bucle for del PDF [cite: 488] es para lógicas más complejas como 'break', no se necesita aquí)
    }
}