package Persistance;

import Business.Adventure;
import Business.Combat;
import Business.Monster;
import Business.Personatge;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AdventureDAO {

    private static final String path = "DPO-Primer-Semestre/Files/adventures.json";

    private final boolean remote;

    public AdventureDAO(int dataType){
        if(dataType == 1){
            this.remote = false;
        }else {
            this.remote = true;
        }
    }



    public List<Adventure> readAdventuresfromRemote() throws IOException {
        ApiHelper apiHelper = new ApiHelper();
        String response = apiHelper.getFromUrl("https://balandrau.salle.url.edu/dpoo/S1-Project_44/adventures");

        Gson gson = new Gson();
        Type listType = new TypeToken<List<Adventure>>() {}.getType();
        List<Adventure> adventures = gson.fromJson(response, listType);

        return adventures;
    }

    public List<Adventure> getAdventures() throws IOException {
        if(remote){
            return readAdventuresfromRemote();
        } else{
            return readAdventuresFromJson();
        }
    }

    /**
     * Function that returns all the adventures saved in adventure data file
     * @return list of adventures read
     * @throws FileNotFoundException when file is not found
     */
    public List<Adventure> getAdventuresFromJson() throws FileNotFoundException{
        return readAdventuresFromJson();
    }

    /**
     * Function that adds an adventure to de adventure data file
     * @param adventure adventure to be added
     * @throws IOException if an error occurs during rewriting of the file
     * @throws FileNotFoundException if the file is not found
     */
    public void addAdventure(Adventure adventure) throws IOException , FileNotFoundException{
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        JsonElement fileElement = initFile();

        List<Adventure> adventures = readAdventuresFromJson();

        adventures.add(adventure);
        FileWriter fw = new FileWriter(path);

        gson.toJson(adventures,fw);
        fw.close();
    }

    /**
     * Function that inits the file and returns it with the parsered info
     * @return parsered information of the file
     * @throws IOException if there is a problem during file creation
     * @throws FileNotFoundException if the file is not found
     */
    private JsonElement initFile() throws IOException, FileNotFoundException{
        File file = new File(path);
        if (!file.exists()) {
            file.createNewFile();
        }

        return JsonParser.parseReader(new FileReader(path));
    }

    /**
     * Function that reads all the adventures from de adventures data file
     * @return list of adventures read
     * @throws FileNotFoundException if the file is not found
     * @throws IllegalAccessError
     */
    public List<Adventure> readAdventuresFromJson() throws FileNotFoundException, IllegalAccessError {
        JsonElement fileElement = JsonParser.parseReader(new FileReader(path));

        if (fileElement.isJsonNull()) {
            return new ArrayList<Adventure>();
        } else {

            JsonArray adventuresArray = fileElement.getAsJsonArray();
            List<Adventure> adventures = new ArrayList<Adventure>();

            for (JsonElement adventureElement : adventuresArray) {

                // create new Adventure to add
                Adventure adventure = readAdventure(adventureElement);
                // add Adventure to the list to be returned
                adventures.add(adventure);
            }

            return adventures;
        }
    }

    /**
     * Funciton that parses a json element and return an adventure
     * @param adventureElement json element corresponding to adventure
     * @return new adventure read
     */
    private Adventure readAdventure(JsonElement adventureElement) {
        // get Json Object
        JsonObject adventureObject = adventureElement.getAsJsonObject();

        // extract each Data type
        String name = adventureObject.get("name").getAsString();
        List<Combat> combats = new ArrayList<>();

        JsonArray jsonCombats = adventureObject.getAsJsonArray("combats");

        for (JsonElement combatElement: jsonCombats) {

            Combat combat = readCombat(combatElement);
            combats.add(combat);

        }

        return new Adventure(name, combats);
    }

    /**
     * Funciton that parses a json element and return a new combat
     * @param combatElement json element corresponding to a combat
     * @return new combat read
     */
    private Combat readCombat(JsonElement combatElement) {
        JsonObject combatObject = combatElement.getAsJsonObject();

        int combatNumber = combatObject.get("combatNumber").getAsInt();
        JsonArray monstersJsonArray = combatObject.getAsJsonArray("monsters");
        List<Monster> monsters = new ArrayList<>();

        for (JsonElement monsterElement : monstersJsonArray) {
            Monster monster = readMonster(monsterElement);
            monsters.add(monster);
        }

        int[] quantity = new int[monsters.size()];
        // read quantity array from json
        JsonArray quantityJsonArray = combatObject.getAsJsonArray("monstersQuantity");
        for (int i = 0; i < monsters.size(); i++) {
            quantity[i] = quantityJsonArray.get(i).getAsInt();
        }
        return new Combat(combatNumber, monsters, quantity);
    }

    /**
     * Funciton that parses a json element and return a new monster
     * @param monsterElement json element corresponding to monster
     * @return new monster read
     */
    private Monster readMonster(JsonElement monsterElement) {
        JsonObject monsterObject = monsterElement.getAsJsonObject();

        String name = monsterObject.get("name").getAsString();
        String challenge = monsterObject.get("challenge").getAsString();
        int experience = monsterObject.get("experience").getAsInt();
        int hitPoints = monsterObject.get("hitPoints").getAsInt();
        int initiative = monsterObject.get("initiative").getAsInt();
        String damageDice = monsterObject.get("damageDice").getAsString();
        String damageType = monsterObject.get("damageType").getAsString();

        return new Monster(name, challenge, experience, hitPoints, initiative, damageDice, damageType);
    }

}
