package highlighting.antlr;

import static org.junit.jupiter.api.Assertions.*;

import highlighting.core.HighlightRegion;
import highlighting.presets.*;
import highlighting.regex.Token;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class MiniJavaTokensTest {
  /**
   * Function to parse a string
   *
   * @param tokens The tokens used
   * @param text The string to parse
   * @return The result of the parsing
   */
  List<HighlightRegion> parseString(List<Token> tokens, String text) {
    List<HighlightRegion> result = new ArrayList<>();

    boolean[] occupied = new boolean[text.length()];

    for (Token token : tokens) {
      for (HighlightRegion region : token.test(text)) {

        boolean free = true;
        for (int i = region.start(); i < region.end(); i++) {
          if (occupied[i]) {
            free = false;
            break;
          }
        }

        if (free) {
          result.add(region);
          for (int i = region.start(); i < region.end(); i++) {
            occupied[i] = true;
          }
        }
      }
    }

    return result;
  }

  /**
   * Eine methode zum einfachen Verifizieren des Ergebnisses von parseString. Die Test-Methode gibt
   * einfach nur in der Konsole aus was gefunden wurde im Text.START_TEXT, nicht mehr. Der
   * Hintergrund ist, dass ich das in diesem Fall einfacher zum Verifizieren habe als der Debugger
   */
  @Test
  public void parseString() {
    // private void parseString() {
    List<Token> tokens = MiniJavaTokens.defaultTokens();
    List<HighlightRegion> regions = parseString(tokens, Texts.START_TEXT);
    System.out.println("=== Found Tokens ===");
    for (HighlightRegion r : regions) {
      System.out.printf(
          "%-12s  [%2d, %2d]  %s%n",
          Texts.START_TEXT.substring(r.start(), r.end()), r.start(), r.end(), r.colour());
    }
  }

  @Test
  public void testString() {
    // given
    // that we have tokens
    List<Token> tokens = MiniJavaTokens.defaultTokens();
    // when
    // parsing a valid text
    List<HighlightRegion> regions = this.parseString(tokens, Texts.START_TEXT);
    // then
    // we can see that the count is correct to the amount in the text
    long keywordCount =
        regions.stream()
            .filter(r -> r.colour().equals(MiniJavaColours.STRING_LITERAL_COLOUR))
            .count();

    assertEquals(1, keywordCount);
  }

  @Test
  public void testCharacter() {
    // given
    // that we have tokens
    List<Token> tokens = MiniJavaTokens.defaultTokens();
    // when
    // parsing a valid text
    List<HighlightRegion> regions = this.parseString(tokens, Texts.START_TEXT);
    // then
    // we can see that the count is correct to the amount in the text
    long keywordCount =
        regions.stream()
            .filter(r -> r.colour().equals(MiniJavaColours.CHAR_LITERAL_COLOUR))
            .count();

    assertEquals(1, keywordCount);
  }

  @Test
  public void testKeywords() {
    // given
    // that we have tokens
    List<Token> tokens = MiniJavaTokens.defaultTokens();
    // when
    List<HighlightRegion> regions = this.parseString(tokens, Texts.START_TEXT);
    // then
    // we can see that the count is correct to the amount in the text
    long keywordCount =
        regions.stream().filter(r -> r.colour().equals(MiniJavaColours.KEYWORD_COLOUR)).count();

    assertEquals(18, keywordCount);
  }

  @Test
  public void testAnnotations() {
    // given
    // that we have tokens
    List<Token> tokens = MiniJavaTokens.defaultTokens();
    // when
    // parsing a valid text
    List<HighlightRegion> regions = this.parseString(tokens, Texts.START_TEXT);
    // then
    // we can see that the count is correct to the amount in the text
    long keywordCount =
        regions.stream().filter(r -> r.colour().equals(MiniJavaColours.ANNOTATION_COLOUR)).count();

    assertEquals(1, keywordCount);
  }

  @Test
  public void testJavadoc() {
    // given
    // that we have tokens
    List<Token> tokens = MiniJavaTokens.defaultTokens();
    // when
    // parsing a valid text
    List<HighlightRegion> regions = this.parseString(tokens, Texts.START_TEXT);
    // then
    // we can see that the count is correct to the amount in the text
    long keywordCount =
        regions.stream()
            .filter(r -> r.colour().equals(MiniJavaColours.JAVADOC_COMMENT_COLOUR))
            .count();

    assertEquals(2, keywordCount);
  }

  @Test
  public void testCommentLong() {
    // given
    // that we have tokens
    List<Token> tokens = MiniJavaTokens.defaultTokens();
    // when
    // parsing a valid text
    List<HighlightRegion> regions = this.parseString(tokens, Texts.START_TEXT);
    // then
    // we can see that the count is correct to the amount in the text
    long keywordCount =
        regions.stream()
            .filter(r -> r.colour().equals(MiniJavaColours.BLOCK_COMMENT_COLOUR))
            .count();

    assertEquals(1, keywordCount);
  }

  @Test
  public void testCommentShort() {
    // given
    // that we have tokens
    List<Token> tokens = MiniJavaTokens.defaultTokens();
    // when
    // parsing a valid text
    List<HighlightRegion> regions = this.parseString(tokens, Texts.START_TEXT);
    // then
    // we can see that the count is correct to the amount in the text
    long keywordCount =
        regions.stream()
            .filter(r -> r.colour().equals(MiniJavaColours.LINE_COMMENT_COLOUR))
            .count();

    assertEquals(2, keywordCount);
  }

  @Test
  public void testNumbers() {
    // given
    // that we have tokens
    List<Token> tokens = MiniJavaTokens.defaultTokens();
    // when
    // parsing a valid text
    List<HighlightRegion> regions = this.parseString(tokens, Texts.START_TEXT);
    // then
    // we can see that the count is correct to the amount in the text
    long keywordCount =
        regions.stream().filter(r -> r.colour().equals(MiniJavaColours.NUMBER_COLOUR)).count();

    assertEquals(0, keywordCount);
  }

  @Test
  public void testBoolean() {
    // given
    // that we have tokens
    List<Token> tokens = MiniJavaTokens.defaultTokens();
    // when
    // parsing a valid text
    List<HighlightRegion> regions = this.parseString(tokens, Texts.START_TEXT);
    // then
    // we can see that the count is correct to the amount in the text
    long keywordCount =
        regions.stream()
            .filter(r -> r.colour().equals(MiniJavaColours.BOOLEAN_LITERAL_COLOUR))
            .count();

    assertEquals(0, keywordCount);
  }

  @Test
  public void testOperator() {
    // given
    // that we have tokens
    List<Token> tokens = MiniJavaTokens.defaultTokens();
    // when
    // parsing a valid text
    List<HighlightRegion> regions = this.parseString(tokens, Texts.START_TEXT);
    // then
    // we can see that the count is correct to the amount in the text
    long keywordCount =
        regions.stream().filter(r -> r.colour().equals(MiniJavaColours.OPERATOR_COLOUR)).count();

    assertEquals(3, keywordCount);
  }
}
