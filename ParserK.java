public abstract class ParserK {
    protected final Lexer input;
    protected final Token[] lookahead;
    protected final int k;
    protected int p = 0;

    public ParserK(Lexer input, int k) {
        this.input = input;
        this.k = k;
        this.lookahead = new Token[k];
        for (int i = 0; i < k; i++) lookahead[i] = input.nextToken();
    }

    protected void consume() {
        lookahead[p] = input.nextToken();
        p = (p + 1) % k;
    }

    protected Token LT(int i) { return lookahead[(p + i - 1) % k]; }
    protected int   LA(int i) { return LT(i).type; }

    protected void match(int x) {
        if (LA(1) == x) consume();
        else throw new MismatchedTokenException(
            "Se esperaba " + input.getTokenName(x) +
            " y llegó " + input.getTokenName(LA(1)) +
            " con '" + LT(1).text + "'"
        );
    }
}
