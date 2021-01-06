import org.junit.Test;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.SpelCompilerMode;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public class Test2 {
    @Test
    public void test(){
        SpelParserConfiguration config = new SpelParserConfiguration(SpelCompilerMode.IMMEDIATE,
                this.getClass().getClassLoader());

        SpelExpressionParser parser = new SpelExpressionParser(config);

        Expression expr = parser.parseExpression("name");

        MyMessage message = new MyMessage();
        message.setName("dfd");
        Object payload = expr.getValue(message);
        System.out.println(message.getName());
    }
   @Test
    public void mixText(){
        SpelExpressionParser parser = new SpelExpressionParser();
        String randomPhrase = parser.parseExpression(
                "random number is #{T(java.lang.Math).random()}",
                new TemplateParserContext()).getValue(String.class);
        System.out.println(randomPhrase);
    }

     class MyMessage{
      private String name;

         public String getName() {
             return name;
         }

         public void setName(String name) {
             this.name = name;
         }
     }
}
