package highlighting.presets;

import static org.junit.jupiter.api.Assertions.*;

import highlighting.core.HighlightRegion;
import highlighting.regex.Token;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Test class for MiniJavaTokens. All tests are for small strings as, larger strings are not
 * possible to test token to token without external logic.
 */
public class MiniJavaTokensTest {

  /** Helper method to collect all regions of a specific color. */
  private long countByColour(List<HighlightRegion> regions, java.awt.Color colour) {
    return regions.stream().filter(r -> r.colour().equals(colour)).count();
  }

  @Test
  public void testKeywordBeginning() {
    // given
    // We have our tokens
    List<Token> tokens = MiniJavaTokens.defaultTokens();

    // when
    // We check the tokens
    List<HighlightRegion> regions =
        tokens.stream().flatMap(t -> t.test("class Test").stream()).toList();

    // then
    // We get a hit for with a keyword the string (at the beginning)
    assertEquals(1, countByColour(regions, MiniJavaColours.KEYWORD_COLOUR));
  }

  @Test
  public void testKeywordMiddle() {
    // given
    // We have our tokens
    List<Token> tokens = MiniJavaTokens.defaultTokens();

    // when
    // We check the tokens
    List<HighlightRegion> regions =
        tokens.stream().flatMap(t -> t.test("abc class xyz").stream()).toList();

    // then
    // we get a keyword for the given string (even in the middle)
    assertEquals(1, countByColour(regions, MiniJavaColours.KEYWORD_COLOUR));
  }

  @Test
  public void testKeywordEnd() {
    // given
    // We have our tokens
    List<Token> tokens = MiniJavaTokens.defaultTokens();

    // when
    // We check the tokens
    List<HighlightRegion> regions =
        tokens.stream().flatMap(t -> t.test("abc class").stream()).toList();

    // then
    // we get a keyword for the given string (even at the end)
    assertEquals(1, countByColour(regions, MiniJavaColours.KEYWORD_COLOUR));
  }

  @Test
  public void testKeywordMultiple() {
    // given
    // We have our tokens
    List<Token> tokens = MiniJavaTokens.defaultTokens();

    // when
    // We check the tokens
    List<HighlightRegion> regions =
        tokens.stream().flatMap(t -> t.test("class public static").stream()).toList();

    // then
    // We get 3 hits for keywords
    assertEquals(3, countByColour(regions, MiniJavaColours.KEYWORD_COLOUR));
  }

  @Test
  public void testKeywordNoMatch() {
    // given
    // We have our tokens
    List<Token> tokens = MiniJavaTokens.defaultTokens();

    // when
    // We check the tokens
    List<HighlightRegion> regions =
        tokens.stream().flatMap(t -> t.test("hello world").stream()).toList();

    // then
    // We get 0 matches for keywords
    assertEquals(0, countByColour(regions, MiniJavaColours.KEYWORD_COLOUR));
  }

  @Test
  public void testKeywordInsideWordNoMatch() {
    // given
    // We have our tokens
    List<Token> tokens = MiniJavaTokens.defaultTokens();

    // when
    // We check the tokens
    List<HighlightRegion> regions =
        tokens.stream().flatMap(t -> t.test("classification").stream()).toList();

    // then
    // we get no hit for a keyword
    assertEquals(0, countByColour(regions, MiniJavaColours.KEYWORD_COLOUR));
  }

  @Test
  public void testAnnotationBeginning() {
    // given
    // We have our tokens
    List<Token> tokens = MiniJavaTokens.defaultTokens();

    // when
    // We check the tokens
    List<HighlightRegion> regions =
        tokens.stream().flatMap(t -> t.test("@Override").stream()).toList();

    // then
    // we will have a hit for an Annotation
    assertEquals(1, countByColour(regions, MiniJavaColours.ANNOTATION_COLOUR));
  }

  @Test
  public void testAnnotationLeadingWhitespace() {
    // given
    // We have our tokens
    List<Token> tokens = MiniJavaTokens.defaultTokens();

    // when
    // We check the tokens
    List<HighlightRegion> regions =
        tokens.stream().flatMap(t -> t.test("    @Override").stream()).toList();

    // then
    // we will have a hit for an Annotation
    assertEquals(1, countByColour(regions, MiniJavaColours.ANNOTATION_COLOUR));
  }

  @Test
  public void testLineCommentDetected() {
    // given
    // We have our tokens
    List<Token> tokens = MiniJavaTokens.defaultTokens();

    // when
    // We check the tokens
    List<HighlightRegion> regions =
        tokens.stream().flatMap(t -> t.test("// hello world").stream()).toList();

    // then
    // we will have a hit for a line comment
    assertEquals(1, countByColour(regions, MiniJavaColours.LINE_COMMENT_COLOUR));
  }

  @Test
  public void testBlockCommentShouldBeDetected() {
    // given
    // We have our tokens
    List<Token> tokens = MiniJavaTokens.defaultTokens();

    // when
    // We check the tokens
    List<HighlightRegion> regions =
        tokens.stream().flatMap(t -> t.test("/* hello world */").stream()).toList();

    // then
    // we will have a hit for a block comment
    assertEquals(1, countByColour(regions, MiniJavaColours.BLOCK_COMMENT_COLOUR));
  }

  @Test
  public void testJavadocDetected() {
    // given
    // We have our tokens
    List<Token> tokens = MiniJavaTokens.defaultTokens();

    // when
    // We check the tokens
    List<HighlightRegion> regions =
        tokens.stream().flatMap(t -> t.test("/** hello world */").stream()).toList();

    // then
    // we will have a hit for a docstring
    assertEquals(1, countByColour(regions, MiniJavaColours.JAVADOC_COMMENT_COLOUR));
  }

  @Test
  public void testStringDetected() {
    // given
    // We have our tokens
    List<Token> tokens = MiniJavaTokens.defaultTokens();

    // when
    // We check the tokens
    List<HighlightRegion> regions =
        tokens.stream().flatMap(t -> t.test("\"hello world\"").stream()).toList();

    // then
    // we will have a hit for a string
    assertEquals(1, countByColour(regions, MiniJavaColours.STRING_LITERAL_COLOUR));
  }

  @Test
  public void testCharacterDetected() {
    // given
    // We have our tokens
    List<Token> tokens = MiniJavaTokens.defaultTokens();

    // when
    // We check the tokens
    List<HighlightRegion> regions = tokens.stream().flatMap(t -> t.test("'a'").stream()).toList();

    // then
    // we will have a hit for a char
    assertEquals(1, countByColour(regions, MiniJavaColours.CHAR_LITERAL_COLOUR));
  }

  @Test
  public void testNumbersDetected() {
    // given
    // We have our tokens
    List<Token> tokens = MiniJavaTokens.defaultTokens();

    // when
    // We check the tokens
    List<HighlightRegion> regions =
        tokens.stream().flatMap(t -> t.test("1 42 999 23f1f 2.213 -32.9").stream()).toList();

    // then
    // we will have a hit for 3 numbers
    assertEquals(5, countByColour(regions, MiniJavaColours.NUMBER_COLOUR));
  }

  @Test
  public void testBooleanDetected() {
    // given
    // We have our tokens
    List<Token> tokens = MiniJavaTokens.defaultTokens();

    // when
    // We check the tokens
    List<HighlightRegion> regions =
        tokens.stream().flatMap(t -> t.test("if true else false").stream()).toList();

    // then
    // we will have 2 hits for boolean
    assertEquals(2, countByColour(regions, MiniJavaColours.BOOLEAN_LITERAL_COLOUR));
  }

  @Test
  public void testOperatorsDetected() {
    // given
    // We have our tokens
    List<Token> tokens = MiniJavaTokens.defaultTokens();

    // when
    // We check the tokens
    List<HighlightRegion> regions =
        tokens.stream().flatMap(t -> t.test("a == b != c").stream()).toList();

    // then
    // we will have 2 hits for 2 operators
    assertEquals(2, countByColour(regions, MiniJavaColours.OPERATOR_COLOUR));
  }

  @Test
  public void testStringContainingCommentSymbols() {
    // given
    // We have our tokens
    List<Token> tokens = MiniJavaTokens.defaultTokens();

    // when
    // We check the tokens
    List<HighlightRegion> regions =
        tokens.stream().flatMap(t -> t.test("\"// not a comment\"").stream()).toList();

    // then
    // We find a string literal
    // NOTE: the naive highlighter will have problems with this and even here the regex will find
    // the comment
    assertEquals(1, countByColour(regions, MiniJavaColours.STRING_LITERAL_COLOUR));
  }
}
