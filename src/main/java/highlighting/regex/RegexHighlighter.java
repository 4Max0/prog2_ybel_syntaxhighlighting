package highlighting.regex;

import highlighting.core.HighlightRegion;
import highlighting.core.SyntaxHighlighter;
import highlighting.presets.MiniJavaTokens;
import java.util.ArrayList;
import java.util.List;

public class RegexHighlighter extends SyntaxHighlighter {

  @Override
  public List<HighlightRegion> collectMatches(String text) {
    List<Token> tokens = MiniJavaTokens.defaultTokens();
    // NOTE: A for each where we put everything in a list would also do, but I'm lazy and this is
    // shorter to write
    return tokens.stream().flatMap(t -> t.test(text).stream()).toList();
    // throw new UnsupportedOperationException("not implemented yet");
  }

  @Override
  public List<HighlightRegion> resolveConflicts(List<HighlightRegion> regions) {
    // NOTE: Due to the implementation in SyntaxHighlighter.computeRegions() I can assume that the
    // provided regions is already sorted
    // NOTE: Because of the implementation of normalized, the tokens get sorted by smallest start
    // and bigger end, so we can simply check for every Region after the first if the start is after
    // the last added region in the result list which means it is not inside the last one and can be
    // added.
    List<HighlightRegion> result = new ArrayList<>();
    for (HighlightRegion region : regions) {

      // We always take the first region
      if (result.isEmpty()) {
        result.add(region);
        continue;
      }

      HighlightRegion last = result.getLast();

      // No overlapping on half-open intervals:
      // [0,5) and [5,8) is valid
      if (region.start() >= last.end()) {
        result.add(region);
      }
    }

    return result;
    // throw new UnsupportedOperationException("not implemented yet");
  }
}
