package edu.pdx.agileteam7;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;

import java.io.IOException;
import java.io.File;
import java.util.Scanner;


public class UploadObject {
    public String AccessKey;
    public String SecretKey;
    public String Bucket;
    public static String FilePath = "";
    public static String FileName = "";


    public UploadObject(String key, String secretkey, String BucketName) {
        this.AccessKey = key;
        this.SecretKey = secretkey;
        this.Bucket = BucketName;
    }

    public void AddToBucket(String Filename, String FilePath) {

        try {
            BasicAWSCredentials awsCreds = new BasicAWSCredentials(this.AccessKey, this.SecretKey);
            AmazonS3Client s3 = new AmazonS3Client(awsCreds);

            //Example of how FilePath should look is below
            //"src/main/test.txt"

            s3.putObject(this.Bucket, Filename, new File(FilePath));

        } catch (SdkClientException e) {
            //System.err.println("ISSUE WITH ARGS");
            e.printStackTrace();
        }
    }

    public void AddToBucket() {
        try {
            BasicAWSCredentials awsCreds = new BasicAWSCredentials(this.AccessKey, this.SecretKey);
            AmazonS3Client s3 = new AmazonS3Client(awsCreds);

            //Example of how FilePath should look is below
            //"src/main/test.txt"

            Scanner myObj = new Scanner(System.in);

            System.out.println("Please enter filename should look something like \n" +
                    "text.txt \n");
            FileName = myObj.nextLine();

            System.out.println("Please enter filepath, should look something like \n" +
                    "src/main/test.txt\n");
            FilePath = myObj.nextLine();

            s3.putObject(this.Bucket, FileName, new File(FilePath));

        } catch (Exception e) {
            System.err.println("Unable to upload file. Received the following error: " + e.getMessage());
        }
    }

    public void AddMultToBucket(int count) {
        String FileName = "";
        String FilePath = "";

        try {
            BasicAWSCredentials awsCreds = new BasicAWSCredentials(this.AccessKey, this.SecretKey);
            AmazonS3Client s3 = new AmazonS3Client(awsCreds);

            int i;
            for (i = 0; i < count; i++) {
                Scanner myObj = new Scanner(System.in);
                System.out.println("Please enter filename should look something like \n" +
                        "text.txt  ");
                FileName = myObj.nextLine();

                System.out.println("Please enter filepath, should look something like \n" +
                        "src/main/test.txt  ");
                FilePath = myObj.nextLine();

                s3.putObject(this.Bucket, FileName, new File(FilePath));
            }

        } catch (SdkClientException p) {
            System.err.println("Unable to upload file. Received the following error: " + p.getMessage());
        }
    }
}







