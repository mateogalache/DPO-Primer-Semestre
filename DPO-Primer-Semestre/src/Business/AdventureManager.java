package Business;

import Persistance.AdventureDAO;
import Persistance.PersonatgeDAO;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdventureManager {
    private static final int minCharacters = 3;
    private List<Combat> combats;

    public AdventureManager() {
    }

    /**
     * Function that get the list of combats in the adventure
     * @return returns the list
     */
    public List<Combat> getCombats() {
        return combats;
    }

    /**
     * Function that add a new combat in the list of combats
     * @param combat the combat to be added
     */
    public void addCombat(Combat combat) {
        List<Combat> combatList;
        if (this.combats == null) {
            combatList = new ArrayList<>();
        } else {
            combatList = combats;
        }
        combatList.add(combat);
        setCombats(combatList);
    }

    /**
     * Function to assign the value of the local variable to the instance variable
     * @param combats the list of combats
     */
    public void setCombats(List<Combat> combats) {
        this.combats = combats;
    }

    /**
     * Function that creates an adventure
     * @param adventureName adventure name
     * @param combats list of combats
     * @throws IOException
     */
    public void createAdventure(String adventureName, List<Combat> combats) throws IOException {
        // Create Adventure Object
        Adventure adventure = new Adventure(adventureName, combats);
        AdventureDAO adventureDAO = new AdventureDAO();
        // Add adventure to JsonFIle
        adventureDAO.addAdventure(adventure);
    }


    /**
     * Function to get the adventures
     * @return returns the list of adventures
     * @throws FileNotFoundException
     * @throws IOException
     */
    public List<Adventure> getAdventures() throws FileNotFoundException, IOException{
        AdventureDAO adventureDAO = new AdventureDAO();
        return adventureDAO.getAdventures();
    }

    /**
     * Function that checks if an adventure can be played by checking if there are characters in.
     * @return returns true if size of the json has de minimum of the characters
     * @throws FileNotFoundException
     */
    public boolean ableToPlayAdventure() throws FileNotFoundException{
        PersonatgeDAO personatgeDAO = new PersonatgeDAO();
        return personatgeDAO.readCharactersFromJson().size() >= minCharacters;
    }

    /**
     * Function that cheks if the name of the adventure is already taken.
     * @param adventureName adventure name
     * @return returns boolean if the name is coorect or not
     * @throws FileNotFoundException
     * @throws IOException
     */
    public boolean checkName(String adventureName) throws FileNotFoundException, IOException{
        AdventureDAO adventureDAO = new AdventureDAO();
        List<Adventure> adventures = adventureDAO.getAdventures();
        boolean able = true;

        for (Adventure adventure : adventures) {
            if (adventure.getName().equals(adventureName)) {
                able = false;
                break;
            }
        }
        return able;
    }

    /**
     * Function that calls 'get adventures' in adventureDAO that reads the adeventure JSON
     * @throws FileNotFoundException
     */
    public void readAdventures() throws FileNotFoundException {
        AdventureDAO adventureDAO = new AdventureDAO();
        adventureDAO.getAdventures();
    }

    /**
     * Function that get the name of the adventures in the list
     * @return returns the names of the adventures
     * @throws IOException
     */
    public List<String> getAdventureNames() throws IOException{
        List<Adventure> adventures = getAdventures();
        List<String> names = new ArrayList<>();
        for (Adventure adventure: adventures) {
            names.add(adventure.getName());
        }
        return names;
    }
}
