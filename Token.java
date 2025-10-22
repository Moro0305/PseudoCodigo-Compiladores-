public class Token {
    public final int type;
    public final String text;
    public Token(int type, String text) { this.type = type; this.text = text; }
    @Override public String toString() { return "<'" + text + "'," + type + ">"; }
}
