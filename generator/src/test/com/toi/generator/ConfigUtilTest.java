package com.toi.generator;

import com.toi.generator.ConfigUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ConfigUtilTest {

    @Test
    public void testMatrixValidationPositive3x3(){
        float[][] testUtilMatrix1 = new float[][] {
                new float[] {0.3f, 0.4f, 0.3f},
                new float[] {0.5f, 0.3f, 0.2f},
                new float[] {0.2f, 0.3f, 0.5f}};

        boolean check = ConfigUtil.checkMatrixProb(testUtilMatrix1);
        System.out.println("Test validation matrix - Positive: " + check);

        Assert.assertTrue(check);
    }

    @Test
    public void testMatrixValidationAll01Negative(){

        float[][] testUtilMatrix2 = new float[][] {
                new float[] {0.1f, 0.1f, 0.1f},
                new float[] {0.1f, 0.1f, 0.1f},
                new float[] {0.1f, 0.1f, 0.1f}};

        boolean check = ConfigUtil.checkMatrixProb(testUtilMatrix2);
        System.out.println("Test validation matrix with all 0.1f: " + check);

        Assert.assertTrue(!check);
    }

    @Test
    public void testMatrixValidationErrInColumns(){

        float[][] testUtilMatrix3 = new float[][] {
                new float[] {0.1f, 0.2f, 0.7f},
                new float[] {0.4f, 0.5f, 0.1f},
                new float[] {0.3f,0.3f, 0.4f}};

        boolean check = ConfigUtil.checkMatrixProb(testUtilMatrix3);
        System.out.println("Test matrix validation with incorrect sums in collumns: " + check);

        Assert.assertTrue(!check);
    }

//    TODO: add tests for getSymbolsFromString method
}
