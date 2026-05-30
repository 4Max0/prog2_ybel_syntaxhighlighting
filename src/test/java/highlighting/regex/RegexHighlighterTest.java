package highlighting.regex;

import static org.junit.jupiter.api.Assertions.*;

import highlighting.core.HighlightRegion;
import highlighting.presets.MiniJavaColours;
import java.util.List;
import org.junit.jupiter.api.Test;

public class RegexHighlighterTest {
  private final RegexHighlighter highlighter = new RegexHighlighter(); // a single instance

  @Test
  public void testIsEmpty() {
    // given
    // we have an empty string
    String text = "";

    // when
    // we parse the text
    List<HighlightRegion> result = highlighter.computeRegions(text);

    // then
    // The result is an empty list of regions
    assertTrue(result.isEmpty());
  }

  @Test
  public void testmatch() {
    // given
    // We have a text with a single hit
    String text = "static";

    // when
    // we parse the text
    List<HighlightRegion> result = highlighter.computeRegions(text);

    // then
    // we have a single hit with the correct values
    assertEquals(1, result.size());
    HighlightRegion region = result.getFirst();
    assertEquals(0, region.start());
    assertEquals(6, region.end());
    assertEquals(MiniJavaColours.KEYWORD_COLOUR, region.colour());
  }

  @Test
  public void testMatchMultiple() {
    // given
    // We have a text with a single hit
    String text = "private static class Main {}";

    // when
    // we parse the text
    List<HighlightRegion> result = highlighter.computeRegions(text);

    // then
    // we have a single hit with the correct values
    assertEquals(3, result.size());
  }

  @Test
  public void testNoMatch() {
    // given
    // We have a string of gibberish
    String text = "$$$$$$$$";

    // when
    // we parse the text
    List<HighlightRegion> result = highlighter.computeRegions(text);

    // then
    // The result is an empty list of regions
    assertTrue(result.isEmpty());
  }

  @Test
  public void testConflictResolved() {
    // given
    // We have a string which conflicts the tokens
    String text = "// @Override this is a test for conflicts ";

    // when
    // we parse the text
    List<HighlightRegion> result = highlighter.computeRegions(text);

    // then
    // The result is an empty list of regions
    assertEquals(1, result.size());
    HighlightRegion region = result.getFirst();
    assertEquals(0, region.start());
    assertEquals(text.length(), region.end());
    assertEquals(MiniJavaColours.LINE_COMMENT_COLOUR, region.colour());
  }

  @Test
  public void testJavaDocNotBlock() {
    // given
    // we have a text that is a Javadoc comment
    String text = "/** documentation */";

    // when
    // we parse the text
    List<HighlightRegion> result = highlighter.computeRegions(text);

    // then
    // we get a Javadoc not a block comment
    assertEquals(1, result.size());

    HighlightRegion region = result.getFirst();
    assertEquals(0, region.start());
    assertEquals(text.length(), region.end());
    assertEquals(MiniJavaColours.JAVADOC_COMMENT_COLOUR, region.colour());
  }

  @Test
  public void testOutsideComment() {
    // given
    // we have a text containing a comment
    String text =
        """
        // this is a test 123
        class
        """;

    // when
    // we parse the text
    List<HighlightRegion> result = highlighter.computeRegions(text);

    // then
    // we see that it hits the correct amount of elements and their colors
    assertEquals(2, result.size());
    assertEquals(MiniJavaColours.LINE_COMMENT_COLOUR, result.getFirst().colour());
    assertEquals(MiniJavaColours.KEYWORD_COLOUR, result.getLast().colour());
  }

  @Test
  public void testOutsideBlockComment() {
    // given
    // we have a text containing a comment
    String text =
        """
        123
        /* this
        is
        a
        test 123 */
        class
        """;

    // when
    // we parse the text
    List<HighlightRegion> result = highlighter.computeRegions(text);

    // then
    // we see that it hits the correct amount of elements and their colors
    assertEquals(3, result.size());
    assertEquals(MiniJavaColours.NUMBER_COLOUR, result.getFirst().colour());
    assertEquals(MiniJavaColours.BLOCK_COMMENT_COLOUR, result.get(1).colour());
    assertEquals(MiniJavaColours.KEYWORD_COLOUR, result.getLast().colour());
  }

  @Test
  public void testAdjacentRegionsNoConflict() {
    // given
    // We have 2 HighlightRegions regions next to each over
    // NOTE: Here we need to explicitly manually give HighlightRegions, because the Tokens most
    // tokens have a space explicitly in them
    List<HighlightRegion> regions =
        List.of(
            new HighlightRegion(0, 5, MiniJavaColours.KEYWORD_COLOUR),
            new HighlightRegion(5, 10, MiniJavaColours.NUMBER_COLOUR));

    // when
    // we resolve the conflicts
    List<HighlightRegion> result = highlighter.resolveConflicts(regions);

    // then
    // we see that there are still 2 regions
    assertEquals(2, result.size());
  }

  @Test
  public void testOverlappingRegionsConflict() {
    // given
    // We have 2 HighlightRegions regions that overlap
    List<HighlightRegion> regions =
      List.of(
        new HighlightRegion(0, 10, MiniJavaColours.KEYWORD_COLOUR),
        new HighlightRegion(5, 15, MiniJavaColours.NUMBER_COLOUR));

    // when
    // we resolve the conflicts
    List<HighlightRegion> result = highlighter.resolveConflicts(regions);

    // then
    // we have the one with the lowest start winning
    assertEquals(1, result.size());
    HighlightRegion region = result.getFirst();
    assertEquals(0, region.start());
    assertEquals(10, region.end());
    assertEquals(MiniJavaColours.KEYWORD_COLOUR, region.colour());
  }

  @Test
  public void testContainedRegionConflict() {
    // given
    // We have 2 HighlightRegions regions that overlap
    List<HighlightRegion> regions =
      List.of(
        new HighlightRegion(0, 20, MiniJavaColours.BLOCK_COMMENT_COLOUR),
        new HighlightRegion(5, 10, MiniJavaColours.KEYWORD_COLOUR));

    // when
    // we resolve the conflicts
    List<HighlightRegion> result = highlighter.resolveConflicts(regions);

    // then
    // we have the outer region winning
    assertEquals(1, result.size());
    assertEquals(MiniJavaColours.BLOCK_COMMENT_COLOUR,
      result.getFirst().colour());
  }
}
