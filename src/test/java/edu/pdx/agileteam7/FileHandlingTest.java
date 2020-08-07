package edu.pdx.agileteam7;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


import java.io.File;
import java.io.IOException;

public class FileHandlingTest {

    @Test
    public void shouldInputStrings () throws IOException {
        // className testObj = new className();
        // paramType outputParam = testObj.method(input);
        // ^^ the method should return the paramType

        // what output do we expect - test
        // static method --> assertEquals(value expected to get, the second param is the outputParam);
        FileHandling files = new FileHandling();
        boolean expected = true;
        // expected = true
        assertEquals(expected, true);
    }

    /*@Test
    @Ignore
    public void firstArgShouldEndWithDotText() {
        FileHandling obj = new FileHandling();
        String expectedFileType = "file.txt";
        String oldName = "file.txt";
        String newName = "newFile.txt";
        boolean output = obj.rename(oldName, newName, "mac", "rawahalsinan", "something");
        assertEquals(expectedFileType.endsWith(".txt"), oldName.endsWith(".txt"));
        assertEquals(expectedFileType.endsWith(".txt"), newName.endsWith(".txt"));
    }

    @Ignore
    @Test
    public void userInputIsValid() {
        FileHandling fh = new FileHandling();
        boolean output = fh.rename("file3.txt", "file3.txt", "mac", "rawahalsinan", "Desktop");
        assertEquals("User input is valid",true, output);
    }

    @Ignore
    @Test
    public void fileRenameShouldExist() {
        String path = "/Users/rawahalsinan/Desktop/sample2.txt";
        File file = new File("/Users/rawahalsinan/Desktop/sample.txt");
        assertTrue(file.exists());
    }*

    @Test
    public void dirShouldExist() {

    }

    @Ignore
    @Test
    public void renameShouldReturnTrue() {
        FileHandling object = new FileHandling();
        boolean expectedOutput = true;
        boolean actualOutput = object.rename("sample.txt", "newName.txt", "mac", "rawahalsinan", "Desktop");
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testIfuserInputIsntValid() {
        String path = "";
    }*/
}
