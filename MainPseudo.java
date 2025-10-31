// Se añaden estas 3 importaciones para leer archivos
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MainPseudo {

    public static void main(String[] args) {

        // --- INICIO DE MODIFICACIONES ---

        // 1. Define la ruta al archivo de pseudocódigo.
        //    Puedes cambiar "programa.txt" por la ruta que prefieras.
        String rutaArchivo = "Area.txt";
        String programa = "";

        try {
            // 2. Llama al nuevo método para leer el archivo
            programa = leerPrograma(rutaArchivo);

            // (Opcional) Imprime el programa que se leyó
            System.out.println("--- Contenido de " + rutaArchivo + " ---");
            System.out.println(programa);
            System.out.println("-------------------------------------\n");

            // 3. El resto de tu código se mantiene igual, pero ahora
            //    se envuelve en el 'try' principal para capturar
            //    el error de lectura de archivo.
            // --- FIN DE MODIFICACIONES ---


            // 1. Análisis Léxico
            PseudoLexer lex = new PseudoLexer();
            lex.analizar(programa);

            System.out.println("*** Análisis léxico ***\n");
            for (Token t: lex.getTokens()) {
                System.out.println(t);
            }

            // 2. Análisis Sintáctico y Generación de Tuplas
            System.out.println("\n*** Análisis sintáctico y Generación ***\n");
            TablaSimbolos ts = new TablaSimbolos();
            // Referencia a PseudoGenerador de tu archivo
            PseudoGenerador generador = new PseudoGenerador();
            // Referencia a PseudoParser de tu archivo
            PseudoParser parser = new PseudoParser(ts, generador);
            parser.analizar(lex);

            System.out.println("Análisis sintáctico y generación completados.");

            // 3. Imprimir Tabla de Símbolos
            System.out.println("\n*** Tabla de símbolos ***");
            // Referencia a TablaSimbolos de tu archivo
            for (Simbolo s : ts.getSimbolos().values()) {
                System.out.println(s);
            }

            // 4. Imprimir Tuplas Generadas
            System.out.println("\n*** Tuplas generadas ***");
            int i = 0;
            // Referencia a Tupla de tu archivo
            for (Tupla t : generador.getTuplas()) {
                System.out.println((i++) + ": " + t);
            }

            // 5. Ejecución del Intérprete
            System.out.println("\n*** Ejecucion del programa ***\n");
            // PseudoInterprete basado en Unidad 5c.pdf
            PseudoInterprete interprete = new PseudoInterprete(ts);
            interprete.interpretar(generador.getTuplas());

        }
        // --- INICIO DE MODIFICACIONES ---
        // Se añade este bloque CATCH para el error de archivo
        catch (IOException e) {
            System.out.println("ERROR de Archivo: No se pudo leer el archivo '" + rutaArchivo + "'.");
            System.out.println("Asegúrate de que el archivo exista en la raíz de tu proyecto.");
            System.out.println(e.getMessage());
        }
        // --- FIN DE MODIFICACIONES ---
        catch (SemanticException e) {
            System.out.println("ERROR Semántico: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // --- MÉTODO NUEVO ---
    /**
     * Lee un archivo de texto y devuelve su contenido como un solo String.
     * @param rutaArchivo La ruta al archivo (ej. "programa.txt")
     * @return El contenido del archivo como un String.
     * @throws IOException Si ocurre un error al leer el archivo.
     */
    private static String leerPrograma(String rutaArchivo) throws IOException {
        // Esta línea lee todos los bytes del archivo y los convierte a un String
        return new String(Files.readAllBytes(Paths.get(rutaArchivo)));
    }
}