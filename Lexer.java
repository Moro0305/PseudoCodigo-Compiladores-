public abstract class Lexer {
    public static final char EOF = (char)-1;

    protected final String input;
    protected int p = 0;        // índice
    protected char c;           // carácter actual

    public Lexer(String input) {
        this.input = input;
        this.c = input.length() > 0 ? input.charAt(0) : EOF;
    }

    protected void consume() {
        p++;
        c = p >= input.length() ? EOF : input.charAt(p);
    }

    protected void ws() {
        while (c == ' ' || c == '\t' || c == '\n' || c == '\r') consume();
    }

    public abstract Token nextToken();
    public abstract String getTokenName(int tokenType);
}
