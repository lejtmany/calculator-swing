
import com.lejtman.MathParser;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import junit.framework.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.*;

public class MathParserTests {

    @Test
    public void testParseExpression() {
        String expr = "43 + 21 - 6 * 2 + 4";
        double result = MathParser.parse(expr);
        Assert.assertEquals((double) 56, result);
    }

    @Test
    public void testParseExpressionWithNegative() {
        String expr = "43 + 21 - 6 * (-2) + 4";
        double result = MathParser.parse(expr);
        Assert.assertEquals((double) 80, result);
    }

    @Test
    public void parseExpressionWithReciprocal() {
        String expr = "43 + 21 - 6 *" + MathParser.reciproc(2) + " + 4";
        double result = MathParser.parse(expr);
        Assert.assertEquals((double) 65, result);
    }

    @Test
    public void testMatcher() {
        Pattern p = Pattern.compile("-?\\d+(\\.\\d+)?");
        String text = "reciproc(2.00000000000)";
        Matcher m = p.matcher(text);
        if (m.find())
            System.out.println(Double.parseDouble(text.substring(m.start(), m.end())));
    }

    @Test
    public void testEmptyRecipStringThrowsError() {
        assertThatThrownBy(() -> {
            MathParser.parse(MathParser.reciproc("  "));
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testFullRecipStringFollowedByNumberThrowsError() {
        assertThatThrownBy(() -> {
            MathParser.parse(MathParser.reciproc("88") + "9");
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testTwoOperatorsInARowThrowsError() {
        assertThatThrownBy(() -> {
            MathParser.parse("9+ -4");
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testTwoNumbersWithSpaceThrowsError() {
        assertThatThrownBy(() -> {
            MathParser.parse("9 4");
        }).isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    public void testParenthesesRegex() {
        String testString = "(-76)/45+(-9)+6-90+(-89)";
        System.out.println(Arrays.toString(testString.split("((?<!\\(-)(?<=[*+/-])|(?=[*+/-]))(?<!\\()")));
    }
    }
        

