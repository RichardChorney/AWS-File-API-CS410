package edu.pdx.agileteam7;
import com.amazonaws.services.dynamodbv2.xspec.S;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.*;

public class FileHandling {

    /**
     * Method that renames a file given the path of the file and the new name of the
     * file.
     *
     * @param oldname is the oldname of the file.
     * @param newname is the new name of the file (renamed to)
     *
     * The following params are used to define/specify the path in order to correctly rename
     * @param os is the operating system the user is using
     * @param name is the username in the machine
     * @param path is any directory/directories that the user wants to add to complete the above
     *
     * @return true if success, otherwise false.
     * */
    public static boolean rename(String oldname, String newname, String os, String name, String path) {
        if(os.substring(0,3).toUpperCase().equals("mac".toUpperCase())) {
            os = "Users";
        } else if(os.equals("windows")) {
            os = "C:\\";
        } else {
            os = "/";
        }
        os = "/" + os;
        name = "/" + name;
        path = "/" + path;

        String finalPath = os + name + path;
        //File old = new File("/mySample2.txt");
        File pathFile = new File(oldname);
        System.out.println(pathFile.getPath());
        System.out.println(finalPath);

        File oldName = new File(finalPath + "/" +  oldname);
        File New = new File(newname);
        File newName = new File(finalPath + "/"+ New);
        if(oldName.renameTo(newName)) {
            return true;
        } else {return false;}
        /*File obj = new File ("/Users/rawahalsinan/Desktop/mysample.txt");
        File file = new File("mysample2.txt");
        //String path = file.getAbsolutePath();
        //System.out.println(path);
        if(obj.renameTo(file)) {
            System.out.println("success");
        } else {
            System.out.println("error.");
        }*/
/*        File o = new File("mySample.txt");
        String path = o.getAbsolutePath();
        File name = new File(path);

        File newName = new File("mySample1.txt");
        if(name.renameTo(newName)) {
            System.out.print("Success");
        } else {
            System.out.print("err");
        }*/
    }

    /**
     *
     * @param os operating system the machine
     * @param name name of the user
     * @param path path for the directory in order to list the files in it.
     */
    protected static void fileList(String os, String name, String path) {
        if(os.substring(0,3).toUpperCase().equals("mac".toUpperCase())) {
            os = "Users";
        } else if(os.equals("windows")) {
            os = "C:\\";
        } else {
            os = "/";
        }

        System.out.println("Files: \n");
        File dir = new File("/" + os + "/" + name + "/" + path + "/");
        File[] filesInDir = dir.listFiles();
        for(File file : filesInDir) {
            System.out.println(file.getName());
        }
    }

    /**
     * @param os operating system of the computer
     * @param name name of the user in this computer
     * @param path path for the directory to list the directories in it
     */
    protected static void dirList(String os, String name, String path) {
        //String dirName= "/Users/rawahalsinan/";
        if(os.substring(0,3).toUpperCase().equals("mac".toUpperCase())) {
             os = "Users";
        } else if(os.equals("windows")) {
            os = "C:\\";
        } else {
            os = "/";
        }

        System.out.print(os);
        String dirName = "/" + os + "/" + name + "/" + path + "/";
        File file = new File(dirName);
        if(file.isDirectory()) {
            System.out.println("Directories in " + dirName + ":");
            String str[] = file.list();
            for(int i = 0; i < str.length; i++) {
                File f = new File(dirName + "" + str[i]);
                if(f.isDirectory()) {
                    System.out.println(str[i]);
                } /*else {
                    System.out.println(str[i] + " is a file");
                }*/
            }
        } /*else {
            System.out.println(dirName + "is not a directory");
        }*/
    }
}
