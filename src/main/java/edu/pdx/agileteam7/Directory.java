package edu.pdx.agileteam7;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
//import com.sun.org.apache.xml.internal.utils.SystemIDResolver;

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

    /**
     *
     * @param sourceBucket is the name of the bucket
     * @param targetDirectory is the directory/directories to delete
     * @return true if deleted, false otherwise
     */
    public static boolean delDirs(String sourceBucket, String targetDirectory) {
        try {
            S3.deleteObject(sourceBucket, targetDirectory);
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Function which changes the permissions on a file inside a bucket. Asks the user for a canonical ID from AWS.
     * Adds a grant to that file with the given permission for the given ID.
     * AWS seems to intentionally hold duplicate grants in a file's grant list.
     * @param sourceBucket
     * @param targetFile
     * @param permissionID
     * @param permissionLevel
     * @return
     */
    public static boolean changePermission(String sourceBucket, String targetFile, String permissionID, String permissionLevel) {
        try {
            //Uses AWS S3 object to create an access control list object which holds the file's
            //permissions known as grants
            AccessControlList objectPermissions = S3.getObjectAcl(sourceBucket, targetFile);

            //Grant and permission objects are initialized based on input strings
            Grantee granteeID = new CanonicalGrantee(permissionID);
            Permission permission = Permission.valueOf(permissionLevel);

            //List of grants is creates to display grants to user before changing them
            List<Grant> grantsList = objectPermissions.getGrantsAsList();

            //Iterates through grants list and displays to user. The first entry on the
            //list will always be the owner so the loop is formatted to give a different header
            //for the first entry.
            boolean firstTimeThroughLoop = true;
            for (Grant grant : grantsList) {
                if (firstTimeThroughLoop == true) {
                    System.out.println("ORIGINAL FILE PERMISSIONS:");
                    System.out.println("  File Owner ID : Permission ");
                    firstTimeThroughLoop = false;
                } else
                    System.out.println("  Additional Grantee ID : Permission");
                System.out.format("    %s: %s\n", grant.getGrantee().getIdentifier(), grant.getPermission().toString());
            }

            //Adds permission to permission list object
            objectPermissions.grantPermission(granteeID, permission);

            //Permission list object is uploaded to AWS and saved.
            S3.setObjectAcl(sourceBucket, targetFile, objectPermissions);

            //List for display is updated after the grant was added.
            grantsList = objectPermissions.getGrantsAsList();

            //Iterates through list displaying grants to user
            firstTimeThroughLoop = true;
            for (Grant grant : grantsList) {
                if (firstTimeThroughLoop == true) {
                    System.out.println("ORIGINAL FILE PERMISSIONS:");
                    System.out.println("  File Owner ID : Permission ");
                    firstTimeThroughLoop = false;
                } else
                    System.out.println("  Additional Grantee ID : Permission");
                System.out.format("    %s: %s\n", grant.getGrantee().getIdentifier(), grant.getPermission().toString());
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Function which changes the permissions on a bucket. Asks the user for a canonical ID from AWS.
     * Adds a grant to that bucket with the given permission for the given ID.
     * AWS seems to intentionally hold duplicate grants in a bucket's grant list.
     * @param sourceBucket
     * @param permissionID
     * @param permissionLevel
     * @return
     */
    public static boolean changePermissionBucket(String sourceBucket, String permissionID, String permissionLevel) {
        try {
            //Uses AWS S3 object to create an access control list object which holds the buckets
            //permissions known as grants
            AccessControlList objectPermissions = S3.getBucketAcl(sourceBucket);

            //Grant and permission objects are initialized based on input strings
            Grantee granteeID = new CanonicalGrantee(permissionID);
            Permission permission = Permission.valueOf(permissionLevel);

            //List of grants is creates to display grants to user before changing them
            List<Grant> grantsList = objectPermissions.getGrantsAsList();

            //Iterates through grants list and displays to user. The first entry on the
            //list will always be the owner so the loop is formatted to give a different header
            //for the first entry. The 1st entry is listed twice so the loop is also formatted
            //to skip displaying the 2nd entry.
            int firstTimeThroughLoop = 1;
            for(Grant grant : grantsList){
                if(firstTimeThroughLoop == 1) {
                    System.out.println("ORIGINAL BUCKET PERMISSIONS:");
                    System.out.println("  Bucket Owner ID : Permission ");
                    --firstTimeThroughLoop;
                }
                else if(firstTimeThroughLoop == 0) {
                    --firstTimeThroughLoop;
                    continue;
                }
                else
                    System.out.println("  Additional Grantee ID : Permission");
                System.out.format("    %s: %s\n", grant.getGrantee().getIdentifier(), grant.getPermission().toString());
            }

            //Adds permission to permission list object
            objectPermissions.grantPermission(granteeID, permission);

            //Permission list object is uploaded to AWS and saved.
            S3.setBucketAcl(sourceBucket,objectPermissions);

            //List for display is updated after the grant was added.
            grantsList = objectPermissions.getGrantsAsList();

            //Iterates through list displaying grants to user
            firstTimeThroughLoop = 1;
            for(Grant grant : grantsList){
                if(firstTimeThroughLoop == 1) {
                    System.out.println("UPDATED BUCKET PERMISSIONS");
                    System.out.println("  Bucket Owner ID : Permission ");
                    --firstTimeThroughLoop;
                }
                else if(firstTimeThroughLoop == 0) {
                    --firstTimeThroughLoop;
                    continue;
                }
                else
                    System.out.println("  Additional Grantee ID : Permission");
                System.out.format("    %s: %s\n", grant.getGrantee().getIdentifier(), grant.getPermission().toString());
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
