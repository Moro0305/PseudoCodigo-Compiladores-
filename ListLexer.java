public class ListLexer extends Lexer {
    public static final int EOF_TYPE = -1;
    public static final int NAME = 1;
    public static final int LBRACK = 2;  // '['
    public static final int RBRACK = 3;  // ']'
    public static final int COMMA  = 4;  // ','
    public static final int EQ     = 5;  // '='

    public ListLexer(String input) { super(input); }

    @Override
    public Token nextToken() {
        while (c != EOF) {
            if (Character.isWhitespace(c)) { ws(); continue; }
            switch (c) {
                case '[': consume(); return new Token(LBRACK, "[");
                case ']': consume(); return new Token(RBRACK, "]");
                case ',': consume(); return new Token(COMMA, ",");
                case '=': consume(); return new Token(EQ, "=");
                default:
                    if (isLETTER()) return name();
                    throw new RuntimeException("carácter inválido: '"+c+"'");
            }
        }
        return new Token(EOF_TYPE,"<EOF>");
    }

    private boolean isLETTER() { return Character.isLetter(c); }

    private Token name() {
        StringBuilder sb=new StringBuilder();
        while (Character.isLetterOrDigit(c) || c=='_') { sb.append(c); consume(); }
        return new Token(NAME, sb.toString());
    }

    @Override
    public String getTokenName(int tokenType) {
        switch (tokenType) {
            case EOF_TYPE: return "EOF";
            case NAME: return "NAME";
            case LBRACK: return "LBRACK";
            case RBRACK: return "RBRACK";
            case COMMA: return "COMMA";
            case EQ: return "EQ";
            default: return "t" + tokenType;
        }
    }
}
