package edu.psuti.toi.generator;

import edu.psuti.toi.generator.utils.ConfigurationUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ConfigurationUtilsTest {

    @Test
    public void testMatrixValidationPositive3x3() {
        float[][] testUtilMatrix1 = new float[][]{
                new float[]{0.3f, 0.4f, 0.3f},
                new float[]{0.5f, 0.3f, 0.2f},
                new float[]{0.2f, 0.3f, 0.5f}};

        boolean check = ConfigurationUtils.validateProbabilityMatrix(testUtilMatrix1);
        System.out.println("Test validation matrix - Positive: " + check);

        Assert.assertTrue(check);
    }

    @Test
    public void testMatrixValidationAll01Negative() {

        float[][] testUtilMatrix2 = new float[][]{
                new float[]{0.1f, 0.1f, 0.1f},
                new float[]{0.1f, 0.1f, 0.1f},
                new float[]{0.1f, 0.1f, 0.1f}};

        boolean check = ConfigurationUtils.validateProbabilityMatrix(testUtilMatrix2);
        System.out.println("Test validation matrix with all 0.1f: " + check);

        Assert.assertTrue(!check);
    }

    @Test
    public void testGetSymbolsWithNewLine() {
        String strWithNewLine = "\n2,1,3";
        char[] expected = {'2', '1', '3'};
        char[] actual = ConfigurationUtils.getSymbols(strWithNewLine);

        Assert.assertEquals(actual, expected);
    }

    @Test(
            expectedExceptions = {IllegalArgumentException.class}
    )
    public void testGetSymbolsEmptyStr() {
        String parsedStr = "";
        ConfigurationUtils.getSymbols(parsedStr);
    }


    @Test(
            expectedExceptions = {IllegalArgumentException.class}
    )
    public void testGetSymbolsFromNotSeparatedString() {
        String parsedStr = "asdasd";
        ConfigurationUtils.getSymbols(parsedStr);
    }

}
