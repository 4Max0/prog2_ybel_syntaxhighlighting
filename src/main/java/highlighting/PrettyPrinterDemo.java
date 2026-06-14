package highlighting;

import highlighting.antlr.*;
import java.util.Scanner;
import org.antlr.v4.runtime.*;

public class PrettyPrinterDemo {
  public static void main(String[] args) {
    // Basically the Default Text from the Highlighting with some gibberish added.
    String source1 =
        """
        package controller; import com.badlogic.gdx.Game;import com.badlogic.gdx.graphics.g2d.SpriteBatch;
        /* ApplicationListener that delegates to the MainGameController. Just some setup. */
        public class LibgdxSetup extends Game {
        int abc = b;
        private final MainController mc;
        private final String FOO = "wuppie fluppie";
        private SpriteBatch batch;
        private SpriteBatch hudBatch;
        public LibgdxSetup(MainController mc) {this.mc = mc;}
        public void create() { char ch = new Character('a'); return null; { while (null) {  if ( abc == a) {i = i + a;} } } }
        }
        """;

    // Very simple test that shows 2 class members
    String source2 =
        """
        public class a { a b = "abc"; String acvx = 'a';}
        """;

    // package, import and some functions
    String source3 =
        """
        package test;
        import foo.Bar;
        public class TestClass {
        int a = zxcv; int b = b; int c = a + b;
        public void foo() {
        return;
        }
        public void bar() { a = b; b = c; c = a;
        }
        }
        """;

    // just some blocks
    String source4 =
        """
        public class DeepBlocks {
        public void test() {
        {
        {
        {
        int x = asd;
        x = x + b;
        }
        }
        }
        }
        }
        """;

    // a method with a little bit of code inside
    String source5 =
        """
        public class Objects {
        public void test() {
        SpriteBatch sb = new SpriteBatch();
        MainController mc = new MainController(a, b, c);
        char ch = new Character('a');
        String s = "hello" + "world";
        }
        }
        """;

    Scanner sc = new Scanner(System.in);
    int indentation;
    while (true) {
      System.out.print("Enter indentation width (int): ");

      if (sc.hasNextInt()) {
        indentation = sc.nextInt();
        break;
      } else {
        System.out.println("Please enter a valid integer!");
        sc.next(); // wrong input
      }
    }

    IO.println("Result from source1:\n\"\"\"\n" + pretty(source1, indentation) + "\n\"\"\"\n\n\n");
    IO.println("Result from source2:\n\"\"\"\n" + pretty(source2, indentation) + "\n\"\"\"\n\n\n");
    IO.println("Result from source3:\n\"\"\"\n" + pretty(source3, indentation) + "\n\"\"\"\n\n\n");
    IO.println("Result from source4:\n\"\"\"\n" + pretty(source4, indentation) + "\n\"\"\"\n\n\n");
    IO.println("Result from source5:\n\"\"\"\n" + pretty(source5, indentation) + "\n\"\"\"\n\n\n");
  }

  private static String pretty(String source, int indentations) {
    MiniJavaLexer lexer = new MiniJavaLexer(CharStreams.fromString(source));
    MiniJavaParser parser = new MiniJavaParser(new CommonTokenStream(lexer));
    MiniJavaParser.CompilationUnitContext tree = parser.compilationUnit();
    PrettyPrinterVisitor visitor = new PrettyPrinterVisitor(indentations);
    visitor.visit(tree);
    return visitor.result();
  }
}
