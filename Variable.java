public class Variable extends Simbolo {

    // CAMBIO: Añadido campo para guardar el valor en tiempo de ejecución
    private float valor = 0; //

    public Variable(String nombre, Tipo tipo) {
        super(nombre, tipo); //
    }

    // CAMBIO: Nuevo método para asignar un valor
    public void setValor(float valor) { //
        this.valor = valor; //
    }

    // CAMBIO: Nuevo método para obtener un valor
    public float getValor() { //
        return valor; //
    }
}