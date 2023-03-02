package Business;

import java.util.List;

public class Adventure {
    private final String name;
    private final List<Combat> combats;

    /**
     * Function that gets the name of the adventure
     * @return returns the name
     */
    public String getName() {
        return name;
    }

    /**
     * Function that gets the list of combats of the adventure
     * @return return the list of combats
     */
    public List<Combat> getCombats() {
        return combats;
    }

    /**
     * Function to assign the value of the local variable to the instance variable
     * @param name name of adventure
     * @param combats list of combats in the adventure
     */
    public Adventure(String name, List<Combat> combats) {
        this.combats = combats;
        this.name = name;
    }

    /**
     * Function that get the number of combats
     * @return return the size of the combats list
     */
    public int getNumberOfCombats() {
        return combats.size();
    }
}
