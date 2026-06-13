package highlighting.antlr;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

/// MiniJava Pretty Printer (minimal, stateful)
///
/// Requirements:
/// - Reproduce the whole program (comments and whitespaces are gone).
/// - Ignore whitespace from the input; instead, generate:
///     - indentation for class bodies and blocks,
///     - exactly one line per statement (lines ending in ';').
///
/// Simplification:
/// Everything that is not indentation or line breaks is printed as raw tokens (with a very simple
/// space heuristic). Expression and signature formatting is therefore not "nice", which is
/// acceptable for this exercise.
public final class PrettyPrinterVisitor extends MiniJavaBaseVisitor<Void> {

  private final StringBuilder out = new StringBuilder();
  private final int indentWidth;
  private int currentIndent = 0;
  private boolean atLineStart = true;

  // For simple spacing between tokens:
  private Token lastToken = null;

  public PrettyPrinterVisitor(int indentWidth) {
    this.indentWidth = Math.max(0, indentWidth);
  }

  public String result() {
    return out.toString();
  }

  @Override
  public Void visitCompilationUnit(MiniJavaParser.CompilationUnitContext ctx) {
    // package
    if (ctx.packageDecl() != null) {
      visit(ctx.packageDecl());
      // separate package from imports with one blank line
      nl();
      nl();
    }

    // imports
    for (var child : ctx.importDecl()) {
      visit(child);
      nl();
    }

    // newline between import and types
    if (!ctx.importDecl().isEmpty() && !ctx.typeDecl().isEmpty()) {
      nl();
    }

    // types
    for (var child : ctx.typeDecl()) {
      visit(child);
    }
    return null;
  }

  @Override
  public Void visitClassBody(MiniJavaParser.ClassBodyContext ctx) {
    writeln(" {");
    this.currentIndent++;

    // go through the children of class body statements with a new line
    for (var child : ctx.classBodyDeclaration()) {
      visit(child);
      nl();
    }

    this.currentIndent--;
    writeln("}");
    return null;
  }

  @Override
  public Void visitBlock(MiniJavaParser.BlockContext ctx) {
    writeln("{");
    currentIndent++;

    // go through the children of block statements with a new line
    for (var child : ctx.blockStatement()) {
      visit(child);
      nl();
    }

    currentIndent--;
    // closing bracket
    write("}");
    return null;
  }

  @Override
  public Void visitStatement(MiniJavaParser.StatementContext ctx) {
    // the statements are already ensured to be on one line through their parents (visitBlock and
    // visitClassBody), so we only need to look at the children, and it will automatically newline
    // when we need to.
    for (var child : ctx.children) {
      visit(child);
    }
    return null;
  }

  // ---------------- helper methods ----------------

  private void indent() {
    if (atLineStart) {
      out.repeat(" ", Math.max(0, indentWidth * currentIndent));
      atLineStart = false;
    }
  }

  private void write(String s) {
    if (s == null || s.isEmpty()) return;
    indent();
    out.append(s);
  }

  private void nl() {
    out.append('\n');
    atLineStart = true;
    lastToken = null; // Reset spacing context at the beginning of a line
  }

  private void writeln(String s) {
    write(s);
    nl();
  }

  // --------------- token output + basic spacing ---------------

  @Override
  public Void visitTerminal(TerminalNode node) {
    Token t = node.getSymbol();
    String text = t.getText();

    if (lastToken != null) {
      int prevType = lastToken.getType();
      int curType = t.getType();

      // Simple heuristic: insert a space between "word-like" tokens
      if (needsSpaceBetween(prevType, curType)) write(" ");
    }

    write(text);
    lastToken = t;
    return null;
  }

  private boolean needsSpaceBetween(int prevType, int curType) {
    return isWordLike(prevType) && isWordLike(curType);
  }

  private boolean isWordLike(int type) {
    return type == MiniJavaLexer.IDENTIFIER
        || type == MiniJavaLexer.STRING_LITERAL
        || type == MiniJavaLexer.CHAR_LITERAL
        || type == MiniJavaLexer.NULL
        || type == MiniJavaLexer.PACKAGE
        || type == MiniJavaLexer.IMPORT
        || type == MiniJavaLexer.CLASS
        || type == MiniJavaLexer.PUBLIC
        || type == MiniJavaLexer.PRIVATE
        || type == MiniJavaLexer.FINAL
        || type == MiniJavaLexer.RETURN
        || type == MiniJavaLexer.NEW
        || type == MiniJavaLexer.IF
        || type == MiniJavaLexer.ELSE
        || type == MiniJavaLexer.WHILE
        || type == MiniJavaLexer.EXTENDS
        || type == MiniJavaLexer.IMPLEMENTS;
  }
}
