import java.util.ArrayList;

public class PseudoInterprete {
    TablaSimbolos ts; // [cite: 219]

    public PseudoInterprete(TablaSimbolos ts) { // [cite: 220]
        this.ts = ts; // [cite: 222]
    }

    public void interpretar(ArrayList<Tupla> tuplas) { // [cite: 225]
        int indiceTupla = 0; // [cite: 226]
        Tupla t = tuplas.get(0); // [cite: 227]

        // Bucle de ejecución principal
        do {
            indiceTupla = t.ejecutar(ts); // [cite: 229]
            if (indiceTupla != -1) {
                t = tuplas.get(indiceTupla); // [cite: 230]
            }
        } while (indiceTupla != -1 && !(t instanceof FinPrograma)); // [cite: 232] (Ajustado para manejar el -1)
    }
}