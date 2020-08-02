package edu.pdx.agileteam7;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.devicefarm.model.ArgumentException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.util.List;

import static edu.pdx.agileteam7.App.S3;

public class Buckets {

    /**
     * Function that gets a bucket from AWS and returns it to user
     *
     * @param bucket_name
     * @return
     */
    public static Bucket getBucket(String bucket_name) {
        Bucket named_bucket = null;

        List<Bucket> buckets = S3.listBuckets();

        for (Bucket b : buckets) {
            if (b.getName().equals(bucket_name)) {
                named_bucket = b;
            }
        }

        if (named_bucket == null) {
            System.out.println("Was not able to get to bucket. Currently bucket is null");
        } else {
            System.out.println("Current bucket now points to bucket " + bucket_name);
        }
        return named_bucket;
    }

    /**
     * Function that creates a bucket points the current bucket to that returned bucket
     *
     * @param bucket_name
     * @return
     */
    public static Bucket createBucket(String bucket_name) {
        Bucket b = null;
        if (S3.doesBucketExistV2(bucket_name)) {
            System.out.format("Bucket %s already exists. Remember that your bucket name has to be unique to every possible bucket in AWS (globally) \n", bucket_name);
            b = getBucket(bucket_name);
        } else {
            try {
                b = S3.createBucket(bucket_name);
            } catch (AmazonS3Exception e) {
                System.err.println(e.getErrorMessage());
            }
        }
        System.out.println("Current bucket now points to bucket " + bucket_name);
        return b;
    }

    /**
     * This method lists all of the objects in a bucket
     *
     * @param bucketName The name of the bucket to which to list objects from
     */
    public static void listObjects(String bucketName) {
        try {
            ListObjectsV2Result result = S3.listObjectsV2(bucketName);
            List<S3ObjectSummary> objects = result.getObjectSummaries();
            for (S3ObjectSummary os : objects) {
                System.out.println("* " + os.getKey());
            }
        } catch (Exception e) {
            throw new ArgumentException("ERROR");
        }
    }
}

