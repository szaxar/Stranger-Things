package ST3.utils;

import java.io.File;
import java.io.IOException;

public class ResourcesUtils {

    private static final String resourcesHelperFileName = "resources.txt";

    private static String getResourcesPath() {
        String path = getResourcePath(resourcesHelperFileName);
        path = path.substring(5, path.length() - resourcesHelperFileName.length());
        return path;
    }

    public static String getTilePath(String filename) {
        return getResourcePath("Tiles/" + filename);
    }

    public static String getResourcePath(String path) {
        String resourcePath = ResourcesUtils.class.getClassLoader().getResource(path).getPath();
        if (resourcePath.charAt(2) == ':') {
            resourcePath = resourcePath.substring(1);
        }
        return "file:" + resourcePath;
    }

    public static String getSavedMapsDirectoryPath() throws IOException {
        String path = getResourcesPath() + "SavedMaps";
        createDirectoryIfNotExists(path);
        return path + "/";
    }

    public static String getSavedCreaturesDirectoryPath() throws IOException {
        String path = getResourcesPath() + "SavedCreatures";
        createDirectoryIfNotExists(path);
        return path + "/";
    }

    public static String getSavedGamesDirectoryPath() throws IOException {
        String path = getResourcesPath() + "SavedGames";
        createDirectoryIfNotExists(path);
        return path + "/";
    }

    private static void createDirectoryIfNotExists(String path) throws IOException {
        File dir = new File(path);
        if (!dir.exists() || !dir.isDirectory()) {
            if (!dir.mkdir()) {
                throw new IOException();
            }
        }
    }

}
