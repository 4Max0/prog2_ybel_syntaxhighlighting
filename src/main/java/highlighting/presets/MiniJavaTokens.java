package highlighting.presets;

import highlighting.regex.Token;
import java.util.List;
import java.util.regex.Pattern;

public final class MiniJavaTokens {

  // Basic token set for MiniJava. Extend this list with further tokens as needed (e.g. identifiers,
  // numeric literals, operators, brackets, whitespace), following the same pattern. Each token is
  // defined by a regular expression and a colour. Optionally, a specific capturing group within the
  // pattern can be selected as the "highlighted" region.
  public static List<Token> defaultTokens() {
    return List.of(
        // Javadoc: /** ... */
        Token.of(
            Pattern.compile("/\\*\\*[^*]*\\*+(?:[^/*][^*]*\\*+)*/"),
            MiniJavaColours.JAVADOC_COMMENT_COLOUR),

        // Line Comment: // ...
        Token.of(Pattern.compile("//[^\\n\\r]*"), MiniJavaColours.LINE_COMMENT_COLOUR),

        // Block Comment: /* ... */
        Token.of(
            Pattern.compile("/\\*[^*]*\\*+(?:[^/*][^*]*\\*+)*/"),
            MiniJavaColours.BLOCK_COMMENT_COLOUR),

        // Keywords: null import package public
        Token.of(
            Pattern.compile(
                "\\b(package|import|class|public|private|final|return|null|new|extends|this|static)\\b"),
            MiniJavaColours.KEYWORD_COLOUR),

        // boolean: true false
        Token.of(Pattern.compile("\\b(true|false)\\b"), MiniJavaColours.BOOLEAN_LITERAL_COLOUR),

        // Strings: "hello world"
        Token.of(Pattern.compile("\"([^\"\\\\]|\\\\.)*\""), MiniJavaColours.STRING_LITERAL_COLOUR),

        // Characters: 'a' 'v' '\n'
        Token.of(Pattern.compile("'([^'\\\\]|\\\\.)'"), MiniJavaColours.CHAR_LITERAL_COLOUR),

        // Annotation @Override
        Token.of(Pattern.compile("@[A-Za-z-][A-Za-z0-9-]*\\b"), MiniJavaColours.ANNOTATION_COLOUR),

        // Numbers: 1 +213.4 -23.4 213.3
        Token.of(Pattern.compile("(?<!\\w)[+-]?\\d+(\\.\\d+)?\\b"), MiniJavaColours.NUMBER_COLOUR),

        // Operator: == = != > <
        Token.of(
            Pattern.compile("==|!=|!<|!>|<=|>=|&&|\\|\\||[!&|+\\-*/=<>]"),
            MiniJavaColours.OPERATOR_COLOUR));
  }
}
