package edu.pdx.agileteam7;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;

import java.util.List;

public class Buckets {
    /**
     * Function that checks AWS Access Keys and Secret Keys are valid
     */
    public static void checkKeys ()  {
        if(App.AWS_ACCESS_KEYS == "" || App.AWS_SECRET_KEYS == "" ) {
            throw new NullPointerException("AWS_ACCESS_KEYS or AWS_SECRET_KEYS not specified");
        }
    }

    /**
     * Function that gets a bucket from AWS and returns it to user
     * @param bucket_name
     * @return
     */
    public static Bucket getBucket(String bucket_name) {
        checkKeys();

        BasicAWSCredentials awsCreds = new BasicAWSCredentials(App.AWS_ACCESS_KEYS, App.AWS_SECRET_KEYS);
        AmazonS3Client s3 = new AmazonS3Client(awsCreds);

        Bucket named_bucket = null;

        List<Bucket> buckets = s3.listBuckets();

        for (Bucket b : buckets) {
            if (b.getName().equals(bucket_name)) {
                named_bucket = b;
            }
        }
        System.out.println("Current bucket now points to bucket " + bucket_name);

        if(named_bucket == null) {
            System.out.println("Was not able to get to bucket. Currently bucket is null");
        }
        return named_bucket;
    }

    /**
     * Function that creates a bucket points the current bucket to that returned bucket
     * @param bucket_name
     * @return
     */
    public static Bucket createBucket(String bucket_name) {
        checkKeys();

        BasicAWSCredentials awsCreds = new BasicAWSCredentials(App.AWS_ACCESS_KEYS, App.AWS_SECRET_KEYS);
        AmazonS3Client s3 = new AmazonS3Client(awsCreds);

        Bucket b = null;
        if (s3.doesBucketExistV2(bucket_name)) {
            System.out.format("Bucket %s already exists. Remember that your bucket name has to be unique to every possible bucket in AWS (globally) \n", bucket_name);
            b = getBucket(bucket_name);
        } else {
            try {
                b = s3.createBucket(bucket_name);
            } catch (AmazonS3Exception e) {
                System.err.println(e.getErrorMessage());
            }
        }
        System.out.println("Current bucket now points to bucket " + bucket_name);
        return b;
    }
}
