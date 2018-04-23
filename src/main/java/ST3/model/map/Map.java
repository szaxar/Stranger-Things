package ST3.model.map;

import ST3.model.creature.Creature;
import ST3.model.creature.Enemy;
import ST3.model.creature.Hero;
import ST3.model.creature.NPC;
import ST3.utils.IJsoner;
import ST3.presenter.map.Point;
import com.sun.istack.internal.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class Map implements IJsoner {

    protected final int width;
    protected final int height;
    protected final Tile[][] tiles;
    protected final HashMap<Hero, Point> heroPointMap = new HashMap<>();
    protected final HashMap<Enemy, Point> enemyPointMap = new HashMap<>();
    protected final HashMap<NPC, Point> npcPointMap = new HashMap<>();

    public Map(int width, int height) {
        this.width = width;
        this.height = height;

        tiles = new Tile[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = Tile.GRASS;
            }
        }
    }

    public Map(JSONObject jsonObject) throws JSONException {
        width = jsonObject.getInt("width");
        height = jsonObject.getInt("height");
        tiles = new Tile[width][height];
        setTiles(jsonObject.getJSONObject("tiles"));

        JSONArray heroesJSONArray = jsonObject.getJSONArray("heroes");
        for (int i = 0; i < heroesJSONArray.length(); i++) {
            JSONObject creaturePointJSON = heroesJSONArray.getJSONObject(i);
            Hero creature = new Hero(creaturePointJSON.getJSONObject("creature"));
            Point position = new Point(creaturePointJSON.getInt("x"), creaturePointJSON.getInt("y"));
            heroPointMap.put(creature, position);
        }

        JSONArray enemiesJSONArray = jsonObject.getJSONArray("enemies");
        for (int i = 0; i < enemiesJSONArray.length(); i++) {
            JSONObject creaturePointJSON = enemiesJSONArray.getJSONObject(i);
            Enemy creature = new Enemy(creaturePointJSON.getJSONObject("creature"));
            Point position = new Point(creaturePointJSON.getInt("x"), creaturePointJSON.getInt("y"));
            enemyPointMap.put(creature, position);
        }

        JSONArray npcsJSONArray = jsonObject.getJSONArray("npcs");
        for (int i = 0; i < npcsJSONArray.length(); i++) {
            JSONObject creaturePointJSON = npcsJSONArray.getJSONObject(i);
            NPC creature = new NPC(creaturePointJSON.getJSONObject("creature"));
            Point position = new Point(creaturePointJSON.getInt("x"), creaturePointJSON.getInt("y"));
            npcPointMap.put(creature, position);
        }
    }

    private void setTiles(JSONObject jsonObject) throws JSONException {
        JSONObject tileIdHashMapJSON = jsonObject.getJSONObject("tileIdMap");
        HashMap<Integer, Tile> idTileHashMap = new HashMap<>();
        Iterator keysIterator = tileIdHashMapJSON.keys();
        while (keysIterator.hasNext()) {
            String key = (String) keysIterator.next();
            Tile tile = Tile.valueByName(key);
            if (tile == null) {
                continue;
            }
            idTileHashMap.put(tileIdHashMapJSON.getInt(key), tile);
        }
        JSONArray tilesAsJSON = jsonObject.getJSONArray("tiles");
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int id = tilesAsJSON.getInt(x * height + y);
                if (idTileHashMap.containsKey(id)) {
                    tiles[x][y] = idTileHashMap.get(id);
                    continue;
                }
                tiles[x][y] = Tile.GRASS;
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Tile getTile(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return null;
        }
        return tiles[x][y];
    }

    public void addCreature(Creature creature, int x, int y) {
        if (creature instanceof Hero) {
            addCreature(heroPointMap, (Hero) creature, x, y);
        } else if (creature instanceof Enemy) {
            addCreature(enemyPointMap, (Enemy) creature, x, y);
        } else if (creature instanceof NPC) {
            addCreature(npcPointMap, (NPC) creature, x, y);
        }
    }

    public Point getCreaturePosition(Creature creature) {
        if (creature instanceof Hero) {
            return getCreaturePosition(heroPointMap, (Hero) creature);
        } else if (creature instanceof Enemy) {
            return getCreaturePosition(enemyPointMap, (Enemy) creature);
        } else if (creature instanceof NPC) {
            return getCreaturePosition(npcPointMap, (NPC) creature);
        }
        return null;
    }

    public void moveCreature(Creature creature, int x, int y) {
        if (creature instanceof Hero) {
            moveCreature(heroPointMap, (Hero) creature, x, y);
        } else if (creature instanceof Enemy) {
            moveCreature(enemyPointMap, (Enemy) creature, x, y);
        } else if (creature instanceof NPC) {
            moveCreature(npcPointMap, (NPC) creature, x, y);
        }
    }

    public void removeCreature(Creature creature) {
        if (creature instanceof Hero) {
            removeCreature(heroPointMap, (Hero) creature);
        } else if (creature instanceof Enemy) {
            removeCreature(enemyPointMap, (Enemy) creature);
        } else if (creature instanceof NPC) {
            removeCreature(npcPointMap, (NPC) creature);
        }
    }

    private <CreatureType extends Creature> void addCreature(@NotNull HashMap<CreatureType, Point> creaturePointMap, CreatureType creature, int x, int y) throws ObjectDoubledException, TileTakenException {
        if (creaturePointMap.containsKey(creature)) {
            throw new ObjectDoubledException();
        }
        Point point = new Point(x, y);
        if (isTileTaken(point)) {
            throw new TileTakenException();
        }
        creaturePointMap.put(creature, point);
    }

    private <CreatureType extends Creature> Point getCreaturePosition(@NotNull HashMap<CreatureType, Point> creaturePointMap, CreatureType creature) throws ObjectNotOnMapException {
        if (!creaturePointMap.containsKey(creature)) {
            throw new ObjectNotOnMapException();
        }
        return creaturePointMap.get(creature);
    }

    private <CreatureType extends Creature> void moveCreature(@NotNull HashMap<CreatureType, Point> creaturePointMap, CreatureType creature, int x, int y) throws ObjectNotOnMapException, TileTakenException {
        if (!creaturePointMap.containsKey(creature)) {
            throw new ObjectNotOnMapException();
        }
        Point point = new Point(x, y);
        if (isTileTaken(point)) {
            throw new TileTakenException();
        }
        creaturePointMap.replace(creature, point);
    }

    private <CreatureType extends Creature> void removeCreature(@NotNull HashMap<CreatureType, Point> creaturePointMap, CreatureType creature) throws ObjectNotOnMapException {
        if (!creaturePointMap.containsKey(creature)) {
            throw new ObjectNotOnMapException();
        }
        creaturePointMap.remove(creature);
    }

    public boolean isTileTaken(Point tileToCheck) {
        return getCreatureByPosition(tileToCheck) != null;
    }


    public Creature getCreatureByPosition(Point point){
        for(Enemy enemy : enemyPointMap.keySet()){
            if(enemyPointMap.get(enemy).equals(point)) return enemy;
        }
        for(Hero hero : heroPointMap.keySet()){
            if(heroPointMap.get(hero).equals(point)) return hero;
        }
        for(NPC npc : npcPointMap.keySet()){
            if(npcPointMap.get(npc).equals(point)) return npc;
        }
        return null;
    }

    public Set<java.util.Map.Entry<Hero, Point>> getHeroPointEntrySet() {
        return heroPointMap.entrySet();
    }

    public Set<java.util.Map.Entry<Enemy, Point>> getEnemyPointEntrySet() {
        return enemyPointMap.entrySet();
    }

    public Set<java.util.Map.Entry<NPC, Point>> getNpcPointEntrySet() {
        return npcPointMap.entrySet();
    }

    @Override
    public JSONObject toJSONObject() throws JSONException {
        JSONObject mapAsJSON = new JSONObject();
        mapAsJSON.put("width", width);
        mapAsJSON.put("height", height);
        mapAsJSON.put("tiles", getTilesAsJSONObject());
        mapAsJSON.put("heroes", getCreaturesAsJSONArray(heroPointMap));
        mapAsJSON.put("enemies", getCreaturesAsJSONArray(enemyPointMap));
        mapAsJSON.put("npcs", getCreaturesAsJSONArray(npcPointMap));
        return mapAsJSON;
    }

    private JSONObject getTilesAsJSONObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        HashMap<Tile, Integer> tileIdHashMap = new HashMap<>();
        Tile[] tileValues = Tile.values();
        for (int i = 0; i < tileValues.length; i++) {
            tileIdHashMap.put(tileValues[i], i);
        }
        JSONObject tileIdHashMapJSON = new JSONObject();
        for (java.util.Map.Entry<Tile, Integer> tileIdEntry : tileIdHashMap.entrySet()) {
            tileIdHashMapJSON.put(tileIdEntry.getKey().toString(), tileIdEntry.getValue());
        }
        jsonObject.put("tileIdMap", tileIdHashMapJSON);
        JSONArray tilesAsJSON = new JSONArray();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tilesAsJSON.put(tileIdHashMap.get(tiles[x][y]));
            }
        }
        jsonObject.put("tiles", tilesAsJSON);
        return jsonObject;
    }

    private <CreatureType extends Creature> JSONArray getCreaturesAsJSONArray(@NotNull HashMap<CreatureType, Point> creaturePointMap) throws JSONException {
        JSONArray creaturesAsJSONArray = new JSONArray();
        for (java.util.Map.Entry<CreatureType, Point> creaturePointEntry : creaturePointMap.entrySet()) {
            CreatureType creature = creaturePointEntry.getKey();
            Point position = creaturePointEntry.getValue();

            JSONObject creaturePointAsJSON = new JSONObject();
            creaturePointAsJSON.put("x", position.x);
            creaturePointAsJSON.put("y", position.y);
            creaturePointAsJSON.put("creature", creature.toJSONObject());
            creaturesAsJSONArray.put(creaturePointAsJSON);
        }
        return creaturesAsJSONArray;
    }

}
