public class MainPseudo {
    public static void main(String[] args) {
        String programa = String.join("\n",
                "inicio-programa",
                "variables: numeroDeElementos, promedio, i, elemento",
                "leer numeroDeElementos",
                "promedio = 0",
                "repite (i, 0, numeroDeElementos)",
                "    leer elemento",
                "    promedio = promedio + elemento",
                "fin-repite",
                "fin-programa"
        );

        try {
            PseudoLexer lex = new PseudoLexer(programa);
            PseudoParser p = new PseudoParser(lex);
            p.programa();
            System.out.println("Programa válido");

            // --- Código para demostrar la tabla de símbolos ---
            System.out.println("\n*** Tabla de símbolos ***");
            // Se obtiene la tabla de símbolos del parser
            TablaSimbolos ts = p.getTablaSimbolos();
            // Se recorren los valores de la tabla y se imprimen
            for (Simbolo s : ts.getSimbolos().values()) {
                System.out.println(s);
            }
            // --- Fin del código de demostración ---

        } catch (SemanticException e) {
            System.out.println("ERROR Semántico: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}