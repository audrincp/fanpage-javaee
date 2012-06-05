package corejsf;

import java.io.File;

/**
 * определение местоположения загруженных файлов
 */
public class UploadFiles {
    private static String mainFolder = "upload";
    public static String getPathForImage (String folder, int id) {
        try {
            File f = new File ("docroot/" + mainFolder + "/" + folder);
            f.mkdirs ();
        }
        catch (Exception e) {
            System.out.println ("Error in make dir: " + e);
        }
        return "docroot/" + mainFolder + "/" + folder + "/" + id + ".jpg";
    }
    public static String getSrcForImage (String folder, int id) {
        return "/../" + mainFolder + "/" + folder + "/" + id + ".jpg";
    }
}
