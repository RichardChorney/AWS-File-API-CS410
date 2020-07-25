package edu.pdx.agileteam7;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.devicefarm.model.ArgumentException;
import com.amazonaws.services.elasticbeanstalk.model.SystemStatus;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagement;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClientBuilder;
import com.amazonaws.services.identitymanagement.model.ListAccessKeysRequest;
import com.amazonaws.services.identitymanagement.model.ListAccessKeysResult;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;

import javax.sound.midi.SysexMessage;
import javax.swing.plaf.synth.SynthTextAreaUI;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;


/**
 * Hello world!
 *
 */
public class App 
{

    public static String AWS_ACCESS_KEYS = "";
    public static String AWS_SECRET_KEYS = "";
    public static Bucket currentBucket;



    public static void main(String[] args) {
        final String USAGE = "\n" +
                "Commands \n" +
                "q: to quit\n" +
                "cb: to create new bucket\n" +
                "gb: to get a bucket\n" +
                "mkdir: make directory\n" +
                "cp: copy directory\n" +
                "adfl: add 1 file to bucket\n"+
                "adMfl: adds mult. files\n";

        // Asks for user input
        String newestCommand = "";
        Scanner myObj = new Scanner(System.in);

        System.out.println("Please enter access key: ");
        AWS_ACCESS_KEYS = myObj.nextLine();
//        AWS_ACCESS_KEYS = "AKIATB55VFIM6ETVL7AA";
        System.out.println("Please enter secret key: ");
        AWS_SECRET_KEYS = myObj.nextLine();
//        AWS_SECRET_KEYS = "wPVnQ4S5RUuoZoZTOhFrOZnwyUu830/hck04oqD4";

        // Checks for valid AWS credentials
        if(!validateCredentials()){
            System.out.println("Login Failed: Please enter valid Access and Secret Keys");
            System.exit(1);
        }

        int callCounts = 0;

        // Main driver that checks for user commands
        while(true) {

            callCounts++;

            // Exit after 25 calls
            if(callCounts == 25) {
                return;
            }
            try {
                System.out.println(USAGE);
                newestCommand = myObj.nextLine();
                System.out.println(newestCommand);
                if (newestCommand.equals("q")) {
                    System.out.println("Goodbye");
                    break;
                } else if (newestCommand.equals("cb")) {
                    System.out.println("Please enter a bucket name to create: ");
                    String bucketName = myObj.nextLine();
                    currentBucket = Buckets.createBucket(bucketName);
                } else if (newestCommand.equals("gb")) {
                    System.out.println("Please enter a bucket name to get: ");
                    String bucketName = myObj.nextLine();
                    currentBucket = Buckets.getBucket(bucketName);
                    Buckets.listObjects(bucketName);
                } else if (newestCommand.equals("mkdir")) {
                    //User Input
                    System.out.println("Please enter bucket: ");
                    String bucketName = myObj.nextLine();
                    System.out.println("Please enter directory (must end with / ): ");
                    String directoryName = myObj.nextLine();
                    //Calls directory creation function
                    boolean success = Directory.mkdir(bucketName,directoryName);
                    //Barks failure on function returning an error
                    if(!success){
                        System.out.println("Directory creation failed.");
                    }
                } else if (newestCommand.equals("cp")) {
                    //User Input
                    System.out.println("Please enter source bucket name: ");
                    String sourceName = myObj.nextLine();
                    System.out.println("Please enter source directory (must end with / ): ");
                    String sourceDirectory = myObj.nextLine();
                    System.out.println("Please enter target bucket name: ");
                    String targetName = myObj.nextLine();
                    System.out.println("Please enter target directory (must end with / ): ");
                    String targetDirectory = myObj.nextLine();
                    //Calls directory copying function
                    boolean success = Directory.cp(sourceName,sourceDirectory,targetName, targetDirectory);
                    //Barks failure on cp returning an error
                    if(!success){
                        System.out.println("Directory copy failed.");
                    }
                } else if (newestCommand.equals("adfl")){
                    System.out.println("Please enter bucket name we are working with: ");
                    String BucketNAME = myObj.nextLine();
                    UploadObject object = new UploadObject(AWS_ACCESS_KEYS, AWS_SECRET_KEYS,BucketNAME);

                    object.AddToBucket();


                }else if(newestCommand.equals("adMfl")) {

                    System.out.println("Please enter bucket name we are working with: ");
                    String BucketNAME = myObj.nextLine();
                    UploadObject object = new UploadObject(AWS_ACCESS_KEYS, AWS_SECRET_KEYS,BucketNAME);
                    System.out.println("Enter how many files you want to upload");
                    int num = myObj.nextInt();

                    object.AddMultToBucket(num);

                }else {
                    System.out.println("Please enter a valid command");
                }
                // If current bucket successfully created or retrieved.
                if(currentBucket != null){
                    while(true){
                        System.out.println("\nEnter one of the following commands.\n" +
                                "b: return to the previous menu\n" +
                                "ls: list the objects in the current bucket\n" +
                                "go: Downloads the object to current directory\n" +
                                "gm: Downloads multiple objects\n");
                        newestCommand = myObj.nextLine();
                        if (newestCommand.equals("b")) {
                            break;
                        }
                        else if(newestCommand.equals("ls")){
                            Buckets.listObjects(currentBucket.getName());
                        }
                        else if(newestCommand.equals("go")){
                            System.out.println("Please enter the name of the object");
                            String objectName = myObj.nextLine();
                            if(getObject(objectName, currentBucket.getName())){
                                System.out.println("Object downloaded.");
                            }
                            else{
                                System.out.println("Download failed");
                            }
                        }
                        else if (newestCommand.equals("gm")){
                            int numObjects = 0;
                            System.out.println("Enter the number of objects to download");
                            try{
                                numObjects = Integer.parseInt(myObj.nextLine());
                                if(numObjects <= 5){
                                    for(int i = 0; i < numObjects; i++){
                                        System.out.println("Please enter the name of the object");
                                        String objectName = myObj.nextLine();
                                        if(getObject(objectName, currentBucket.getName())){
                                            System.out.println("Object downloaded.");
                                        }
                                        else{
                                            System.out.println("Download failed");
                                        }
                                    }
                                }
                                else{
                                    System.out.println("Max number of downloads is 5");
                                }
                            } catch (NumberFormatException e){
                                System.out.println("Invalid number. Please enter a whole number <= 5");
                            }
                        }
                        else{
                            System.out.println("Please enter a valid command");
                        }
                    }
                }
            } catch (Exception a) {
                System.out.println("Please enter a valid command");
            }
        }
    }

    public static void listObjects(String bucketName){
        System.out.format("Objects in bucket %s:\n", bucketName);
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(App.AWS_ACCESS_KEYS, App.AWS_SECRET_KEYS);
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCreds)).withRegion(Regions.US_EAST_1).build();
        try {
            //AmazonS3Client s3 = new AmazonS3Client(awsCreds);

            ListObjectsV2Result result = s3.listObjectsV2(bucketName);
            List<S3ObjectSummary> objects = result.getObjectSummaries();
            for (S3ObjectSummary os : objects) {
                System.out.println("* " + os.getKey());
            }
        }
        catch (Exception e){
            throw new ArgumentException("ERROR");
        }
    }

    public static boolean validateCredentials(){
        try {
            BasicAWSCredentials awsCreds = new BasicAWSCredentials(App.AWS_ACCESS_KEYS, App.AWS_SECRET_KEYS);
            final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCreds)).withRegion(Regions.US_EAST_1).build();
            List<Bucket> buckets = s3.listBuckets();
            System.out.println("Your Amazon S3 buckets are:");
            for (Bucket b : buckets) {
                System.out.println("* " + b.getName());
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean getObject(String objectName, String bucketName) {
        System.out.format("Downloading %s from S3 bucket %s...\n", objectName, bucketName);
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(App.AWS_ACCESS_KEYS, App.AWS_SECRET_KEYS);
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCreds)).withRegion(Regions.US_EAST_1).build();
        try {
            S3Object o = s3.getObject(bucketName, objectName);
            S3ObjectInputStream s3is = o.getObjectContent();
            FileOutputStream fos = new FileOutputStream(new File(objectName));
            byte[] read_buf = new byte[1024];
            int read_len = 0;
            while ((read_len = s3is.read(read_buf)) > 0) {
                fos.write(read_buf, 0, read_len);
            }
            s3is.close();
            fos.close();
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            return false;
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }
}


