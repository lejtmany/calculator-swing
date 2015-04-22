

import com.lejtman.MathExpressionParser;
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


public class MathExpressionParserTests {
    
    @Test
    public void parseExpressionTest(){
        String expr = "43 + 21 - 6 * 2 + 4";
        double result = MathExpressionParser.parse(expr);
        Assert.assertEquals((double) 56, result);
    }
    
    @Test
    public void parseExpressionTestWithNegative(){
        String expr = "43 + 21 - 6 *" + MathExpressionParser.NEGATIVE_SIGN+ "2 + 4";
        double result = MathExpressionParser.parse(expr);
        Assert.assertEquals((double) 80, result);
    }

}
