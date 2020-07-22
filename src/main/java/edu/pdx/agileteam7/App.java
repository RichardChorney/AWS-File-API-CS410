package edu.pdx.agileteam7;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.elasticbeanstalk.model.SystemStatus;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;

import javax.sound.midi.SysexMessage;
import javax.swing.plaf.synth.SynthTextAreaUI;
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
                "cp: copy directory\n";

        // Asks for user input
        String newestCommand = "";
        Scanner myObj = new Scanner(System.in);

        System.out.println("Please enter access key: ");
        AWS_ACCESS_KEYS = myObj.nextLine();

        System.out.println("Please enter secret key: ");
        AWS_SECRET_KEYS = myObj.nextLine();

        // Checks for valid AWS credentials
        try {
            BasicAWSCredentials awsCreds = new BasicAWSCredentials(App.AWS_ACCESS_KEYS, App.AWS_SECRET_KEYS);
            System.out.println("entering credentials check ");
        } catch (Exception e) {
            System.out.println("Login Failed: Please enter valid Access and Secret Keys");
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
                } else if (newestCommand.equals("mkdir")) {
                    System.out.println("Please enter bucket: ");
                    String bucketName = myObj.nextLine();
                    System.out.println("Please enter directory: ");
                    String directoryName = myObj.nextLine();
                    Directory.mkdir(bucketName,directoryName);
                    //call make directory function with directoryName input string;
                } else if (newestCommand.equals("cp")) {
                    System.out.println("Please enter source bucket name: ");
                    String sourceName = myObj.nextLine();
                    System.out.println("Please enter source bucket directory: ");
                    String sourceDirectory = myObj.nextLine();
                    System.out.println("Please enter target bucket name: ");
                    String targetName = myObj.nextLine();
                    System.out.println("Please enter target bucket directory: ");
                    String targetDirectory = myObj.nextLine();
                    Directory.cp(sourceName,sourceDirectory,targetName, targetDirectory);
                    //call copy directory function with input strings;
                } else {
                    System.out.println("Please enter a valid command");
                }
            } catch (Exception a) {
                System.out.println("Please enter a valid command");
            }
        }

    }

}
