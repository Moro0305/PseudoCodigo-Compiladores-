import java.util.ArrayList;

// NO EXTIENDE ParserK
public class PseudoParser {
    private ArrayList<Token> tokens;
    private int indiceToken = 0;
    private TablaSimbolos ts;
    private TipoIncorporado real;
    private PseudoGenerador generador;
    private boolean errorSintactico = false; // Para manejo de errores
    private PseudoLexer lexer; // guardamos el lexer para usar métodos de instancia como getTokenName

    public PseudoParser(TablaSimbolos ts, PseudoGenerador generador) {
        this.ts = ts;
        this.generador = generador;
        // [cite: 504-506]
    }

    public void analizar(PseudoLexer lexer) throws SemanticException {
        this.lexer = lexer; // guardar referencia al lexer
        this.tokens = lexer.getTokens(); // [cite: 508]
        this.indiceToken = 0;
        this.errorSintactico = false;

        this.real = new TipoIncorporado("real");
        ts.definir(real); // [cite: 509-510]

        programa(); // Inicia el análisis

        if (errorSintactico) {
            throw new SyntaxException("Error de sintaxis. Compilación detenida.");
        }
        if (indiceToken != tokens.size() - 1) { // -1 por el EOF
            System.out.println("Error: Se esperaba fin de programa.");
        }
    }

    // Método 'match' adaptado del PDF [cite: 521]
    private boolean match(int tokenType) {
        if (indiceToken >= tokens.size()) return false;

        Token t = tokens.get(indiceToken);
        if (t.type == tokenType) {
            indiceToken++;
            return true;
        } else {
            String esperado = (lexer != null) ? lexer.getTokenName(tokenType) : ("t" + tokenType);
            System.out.println("Error de sintaxis: Se esperaba " + esperado + " pero se encontró " + t.text);
            errorSintactico = true;
            return false;
        }
    }

    // Método para ver el token actual
    private Token currentToken() {
        if (indiceToken >= tokens.size()) return tokens.get(tokens.size() -1); // EOF
        return tokens.get(indiceToken);
    }

    // <Programa> -> inicio-programa <DeclVars>? <Enunciados> fin-programa
    private void programa() throws SemanticException {
        if (!match(PseudoLexer.INICIO_PROGRAMA)) return;

        if (currentToken().type == PseudoLexer.VARIABLES) {
            declVars();
        }

        enunciados();

        if (match(PseudoLexer.FIN_PROGRAMA)) {
            generador.crearTuplaFinPrograma(); // [cite: 524-525]
        } else {
            System.out.println("Error: Se esperaba 'fin-programa'");
        }
    }

    // declVars : 'variables' ':' ID (',' ID)*
    private void declVars() throws SemanticException {
        match(PseudoLexer.VARIABLES);
        match(PseudoLexer.DOSPUNTOS);

        Token id = currentToken();
        if (!match(PseudoLexer.ID)) return;
        ts.definir(new Variable(id.text, real));

        while (currentToken().type == PseudoLexer.COMA) {
            match(PseudoLexer.COMA);
            id = currentToken();
            if (!match(PseudoLexer.ID)) break;
            ts.definir(new Variable(id.text, real));
        }
    }

    // <Enunciados> -> (<Enunciado>)*
    private void enunciados() throws SemanticException {
        while (iniciaEnunciado()) {
            enunciado();
        }
    }

    private boolean iniciaEnunciado() {
        if (errorSintactico) return false;
        int la1 = currentToken().type;
        return la1 == PseudoLexer.ID ||
                la1 == PseudoLexer.LEER ||
                la1 == PseudoLexer.ESCRIBIR ||
                la1 == PseudoLexer.SI ||       // <-- AÑADIDO
                la1 == PseudoLexer.MIENTRAS;  // <-- AÑADIDO
        // 'repite' no está en el PDF 'Unidad 5b', se omite.
    }

    // <Enunciado> -> <Asignacion> | <Leer> | <Escribir> | <Si> | <Mientras>
    private void enunciado() throws SemanticException {
        if (errorSintactico) return;

        int la1 = currentToken().type;
        if (la1 == PseudoLexer.ID) {
            asignacion();
        } else if (la1 == PseudoLexer.LEER) {
            leer();
        } else if (la1 == PseudoLexer.ESCRIBIR) {
            escribir();
        } else if (la1 == PseudoLexer.SI) {
            si(); // [cite: 582]
        } else if (la1 == PseudoLexer.MIENTRAS) {
            mientras(); // [cite: 597]
        }
    }

    // <Asignacion> -> ID = <Valor> (<OperadorAritmetico> <Valor>)?
    // Adaptado del PDF [cite: 527-538]
    private void asignacion() throws SemanticException {
        Token var = currentToken();
        if (!match(PseudoLexer.ID)) return;
        validarDeclarada(var.text); // Validación semántica

        if (!match(PseudoLexer.EQ)) return;

        Token val1 = valor(); // <Valor>

        int opType = currentToken().type;
        if (opType == PseudoLexer.MAS || opType == PseudoLexer.MENOS || opType == PseudoLexer.MULT || opType == PseudoLexer.DIV) {
            Token op = currentToken();
            match(opType);
            Token val2 = valor(); // <Valor>
            generador.crearTuplaAsignacion(var, val1, op, val2); // [cite: 533] adaptado
        } else {
            generador.crearTuplaAsignacion(var, val1); // [cite: 533] adaptado
        }
    }

    // <Leer> -> leer ID
    // Adaptado del PDF [cite: 539-549]
    private void leer() throws SemanticException {
        match(PseudoLexer.LEER);
        Token var = currentToken();
        if (!match(PseudoLexer.ID)) return;
        validarDeclarada(var.text); // Validación semántica
        generador.crearTuplaLeer(var); // [cite: 544]
    }

    // <Escribir> -> escribir (<Valor> | CADENA) (',' ID)?
    // Adaptado del PDF [cite: 550-578]
    private void escribir() throws SemanticException {
        match(PseudoLexer.ESCRIBIR);

        Token val1 = currentToken();
        if (val1.type == PseudoLexer.CADENA) {
            match(PseudoLexer.CADENA);
            if (currentToken().type == PseudoLexer.COMA) {
                match(PseudoLexer.COMA);
                Token var = currentToken();
                if (!match(PseudoLexer.ID)) return;
                validarDeclarada(var.text);
                generador.crearTuplaEscribir(val1, var); // 'escribir "cadena", var' [cite: 558]
            } else {
                generador.crearTuplaEscribir(val1); // 'escribir "cadena"' [cite: 564]
            }
        } else {
            Token var = valor(); // 'escribir var' o 'escribir num' (tratado como valor)
            generador.crearTuplaEscribir(var); // [cite: 573]
        }
    }

    // <Si> -> si <Comparacion> entonces <Enunciados> fin-si
    // PDF [cite: 579-593]
    private void si() throws SemanticException {
        match(PseudoLexer.SI);

        int indiceTuplaComp = generador.getIndiceSiguienteTupla(); // [cite: 581]
        comparacion();

        if (!match(PseudoLexer.ENTONCES)) return;

        enunciados();

        if (match(PseudoLexer.FIN_SI)) {
            generador.conectarSi(indiceTuplaComp); // [cite: 588]
        }
    }

    // <Mientras> -> mientras <Comparacion> <Enunciados> fin-mientras
    // PDF [cite: 594-607]
    private void mientras() throws SemanticException {
        match(PseudoLexer.MIENTRAS);

        int indiceTuplaComp = generador.getIndiceSiguienteTupla(); // [cite: 596]
        comparacion();

        enunciados();

        if (match(PseudoLexer.FIN_MIENTRAS)) {
            generador.conectarMientras(indiceTuplaComp); // [cite: 602]
        }
    }

    // <Comparacion> -> ( <Valor> <OperadorRelacional> <Valor> )
    // PDF [cite: 608-621]
    private void comparacion() throws SemanticException {
        if (!match(PseudoLexer.LPAREN)) return;

        Token val1 = valor();

        Token op = currentToken();
        // Debemos aceptar operadores relacionales provistos por el lexer
        int opType = op.type;
        boolean esOpRel = opType == PseudoLexer.LT || opType == PseudoLexer.GT ||
                opType == PseudoLexer.LTE || opType == PseudoLexer.GTE ||
                opType == PseudoLexer.NEQ || opType == PseudoLexer.EQ;
        if (!esOpRel) {
            System.out.println("Error: Se esperaba un operador relacional pero se encontró " + op.text);
            errorSintactico = true;
            return;
        }
        // Si es un operador relacional, consumimos
        match(opType);

        Token val2 = valor();

        if (!match(PseudoLexer.RPAREN)) return;

        generador.crearTuplaComparacion(val1, op, val2); // [cite: 616]
    }

    // <Valor> -> ID | NUM
    private Token valor() throws SemanticException {
        Token t = currentToken();
        if (t.type == PseudoLexer.ID) {
            match(PseudoLexer.ID);
            validarDeclarada(t.text);
            return t;
        } else if (t.type == PseudoLexer.NUM) {
            match(PseudoLexer.NUM);
            return t;
        } else {
            System.out.println("Error: Se esperaba ID o NUMERO, se encontró " + t.text);
            errorSintactico = true;
            return t;
        }
    }

    // Tu método de validación semántica
    private void validarDeclarada(String id) throws SemanticException {
        if (ts.resolver(id) == null) {
            throw new SemanticException("Error semántico: Variable '" + id + "' no ha sido declarada.");
        }
    }
}