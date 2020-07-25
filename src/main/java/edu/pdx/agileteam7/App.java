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
import java.io.File;

/**
 * Hello world!
 *
 */
public class App 
{

    public static String AWS_ACCESS_KEYS = "";
    public static String AWS_SECRET_KEYS = "";
    public static Bucket currentBucket;
    public static String name = new String();


    public static void main(String[] args) {
        final String USAGE = "\n" +
                "Commands \n" +
                "q: to quit\n" +
                "cb: to create new bucket\n" +
                "gb: to get a bucket\n" +
                "list: list dictionaries and files in local machine\n" +
                "rename: rename file in local machine\n";

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
                } else if(newestCommand.equals("list")) {
                    System.out.print("Please enter the OS you're using: ");
                    String os = myObj.nextLine();
                    System.out.print("Please enter the name of the user: ");
                    String name = myObj.nextLine();
                    System.out.print("Are there other directories that you would like to add to the path? ");
                    String option = myObj.nextLine();
                    String path = new String();
                    if(option.substring(0,1).toUpperCase().equals("Y")) {
                        System.out.print("Please enter the name of the directory" +
                                "(if it's more than one directories ahead, please put a '/' afterwards): ");
                        path = myObj.nextLine();
                    } else if(option.substring(0,1).toUpperCase().equals("N")) {
                        path = "";
                    } else {
                        System.out.println("Please enter a valid answer.");
                    }

                    System.out.print("Would you like to have all files or directories printed? ");
                    option = myObj.nextLine();
                    if(option.toUpperCase().equals("FILES")) {
                        FileHandling.fileList(os, name, path);
                    } else {
                        FileHandling.dirList(os, name, path);
                    }
                } else if(newestCommand.equals("rename")) {
                    String path = new String();
                    System.out.print("Please enter the OS you're using: ");
                    String os = myObj.nextLine();
                    System.out.print("Please enter the name of the user: ");
                    String name = myObj.nextLine();
                    System.out.print("Are there other directories that you would like to add to the path? ");
                    String option = myObj.nextLine();
                    if(option.substring(0,1).toUpperCase().equals("Y")) {
                        System.out.print("Please enter the name of the directory" +
                                "(if it's more than one directories ahead, please put a '/' afterwards): ");
                        path = myObj.nextLine();
                    } else if(option.substring(0,1).toUpperCase().equals("N")) {
                        path = "";
                    } else {
                        System.out.println("Please enter a valid answer");
                    }

                    System.out.print("Please enter the file name: ");
                    String old = myObj.nextLine();

                    System.out.print("Please enter the new name: ");
                    String newName = myObj.nextLine();
                    FileHandling.rename(old, newName, os, name, path);

                }else {
                    System.out.println("Please enter a valid command");
                }
            } catch (Exception a) {
                System.out.println("Please enter a valid command");
            }
        }

    }

}
