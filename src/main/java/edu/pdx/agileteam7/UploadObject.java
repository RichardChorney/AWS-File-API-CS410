package edu.pdx.agileteam7;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.IOException;

public class UploadObject {

    String bucketName = "georgekingston0711";
    String key ="";
    String secretkey= "";

    public UploadObject(String bucketName) {


        try {

            BasicAWSCredentials awsCreds = new BasicAWSCredentials("AKIATB55VFIMQXG64JQB", "sIzFarHjMx73rqRDGyTMj5tIFIx6CzDOseyNPNXY");
            AmazonS3Client s3 = new AmazonS3Client(awsCreds);


            s3.putObject(bucketName, "test.txt", new File("/Users/georgekingston/agile/agileFTP/src/main/test.txt"));

        } catch (AmazonServiceException e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        }
    }


    public static void uploadfile() throws IOException {


    }
}

