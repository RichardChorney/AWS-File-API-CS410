package edu.pdx.agileteam7;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
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
    public static boolean mkdir(String sourceBucket, String targetDirectory) {
        try {
            //Validates AWS credentials then creates AWS object for AWS function calls.
            BasicAWSCredentials awsCreds = new BasicAWSCredentials(App.AWS_ACCESS_KEYS, App.AWS_SECRET_KEYS);
            final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCreds)).withRegion(Regions.US_EAST_1).build();


            //Uses s3.putObject which normally is used to upload to S3. Placing a null value
            //in the local source directory argument creates a 0 size file. If the file ends
            //with / it will be interpretted by S3 as a directory.
            s3.putObject(sourceBucket,targetDirectory,"");

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
     * @param sourceName
     * @param sourceDirectory
     * @param targetName
     * @param targetDirectory
     */
    public static boolean cp(String sourceName, String sourceDirectory, String targetName, String targetDirectory) {
        //Initializes S3 object which calls amazon functions.
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.DEFAULT_REGION).build();

        //Creates a list of all objects in a bucket to search through
        ListObjectsV2Result bucketObjectList = s3.listObjectsV2(sourceName);
        List<S3ObjectSummary> bucketNamesList = bucketObjectList.getObjectSummaries();


        //Loops through list of object names in selected bucket.
        for(S3ObjectSummary names : bucketNamesList){

            //Add code to copy selected object over to target bucket


        }
        return true;
    }
}
