package edu.pdx.agileteam7;

import com.amazonaws.services.dynamodbv2.xspec.S;
import org.junit.Test;

import java.io.IOException;

public class uploadtest {

   @Test
    public void testone() throws IOException {
      System.out.println("Test");

    UploadObject object = new UploadObject("AKIATB55VFIMQXG64JQB","sIzFarHjMx73rqRDGyTMj5tIFIx6CzDOseyNPNXY","georgekingston0711");

    object.AddToBucket();
   //"text.txt","src/main/test.txt");



   }





}
