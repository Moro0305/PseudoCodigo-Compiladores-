import java.util.HashSet;
import java.util.Set;

public class PseudoParser extends ParserK {
    // Se reemplaza el HashSet por una instancia de TablaSimbolos
    private final TablaSimbolos ts;
    // Se crea una instancia del tipo de dato 'real'
    private final TipoIncorporado real;

    public PseudoParser(Lexer input) throws SemanticException {
        super(input, 2);
        this.ts = new TablaSimbolos();
        this.real = new TipoIncorporado("real");
        // Se registra el tipo de dato 'real' en la tabla de símbolos al inicio
        ts.definir(real);
    }

    // programa : 'inicio-programa' declVars? enunciados 'fin-programa'
    public void programa() throws SemanticException {
        match(PseudoLexer.INICIO_PROGRAMA);
        if (LA(1) == PseudoLexer.VARIABLES) declVars();
        enunciados();
        match(PseudoLexer.FIN_PROGRAMA);
    }

    // declVars : 'variables' ':' ID (',' ID)*
    private void declVars() throws SemanticException {
        match(PseudoLexer.VARIABLES);
        match(PseudoLexer.DOSPUNTOS);
        String id = LT(1).text;
        // Se define una nueva variable en la tabla de símbolos
        ts.definir(new Variable(id, real));
        match(PseudoLexer.ID);

        while (LA(1) == PseudoLexer.COMA) {
            match(PseudoLexer.COMA);
            id = LT(1).text;
            // Se definen las variables adicionales
            ts.definir(new Variable(id, real));
            match(PseudoLexer.ID);
        }
    }

    // enunciados : (enunciado)*
    private void enunciados() throws SemanticException {
        while (iniciaEnunciado(LA(1))) {
            enunciado();
        }
    }

    private boolean iniciaEnunciado(int la1) {
        return la1 == PseudoLexer.ID ||
                la1 == PseudoLexer.LEER ||
                la1 == PseudoLexer.ESCRIBIR ||
                la1 == PseudoLexer.REPITE;
    }

    // enunciado : asignacion | leer | escribir | repite
    private void enunciado() throws SemanticException {
        if (LA(1) == PseudoLexer.ID) {
            asignacion();
        } else if (LA(1) == PseudoLexer.LEER) {
            leer();
        } else if (LA(1) == PseudoLexer.ESCRIBIR) {
            escribir();
        } else if (LA(1) == PseudoLexer.REPITE) {
            repite();
        } else {
            throw new MismatchedTokenException("Enunciado inesperado: " + LT(1));
        }
    }

    // asignacion : ID '=' expr
    private void asignacion() throws SemanticException {
        String id = LT(1).text;
        match(PseudoLexer.ID);
        validarDeclarada(id);
        match(PseudoLexer.EQ);
        expr();
    }

    // leer : 'leer' ID
    private void leer() throws SemanticException {
        match(PseudoLexer.LEER);
        String id = LT(1).text;
        match(PseudoLexer.ID);
        validarDeclarada(id);
    }

    // escribir : 'escribir' expr
    private void escribir() throws SemanticException {
        match(PseudoLexer.ESCRIBIR);
        expr();
    }

    private void repite() throws SemanticException {
        match(PseudoLexer.REPITE);
        match(PseudoLexer.LPAREN);
        String idx = LT(1).text;
        match(PseudoLexer.ID);
        validarDeclarada(idx);
        match(PseudoLexer.COMA);
        expr();  // valor inicial
        match(PseudoLexer.COMA);
        expr();  // valor final
        match(PseudoLexer.RPAREN);
        while (iniciaEnunciado(LA(1))) enunciado();
        match(PseudoLexer.FIN_REPITE);
    }

    private void expr() throws SemanticException {
        term();
        while (LA(1) == PseudoLexer.MAS || LA(1) == PseudoLexer.MENOS) {
            consume();
            term();
        }
    }

    private void term() throws SemanticException {
        factor();
        while (LA(1) == PseudoLexer.MULT || LA(1) == PseudoLexer.DIV) {
            consume();
            factor();
        }
    }

    private void factor() throws SemanticException {
        if (LA(1) == PseudoLexer.NUM) {
            match(PseudoLexer.NUM);
        } else if (LA(1) == PseudoLexer.ID) {
            String id = LT(1).text;
            match(PseudoLexer.ID);
            validarDeclarada(id);
        } else if (LA(1) == PseudoLexer.LPAREN) {
            match(PseudoLexer.LPAREN);
            expr();
            match(PseudoLexer.RPAREN);
        } else {
            throw new MismatchedTokenException("Expresión inesperada: " + LT(1));
        }
    }

    // Se modifica el método para usar la TablaSimbolos
    private void validarDeclarada(String id) throws SemanticException {
        // Se usa el método resolver para buscar la variable
        if (ts.resolver(id) == null) {
            // Si la variable no se encuentra, se lanza una SemanticException
            throw new SemanticException("Error semántico: Variable '" + id + "' no ha sido declarada.");
        }
    }

    public TablaSimbolos getTablaSimbolos() {
        return this.ts;
    }
}