package edu.psuti.toi.generator.cli;

import edu.psuti.toi.generator.Configuration;
import edu.psuti.toi.generator.Generator;
import edu.psuti.toi.generator.utils.ConfigurationUtils;
import edu.psuti.toi.generator.utils.IOUtils;

import java.nio.charset.Charset;

public class Console {
    public static void main(String[] args) {
        final byte ARGS_SIZE = 4;
        //todo: add more catch blocks for better err msgs
        final byte errCode;
        try {
            if (args.length == ARGS_SIZE) {
                char[] chars  = ConfigurationUtils
                        .getSymbols(IOUtils.readFile(args[0], Charset.defaultCharset()));

                float[][] probMatrix = IOUtils.parseMatrix(IOUtils.readFile(
                                args[1]
                                , Charset.defaultCharset())
                        , chars.length
                );

                Generator generator = new Generator(new Configuration(chars,probMatrix));
                StringBuilder resBuilder = new StringBuilder();
                IOUtils.outputResults(
                        generator.getCfg()
                        ,generator.getRandomText(Integer.parseInt(args[2]))
                        ,args[3]
                        ,resBuilder
                );
                System.out.print(resBuilder.toString());
            } else throw new IllegalArgumentException();
        } catch (Exception ex){
            printHelp();
        }
    }

    public static void printHelp(){
        System.out.println(
                "Usage: generator [chars.txt] [matrix.txt] [sequence_size] [out.txt]\r\n" +
                        "chars.txt: file with N comma separated chars\r\n" +
                        "matrix.txt: probability matrix NxN or vector N. \r\n\t" +
                        "Sum of rows should be equals to 1\r\n" +
                        "sequence size: quantity of generated symbols\r\n" +
                        "out.txt: file to output generated sequence."
        );
    }
}
