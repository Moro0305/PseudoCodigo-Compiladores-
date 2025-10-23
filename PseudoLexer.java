import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class PseudoLexer extends Lexer {
    public static final int EOF_TYPE = -1;

    // Palabras reservadas / símbolos
    public static final int INICIO_PROGRAMA = 1;   // inicio-programa
    public static final int FIN_PROGRAMA    = 2;   // fin-programa
    public static final int VARIABLES       = 3;   // variables
    public static final int LEER            = 4;   // leer
    public static final int ESCRIBIR        = 5;   // escribir
    public static final int REPITE          = 6;   // repite
    public static final int FIN_REPITE      = 7;   // fin-repite
    public static final int SI              = 8;   // si
    public static final int ENTONCES        = 9;   // entonces
    public static final int FIN_SI          = 10;  // fin-si
    public static final int MIENTRAS        = 11;  // mientras
    public static final int FIN_MIENTRAS    = 12;  // fin-mientras

    public static final int ID              = 13;
    public static final int NUM             = 14;
    public static final int CADENA          = 15;

    public static final int COMA            = 16;  // ,
    public static final int DOSPUNTOS       = 17;  // :
    public static final int LPAREN          = 18;  // (
    public static final int RPAREN          = 19;  // )
    public static final int EQ              = 20;  // =
    public static final int MAS             = 21;  // +
    public static final int MENOS           = 22;  // -
    public static final int MULT            = 23;  // *
    public static final int DIV             = 24;  // /

    // --- INICIO: Operadores Relacionales ---
    public static final int LT              = 25;  // <
    public static final int GT              = 26;  // >
    public static final int LTE             = 27;  // <=
    public static final int GTE             = 28;  // >=
    public static final int NEQ             = 29;  // !=
    // --- FIN: Operadores Relacionales ---


    private static final Map<String,Integer> KEYWORDS = new HashMap<>();
    static {
        KEYWORDS.put("inicio-programa", INICIO_PROGRAMA);
        KEYWORDS.put("fin-programa", FIN_PROGRAMA);
        KEYWORDS.put("variables", VARIABLES);
        KEYWORDS.put("leer", LEER);
        KEYWORDS.put("escribir", ESCRIBIR);
        KEYWORDS.put("repite", REPITE);
        KEYWORDS.put("fin-repite", FIN_REPITE);
        KEYWORDS.put("si", SI);
        KEYWORDS.put("entonces", ENTONCES);
        KEYWORDS.put("fin-si", FIN_SI);
        KEYWORDS.put("mientras", MIENTRAS);
        KEYWORDS.put("fin-mientras", FIN_MIENTRAS);
    }

    // --- Código añadido para el generador de tuplas ---
    private ArrayList<Token> tokens = new ArrayList<>();

    /**
     * Constructor vacío requerido por el PseudoParser del PDF.
     */
    public PseudoLexer() {
        super(""); // Llama al super con un string vacío, no se usará
    }

    /**
     * Llena la lista interna de tokens a partir de un string de entrada.
     * @param input El código fuente completo.
     */
    public void analizar(String input) {
        // No reasignamos el campo final heredado. Creamos un lexer temporal.
        tokens.clear(); // Limpia tokens de análisis anteriores

        PseudoLexer temp = new PseudoLexer(input);
        Token t = temp.nextToken();
        while (t.type != EOF_TYPE) {
            tokens.add(t);
            t = temp.nextToken();
        }
        tokens.add(t); // Añade el token EOF al final
    }

    /**
     * Devuelve la lista de tokens generada por analizar().
     * @return ArrayList de Tokens.
     */
    public ArrayList<Token> getTokens() {
        return tokens;
    }

    /**
     * Constructor original (se mantiene por compatibilidad).
     * @param input El código fuente.
     */
    public PseudoLexer(String input) { super(input); }
    // --- Fin del código añadido ---


    @Override
    public Token nextToken() {
        while (c != EOF) {
            if (Character.isWhitespace(c)) { ws(); continue; }
            switch (c) {
                case ',': consume(); return new Token(COMA, ",");
                case ':': consume(); return new Token(DOSPUNTOS, ":");
                case '(': consume(); return new Token(LPAREN, "(");
                case ')': consume(); return new Token(RPAREN, ")");
                case '=': consume(); return new Token(EQ, "=");
                case '+': consume(); return new Token(MAS, "+");
                case '-': consume(); return new Token(MENOS, "-");
                case '*': consume(); return new Token(MULT, "*");
                case '/':
                    // comentario de línea con //
                    consume();
                    if (c == '/') {
                        while (c != '\n' && c != '\r' && c != EOF) consume();
                        continue;
                    }
                    return new Token(DIV, "/");

                // --- INICIO: Casos para Operadores Relacionales ---
                case '<':
                    // Revisa si el siguiente char es '='
                    if (p + 1 < input.length() && input.charAt(p + 1) == '=') {
                        consume(); // consume '<'
                        consume(); // consume '='
                        return new Token(LTE, "<=");
                    } else {
                        consume(); // consume '<'
                        return new Token(LT, "<");
                    }
                case '>':
                    // Revisa si el siguiente char es '='
                    if (p + 1 < input.length() && input.charAt(p + 1) == '=') {
                        consume(); // consume '>'
                        consume(); // consume '='
                        return new Token(GTE, ">=");
                    } else {
                        consume(); // consume '>'
                        return new Token(GT, ">");
                    }
                case '!':
                    // Revisa si el siguiente char es '='
                    if (p + 1 < input.length() && input.charAt(p + 1) == '=') {
                        consume(); // consume '!'
                        consume(); // consume '='
                        return new Token(NEQ, "!=");
                    } else {
                        // '!' por sí solo no es un token válido en este lenguaje
                        throw new RuntimeException("Caracter inválido: '!' debe ser seguido por '='");
                    }
                    // --- FIN: Casos para Operadores Relacionales ---

                case '"':
                    return cadena();
                default:
                    if (isLetter(c)) return palabra();
                    if (Character.isDigit(c)) return numero();
                    throw new RuntimeException("Caracter inválido: '" + c + "'");
            }
        }
        return new Token(EOF_TYPE, "<EOF>");
    }

    @Override
    public String getTokenName(int t) {
        switch (t) {
            case EOF_TYPE: return "EOF";
            case INICIO_PROGRAMA: return "inicio-programa";
            case FIN_PROGRAMA: return "fin-programa";
            case VARIABLES: return "variables";
            case LEER: return "leer";
            case ESCRIBIR: return "escribir";
            case REPITE: return "repite";
            case FIN_REPITE: return "fin-repite";
            case SI: return "si";
            case ENTONCES: return "entonces";
            case FIN_SI: return "fin-si";
            case MIENTRAS: return "mientras";
            case FIN_MIENTRAS: return "fin-mientras";
            case ID: return "ID";
            case NUM: return "NUM";
            case CADENA: return "CADENA";
            case COMA: return "COMA";
            case DOSPUNTOS: return "DOSPUNTOS";
            case LPAREN: return "LPAREN";
            case RPAREN: return "RPAREN";
            case EQ: return "EQ";
            case MAS: return "MAS";
            case MENOS: return "MENOS";
            case MULT: return "MULT";
            case DIV: return "DIV";

            // --- INICIO: Nombres de Operadores Relacionales ---
            case LT: return "LT";
            case GT: return "GT";
            case LTE: return "LTE";
            case GTE: return "GTE";
            case NEQ: return "NEQ";
            // --- FIN: Nombres de Operadores Relacionales ---

            default: return "t" + t;
        }
    }

    private Token palabra() {
        StringBuilder sb = new StringBuilder();
        while (isLetter(c) || Character.isDigit(c) || c == '_' || c == '-') {
            sb.append(c); consume();
        }
        String w = sb.toString();
        Integer kw = KEYWORDS.get(w);
        if (kw != null) return new Token(kw, w);
        // Si trae guiones pero no es palabra reservada, inválido
        if (w.indexOf('-') >= 0) throw new RuntimeException("Lexema inválido con '-': " + w);
        return new Token(ID, w);
    }

    private Token numero() {
        StringBuilder sb = new StringBuilder();
        while (Character.isDigit(c)) { sb.append(c); consume(); }
        if (c == '.') { // opcional decimal
            sb.append(c); consume();
            if (!Character.isDigit(c)) throw new RuntimeException("Número mal formado");
            while (Character.isDigit(c)) { sb.append(c); consume(); }
        }
        return new Token(NUM, sb.toString());
    }

    private Token cadena() {
        StringBuilder sb = new StringBuilder();
        consume(); // "
        while (c != '"' && c != EOF) { sb.append(c); consume(); }
        if (c != '"') throw new RuntimeException("Cadena sin cerrar");
        consume(); // "
        return new Token(CADENA, sb.toString());
    }

    private boolean isLetter(char ch) { return Character.isLetter(ch); }
}
