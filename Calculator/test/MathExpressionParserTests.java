

import com.lejtman.MathExpressionParser;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


public class MathExpressionParserTests {
    
    @Test
    public void testParseExpression(){
        String expr = "43 + 21 - 6 * 2 + 4";
        double result = MathExpressionParser.parse(expr);
        Assert.assertEquals((double) 56, result);
    }
    
    @Test
    public void testParseExpressionWithNegative(){
        String expr = "43 + 21 - 6 *" + MathExpressionParser.NEGATIVE_SIGN+ "2 + 4";
        double result = MathExpressionParser.parse(expr);
        Assert.assertEquals((double) 80, result);
    }
    
    @Test
    public void parseExpressionWithReciprocal(){
        String expr = "43 + 21 - 6 *" + MathExpressionParser.reciproc(2)+ " + 4";
        double result = MathExpressionParser.parse(expr);
        Assert.assertEquals((double) 65, result);
    }
    
    @Test
    public void testMatcher(){
//        System.out.println(Pattern.matches("-?\\d+(\\.\\d+)?", "reciproc(2.00000000000)"));
//        Pattern p = Pattern.compile("-?\\d+(\\.\\d+)?");
//        Matcher m = p.matcher("reciproc(2.00000000000)");
//        System.out.println(Double.parseDouble(m.group()));        
         Pattern p = Pattern.compile("-?\\d+(\\.\\d+)?");
    String text = "reciproc(2.00000000000)";
    Matcher m = p.matcher(text);
    if(m.find())
    {
        System.out.println(Double.parseDouble(text.substring(m.start(), m.end())));
    }
    }

}
