package edu.pdx.agileteam7;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;

import java.util.List;

import static edu.pdx.agileteam7.App.S3;


public class Directory {
    /**
     * Function that creates a new directory on the amazon S3 server
     * Inputs a directory string for the new directory's location
     *
     * @param sourceBucket
     * @param targetDirectory
     */
    public static boolean mkdir(String sourceBucket, String targetDirectory) {
        try {
            //Uses s3.putObject which normally is used to upload to S3. Placing a null value
            //in the local source directory argument creates a 0 size file. If the file ends
            //with / it will be interpretted by S3 as a directory.
            S3.putObject(sourceBucket, targetDirectory, "");

            //Barks directory creation
            System.out.println("Directory " + targetDirectory + " created.");

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Function which copies a directory to another directory.
     * Function creates new directory if destination is nonexistent.
     * Takes input strings for source and destination directories.
     *
     * @param sourceBucket
     * @param sourceDirectory
     * @param targetBucket
     * @param targetDirectory
     */
    public static boolean cp(String sourceBucket, String sourceDirectory, String targetBucket, String targetDirectory) {
        try {
            //Creates a request object which sets the delimiter ahead of list creation
            //Ended up not needing but keeping code up for now as a demonstration-reference.

            //ListObjectsV2Request request = new ListObjectsV2Request().withBucketName(sourceBucket).withDelimiter("/");

            //List of bucket objects is created using a bucket name or a ListObjectsV2Request object
            ListObjectsV2Result bucketObjectList = S3.listObjectsV2(sourceBucket);

            //Creates list of object key names from original list of bucket objects
            List<S3ObjectSummary> objects = bucketObjectList.getObjectSummaries();

            //Loops through key names list. Tries to find if beginning of key name string
            //matches the source directory name prefix ex. in "root/test/file.txt", "root/test/"
            //is the prefix which corresponds to a test folder inside a root folder. This is what
            //needs to match in order to identify the correct source objects to copy.
            for (S3ObjectSummary os : objects) {
                String matchString = os.getKey();
                String targetString = null;
                boolean match = false;

                //Matched which key name starts with input sourceDirectory string
                match = matchString.startsWith(sourceDirectory, 0);
                if (match) {

                    //Processes targetString to have the file name at the end of the matchString, but
                    //to replace to prefix part of the string with the new targetDirectory string.
                    targetString = matchString.replaceFirst(sourceDirectory, targetDirectory);
                    S3.copyObject(sourceBucket, matchString, targetBucket, targetString);
                }
            }

            //Barks directory copy success
            System.out.println("Directory " + sourceDirectory + " copied to " + targetDirectory);

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
