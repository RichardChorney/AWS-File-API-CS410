package edu.pdx.agileteam7;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.xspec.S;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.io.BufferedReader;
import java.io.FileReader;
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
            e.printStackTrace();
        }
    }

    /**
     * a method that deletes a file from the current bucket
     */
    public void deleteFileFromBucket() {
            BasicAWSCredentials awsCreds = new BasicAWSCredentials(this.AccessKey, this.SecretKey);
            AmazonS3Client s3 = new AmazonS3Client(awsCreds);
            String bucket_name = this.Bucket;
            Scanner myObj = new Scanner(System.in);
            System.out.print("Please enter the name of the file: (i.e. file.txt)");
            String object_key = myObj.nextLine();
            try {
                s3.deleteObject(bucket_name, object_key);
            } catch(AmazonServiceException e) {
                System.err.println(e.getErrorMessage());
                System.exit(1);
            }
        /*try {
            BasicAWSCredentials awsCreds = new BasicAWSCredentials(this.AccessKey, this.SecretKey);
            AmazonS3Client s3 = new AmazonS3Client(awsCreds);
            Scanner myObj = new Scanner(System.in);
            System.out.println("Please enter the path of the file i.e. src/main/test.txt \n");
            FilePath = myObj.nextLine();
            s3.deleteObject(this.Bucket, FilePath);

        } catch(Exception e) {e.printStackTrace();}*/
    }

    /**
     * a method that deletes folder/directories from bucket
     * */
    public void deleteFolderFromBucket() {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(this.AccessKey, this.SecretKey);
        AmazonS3Client s3 = new AmazonS3Client(awsCreds);
        String bucket_name = this.Bucket;
        Scanner myObj = new Scanner(System.in);
        System.out.print("Please enter the name of the folder: ");
        String object_keys = myObj.nextLine();
        ObjectListing objects = s3.listObjects(bucket_name, object_keys);
        for(S3ObjectSummary os : objects.getObjectSummaries()) {
            try {
                s3.deleteObject(bucket_name, os.getKey());
            } catch(AmazonServiceException e) {System.err.print(e.getErrorMessage());}
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
            p.printStackTrace();
        }
    }

    /**
     * a method to rename a file in a bucket
     */
    public void renameFile() {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(this.AccessKey, this.SecretKey);
        AmazonS3Client s3 = new AmazonS3Client(awsCreds);
        Scanner myObj = new Scanner(System.in);
        System.out.println("Please enter the name of the file to rename: ");
        String oldKey = myObj.nextLine();
        System.out.println("Please enter the new name of the file: ");
        String newKey = myObj.nextLine();
        s3.copyObject(this.Bucket, oldKey, this.Bucket, newKey);
        s3.deleteObject(this.Bucket, oldKey);
    }
}






