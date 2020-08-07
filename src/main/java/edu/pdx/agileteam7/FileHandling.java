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
    public static boolean rename(String old, String New) {
        File oldName = new File(old);
        File newName = new File(New);
        System.out.println(oldName);
        if (oldName.renameTo(newName)) {
            return true;
        } else {
            return false;
        }
        /*if ((os.substring(0, 3).toUpperCase().equals("mac".toUpperCase()))){
                os = "Users";
        /*} else if(os.equals("windows")) {
            os = "C:\\";

        } else {
            os = "/";
        }*
            os = "/" + os;
            name = "/" + name;
            path = "/" + path;

            String finalPath = os + name + path;
            //File old = new File("/mySample2.txt");
            File pathFile = new File(oldname);
            //System.out.println(pathFile.getPath());
            System.out.println(finalPath);

            File oldName = new File(finalPath + "/" + oldname);
            File New = new File(newname);
            File newName = new File(finalPath + "/" + New);
            if (oldName.renameTo(newName)) {
                return true;
            } else {
                return false;
            }
        } else if ((os.substring(0, 5).toUpperCase().equals("linux".toUpperCase()))
        || (os.substring(0, 4).toUpperCase().equals("unix".toUpperCase()))
        ){
            os = "/";
            name = "/" + name;
            path = "/" + path;

            String finalPath = os + name + path;
            //File old = new File("/mySample2.txt");
            File pathFile = new File(oldname);
            //System.out.println(pathFile.getPath());
            System.out.println(finalPath);

            File oldName = new File(finalPath + "/" + oldname);
            File New = new File(newname);
            File newName = new File(finalPath + "/" + New);
            if (oldName.renameTo(newName)) {
                return true;
            } else {
                return false;
            }
        } else {
            os = "C:\\";

            os = "\\" + os;
            name = "\\" + name;
            path = "\\" + path;

            String finalPath = os + name + path;
            File pathFile = new File(oldname);
            System.out.println(finalPath);

            File oldName = new File(finalPath + "\\" + oldname);
            File New = new File(newname);
            File newName = new File(finalPath + "\\" + New);
            if (oldName.renameTo(newName)) {
                return true;
            } else {
                return false;
            }
        }*/
    }

    /**
     *
     * @param os operating system the machine
     * @param name name of the user
     * @param path path for the directory in order to list the files in it.
     */
    protected static void fileList(String path) {
        System.out.println("Files: \n");
        File dir = new File(path);
        File[] filesInDir = dir.listFiles();
        for(File file : filesInDir) {
            System.out.println(file.getName());
        }
        /*if(os.substring(0,3).toUpperCase().equals("mac".toUpperCase())) {
            os = "Users";
        } else if(os.equals("windows")) {
            os = "C:\\";
        } else {
            os = "/";
        }

        System.out.println("Files: \n");
        if((os.equals("Users")) || (os.equals("/"))) {
            File dir = new File("/" + os + "/" + name + "/" + path + "/");
            File[] filesInDir = dir.listFiles();
            for(File file : filesInDir) {
                System.out.println(file.getName());
            }
        } else if(os.equals("C:\\")) {
            File dir = new File("\\" + os + "\\" + name + "\\" + path + "\\");
            File[] filesInDir = dir.listFiles();
            for(File file : filesInDir) {
                System.out.println(file.getName());
            }
        }*/
    }

    /**
     * @param os operating system of the computer
     * @param name name of the user in this computer
     * @param path path for the directory to list the directories in it
     */
    protected static void dirList(String path) {
        String dirName = new String();
        dirName = path;
        File file = new File(path);
        if(file.isDirectory()) {
            System.out.println("Directories in " + path + ":");
            String str[] = file.list();
            for(int i = 0; i < str.length; i++) {
                File f = new File(dirName + "" + str[i]);
                if(f.isDirectory()) {
                    System.out.println(str[i]);
                }
            }
        }


        /*
        String dirName = new String();
        if(os.substring(0,3).toUpperCase().equals("mac".toUpperCase())) {
             os = "Users";
             dirName = "/" + os + "/" + name + "/" + path + "/";
        } else if(os.equals("windows")) {
            os = "C:";
            dirName = os + "\\" + name + "\\" + path;
        } else {
            os = "/";
            dirName = "/" + os + "/" + name + "/" + path + "/";
        }

        System.out.println(dirName);
        //String dirName = "/" + os + "/" + name + "/" + path + "/";
        File file = new File(dirName);
        if(file.isDirectory()) {
            System.out.println("Directories in " + dirName + ":");
            String str[] = file.list();
            for(int i = 0; i < str.length; i++) {
                File f = new File(dirName + "" + str[i]);
                if(f.isDirectory()) {
                    System.out.println(str[i]);
                }
            }
        }*/
    }
}
