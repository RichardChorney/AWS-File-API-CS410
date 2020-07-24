package edu.pdx.agileteam7;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class UploadObject {
    public String AccessKey;
    public String SecretKey;
    public String Bucket;


    public UploadObject(String key,String secretkey,String BucketName) {
        this.AccessKey=key;
        this.SecretKey=secretkey;
        this.Bucket=BucketName;
    }

    public void AddToBucket()throws IOException{

        String FileName ="";
        String FilePath = "";

        try {

            BasicAWSCredentials awsCreds = new BasicAWSCredentials(this.AccessKey, this.SecretKey);
            AmazonS3Client s3 = new AmazonS3Client(awsCreds);

            //Example of how FilePath should look is below
            //"src/main/test.txt"

            //String newestCommand = "";
            Scanner myObj = new Scanner(System.in);


            System.out.println("Please enter filename should look some thing like \n" +
                    "text.txt ");
            FileName = myObj.nextLine();

            System.out.println("Please enter filepath, should look some thing like \n" +
                    "src/main/test.txt ");
            FilePath = myObj.nextLine();

            s3.putObject(this.Bucket, FileName, new File(FilePath));

        } catch (SdkClientException e) {
            e.printStackTrace();
        }
    }

    public void AddMultToBucket(int count){
        String FileName ="";
        String FilePath = "";

        BasicAWSCredentials awsCreds = new BasicAWSCredentials(this.AccessKey, this.SecretKey);
        AmazonS3Client s3 = new AmazonS3Client(awsCreds);

        //String newestCommand = "";
        Scanner myObj = new Scanner(System.in);

        int i;
        for(i=0;i<count;i++){

            System.out.println("Please enter filename should look something like \n" +
                    "text.txt, for file number: " + i);
            FileName = myObj.nextLine();

            System.out.println("Please enter filepath, should look something like \n" +
                    "src/main/test.txt, for file number: "+ i);
            FilePath = myObj.nextLine();

            s3.putObject(this.Bucket,FileName,new File(FilePath));


        }
    }












}



