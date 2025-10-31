public class FinPrograma extends Tupla {
    public FinPrograma() {
        super(-1, -1);
    }

    @Override
    public String toString() {
        return "("+ super.toString() + ", [])";
    }

    // CAMBIO: Nuevo método de ejecución
    @Override
    public int ejecutar(TablaSimbolos ts) { // [cite: 216]
        return -1; // [cite: 217]
    }
}