import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class app {
    List<String> whitelist;
    public static void main(String[] args) throws IOException {
        app app = new app();
        app.clearWorkspace();

    }
    public void clearWorkspace() throws IOException {
        File dir = new File("./");
        whitelist = new ArrayList<>();
        whitelist.add("LabLoader");
        whitelist.add(".\\.vsc");
        whitelist.add(".\\.idea");
        whitelist.add(".\\.git");
        whitelist.add(".\\.gitignore");
        whitelist.add("labs.properties");
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
            }else{
                System.out.println("should not be deleted: "+f.getPath());
            }
        }
    }
}
