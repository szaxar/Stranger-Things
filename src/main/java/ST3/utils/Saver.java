package ST3.utils;

import ST3.model.creature.Creature;
import ST3.model.creature.Enemy;
import ST3.model.creature.Hero;
import ST3.model.creature.NPC;
import ST3.model.map.EditableMap;
import ST3.model.map.GameMap;
import ST3.model.map.Map;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class Saver {

    private static final String fileExtension = ".json";

    public static boolean saveMap(String filename, Map map) {
        try {
            String directoryPath = ResourcesUtils.getSavedMapsDirectoryPath();
            String fileContent = map.toJSONObject().toString();
            boolean saved = save(directoryPath, filename, fileContent);
            if (saved) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Saving map");
                alert.setHeaderText(null);
                alert.setContentText("Saving map success.");
                alert.showAndWait();
            }
            return saved;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Map loadMap(String filename) {
        try {
            String path = ResourcesUtils.getSavedMapsDirectoryPath() + filename + fileExtension;
            JSONObject jsonObject = loadJSON(path);
            if (jsonObject == null) {
                return null;
            }
            return new Map(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static EditableMap loadEditableMap(String filename) {
        try {
            String path = ResourcesUtils.getSavedMapsDirectoryPath() + filename + fileExtension;
            JSONObject jsonObject = loadJSON(path);
            if (jsonObject == null) {
                return null;
            }
            return new EditableMap(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void deleteMap(String filename) {
        try {
            String path = ResourcesUtils.getSavedMapsDirectoryPath() + "/" + filename + fileExtension;
            deleteFile(path, filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean saveGame(String filename, GameMap gameMap) {
        try {
            String directoryPath = ResourcesUtils.getSavedGamesDirectoryPath();
            String fileContent = gameMap.toJSONObject().toString();
            boolean saved = save(directoryPath, filename, fileContent);
            if (saved) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Saving game");
                alert.setHeaderText(null);
                alert.setContentText("Saving game success.");
                alert.showAndWait();
            }
            return saved;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static GameMap loadNewGameMap(String filename) {
        try {
            String path = ResourcesUtils.getSavedMapsDirectoryPath() + filename + fileExtension;
            JSONObject jsonObject = loadJSON(path);
            if (jsonObject == null) {
                return null;
            }
            return new GameMap(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static GameMap loadGameMap(String filename) {
        try {
            String path = ResourcesUtils.getSavedGamesDirectoryPath() + filename + fileExtension;
            JSONObject jsonObject = loadJSON(path);
            if (jsonObject == null) {
                return null;
            }
            return new GameMap(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void deleteGame(String filename) {
        try {
            String path = ResourcesUtils.getSavedGamesDirectoryPath() + "/" + filename + fileExtension;
            deleteFile(path, filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String saveCreature(Creature creature) {
        try {
            String directoryPath = ResourcesUtils.getSavedCreaturesDirectoryPath();
            String filename = getFirstPossibleFilename(directoryPath, creature.toString());
            JSONObject jsonObject = creature.toJSONObject();
            jsonObject.put("className", creature.getClass().getName());
            String fileContent = jsonObject.toString();
            boolean saved = save(directoryPath, filename, fileContent);
            if (saved) {
                return filename;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getFirstPossibleFilename(String directoryPath, String oldFilename) {
        if (!(new File(directoryPath + "/" + oldFilename + fileExtension)).exists()) {
            return oldFilename;
        }
        for (int i = 1; i < Integer.MAX_VALUE; i++) {
            String newFilename = oldFilename + Integer.toString(i);
            if (!(new File(directoryPath + "/" + newFilename + fileExtension)).exists()) {
                return newFilename;
            }
        }
        return null;
    }

    public static Creature loadCreature(String filename) {
        try {
            String path = ResourcesUtils.getSavedCreaturesDirectoryPath() + filename + fileExtension;
            JSONObject jsonObject = loadJSON(path);
            if (jsonObject == null) {
                return null;
            }
            Class creatureClass = Class.forName(jsonObject.getString("className"));
            if (creatureClass == Hero.class) {
                return new Hero(jsonObject);
            } else if (creatureClass == Enemy.class) {
                return new Enemy(jsonObject);
            } else if (creatureClass == NPC.class) {
                return new NPC(jsonObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void deleteCreature(String filename) {
        try {
            String path = ResourcesUtils.getSavedCreaturesDirectoryPath() + "/" + filename + fileExtension;
            deleteFile(path, filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> loadSavedMaps() {
        String dirPath;
        try {
            dirPath = ResourcesUtils.getSavedMapsDirectoryPath();
        } catch (IOException e) {
            return null;
        }
        return loadSavedFiles(dirPath);
    }

    public static ArrayList<String> loadSavedCreatures() {
        String dirPath;
        try {
            dirPath = ResourcesUtils.getSavedCreaturesDirectoryPath();
        } catch (IOException e) {
            return null;
        }
        return loadSavedFiles(dirPath);
    }

    public static ArrayList<String> loadSavedGames() {
        String dirPath;
        try {
            dirPath = ResourcesUtils.getSavedGamesDirectoryPath();
        } catch (IOException e) {
            return null;
        }
        return loadSavedFiles(dirPath);
    }

    private static boolean save(String directoryPath, String filename, String fileContent) {
        File file = new File(directoryPath + "/" + filename + fileExtension);
        if (file.exists()) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("File exists");
            alert.setHeaderText(null);
            alert.setContentText("File \"" + filename + "\" already exists. Do you want to override file?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() != ButtonType.OK) {
                return true;
            }
        }
        try {
            if (!file.exists() && !file.createNewFile()) {
                return false;
            }

            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(fileContent);

            bufferedWriter.close();
            fileWriter.close();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static JSONObject loadJSON(String path) {
        try {
            File file = new File(path);
            if (!file.exists() || !file.isFile()) {
                return null;
            }
            String content = FileUtils.readFileToString(file, "utf-8");
            return new JSONObject(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static ArrayList<String> loadSavedFiles(String dirPath) {
        File[] files = new File(dirPath).listFiles();
        ArrayList<String> savedFiles = new ArrayList<>();
        if (files == null) return savedFiles;
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(fileExtension)) {
                String filename = file.getName();
                filename = filename.substring(0, filename.length() - fileExtension.length());
                savedFiles.add(filename);
            }
        }
        return savedFiles;
    }

    private static void deleteFile(String path, String filename) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Deleting");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want delete \"" + filename + "\"?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() != ButtonType.OK) {
            return;
        }

        File file = new File(path);
        file.delete();
    }

}
