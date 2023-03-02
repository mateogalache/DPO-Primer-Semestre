package Business;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Combat {
    private final int combatNumber;
    private List<Monster> monsters;
    private int[] monstersQuantity;

    /**
     * Constructor of Combat class
     * @param combatNumber number of the combat
     * @param monsters monsters of the combat
     * @param monstersQuantity quantity of monsters in combat
     */
    public Combat(int combatNumber, List<Monster> monsters, int[] monstersQuantity) {
        this.combatNumber = combatNumber;
        this.monsters = monsters;
        this.monstersQuantity = monstersQuantity;
    }

    /**
     * function that determines the value of the monster's quantity
     * @param monstersQuantity int array that represents the monster's quantity
     */
    public void setMonstersQuantity(int[] monstersQuantity) {
        this.monstersQuantity = monstersQuantity;
    }

    /**
     * function that returns the value of the monster's quantity
     * @return monster's quantity
     */
    public int[] getMonstersQuantity() {
        return monstersQuantity;
    }

    /**
     * function that gets the monsters
     * @return list of monsters
     */
    public List<Monster> getMonsters() {
        return monsters;
    }

    /**
     * Function that gets the life points of the monsters
     * @param monsterOrder list of monsters
     * @return int array that represents their life points
     * @throws FileNotFoundException if data can't be acessed (file)
     */
    public int[] getMonstersLifePoints(List<String> monsterOrder) throws FileNotFoundException {
        // calculate array length
        int sum = 0;
        for (int k : monstersQuantity) {
            sum += k;
        }
        int[] lifePoints = new int[sum];
        CombatManager combatManager = new CombatManager();
        for (int i = 0; i < monsterOrder.size(); i++) {
            lifePoints[i] = combatManager.getMonsterLifePoints(monsterOrder.get(i));
        }

        return lifePoints;
    }

    /**
     * function that given the name of a monster, it returns the monster object
     * @param name name of the monster
     * @return monster searched
     */
    public Monster getMonster(String name) {
        int index = 0;
        for (int i = 0; i < monsters.size(); i++) {
            if (monsters.get(i).getName().equals(name)) {
                index = i;
            }
        }
        return monsters.get(index);
    }
}
