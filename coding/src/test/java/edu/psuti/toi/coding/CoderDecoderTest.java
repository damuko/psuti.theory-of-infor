package edu.psuti.toi.coding;

import edu.psuti.toi.decoding.HuffmanDecoder;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Test
public class CoderDecoderTest {

    @Test
    public void testCodeEncode() {
        final String encodedStr = "aaabbccca";

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        HuffmanEncoder.encode(baos, encodedStr);

        byte[] encodedBytes = baos.toByteArray();

        ByteArrayInputStream bais = new ByteArrayInputStream(encodedBytes);
        ByteArrayOutputStream resultBao = new ByteArrayOutputStream();
        HuffmanDecoder.decode(bais, resultBao);

        String decodedStr = new String(resultBao.toByteArray());

        Assert.assertEquals(decodedStr, encodedStr);
    }
}
