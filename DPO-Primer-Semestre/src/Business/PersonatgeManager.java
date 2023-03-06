package Business;

import Persistance.PersonatgeDAO;
import Presentation.Controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PersonatgeManager {
   // private List<Personatge> personatges;
    private String path;

    private Controller controller;

    private int dataType(){
        return controller.loadType();
    }

    /**
     * Function that creeates the 'personatgeDAO' and gets the json path
     * @throws FileNotFoundException
     */
    public PersonatgeManager() throws FileNotFoundException {

        PersonatgeDAO personatgeDAO = new PersonatgeDAO(dataType());
        setPath(personatgeDAO.getPath());
    }

    /**
     * Function that gets the path by constructor
     * @param path the path of the json
     */
    private void setPath(String path) {
        this.path = path;
    }

    /**
     * Function that gets a random number 1 to 6
     * @return int of the number
     */
    private int throwD6() {
        Dice dice = new Dice("D6", 6);
        return dice.throwDice();
    }

    /**
     * Function that generate the stats of the character by getting random numbers
     * @return int array of the values
     */
    public int[] generateStat() {
        int[] values = new int[3];
        values[0] = throwD6();
        values[1] = throwD6();
        values[2] = calculateEstadistics(values[0], values[1]);
        return values;
    }

    /**
     * Function to calculate the stats value depending of random values got
     * @param firstValue first random number
     * @param secondValue second random number
     * @return int of the final value
     */
    private int calculateEstadistics(int firstValue, int secondValue) {
        int suma = firstValue + secondValue;
        int value = 0;
        if (suma == 2) {value = -1;}
        if (suma >= 3 && suma <= 5) {value = 0;}
        if (suma >= 6 && suma <= 9) {value = 1;}
        if (suma >= 10 && suma <= 11) {value = 2;}
        if (suma == 12) {value = 3;}
        return value;
    }

    /**
     * Function that creates the adventurer
     * @param name nam of the character
     * @param playerName name of the player
     * @param level level of the character
     * @param body body stat of the character
     * @param mind mind stat of the character
     * @param spirit spirit stat of the character
     * @param adventurerType type of character
     * @throws IOException
     */
    public void createAdventurer(String name, String playerName, int level, int body, int mind, int spirit, String adventurerType) throws IOException{
        int xpPoints = calculateInitialLevel(level);
        Adventurer adventurer = new Adventurer(name, playerName, xpPoints, body, mind, spirit, adventurerType);
        PersonatgeDAO personatgeDAO = new PersonatgeDAO(dataType());
        personatgeDAO.addCharacter(adventurer);
    }

    /**
     * Function that calculates the experince points dependending of the initial level
     * @param nivellInicial initial level
     * @return int of the experience points
     */
    private int calculateInitialLevel(int nivellInicial) {
        int xpPoints = 0;
        if (nivellInicial > 1) {
            xpPoints = (nivellInicial - 1) * 100;
        }
        return xpPoints;
    }

    /**
     * Funciton that checks if the character name already exists
     * @param name name of the character
     * @return boolean that is true if the name is in the list of the characters
     * @throws IOException
     */
    public boolean checkIfCharacterNameExists(String name) throws IOException {
        PersonatgeDAO personatgeDAO = new PersonatgeDAO(dataType());
        List<Personatge> personatgeList = personatgeDAO.readCharactersFromJson();
        return inListCharacter(name,personatgeList);
    }

    /**
     * Function that makes a for in the character list to check if the name of the character exists
     * @param name name of the character
     * @param personatgeList list of the character
     * @return boolean says if the name is int the list
     */
    public boolean inListCharacter(String name, List<Personatge> personatgeList) {
        boolean inList = false;
        for (int i = 0; i < personatgeList.size(); i++) {
            if (personatgeList.get(i) != null) {
                if (name.equals(personatgeList.get(i).getNomPersonatge())) {
                    inList = true;
                }
            }
        }
        return inList;
    }

    /**
     * Function that gets the characters from the list
     * @return the list of the characters
     * @throws FileNotFoundException
     */
    public List<String> getAllCharactersFromJson() throws FileNotFoundException{
        PersonatgeDAO personatgeDAO = new PersonatgeDAO(dataType());
        List<Personatge> personatges = personatgeDAO.readCharactersFromJson();
        List<String> names = new ArrayList<String>();

        for (int i = 0; i < personatges.size(); i++) {
            names.add(personatges.get(i).getNomPersonatge());
        }
        return names;
    }

    public List<String> getAllCharactersFromRemote() throws IOException {
        PersonatgeDAO personatgeDAO = new PersonatgeDAO(dataType());

        List<Personatge> personatges = personatgeDAO.readCharactersFromRemote();
        List<String> names = new ArrayList<String>();

        for (int i = 0; i < personatges.size(); i++) {
            names.add(personatges.get(i).getNomPersonatge());
        }
        return names;
    }

    public List<String> getAllCharacters() throws IOException,FileNotFoundException {
        if(dataType() == 1){
            return getAllCharactersFromJson();
        } else{
            return getAllCharactersFromRemote();
        }
    }

    /**
     * Function that get the characters from the list that are from a specific player
     * @param playerName player name
     * @return list of the characters
     * @throws FileNotFoundException
     */
    public List<String> getCharactersFromPlayer(String playerName) throws FileNotFoundException{
        PersonatgeDAO personatgeDAO = new PersonatgeDAO(dataType());
        List<Personatge> personatges = personatgeDAO.readCharactersFromJson();
        List<String> names = new ArrayList<String>();
        playerName = playerName.toUpperCase();

        boolean found = false;
        for (int i = 0; i < personatges.size(); i++) {
            String actualPlayerName = personatges.get(i).getNomJugador().toUpperCase();
            if (actualPlayerName.equals(playerName) || actualPlayerName.contains(playerName)) {
                found = true;
                names.add(personatges.get(i).getNomPersonatge());
            }
        }
        if (found) {
            return names;
        } else {
            return null;
        }
    }


    /**
     * Function that gets the characters positioned in the index gave
     * @param characterIndex index number
     * @param characterNames list of character names
     * @return character positioned in the index gave
     * @throws FileNotFoundException
     */
    public Personatge getCharacterFromIndex(int characterIndex, List<String> characterNames) throws FileNotFoundException{
        // first we get the name of the player you see
        PersonatgeDAO personatgeDAO = new PersonatgeDAO(dataType());
        return personatgeDAO.searchCharacter(characterNames.get(characterIndex));
    }

    /**
     * Function that deletes a character
     * @param nomPersonatge name of the character to be deleted
     * @throws IOException
     */
    public void deleteCharacter(String nomPersonatge) throws IOException {
        PersonatgeDAO personatgeDAO = new PersonatgeDAO(dataType());

        // we won't check if it's null because we previously checked that the name of the character exists
        Personatge personatge = getCharacterFromName(nomPersonatge, personatgeDAO);

        // call the function of PersonatgeDAO that deletes de character
        personatgeDAO.DeleteCharacter(personatge);
    }

    /**
     * Function that gets the character from his name
     * @param nomPersonatge name of the character
     * @param personatgeDAO 'personatgeDAO' that contains functions to read the character json
     * @return the chararacter
     * @throws FileNotFoundException
     */
    private Personatge getCharacterFromName(String nomPersonatge, PersonatgeDAO personatgeDAO) throws FileNotFoundException{
        List<Personatge> personatges = personatgeDAO.readCharactersFromJson();
        Personatge personatge = null;
        for (int i = 0; i < personatges.size(); i++) {
            if (personatges.get(i).getNomPersonatge().equals(nomPersonatge)) {
                personatge = personatges.get(i);
            }
        }
        return personatge;
    }
}
