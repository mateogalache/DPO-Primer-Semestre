package Persistance;

import Business.Adventurer;
import Business.Personatge;
import com.google.gson.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PersonatgeDAO {


    private static final String path = "DPO-Primer-Semestre/Files/characters.json";

    public PersonatgeDAO(){}

    /**
     * Function that reads all the characters from de DataFile
     * @return list of characters read
     * @throws FileNotFoundException if the path is not correct
     */
    public List<Personatge> readCharactersFromJson() throws FileNotFoundException {
        JsonElement fileElement = JsonParser.parseReader(new FileReader(path));
        //JsonObject fileObject = fileElement.getAsJsonObject();

        JsonArray charactersArray = fileElement.getAsJsonArray();
        List<Personatge> personatges = new ArrayList<>();

        for (JsonElement characterElement : charactersArray) {

            // create new monster to add
            Personatge personatge = readPersonatge(characterElement);
            // add monster to the list to be returned
            personatges.add(personatge);
        }

        return personatges;
    }

    /**
     * Function that read one character from the DataFile
     * @param characterElement Json element that contains the information to be parsered
     * @return character read
     */
    private Personatge readPersonatge(JsonElement characterElement) {
        // get Json Object
        JsonObject characterObject = characterElement.getAsJsonObject();

        // extract each Data type
        String name = characterObject.get("name").getAsString();
        String player = characterObject.get("player").getAsString();
        int xp = characterObject.get("xp").getAsInt();
        int body = characterObject.get("body").getAsInt();
        int mind = characterObject.get("mind").getAsInt();
        int spirit = characterObject.get("spirit").getAsInt();
        String type = characterObject.get("class").getAsString();

        return new Adventurer(name, player, xp, mind,body, spirit, type);
    }

    /**
     * function that returns the path of the file
     * @return path of the file
     */
    public String getPath() {
        return path;
    }

    /**
     * Function that adds one character to the character's file
     * @param personatge character to be added
     * @throws IOException when data can't be written correctly
     */
    public void addCharacter(Personatge personatge) throws IOException{
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        List<Personatge> personatges = readCharactersFromJson();

        personatges.add(personatge);

        FileWriter fw = new FileWriter(path);

        gson.toJson(personatges,fw);
        fw.close();
    }

    /**
     * Function that given the name of the character, it returns the character object
     * @param name name of the character
     * @return Character
     * @throws FileNotFoundException when file is not found
     */
    public Personatge searchCharacter(String name) throws FileNotFoundException{
        List<Personatge> personatges = readCharactersFromJson();

        Personatge characterToReturn = personatges.get(0);

        for (Personatge characterAux : personatges) {

            if (characterAux.getNomPersonatge().equals(name)) {
                characterToReturn = characterAux;
            }
        }

        return characterToReturn;
    }

    /**
     * Function that given a character, it delete it from the characters data file
     * @param personatge character to be deleted
     * @throws IOException if file can not be accessed
     */
    public void DeleteCharacter(Personatge personatge) throws IOException {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<Personatge> personatges = readCharactersFromJson();
        List<Personatge> writeCharacters = new ArrayList<>();

        for (Personatge value : personatges) {
            if (!value.getNomPersonatge().equals(personatge.getNomPersonatge())) {
                writeCharacters.add(value);
            }
        }

        FileWriter fw = new FileWriter(path);

        gson.toJson(writeCharacters,fw);
        fw.close();

    }

}
