package edu.pdx.agileteam7;

//import static org.junit.Assert.assertThat;

import static edu.pdx.agileteam7.App.S3;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.transfer.MultipleFileDownload;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.*;


/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }

    @Test(expected = Exception.class)
    public void invalidCredentialsExitsInError() throws Exception {
        App.AWS_ACCESS_KEYS = "blah";
        App.AWS_SECRET_KEYS = "blah";
        App.validateCredentials();
    }

    @Test
    public void validCredentials() throws Exception {
        App.AWS_ACCESS_KEYS = "AKIATB55VFIM6ETVL7AA";
        App.AWS_SECRET_KEYS = "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";
        App.validateCredentials();
    }

    //Tests whether a object is successfully downloaded
    @Test
    public void getObjectDownloads() {
        App.AWS_ACCESS_KEYS = "AKIATB55VFIM6ETVL7AA";
        App.AWS_SECRET_KEYS = "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";

        //Initializes AWS S3 object
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(App.AWS_ACCESS_KEYS, App.AWS_SECRET_KEYS);
        App.S3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCreds)).withRegion(Regions.US_EAST_1).build();

        App.getObject("test.txt", "tippet2");
        File f = new File("test.txt");
        assertThat(f.exists(), equalTo(true));
        if (f.exists()) {
            f.delete();
        }
    }

    @Ignore
    @Test
    public void credentialsFileExists() throws IOException {
        assertThat(App.checkForCredentials(), equalTo(true));
    }

    @Ignore
    //This test requires manually deleting autoTestFolder/ every time until
    //the delete directory function is implemented and can be used for this test in the setup.
    @Test
    public void testMakeDirectory() throws Exception {
        int match = 0;

        try {
            S3 = validateCredentials();
        } catch (Exception e) {
            System.out.println("Login Failed: Please enter valid Access and Secret Keys");
            System.exit(1);
        }

        //Auto Input
        String bucketName = "rcbucket2";
        String directoryName = "autoTestFolder/";

        //Calls directory creation function
        Directory object = new Directory("AKIATB55VFIM6ETVL7AA","wPVnQ4S5RUuoZoZTOhFrOZnwyUu830/hck04oqD4","rcbucket2");
        boolean success = object.mkdir(bucketName, directoryName);
        //Barks failure on function returning an error
        if (!success) {
            System.out.println("success is:" + success);
            System.out.println("Directory creation failed.");
        }

        //Creates List of Object Keys
        ListObjectsV2Result bucketObjectList = S3.listObjectsV2("rcbucket2");
        List<S3ObjectSummary> objects = bucketObjectList.getObjectSummaries();

        //Iterates through list looking for new directory, sets match to 1 if found
        for (S3ObjectSummary os : objects) {
            String matchString = os.getKey();

            //Matched which key name starts with input sourceDirectory string
            if (matchString.equals("autoTestFolder/")) {
                match = 1;
            }
            if (match == 1) {
                break;
            }
        }

        //Barks test status
        if (match == 1)
            System.out.println("Test Make Directory: Pass");
        else
            System.out.println("Test Make Directory: Fail");

        //If the directory exists match will be 1 and test passes.
        assertEquals(1, match);
    }

    @Ignore
    //This test requires manually deleting autoTestFolder/ every time until
    //the delete directory function is implemented and can be used for this test in the setup.
    @Test
    public void testCopyDirectory() {
        boolean fileCopied = true;
        int fileCopiedInt = 1;

        //Validates Credentials
        try {
            S3 = validateCredentials();
        } catch (Exception e) {
            System.out.println("Login Failed: Please enter valid Access and Secret Keys");
            System.exit(1);
        }

        //Auto Input
        String bucketName = "rcbucket2";
        String directoryName = "copy/";
        String targetName = "autoTestCopyFolder/";

        //Calls directory copy function
        Directory object = new Directory("AKIATB55VFIM6ETVL7AA","wPVnQ4S5RUuoZoZTOhFrOZnwyUu830/hck04oqD4","rcbucket2");
        boolean success = object.cp(bucketName, directoryName, bucketName, targetName);

        //Barks failure on function returning an error and test fails
        if (!success) {
            System.out.println("Directory copy failed 1.");
            assertEquals(1, success);
        }

        //Creates List of Object Keys
        ListObjectsV2Result bucketObjectList = S3.listObjectsV2("rcbucket2");
        List<S3ObjectSummary> objects = bucketObjectList.getObjectSummaries();

        //Iterates through list looking for a match between an object key name and the input
        //path for the source directory. Outer loop selects an object that matches the source directory. Inner loop
        //selects an object and sees if it's the same as the source file name combined with a new target prefix filepath.
        for (S3ObjectSummary os : objects) {
            String matchString = os.getKey();
            boolean match = false;
            String targetString = null;

            match = matchString.startsWith(directoryName, 0);

            if (match) {

                //Processes targetString to have the file name at the end of the matchString, but
                //to replace to prefix part of the string with the new targetDirectory string.
                targetString = matchString.replaceFirst(directoryName, targetName);

                match = false;

                for (S3ObjectSummary ob : objects) {
                    String matchStringInner = ob.getKey();
                    boolean matchInner = false;

                    //Matched which key name that starts with the prefix for the target directory
                    matchInner = matchStringInner.startsWith(targetString, 0);
                    if (matchInner) {
                        match = true;
                    }
                }

                //If for-loop ends without finding a match, that means the file this for-loop was looking for
                //was not copied. Therefore the copy operation failed. fileCopied is set to false to do that.
                if (match != true)
                    fileCopied = false;
            }
        }

        //Barks test status
        if (fileCopied == true)
            System.out.println("Test Make Directory: Pass");
        else
            System.out.println("Test Make Directory: Fail");

        //Boolean / int conversion
        if (fileCopied == false)
            fileCopiedInt = 0;

        //If the directory was fully copied, all copies will have a matching copy. fileCopiedInt is set to 0
        //when an individual object was not detected at its expected copy location.
        assertEquals(1, fileCopiedInt);
    }

    @Test
    public void testDeleteDirectories() {
        int match = 0;

        App.AWS_ACCESS_KEYS = "AKIATB55VFIMT42KPLOC";
        App.AWS_SECRET_KEYS = "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";

        //Initializes AWS S3 object
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(App.AWS_ACCESS_KEYS, App.AWS_SECRET_KEYS);
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCreds)).withRegion(Regions.US_EAST_1).build();

        //Auto Input
        String bucketName = "rawags";
        Directory object = new Directory(App.AWS_ACCESS_KEYS,App.AWS_SECRET_KEYS,bucketName);
        boolean output = object.mkdir(bucketName, "directory");
        if (!output) {
            System.out.println("Directory creation failed.");
        }

        // delete directory
        boolean result = Directory.delDirs(bucketName, "directory");
        if (!result) {
            System.out.print("Directory deletion failed.");
        }

        //Creates List of Object Keys
        ListObjectsV2Result bucketObjectList = s3.listObjectsV2("rawags");
        List<S3ObjectSummary> objects = bucketObjectList.getObjectSummaries();

        //Iterates through list looking for new directory, sets match to 1 if found
        for (S3ObjectSummary os : objects) {
            String matchString = os.getKey();

            //Matched which key name starts with input sourceDirectory string
            if (matchString.equals("directory/")) {
                match = 0;
            }
            if (match == 0) {
                break;
            }
        }

        //Barks test status
        if (match == 0)
            System.out.println("Test delete directory: Pass");
        else
            System.out.println("Test delete directory: Fail");

        //If the directory exists match will be 1 and test passes.
        assertEquals(0, match);
    }

    //This test requires manually deleting autoTestFolder/ every time until
    //the delete directory function is implemented and can be used for this test in the setup.
    @Test
    public void testChangeObjectPermission() {
        boolean fileCopied = true;
        int fileCopiedInt = 1;
        String myObj = null;
        //User Input
        String targetFilepath = "test.txt";
        String permissionLevel = "Read";
        String permissionID = "81489afd4507dddba6e9ddf106b9ee30aaddbb35399b5e9119f99810d15c1094";

        //Validates Credentials
        try {
            S3 = validateCredentials();
        } catch (Exception e) {
            System.out.println("Login Failed: Please enter valid Access and Secret Keys");
            System.exit(1);
        }

        //Uses AWS S3 object to create an access control list object which holds the buckets
        //permissions known as grants
        AccessControlList objectPermissions = S3.getObjectAcl("rcbucket2","test.txt");

        //Grant and permission objects are initialized based on input strings
        Grantee granteeID = new CanonicalGrantee(permissionID);
        Permission permission = Permission.valueOf(permissionLevel);

        //List of grants is creates to display grants to user before changing them
        List<Grant> grantsList = objectPermissions.getGrantsAsList();

        int listSizeComparison = 0;
        listSizeComparison = grantsList.size();


        //Calls file permission changing function
        Directory object = new Directory("AKIATB55VFIM6ETVL7AA","wPVnQ4S5RUuoZoZTOhFrOZnwyUu830/hck04oqD4","rcbucket2");
        boolean success = object.changePermission("rcbucket2", targetFilepath, permissionID, permissionLevel);
        //Barks failure on cp returning an error
        if (!success) {
            System.out.println("Permission change failed.");
            assertEquals(1, 0);
        }

        //Uses AWS S3 object to create an access control list object which holds the buckets
        //permissions known as grants
        objectPermissions = S3.getObjectAcl("rcbucket2","test.txt");

        //List of grants is creates to display grants to user before changing them
        grantsList = objectPermissions.getGrantsAsList();

        int listSizeComparison2 = 0;
        listSizeComparison2 = grantsList.size();
        Grant grant = grantsList.get(listSizeComparison2 - 1);

        //Checks to make sure the grant list has grown by 1 and only 1 grant.
        if (listSizeComparison != (listSizeComparison2 - 1)) {
            System.out.println("Permission change failed - new list is not 1 larger " + listSizeComparison + " " + listSizeComparison2);
            assertEquals(1, 0);
        }

        if(!grant.getPermission().toString().equals("READ")){
            System.out.println("Permission change failed - new grant is not Read permission");
            assertEquals(1, 0);
        }

        //Barks test status
        if (success == true)
            System.out.println("Test Change Object Permission: Pass");
        else
            System.out.println("Test Change Object Permission Fail");


        //If the directory was fully copied, all copies will have a matching copy. fileCopiedInt is set to 0
        //when an individual object was not detected at its expected copy location.
        assertEquals(1, 1);
    }

    //Tests to see if the bucket permission function does actually set additional grants
    @Test
    public void testChangeBucketPermission() {
        boolean fileCopied = true;
        int fileCopiedInt = 1;
        String myObj = null;
        //User Input
        String targetFilepath = "test.txt";
        String permissionLevel = "Read";
        String permissionID = "81489afd4507dddba6e9ddf106b9ee30aaddbb35399b5e9119f99810d15c1094";

        //Validates Credentials
        try {
            S3 = validateCredentials();
        } catch (Exception e) {
            System.out.println("Login Failed: Please enter valid Access and Secret Keys");
            System.exit(1);
        }

        //Uses AWS S3 object to create an access control list object which holds the buckets
        //permissions known as grants
        AccessControlList objectPermissions = S3.getBucketAcl("rcbucket2");

        //Grant and permission objects are initialized based on input strings
        Grantee granteeID = new CanonicalGrantee(permissionID);
        Permission permission = Permission.valueOf(permissionLevel);

        //List of grants is creates to display grants to user before changing them
        List<Grant> grantsList = objectPermissions.getGrantsAsList();

        int listSizeComparison = 0;
        listSizeComparison = grantsList.size();


        //Calls file permission changing function
        Directory object = new Directory("AKIATB55VFIM6ETVL7AA","wPVnQ4S5RUuoZoZTOhFrOZnwyUu830/hck04oqD4","rcbucket2");
        boolean success = object.changePermissionBucket("rcbucket2",permissionID,permissionLevel);
        //Barks failure on cp returning an error
        if (!success) {
            System.out.println("Permission change failed.");
            assertEquals(1, 0);
        }

        //Uses AWS S3 object to create an access control list object which holds the buckets
        //permissions known as grants
        objectPermissions = S3.getBucketAcl("rcbucket2");

        //List of grants is creates to display grants to user before changing them
        grantsList = objectPermissions.getGrantsAsList();

        int listSizeComparison2 = 0;
        listSizeComparison2 = grantsList.size();
        Grant grant = grantsList.get(listSizeComparison2 - 1);

        //Checks to make sure the grant list has grown by 1 and only 1 grant.
        if (listSizeComparison != (listSizeComparison2 - 1)) {
            System.out.println("Permission change failed - new list is not 1 larger " + listSizeComparison + " " + listSizeComparison2);
            assertEquals(1, 0);
        }

        if(!grant.getPermission().toString().equals("READ")){
            System.out.println("Permission change failed - new grant is not Read permission");
            assertEquals(1, 0);
        }

        //Barks test status
        if (success == true)
            System.out.println("Test Change Object Permission: Pass");
        else
            System.out.println("Test Change Object Permission Fail");


        //If the directory was fully copied, all copies will have a matching copy. fileCopiedInt is set to 0
        //when an individual object was not detected at its expected copy location.
        assertEquals(1, 1);
    }

    public static AmazonS3 validateCredentials() throws Exception {
  //      BasicAWSCredentials awsCreds = new BasicAWSCredentials(App.AWS_ACCESS_KEYS, App.AWS_SECRET_KEYS);
        BasicAWSCredentials awsCreds = new BasicAWSCredentials("AKIATB55VFIM6ETVL7AA", "wPVnQ4S5RUuoZoZTOhFrOZnwyUu830/hck04oqD4");
        AmazonS3Client s3 = new AmazonS3Client(awsCreds);
        List<Bucket> buckets = s3.listBuckets();
        return s3;
    }
}

