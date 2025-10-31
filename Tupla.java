import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public abstract class Tupla {
    protected int saltoVerdadero, saltoFalso;

    public Tupla(int sv, int sf) {
        this.saltoVerdadero = sv;
        this.saltoFalso = sf;
    }

    public void setSaltoVerdadero(int sv) {
        saltoVerdadero = sv;
    }

    public int getSaltoVerdadero() {
        return saltoVerdadero;
    }

    public void setSaltoFalso(int sf) {
        saltoFalso = sf;
    }

    public int getSaltoFalso() {
        return saltoFalso;
    }

    @Override
    public String toString() {
        // Se mantiene tu 'toString()'
        return "(" + this.getClass().getName() + ", " + saltoVerdadero + ", " + saltoFalso;
    }

    // CAMBIO: Nuevo método abstracto para la ejecución
    // Devuelve el índice de la siguiente tupla
    public abstract int ejecutar(TablaSimbolos ts); // [cite: 46]
}