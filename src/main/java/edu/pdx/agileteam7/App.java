package edu.pdx.agileteam7;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.devicefarm.model.ArgumentException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.transfer.MultipleFileDownload;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
//import com.sun.tools.javac.comp.Env;
import java.io.*;
import javax.sound.midi.SysexMessage;
import javax.swing.plaf.synth.SynthTextAreaUI;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.Scanner;

/**
 *
 */
public class App {
    public static String AWS_ACCESS_KEYS = "";
    public static String AWS_SECRET_KEYS = "";
    public static Bucket CURRENT_BUCKET;
    public static AmazonS3 S3;

    public static void main(String[] args) {
        final String Main_Menu = "\n" +
                "You are in the main menu\n" +
                "Commands \n" +
                "q: to quit\n" +
                "r: to access remote buckets, directories, files\n" +
                "l: to access local directories and files\n";

        final String Remote = "\n" +
                "You are in the remote menu\n" +
                "Commands \n" +
                "b: to go back\n" +
                "ls: list buckets\n" +
                "cb: to create new bucket\n" +
                "gb: to get a bucket\n" +
                "vgl: view logs of buckets\n" +
                "chper: change bucket permission levels\n";

        final String Local = "\n" +
                "b: to go back\n" +
                "list: list dictionaries and files in local machine\n" +
                "rename: rename file in local machine\n";

        final String bucketMenu= "\n" +
                "Please enter one of the following commands.\n" +
                "b: return to the previous menu\n" +
                "mkdir: make directory\n" +
                "cp: copy directory\n" +
                "adfl: add 1 file to bucket\n" +
                "adMfl: adds mult. files\n" +
                "ls: list the objects in the current bucket\n" +
                "go: Downloads the object to local machine\n" +
                "gm: Downloads multiple objects to local machine\n" +
                "gd: Downloads directory to local machine\n" +
                "chper: change permission for file";

        // Asks for user input
        String newestCommand = "";
        Scanner myObj = new Scanner(System.in);

        boolean credentialsUsed = false;

        File credFile = new File("credentials.txt");
        if (credFile.exists()) {
            System.out.println("Credentials located.\nDo you want to use credentials provided in 'credentials.txt'? (yes/no)");
            newestCommand = myObj.nextLine();
            if (newestCommand.toLowerCase().equals("yes")) {
                try {
                    checkForCredentials();
                    credentialsUsed = true;
                } catch (IOException e) {
                    System.out.println("Unable to retrieve credentials");
                }
            } else {
                System.out.println("Credentials file not used..");
            }
        }

        if (!credentialsUsed) {
            System.out.println("Please enter access key: ");
            AWS_ACCESS_KEYS = myObj.nextLine();
            System.out.println("Please enter secret key: ");
            AWS_SECRET_KEYS = myObj.nextLine();
        }

        // Checks for valid AWS credentials
        try {
            S3 = validateCredentials();
        } catch (Exception e) {
            System.out.println("Login Failed: Please enter valid Access and Secret Keys");
            System.exit(1);
        }

        System.out.println("Login Successful");

        int callCounts = 0;

        // Main driver that checks for user commands
        while (true) {
            callCounts++;

            // Exit after 25 calls
            if (callCounts == 25) {
                return;
            }
            try {
                System.out.println(Main_Menu);
                newestCommand = myObj.nextLine();
                if (newestCommand.equals("q")) {
                    System.out.println("Goodbye");
                    break;
                } else if (newestCommand.equals("r")) {
                    while (true) {
                        System.out.println(Remote);
                        newestCommand = myObj.nextLine();
                        boolean vglAccessed = false;
                        if (newestCommand.equals("b")) {
                            System.out.println("Returned to main menu");
                            break;
                        } else if (newestCommand.equals("ls")) {
                            listBuckets();
                        } else if (newestCommand.equals("cb")) {
                            System.out.println("Please enter a bucket name to create: ");
                            String bucketName = myObj.nextLine();
                            CURRENT_BUCKET = Buckets.createBucket(bucketName);
                        } else if (newestCommand.equals("gb")) {
                            System.out.println("Please enter a bucket name to get: ");
                            String bucketName = myObj.nextLine();
                            CURRENT_BUCKET = Buckets.getBucket(bucketName);

                        } else if (newestCommand.equals("vgl")) {

                            CURRENT_BUCKET = Buckets.getBucket("logsagile");
                            Buckets.listObjects("logsagile");
                            vglAccessed = true;
                        } else if(newestCommand.equals("chper")){
                            //User Input
                            System.out.println("Please enter the bucket name: ");
                            String targetBucket = myObj.nextLine();
                            System.out.println("Please enter AWS canonical ID: ");
                            String permissionID = myObj.nextLine();
                            System.out.println("FullControl Read ReadAcp Write WriteAcp\nPlease enter permission level: ");
                            String permissionLevel = myObj.nextLine();
                            //Calls bucket permission changing function
                            boolean success = Directory.changePermissionBucket(targetBucket,permissionID,permissionLevel);
                            //Barks failure on chper returning an error
                            if (!success) {
                                System.out.println("Permission change failed.");
                            }
                        }
                        else {
                            System.out.println("Please enter a valid command");
                            continue;
                        }

                        if (CURRENT_BUCKET != null && !vglAccessed) {
                            System.out.println("\nBucket accessed.");
                            while (true) {
                                System.out.println(bucketMenu);
                                newestCommand = myObj.nextLine();
                                if (newestCommand.equals("b")) {
                                    CURRENT_BUCKET = null;
                                    break;
                                } else if (newestCommand.equals("ls")) {
                                    Buckets.listObjects(CURRENT_BUCKET.getName());
                                } else if (newestCommand.equals("go")) {
                                    System.out.println("Please enter the name of the object");
                                    String objectName = myObj.nextLine();
                                    if (getObject(objectName, CURRENT_BUCKET.getName())) {
                                        System.out.println("Object downloaded.");
                                    } else {
                                        System.out.println("Download failed");
                                    }
                                } else if (newestCommand.equals("gm")) {
                                    int numObjects = 0;
                                    System.out.println("Enter the number of objects to download");
                                    try {
                                        numObjects = Integer.parseInt(myObj.nextLine());
                                        if (numObjects <= 5) {
                                            for (int i = 0; i < numObjects; i++) {
                                                System.out.println("Please enter the name of the object");
                                                String objectName = myObj.nextLine();
                                                if (getObject(objectName, CURRENT_BUCKET.getName())) {
                                                    System.out.println("Object downloaded.");
                                                } else {
                                                    System.out.println("Download failed");
                                                }
                                            }
                                        } else {
                                            System.out.println("Max number of downloads is 5");
                                        }
                                    } catch (NumberFormatException e) {
                                        System.out.println("Invalid number. Please enter a whole number <= 5");
                                    }
                                } else if (newestCommand.equals("gd")) {
                                    System.out.println("Please enter the name of the remote directory you want to copy(must end with / ): ");
                                    String remoteDirectory = myObj.nextLine();
                                    System.out.println("Please enter the name of the local directory you want to be copied into (must end with / ): ");
                                    String localDirectory = myObj.nextLine();
                                    try {
                                        downloadDirectory(CURRENT_BUCKET.getName(), remoteDirectory, localDirectory);
                                    } catch (Exception e) {
                                        System.err.println("Directory could not be downloaded. Received the following error: " + e.getMessage());
                                    }
                                }else if (newestCommand.equals("mkdir")) {
                                    System.out.println("Please enter directory (must end with / ): ");
                                    String directoryName = myObj.nextLine();
                                    //Calls directory creation function
                                    Directory object = new Directory(AWS_ACCESS_KEYS, AWS_SECRET_KEYS, CURRENT_BUCKET.getName());
                                    boolean success = object.mkdir(CURRENT_BUCKET.getName(), directoryName);
                                    //Barks failure on function returning an error
                                    if (!success) {
                                        System.out.println("Directory creation failed.");
                                    }
                                } else if (newestCommand.equals("cp")) {
                                    //User Input
                                    System.out.println("Please enter source directory (must end with / ): ");
                                    String sourceDirectory = myObj.nextLine();
                                    System.out.println("Please enter target bucket name: ");
                                    String targetName = myObj.nextLine();
                                    System.out.println("Please enter target directory (must end with / ): ");
                                    String targetDirectory = myObj.nextLine();
                                    //Calls directory copying function
                                    boolean success = Directory.cp(CURRENT_BUCKET.getName(), sourceDirectory, targetName, targetDirectory);
                                    //Barks failure on cp returning an error
                                    if (!success) {
                                        System.out.println("Directory copy failed.");
                                    }
                                } else if (newestCommand.equals("adfl")) {
                                    UploadObject object = new UploadObject(AWS_ACCESS_KEYS, AWS_SECRET_KEYS, CURRENT_BUCKET.getName());
                                    object.AddToBucket();

                                } else if (newestCommand.equals("adMfl")) {

                                    UploadObject object = new UploadObject(AWS_ACCESS_KEYS, AWS_SECRET_KEYS, CURRENT_BUCKET.getName());
                                    System.out.println("Enter how many files you want to upload");
                                    int num = myObj.nextInt();
                                    object.AddMultToBucket(num);
                                } else if(newestCommand.equals("chper")){
                                    //User Input
                                    System.out.println("Please enter file name including filepath: ");
                                    String targetFilepath = myObj.nextLine();
                                    System.out.println("Please enter AWS canonical ID: ");
                                    String permissionID = myObj.nextLine();
                                    System.out.println("FullControl Read ReadAcp WriteAcp\nPlease enter permission level: ");
                                    String permissionLevel = myObj.nextLine();
                                    //Calls file permission changing function
                                    boolean success = Directory.changePermission(CURRENT_BUCKET.getName(), targetFilepath,permissionID,permissionLevel);
                                    //Barks failure on cp returning an error
                                    if (!success) {
                                        System.out.println("Permission change failed.");
                                    }
                                }
                                else {
                                    System.out.println("Please enter a valid command");
                                }
                            }
                        }
                    }
                } else if (myObj.equals("l")) {
                    // Put the local code here

//                    if(newestCommand.equals("list")) {
//                        System.out.print("Please enter the OS you're using: ");
//                        String os = myObj.nextLine();
//                        System.out.print("Please enter the name of the user: ");
//                        String name = myObj.nextLine();
//                        System.out.print("Are there other directories that you would like to add to the path? ");
//                        String option = myObj.nextLine();
//                        String path = new String();
//                        if(option.substring(0,1).toUpperCase().equals("Y")) {
//                            System.out.print("Please enter the name of the directory" +
//                                    "(if it's more than one directories ahead, please put a '/' afterwards): ");
//                            path = myObj.nextLine();
//                        } else if(option.substring(0,1).toUpperCase().equals("N")) {
//                            path = "";
//                        } else {
//                            System.out.println("Please enter a valid answer.");
//                        }
//
//                        System.out.print("Would you like to have all files or directories printed? ");
//                        option = myObj.nextLine();
//                        if(option.toUpperCase().equals("FILES")) {
//                            FileHandling.fileList(os, name, path);
//                        } else {
//                            FileHandling.dirList(os, name, path);
//                        }
//                    } else if(newestCommand.equals("rename")) {
//                        String path = new String();
//                        System.out.print("Please enter the OS you're using: ");
//                        String os = myObj.nextLine();
//                        System.out.print("Please enter the name of the user: ");
//                        String name = myObj.nextLine();
//                        System.out.print("Are there other directories that you would like to add to the path? ");
//                        String option = myObj.nextLine();
//                        if(option.substring(0,1).toUpperCase().equals("Y")) {
//                            System.out.print("Please enter the name of the directory" +
//                                    "(if it's more than one directories ahead, please put a '/' afterwards): ");
//                            path = myObj.nextLine();
//                        } else if(option.substring(0,1).toUpperCase().equals("N")) {
//                            path = "";
//                        } else {
//                            System.out.println("Please enter a valid answer");
//                        }
//
//                        System.out.print("Please enter the file name: ");
//                        String old = myObj.nextLine();
//
//                        System.out.print("Please enter the new name: ");
//                        String newName = myObj.nextLine();
//                        FileHandling.rename(old, newName, os, name, path);
//                    }

                } else {
                    System.out.println("Please enter a valid command");
                }

            } catch (Exception a) {
                System.out.println("Reverting back to main");
            }
        }
        if (!credentialsUsed) {
            System.out.println("Do you want to save your login information?");
            newestCommand = myObj.nextLine();
            if (newestCommand.toLowerCase().equals("yes")) {
                try {
                    saveCredentials();
                    System.out.println("Login information saved");
                } catch (IOException e) {
                    System.out.println("Unable to save login information");
                    System.exit(0);
                }
            }
        }
    }

    /**
     * A method to validate the credentials of the user. AWS doesn't provide a direct way to validate credentials.
     * The only way is attempt an operation using the credentials such as listing buckets.
     *
     * @return returns true if able to list buckets and false if credentials were rejected.
     */
    public static AmazonS3 validateCredentials() throws Exception {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(App.AWS_ACCESS_KEYS, App.AWS_SECRET_KEYS);
        AmazonS3Client s3 = new AmazonS3Client(awsCreds);
        List<Bucket> buckets = s3.listBuckets();
        return s3;
    }

    public static void listBuckets() {
        List<Bucket> buckets = S3.listBuckets();
        System.out.println("Your Amazon S3 buckets are:");
        for (Bucket b : buckets) {
            System.out.println("* " + b.getName());
        }
    }

    /**
     * A method to download an object from a remote server
     *
     * @param objectName the name of the object to download
     * @param bucketName the name of the bucket that the object is in
     * @return returns whether the object was successfully downloaded or not
     */
    public static boolean getObject(String objectName, String bucketName) {
        System.out.format("Downloading %s from S3 bucket %s...\n", objectName, bucketName);
        try {
            S3Object o = S3.getObject(bucketName, objectName);
            S3ObjectInputStream s3is = o.getObjectContent();
            FileOutputStream fos = new FileOutputStream(new File(objectName));
            byte[] read_buf = new byte[1024];
            int read_len = 0;
            while ((read_len = s3is.read(read_buf)) > 0) {
                fos.write(read_buf, 0, read_len);
            }
            s3is.close();
            fos.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    private static void downloadDirectory(String bucket_name, String key_prefix, String dir_path) throws IllegalArgumentException{
        if(!S3.doesObjectExist(CURRENT_BUCKET.getName(),key_prefix)){
            throw new IllegalArgumentException("Remote directory does not exist");
        }
        System.out.println("downloading to directory: " + dir_path);
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(App.AWS_ACCESS_KEYS, App.AWS_SECRET_KEYS);
        AmazonS3Client s3 = new AmazonS3Client(awsCreds);
        TransferManager transferManager = new TransferManager(s3);

        try {
            MultipleFileDownload transfer = transferManager.downloadDirectory(bucket_name, key_prefix, new File(dir_path));
            transfer.waitForCompletion();
        } catch (Exception e) {
            System.err.println("Unable to download directory. Received the following error: " + e.getMessage());
        }
        transferManager.shutdownNow();
    }

    public static boolean checkForCredentials() throws IOException {
        File file = new File("credentials.txt");
        if (file.exists()) {
            BufferedReader br = new BufferedReader(new FileReader(file));
            AWS_ACCESS_KEYS = br.readLine();
            AWS_SECRET_KEYS = br.readLine();
            br.close();
            return true;
        }
        return false;
    }

    private static void saveCredentials() throws IOException {
        String path = "credentials.txt";
        File file = new File(path);

        //Overwrite file if it exists
        FileWriter myWriter = new FileWriter(path, false);
        myWriter.write(AWS_ACCESS_KEYS + "\n");
        myWriter.write(AWS_SECRET_KEYS);
        myWriter.close();
    }
}


