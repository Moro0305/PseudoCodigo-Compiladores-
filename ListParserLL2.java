public class ListParserLL2 extends ParserK {
    public ListParserLL2(Lexer input) { super(input, 2); }

    // list : '[' elements ']' ;
    public void list() {
        match(ListLexer.LBRACK);
        elements();
        match(ListLexer.RBRACK);
    }

    // elements : element (',' element)* ;
    public void elements() {
        element();
        while (LA(1) == ListLexer.COMMA) {
            match(ListLexer.COMMA);
            element();
        }
    }

    // element : NAME '=' NAME | NAME | list ;
    public void element() {
        if (LA(1) == ListLexer.NAME && LA(2) == ListLexer.EQ) {
            match(ListLexer.NAME);
            match(ListLexer.EQ);
            match(ListLexer.NAME);
        } else if (LA(1) == ListLexer.NAME) {
            match(ListLexer.NAME);
        } else if (LA(1) == ListLexer.LBRACK) {
            list();
        } else {
            throw new MismatchedTokenException("Elemento inválido (LL(2)): " + LT(1));
        }
    }
}
