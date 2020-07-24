package edu.pdx.agileteam7;

//import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void invalidCredentialsExitsInError() {
        App.AWS_ACCESS_KEYS = "blah";
        App.AWS_SECRET_KEYS = "blah";
        assertThat(App.validateCredentials(), equalTo(false));
    }

    //This test requires manually deleting autoTestFolder/ every time until
    //the delete directory function is implemented and can be used for this test in the setup.
    @Test
    public void testMakeDirectory(){
        int match = 0;

        //Sets Credentials
        App.AWS_ACCESS_KEYS = "AKIATB55VFIM6ETVL7AA";
        App.AWS_SECRET_KEYS = "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";

        //Initializes AWS S3 object
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(App.AWS_ACCESS_KEYS, App.AWS_SECRET_KEYS);
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCreds)).withRegion(Regions.US_EAST_1).build();

        //Auto Input
        String bucketName = "rcbucket2";
        String directoryName = "autoTestFolder/";

        //Calls directory creation function
        boolean success = Directory.mkdir(bucketName,directoryName);
        //Barks failure on function returning an error
        if(!success){
            System.out.println("Directory creation failed.");
        }

        //Creates List of Object Keys
        ListObjectsV2Result bucketObjectList = s3.listObjectsV2("rcbucket2");
        List<S3ObjectSummary> objects = bucketObjectList.getObjectSummaries();

        //Iterates through list looking for new directory, sets match to 1 if found
        for (S3ObjectSummary os : objects) {
            String matchString = os.getKey();

            //Matched which key name starts with input sourceDirectory string
            if(matchString.equals("autoTestFolder/")) {
                match = 1;
            }
            if(match == 1) {
                break;
            }
        }

        //Barks test status
        if(match == 1)
            System.out.println("Test Make Directory: Pass");
        else
            System.out.println("Test Make Directory: Fail");

        //If the directory exists match will be 1 and test passes.
        assertEquals(1,match);
    }

    //This test requires manually deleting autoTestFolder/ every time until
    //the delete directory function is implemented and can be used for this test in the setup.
    @Test
    public void testCopyDirectory(){
        boolean fileCopied = true;
        int fileCopiedInt = 1;

        //Sets Credentials
        App.AWS_ACCESS_KEYS = "AKIATB55VFIM6ETVL7AA";
        App.AWS_SECRET_KEYS = "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";

        //Initializes AWS S3 object
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(App.AWS_ACCESS_KEYS, App.AWS_SECRET_KEYS);
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCreds)).withRegion(Regions.US_EAST_1).build();

        //Auto Input
        String bucketName = "rcbucket2";
        String directoryName = "copy/";
        String targetName = "autoTestCopyFolder/";

        //Calls directory copy function
        boolean success = Directory.cp(bucketName,directoryName,bucketName,targetName);

        //Barks failure on function returning an error and test fails
        if(!success){
            System.out.println("Directory copy failed 1.");
            assertEquals(1,success);
        }

        //Creates List of Object Keys
        ListObjectsV2Result bucketObjectList = s3.listObjectsV2("rcbucket2");
        List<S3ObjectSummary> objects = bucketObjectList.getObjectSummaries();

        //Iterates through list looking for a match between an object key name and the input
        //path for the source directory. Outer loop selects an object that matches the source directory. Inner loop
        //selects an object and sees if it's the same as the source file name combined with a new target prefix filepath.
        for (S3ObjectSummary os : objects) {
            String matchString = os.getKey();
            boolean match = false;
            String targetString = null;

            match = matchString.startsWith(directoryName, 0);

            if(match){

                //Processes targetString to have the file name at the end of the matchString, but
                //to replace to prefix part of the string with the new targetDirectory string.
                targetString = matchString.replaceFirst(directoryName,targetName);

                match = false;

                for (S3ObjectSummary ob : objects) {
                    String matchStringInner = ob.getKey();
                    boolean matchInner = false;

                    //Matched which key name that starts with the prefix for the target directory
                    matchInner = matchStringInner.startsWith(targetString, 0);
                    if(matchInner){
                        match = true;
                    }
                }

                //If for-loop ends without finding a match, that means the file this for-loop was looking for
                //was not copied. Therefore the copy operation failed. fileCopied is set to false to do that.
                if(match != true)
                    fileCopied = false;
            }


        }

        //Barks test status
        if(fileCopied == true)
            System.out.println("Test Make Directory: Pass");
        else
            System.out.println("Test Make Directory: Fail");

        //Boolean / int conversion
        if(fileCopied == false)
            fileCopiedInt = 0;

        //If the directory was fully copied, all copies will have a matching copy. fileCopiedInt is set to 0
        //when an individual object was not detected at its expected copy location.
        assertEquals(1,fileCopiedInt);
    }

}

