public class MainLL2 {
    public static void main(String[] args) {
        probar("[rojo=amarillo, azul, blanco, verde]");       // válido
        probar("[rojo=, azul]");                               // inválido (falta NAME después de '=')
        probar("[=amarillo, azul]");                           // inválido (falta NAME antes de '=')
        probar("[rojo=amarillo, , verde]");                    // inválido (coma doble)
        probar("[[a,b], c=d, e]");                             // válido con lista anidada y asignación
    }

    private static void probar(String input) {
        try {
            ListLexer lexer = new ListLexer(input);
            ListParserLL2 parser = new ListParserLL2(lexer);
            parser.list();
            System.out.println("OK: " + input);
        } catch (RuntimeException ex) {
            System.out.println("ERROR: " + input);
            System.out.println("  -> " + ex.getMessage());
        }
    }
}
