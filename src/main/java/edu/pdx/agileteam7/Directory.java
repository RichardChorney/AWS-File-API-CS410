package edu.pdx.agileteam7;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.util.List;


public class Directory {
    /**
     * Function that creates a new directory on the amazon S3 server
     * Inputs a directory string for the new directory's location
     * @param sourceBucket
     * @param targetDirectory
     */
    public static void mkdir(String sourceBucket, String targetDirectory) {
        //Initializes S3 object which calls amazon functions.
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.DEFAULT_REGION).build();

        //Copies root.txt into new .txt file that simulates directories in S3
        //A the string before and including a forward slash in a file name
        //acts as the directory in S3. file/location/text.txt -> turns into
        // file/location/ directory
        s3.copyObject(sourceBucket, "root.txt", sourceBucket, targetDirectory);


    }

    /**
     * Function which copies a directory to another directory.
     * Function creates new directory if destination is nonexistent.
     * Takes input strings for source and destination directories.
     * @param sourceName
     * @param sourceDirectory
     * @param targetName
     * @param targetDirectory
     */
    public static void cp(String sourceName, String sourceDirectory, String targetName, String targetDirectory) {
        //Initializes S3 object which calls amazon functions.
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.DEFAULT_REGION).build();

        //Creates a list of all objects in a bucket to search through
        ListObjectsV2Result bucketObjectList = s3.listObjectsV2(sourceName);
        List<S3ObjectSummary> bucketNamesList = bucketObjectList.getObjectSummaries();


        //Loops through list of object names in selected bucket.
        for(S3ObjectSummary names : bucketNamesList){

            //Add code to copy selected object over to target bucket


        }



//        s3.copyObject(sourceName, sourceDirectory, targetName, targetDirectory);

    }
}
