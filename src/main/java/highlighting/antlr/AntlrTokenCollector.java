package highlighting.antlr;

import highlighting.core.HighlightRegion;
import highlighting.core.SyntaxHighlighter;
import highlighting.presets.MiniJavaColours;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.*;

// This highlighter uses the ANTLR-generated MiniJavaLexer to turn the input text into a token
// stream. {@code collectMatches(String)} is the only method you need to implement: extract tokens
// of interest and map them to {@code HighlightRegions} using the colours from {@code
// MiniJavaColours}. Sorting, filtering of invalid regions, and conflict handling are performed by
// the base class {@code SyntaxHighlighter} via the template method {@code computeRegions(...)}.
public class AntlrTokenCollector extends SyntaxHighlighter {

  // {@code MiniJavaLexer} to collect highlight regions.
  //
  // Requirements / hints:
  // - Iterate over the lexer tokens (typically via {@code CommonTokenStream}); ignore the EOF
  // token.
  // - For each token type that should be coloured (e.g., keywords, string/char literals, comments),
  // create a {@code HighlightRegion} with the corresponding colour from {@code MiniJavaColours}.
  // - Use {@code Token#getStartIndex()} and {@code Token#getStopIndex()} (inclusive) to compute
  // {@code [start, end)} ranges: {@code start = startIndex, end = stopIndex + 1}.
  // - Do not sort, merge, or resolve overlaps here; return all candidates as you find them.
  // Normalisation and conflict resolution are handled later by the template method.
  // - Annotation highlighting: colour '@' and the immediately following IDENTIFIER token (if
  // present).
  @Override
  public List<HighlightRegion> collectMatches(String text) {
    List<HighlightRegion> result = new ArrayList<>();
    var input = CharStreams.fromString(text);
    MiniJavaLexer lexer = new MiniJavaLexer(input);
    var tokens = new CommonTokenStream(lexer);
    tokens.fill();

    for (int i = 0; i < tokens.size(); i++) {
      Token t = tokens.get(i);

      // skip the end of file token
      if (t.getType() == Token.EOF) {
        continue;
      }

      // determine the colour by helper function
      Color colour = deduceColour(t);
      // if not null we add it to the tokens
      if (colour != null) {
        result.add(new HighlightRegion(t.getStartIndex(), t.getStopIndex() + 1, colour));
      }

      // Annotation highlighting handling
      // we add the current and the next element (if it is an identifier)
      if (t.getType() == MiniJavaLexer.AT) {
        // add current
        result.add(
            new HighlightRegion(
                t.getStartIndex(), t.getStopIndex() + 1, MiniJavaColours.ANNOTATION_COLOUR));

        // add next if it is identifier.
        if (i + 1 < tokens.size() && tokens.get(i + 1).getType() == MiniJavaLexer.IDENTIFIER) {
          Token id = tokens.get(i + 1);
          result.add(
              new HighlightRegion(
                  id.getStartIndex(), id.getStopIndex() + 1, MiniJavaColours.ANNOTATION_COLOUR));
        }
      }
    }

    return result;
  }

  /**
   * Helper function the get the Colour
   *
   * @param token the token to check
   * @return the AWT Colour from MiniJavaColours
   */
  private Color deduceColour(Token token) {
    return switch (token.getType()) {

      // Keywords
      case MiniJavaLexer.PACKAGE,
          MiniJavaLexer.IMPORT,
          MiniJavaLexer.CLASS,
          MiniJavaLexer.PUBLIC,
          MiniJavaLexer.PRIVATE,
          MiniJavaLexer.FINAL,
          MiniJavaLexer.RETURN,
          MiniJavaLexer.NULL,
          MiniJavaLexer.NEW,
          MiniJavaLexer.IF,
          MiniJavaLexer.ELSE,
          MiniJavaLexer.WHILE,
          MiniJavaLexer.EXTENDS,
          MiniJavaLexer.IMPLEMENTS ->
          MiniJavaColours.KEYWORD_COLOUR;

      // String Literals
      case MiniJavaLexer.STRING_LITERAL -> MiniJavaColours.STRING_LITERAL_COLOUR;

      // Char Literal
      case MiniJavaLexer.CHAR_LITERAL -> MiniJavaColours.CHAR_LITERAL_COLOUR;

      // Comments
      case MiniJavaLexer.LINE_COMMENT -> MiniJavaColours.LINE_COMMENT_COLOUR;

      // Block Comment
      case MiniJavaLexer.BLOCK_COMMENT -> MiniJavaColours.BLOCK_COMMENT_COLOUR;

      // Javadoc
      case MiniJavaLexer.JAVADOC_COMMENT -> MiniJavaColours.JAVADOC_COMMENT_COLOUR;

      // Operators
      case MiniJavaLexer.PLUS,
          MiniJavaLexer.MINUS,
          MiniJavaLexer.STAR,
          MiniJavaLexer.SLASH,
          MiniJavaLexer.PERCENT,
          MiniJavaLexer.ASSIGN,
          MiniJavaLexer.LT,
          MiniJavaLexer.GT,
          MiniJavaLexer.BANG,
          MiniJavaLexer.LE,
          MiniJavaLexer.GE,
          MiniJavaLexer.EQUAL,
          MiniJavaLexer.NOTEQUAL,
          MiniJavaLexer.AND,
          MiniJavaLexer.OR,
          MiniJavaLexer.QUESTION,
          MiniJavaLexer.COLON ->
          MiniJavaColours.OPERATOR_COLOUR;

      // undetectable
      default -> null;
    };
  }
}
