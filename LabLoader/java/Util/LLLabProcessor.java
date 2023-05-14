package Util;

import Exceptions.LLCLIException;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class LLLabProcessor {
    String labName;
    String zipFileName;
    List<String> whitelist;
    public static String[] excludedFromDeletion = {"labs.jar", ".idea", ".vsc", ".git", "labs.properties", "labs.md",
            "LLCLIException.java","LLCLIException.class","LLCLIParser.java","LLCLIParser.class","LLLabProcessor.java",
            "LLLabProcessor.class", "LLPropsUtil.java", "LLPropsUtil.class", "LLWebUtil.java", "LLWebUtil.class",
            "LabLoaderApplication.java","LabLoaderApplication.class"};

    public LLLabProcessor(String labName, String zipFileName) throws LLCLIException {
        this.labName = labName;
        this.zipFileName = zipFileName;
        whitelist = new ArrayList<>();
        whitelist.add("LabLoader");
        whitelist.add(".\\.vsc");
        whitelist.add(".\\.idea");
        whitelist.add(".\\.git");
        whitelist.add(".\\.gitignore");
        whitelist.add("labs.properties");
        LLPropsUtil.setCurrentLab(labName);
    }
    public LLLabProcessor(String labName) throws LLCLIException {
        this(labName, "out.zip");
    }
    public void processCanonical() throws LLCLIException, IOException {
        clearWorkspace();
        loadCanonicalLabZip();
        try{
            unzipFile();
        }catch(IOException e){
            throw new LLCLIException("There was some issue unzipping your lab.");
        }
        File zipFile = new File(zipFileName);
        zipFile.delete();
    }
    public void processSaved() throws LLCLIException, IOException {
        clearWorkspace();
        loadSavedLabZip();
        try{
            unzipFile();
        }catch(IOException e){
            throw new LLCLIException("There was some issue unzipping your lab.");
        }
        File zipFile = new File(zipFileName);
        zipFile.delete();

    }
    /**
     * convert input stream from web into a zip file
     * @throws URISyntaxException
     * @throws IOException
     */
    public void loadCanonicalLabZip() throws LLCLIException {

        try (FileOutputStream out = new FileOutputStream(zipFileName)) {
            LLWebUtil.getCanonicalZip(labName).transferTo(out);
        }catch(IOException e){
            throw new LLCLIException("There was some issue loading the lab contents.");
        }
    }
    /**
     * convert input stream from web into a zip file
     * @throws URISyntaxException
     * @throws IOException
     */
    public void loadSavedLabZip() throws LLCLIException {

        try (FileOutputStream out = new FileOutputStream(zipFileName)) {
            LLWebUtil.getSavedZip(labName).transferTo(out);
        }catch(IOException e){
            throw new LLCLIException("There was some issue loading the lab contents.");
        }
    }
    /**
     * the zip file already exists, now unzip all contents
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void unzipFile() throws IOException {
        String fileZip = "./"+zipFileName;
        File destDir = new File("./");
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip));
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            File newFile = newFile(destDir, zipEntry);
            if (zipEntry.isDirectory()) {
                if (!newFile.isDirectory() && !newFile.mkdirs()) {
                    throw new IOException("Failed to create directory " + newFile);
                }
            } else {
                // fix for Windows-created archives
                File parent = newFile.getParentFile();
                if (!parent.isDirectory() && !parent.mkdirs()) {
                    throw new IOException("Failed to create directory " + parent);
                }

                // write file content
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
            }
            zipEntry = zis.getNextEntry();
        }

        zis.closeEntry();
        zis.close();
    }
    /**
     * unpacks subdirectories of the zip file
     */
    public File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }
        return destFile;
    }

    public void clearWorkspace() throws IOException {
        File dir = new File("./");

        recursivelyPrintFiles(dir);
    }
    public void recursivelyPrintFiles(File dir) throws IOException {
        File[] files = dir.listFiles();
        for(File f : files){
            if(f.isDirectory()){
                recursivelyPrintFiles(f);
            }
            boolean deleteFlag = true;
            for(String s : whitelist){

                if(f.getPath().contains(s)) {
                    deleteFlag = false;
                }

            }
            if(deleteFlag){
                System.out.println("should be deleted: "+f.getPath());
                f.delete();
            }else{
                System.out.println("should not be deleted: "+f.getPath());
            }
        }
    }
}
