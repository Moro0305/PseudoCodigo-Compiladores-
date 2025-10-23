public class MainPseudo {
    public static void main(String[] args) {
        String programa = String.join("\n",
                "inicio-programa",
                "variables: numeroDeElementos, promedio, i, elemento",
                "leer numeroDeElementos",
                "promedio = 0",
                "i = 0",
                "mientras (i < numeroDeElementos)", // <-- Usando 'mientras' del PDF
                "    leer elemento",
                "    promedio = promedio + elemento",
                "    i = i + 1",
                "fin-mientras",
                "promedio = promedio / numeroDeElementos",
                "escribir \"El promedio es\", promedio",
                "fin-programa"
        );

        // --- INICIO DE MODIFICACIONES ---
        // Adaptado de "PruebaTuplas"

        try {
            // 1. Análisis Léxico
            PseudoLexer lex = new PseudoLexer();
            lex.analizar(programa); // [cite: 625]

            System.out.println("*** Análisis léxico ***\n");
            for (Token t: lex.getTokens()) {
                System.out.println(t); // [cite: 627-630]
            }

            // 2. Análisis Sintáctico y Generación de Tuplas
            System.out.println("\n*** Análisis sintáctico y Generación ***\n");
            TablaSimbolos ts = new TablaSimbolos();
            PseudoGenerador generador = new PseudoGenerador(); // Constructor vacío, los tokens no son necesarios aquí
            PseudoParser parser = new PseudoParser(ts, generador); // [cite: 633]
            parser.analizar(lex); // [cite: 633]

            System.out.println("Análisis sintáctico y generación completados.");

            // 3. Imprimir Tabla de Símbolos
            System.out.println("\n*** Tabla de símbolos ***");
            for (Simbolo s : ts.getSimbolos().values()) {
                System.out.println(s); // [cite: 635-636]
            }

            // 4. Imprimir Tuplas Generadas
            System.out.println("\n*** Tuplas generadas ***");
            int i = 0;
            for (Tupla t : generador.getTuplas()) {
                System.out.println((i++) + ": " + t); // [cite: 638-640]
            }
            // --- FIN DE MODIFICACIONES ---

        } catch (SemanticException e) {
            System.out.println("ERROR Semántico: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}