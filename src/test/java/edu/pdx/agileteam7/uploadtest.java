package edu.pdx.agileteam7;

import java.util.Scanner;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.dynamodbv2.xspec.S;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

public class uploadtest {

    @Test
    public void testone() throws IOException {

        UploadObject object = new UploadObject("AKIATB55VFIMQXG64JQB", "sIzFarHjMx73rqRDGyTMj5tIFIx6CzDOseyNPNXY", "georgekingston0711");

        object.AddToBucket("test.txt", "src/main/test.txt");
        //"text.txt","src/main/test.txt");

    }

    @Ignore
    @Test
    public void testformethodoverloading() {

        UploadObject object = new UploadObject("AKIATB55VFIMQXG64JQB", "sIzFarHjMx73rqRDGyTMj5tIFIx6CzDOseyNPNXY", "georgekingston0711");

        object.AddToBucket();
    }

    @Ignore
    @Test
    public void TestUploadSingleShouldGetError() throws IOException {

        UploadObject object = new UploadObject("AKIATB55VFIMQXG64JQB", "sIzFarHjMx73rqRDGyTMj5tIFIx6CzDOseyNPNXY", "georgekingston0711");

        object.AddToBucket("testDifName.txt", "src/main/test.txt");
    }

    @Ignore
    @Test
    public void TestMultiFiles() {

        UploadObject object = new UploadObject("AKIATB55VFIMQXG64JQB", "sIzFarHjMx73rqRDGyTMj5tIFIx6CzDOseyNPNXY", "georgekingston0711");
        Scanner myObj = new Scanner(System.in);
        System.out.println("Please enter number of files to enter ");
        int num = myObj.nextInt();
        object.AddMultToBucket(num);
    }

    @Ignore
    @Test
    public void fileShouldExist() {
        UploadObject object = new UploadObject("AKIATB55VFIMT42KPLOC", "PSCgghKEugSrwEQqaIPPKXwtnb4NmLs9Kdec0LkD", "rawags");
        Scanner obj = new Scanner(System.in);
        System.out.print("Please enter the name of the file to delete: ");
        String name = obj.nextLine();
        object.deleteFileFromBucket();
    }

}
