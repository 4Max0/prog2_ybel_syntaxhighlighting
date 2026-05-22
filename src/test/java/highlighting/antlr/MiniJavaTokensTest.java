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

  @Test
  void testString() {
    // given
    // that we have tokens
    List<Token> tokens = MiniJavaTokens.defaultTokens();
    // when
    // parsing a valid text
    List<HighlightRegion> regions = this.parseString(tokens, Texts.START_TEXT);
    // then
    // we can see that the count is correct
    long keywordCount =
        regions.stream()
            .filter(r -> r.colour().equals(MiniJavaColours.STRING_LITERAL_COLOUR))
            .count();

    assertEquals(1, keywordCount);
  }

  @Test
  void testCharacter() {
    // given
    // that we have tokens
    List<Token> tokens = MiniJavaTokens.defaultTokens();
    // when
    // parsing a valid text
    List<HighlightRegion> regions = this.parseString(tokens, Texts.START_TEXT);
    // then
    long keywordCount =
        regions.stream()
            .filter(r -> r.colour().equals(MiniJavaColours.CHAR_LITERAL_COLOUR))
            .count();

    assertEquals(1, keywordCount);
  }

  @Test
  void testKeywords() {
    // given
    // that we have tokens
    List<Token> tokens = MiniJavaTokens.defaultTokens();
    // when
    List<HighlightRegion> regions = this.parseString(tokens, Texts.START_TEXT);
    // then
    long keywordCount =
        regions.stream().filter(r -> r.colour().equals(MiniJavaColours.KEYWORD_COLOUR)).count();

    assertEquals(17, keywordCount);
  }

  @Test
  void testAnnotations() {
    // given
    // that we have tokens
    List<Token> tokens = MiniJavaTokens.defaultTokens();
    // when
    // parsing a valid text
    List<HighlightRegion> regions = this.parseString(tokens, Texts.START_TEXT);
    // then
    long keywordCount =
        regions.stream().filter(r -> r.colour().equals(MiniJavaColours.ANNOTATION_COLOUR)).count();

    assertEquals(0, keywordCount);
  }

  @Test
  void testJavadoc() {
    // given
    // that we have tokens
    List<Token> tokens = MiniJavaTokens.defaultTokens();
    // when
    // parsing a valid text
    List<HighlightRegion> regions = this.parseString(tokens, Texts.START_TEXT);
    // then
    long keywordCount =
        regions.stream()
            .filter(r -> r.colour().equals(MiniJavaColours.JAVADOC_COMMENT_COLOUR))
            .count();

    assertEquals(2, keywordCount);
  }

  @Test
  void testCommentLong() {
    // given
    // that we have tokens
    List<Token> tokens = MiniJavaTokens.defaultTokens();
    // when
    // parsing a valid text
    List<HighlightRegion> regions = this.parseString(tokens, Texts.START_TEXT);
    // then
    long keywordCount =
        regions.stream()
            .filter(r -> r.colour().equals(MiniJavaColours.BLOCK_COMMENT_COLOUR))
            .count();

    assertEquals(1, keywordCount);
  }

  @Test
  void testCommentShort() {
    // given
    // that we have tokens
    List<Token> tokens = MiniJavaTokens.defaultTokens();
    // when
    // parsing a valid text
    List<HighlightRegion> regions = this.parseString(tokens, Texts.START_TEXT);
    // then
    long keywordCount =
        regions.stream()
            .filter(r -> r.colour().equals(MiniJavaColours.LINE_COMMENT_COLOUR))
            .count();

    assertEquals(1, keywordCount);
  }

  @Test
  void testNumbers() {
    // given
    // that we have tokens
    List<Token> tokens = MiniJavaTokens.defaultTokens();
    // when
    // parsing a valid text
    List<HighlightRegion> regions = this.parseString(tokens, Texts.START_TEXT);
    // then
    long keywordCount =
        regions.stream().filter(r -> r.colour().equals(MiniJavaColours.NUMBER_COLOUR)).count();

    assertEquals(0, keywordCount);
  }

  @Test
  void testBoolean() {
    // given
    // that we have tokens
    List<Token> tokens = MiniJavaTokens.defaultTokens();
    // when
    // parsing a valid text
    List<HighlightRegion> regions = this.parseString(tokens, Texts.START_TEXT);
    // then
    long keywordCount =
        regions.stream()
            .filter(r -> r.colour().equals(MiniJavaColours.BOOLEAN_LITERAL_COLOUR))
            .count();

    assertEquals(0, keywordCount);
  }

  @Test
  void testOperator() {
    // given
    // that we have tokens
    List<Token> tokens = MiniJavaTokens.defaultTokens();
    // when
    // parsing a valid text
    List<HighlightRegion> regions = this.parseString(tokens, Texts.START_TEXT);
    // then
    long keywordCount =
        regions.stream().filter(r -> r.colour().equals(MiniJavaColours.OPERATOR_COLOUR)).count();

    assertEquals(3, keywordCount);
  }
}
