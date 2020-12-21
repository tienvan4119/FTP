import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.io.FileUtils;


import java.io.*;

public class FTP_CLIENT {


    private static void showServerReply(FTPClient ftpClient) {
        String[] replies = ftpClient.getReplyStrings();
        if (replies != null && replies.length > 0) {
            for (String aReply : replies) {
                System.out.println("SERVER: " + aReply);
            }
        }
    }

    public static boolean uploadSingleFile(FTPClient ftpClient,
                                           String localFilePath, String remoteFilePath) throws IOException {
        File localFile = new File(localFilePath);

        InputStream inputStream = new FileInputStream(localFile);
        try {
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            return ftpClient.storeFile(remoteFilePath, inputStream);
        } finally {
            inputStream.close();
        }
    }

    public static void uploadDirectory(FTPClient ftpClient,
                                       String remoteDirPath, String localParentDir, String remoteParentDir)
            throws IOException {

        System.out.println("LISTING directory: " + localParentDir);

        File localDir = new File(localParentDir);
        if(localDir.isFile()){
            System.out.println("yo wtf is File");
//            boolean created = ftpClient.makeDirectory(remoteDirPath + localDir.getName());
            File[] subFiles = localDir.listFiles();
            if (subFiles != null && subFiles.length > 0) {
                for (File item : subFiles) {
                    String remoteFilePath = remoteDirPath + "/" + remoteParentDir
                            + "/" + item.getName();
                    if (remoteParentDir.equals("")) {
                        remoteFilePath = remoteDirPath + "/" + item.getName();
                    }

                    if (item.isFile()) {
                        // upload the file
                        System.out.println(item.getName());
                        String localFilePath = item.getAbsolutePath();
                        System.out.println("About to upload the file: " + localFilePath);
                        System.out.println(localFilePath);
                        System.out.println(remoteFilePath);

                        boolean uploaded = uploadSingleFile(ftpClient,
                                localFilePath, remoteFilePath);
                        if (uploaded) {
                            System.out.println("UPLOADED a file to: "
                                    + remoteFilePath);
                        } else {
                            System.out.println("COULD NOT upload the file: "
                                    + localFilePath);
                        }
                    } else if(item.isDirectory()) {
                        // create directory on the server
                        System.out.println("YESSSSSSSSSSSSS");
                        boolean created = ftpClient.makeDirectory(remoteFilePath);
                        if (created) {
                            System.out.println("CREATED the directory: "
                                    + remoteFilePath);
                        } else {
                            System.out.println("COULD NOT create the directory: "
                                    + remoteFilePath);
                        }

                        // upload the sub directory
                        String parent = remoteParentDir + "/" + item.getName();
                        if (remoteParentDir.equals("")) {
                            parent = item.getName();
                        }

                        localParentDir = item.getAbsolutePath();
                        uploadDirectory(ftpClient, remoteDirPath, localParentDir,
                                parent);
                    }
                }
            }

        }else {
            System.out.println("ah yes, directory");
            boolean create = ftpClient.makeDirectory(remoteDirPath + "/" +  localDir.getName());
            remoteDirPath = remoteDirPath + "/" + localDir.getName();
            File[] subFiles = localDir.listFiles();
            if (subFiles != null && subFiles.length > 0) {
                for (File item : subFiles) {
                    System.out.println("no");
                    String remoteFilePath = remoteDirPath
                            + "/" + item.getName();

                    if (item.isFile()) {
                        // upload the file
                        System.out.println(item.getName());
                        String localFilePath = item.getAbsolutePath();
                        System.out.println("About to upload the file: " + localFilePath);
                        System.out.println(localFilePath);
//                        System.out.println(remoteFilePath);

                        boolean uploaded = uploadSingleFile(ftpClient,
                                localFilePath, remoteFilePath);
                        if (uploaded) {
                            System.out.println("UPLOADED a file to: "
                                    + remoteFilePath);
                        } else {
                            System.out.println("COULD NOT upload the file: "
                                    + localFilePath);
                        }
                    } else if(item.isDirectory()) {
                        // create directory on the server

                        boolean created = ftpClient.makeDirectory(remoteFilePath);
                        if (created) {
                            System.out.println("CREATED the directory: "
                                    + remoteFilePath);
                        } else {
                            System.out.println("COULD NOT create the directory: "
                                    + remoteFilePath);
                        }

                        // upload the sub directory
                        String parent = remoteParentDir + "/" + item.getName();
                        if (remoteParentDir.equals("")) {
                            parent = item.getName();
                        }

                        localParentDir = item.getAbsolutePath();
                        uploadDirectory(ftpClient, remoteDirPath, localParentDir,
                                parent);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        String server = "localhost";
        int port = 21;
        String user = "tienvan4119";
        String pass = "van411208";
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(server, port);
            showServerReply(ftpClient);
            int replyCode = ftpClient.getReplyCode();

            if (!FTPReply.isPositiveCompletion(replyCode)) {
                System.out.println("Operation failed. Server reply code: " + replyCode);
                return;
            }
            boolean success = ftpClient.login(user, pass);
            showServerReply(ftpClient);
            if (!success) {
                System.out.println("Could not login to the server");
                return;
            } else {
                System.out.println("LOGGED IN SERVER");
                ftpClient.enterLocalPassiveMode();

                String remoteDirPath = "/Upload";
                String localDirPath = "E:/FTP_CLIENT/src/gg";

                uploadDirectory(ftpClient, remoteDirPath, localDirPath, "");

                FTPFile[] files = ftpClient.listFiles();

//                for (FTPFile ftpFile : files) {
//                    // Check the file type and print result
//
//                    if (ftpFile.getType() == FTPFile.FILE_TYPE) {
//
//                        System.out.println("File: " + ftpFile.getName() +
//
//                                "size-> " + FileUtils.byteCountToDisplaySize(
//
//                                ftpFile.getSize()));
//                    } else if (ftpFile.getType() == FTPFile.DIRECTORY_TYPE) {
//                        System.out.println("Directory: " + ftpFile.getName() +
//                                "size-> " + FileUtils.byteCountToDisplaySize(
//                                ftpFile.getSize()));
//                    }
//                }


            }
        } catch (IOException ex) {
            System.out.println("something wrong");

        }
    }
}
