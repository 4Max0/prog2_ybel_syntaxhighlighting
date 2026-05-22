package highlighting.presets;

import highlighting.regex.Token;
import java.util.List;
import java.util.regex.Pattern;

public final class MiniJavaTokens {

  // TODO (Phase I+II: RegexHighlighter/ScanningHighlighter)
  // TODO: Define the MiniJava tokens used by the highlighters. Each token is a mapping from a
  // regular expression to a colour (and, if applicable, a specific matching group). The order of
  // tokens in this list determines their relative priority during highlighting. One example token
  // definition is provided below; define the remaining tokens in an analogous way.

  // Basic token set for MiniJava. Extend this list with further tokens as needed (e.g. identifiers,
  // numeric literals, operators, brackets, whitespace), following the same pattern. Each token is
  // defined by a regular expression and a colour. Optionally, a specific capturing group within the
  // pattern can be selected as the "highlighted" region.
  public static List<Token> defaultTokens() {
    return List.of(
        // Strings: "hello world"
        Token.of(Pattern.compile("\"([^\"\\\\]|\\\\.)*\""), MiniJavaColours.STRING_LITERAL_COLOUR),

        // Characters: 'a' 'v' '\n'
        Token.of(Pattern.compile("'([^'\\\\]|\\\\.)'"), MiniJavaColours.CHAR_LITERAL_COLOUR),

        // boolean: true false
        Token.of(Pattern.compile("\\b(true|false)\\b"), MiniJavaColours.BOOLEAN_LITERAL_COLOUR),

        // Keywords: null import package public
        Token.of(
            Pattern.compile(
                "\\b(package|import|class|public|private|final|return|null|new|extends|this)\\b"),
            MiniJavaColours.KEYWORD_COLOUR),

        // Annotation @Override
        Token.of(Pattern.compile("@[A-Za-z-][A-Za-z0-9-]*"), MiniJavaColours.ANNOTATION_COLOUR),

        // Javadoc: /** ... */
        Token.of(
            Pattern.compile("/\\*\\*[^*]*\\*+(?:[^/*][^*]*\\*+)*/"),
            MiniJavaColours.JAVADOC_COMMENT_COLOUR),

        // Comment long: /* ... */
        Token.of(
            Pattern.compile("/\\*[^*]*\\*+(?:[^/*][^*]*\\*+)*/"),
            MiniJavaColours.BLOCK_COMMENT_COLOUR),

        // Comment short // ...
        Token.of(Pattern.compile("//.*"), MiniJavaColours.LINE_COMMENT_COLOUR),

        // Numbers: 1 2 13 1456
        Token.of(Pattern.compile("\\b\\d+\\b"), MiniJavaColours.NUMBER_COLOUR),

        // Operator: == = != > <
        Token.of(
            Pattern.compile("==|!=|<=|>=|&&|\\|\\||[+\\-*/=<>]"), MiniJavaColours.OPERATOR_COLOUR));
  }
}
