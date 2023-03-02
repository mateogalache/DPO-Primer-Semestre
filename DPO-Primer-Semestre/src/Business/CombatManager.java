package Business;

import Persistance.MonsterDAO;

import java.io.FileNotFoundException;
import java.util.*;

import static Business.QuickSort.quickSort;

public class CombatManager {

    private Combat combat;
    private Party party;
    private List<String> characterOrder; // sorted List of Character's name in pitch
    private int[] characterOrderValues; // sorted array of the initiatives calculated
    private List<String> monsterOrder;

    private int[] actualLifePoints; // array that contains the actual life points of all the criatures on pitch (sorted by initiative)


    // to get the initial party lifePoints we can check it through the party attribute (party.getLifePoints())
    private int[] partyLifePoints; // actual party lifePoints



    public CombatManager(){};

    /**
     * Function that returns a list of all the monsters
     * @return list of monsters
     * @throws FileNotFoundException if the file of monsters is not found
     */
    public List<Monster> listMonsters() throws FileNotFoundException {
        MonsterDAO monsterDAO = new MonsterDAO();
        return monsterDAO.readMonstersFromJson();
    }

    /**
     * Function that indicates if a monster is a boss or not
     * @param name name of the monster
     * @return true if monster is a boss
     * @throws FileNotFoundException if the file of monsters is not found
     */
    public boolean isBoss(String name) throws FileNotFoundException {
        List<Monster> monsterList = listMonsters();
        int index = 0;
        // first we search the boss
        for (int i = 0; i < monsterList.size(); i++) {
            if (monsterList.get(i).getName().equals(name)) {
                index = i;
            }
        }
        // return monsterType == Boss
        return monsterList.get(index).getChallenge().equals("Boss");
    }

    /**
     * function that resizes the combat quantity array length
     * @param combat combat to update
     * @param i index to copy to
     */
    public void resizeCombatQuantityLength(Combat combat, int i) {
        int[] auxArray = Arrays.copyOf(combat.getMonstersQuantity(), combat.getMonstersQuantity().length + i);
        combat.setMonstersQuantity(auxArray);
    }

    /**
     * function that removes an element from monsterquantity attribute of the class combat
     * @param combat combat to be updated
     * @param index position in the array to be deleted
     */
    public void removeElementFromArray(Combat combat, int index) {
        int[] auxArray = new int[combat.getMonstersQuantity().length];
        int j = 0;
        for (int i = 0; i < combat.getMonstersQuantity().length; i++) {
            if (i != index) {
                auxArray[j] = combat.getMonstersQuantity()[i];
                j++;
            }

        }
        combat.setMonstersQuantity(auxArray);
        resizeCombatQuantityLength(combat, -1);

    }

    /**
     * function that returns the list of names of the monsters
     * @return list of names of the monsters
     * @throws FileNotFoundException if the file is not found
     */
    public List<String> listMonsterNames() throws FileNotFoundException {
        MonsterDAO monsterDAO = new MonsterDAO();
        List<Monster> monsters = monsterDAO.readMonstersFromJson();
        List<String> monsterNames = new ArrayList<>();

        for (Monster monster : monsters) {
            monsterNames.add(monster.getName());
        }
        return monsterNames;
    }

    /**
     * function that returns the list of challenges of each monster
     * @return list of challenges
     * @throws FileNotFoundException if the file is not found
     */
    public List<String> listMonsterChallenges() throws FileNotFoundException {
        MonsterDAO monsterDAO = new MonsterDAO();
        List<Monster> monsters = monsterDAO.readMonstersFromJson();
        List<String> monsterChallenge = new ArrayList<>();

        for (Monster monster : monsters) {
            monsterChallenge.add(monster.getChallenge());
        }
        return monsterChallenge;
    }

    /**
     * function that given a list of monsters, returns the amount of bosses in the list
     * @param monsters list of monsters
     * @return number of bosses in list
     * @throws FileNotFoundException if the file is not found
     */
    public int getAmountOfBosses(List<String> monsters) throws FileNotFoundException{
        int nBosses = 0;

        for (String monster : monsters) {
            boolean boss = isBoss(monster);
            if (boss) {
                nBosses++;
            }
        }

        return nBosses;
    }

    /**
     * function that starts the party attribute
     * @param party party to be set as an attribute
     */
    public void initParty(Party party) {
        this.party = party;

    }

    /**
     * function that prepares the combat to be played, sorting order and generating life points
     * @param combat combat to be started
     * @param index index of the combat (to generate or not party life points)
     * @throws FileNotFoundException if the file of monsters is not found
     */
    public void startCombat(Combat combat, int index) throws FileNotFoundException{

        this.combat = combat;

        int[] characterValues = getCharacterOrder(party.getPersonatges());
        this.characterOrderValues = characterValues;
        int[] monsterValues = getMonsterOrder(combat.getMonsters(), combat.getMonstersQuantity());

        sortOrder(characterValues, monsterValues);
        generateLifePoints(index);

    }

    /**
     * function that sorts the monsters and characters initiative to sort them in descendent order
     * @param characterValues initiative values of the characters
     * @param monsterValues initiative values of the monsters
     */
    private void sortOrder(int[] characterValues, int[] monsterValues) {
        List<String> names = new ArrayList<>();
        List<Integer> values = new ArrayList<>();
        boolean end = false;
        int i = 0;
        int j = 0;
        while (!end) {
          if (i == characterValues.length && j == monsterValues.length) {
              end = true;
          }  else {
              if (i == characterValues.length) {
                  while (j < monsterValues.length) {
                      names.add(monsterOrder.get(j));
                      values.add(monsterValues[j]);
                      j++;
                  }
              }

             else if (j == monsterValues.length) {
                  while (i < characterValues.length) {
                      names.add(characterOrder.get(i));
                      values.add(characterValues[i]);
                      i++;
                  }
              } else {
                  if (characterValues[i] > monsterValues[j]) {
                      names.add(characterOrder.get(i));
                      values.add(characterValues[i]);
                      i++;
                  } else {
                      names.add(monsterOrder.get(j));
                      values.add(monsterValues[j]);
                      j++;
                  }
              }


          }
        }
        this.characterOrder = names;
        int[] aux = new int[values.size()];
        for (int k = 0; k < values.size(); k++) {
            aux[k] = values.get(k);
        }
        this.characterOrderValues = aux;
    }

    /**
     * function that returns the combat managed by the combat manager class
     * @return combat
     */
    public Combat getCombat() {
        return combat;
    }

    /**
     * function that sets the combat to be managed by the combat manager class
     * @param combat combat to be managed
     */
    public void setCombat(Combat combat) {
        this.combat = combat;
    }

    /**
     * function that returns the party managed by the combat manager class
     * @return party
     */
    public Party getParty() {
        return party;
    }

    /**
     * function that returns the initiative order (names)
     * @return initiative order
     */
    public List<String> getInitiativeOrder() {
        return characterOrder;
    }

    /**
     * function that returns the initiative order (values)
     * @return initiative order
     */
    public int[] getInitiativeOrderValues() {
        return characterOrderValues;
    }

    /**
     * function that returns the monsters order values sorted descendent
     * @param monsters monster names
     * @param monstersQuantity monsters quantity of each type
     * @return sorted order of monsters
     */
    private int[] getMonsterOrder(List<Monster> monsters, int[] monstersQuantity) {

        // calculate array length
        int sum = 0;
        for (int k : monstersQuantity) {
            sum += k;
        }

        String[] names = new String[sum];
        int[] values = new int[sum];

        int index = 0;
        for (int i = 0; i < monsters.size(); i++) {
            for (int j = 0; j < monstersQuantity[i]; j++) {
                names[index] = monsters.get(i).getName();
                values[index] = monsters.get(i).getInitiative() + monsters.get(i).throwD12();
                index++;
            }
        }

        /*
         * Here comes the part of sorting values to return id sorted by Inititive;
         * SORT(names, values);
         * */
        quickSort(values, names,0, values.length - 1);

        this.monsterOrder = Arrays.stream(names).toList();
        return values;
    }

    /**
     * function that returns the character order sorted descendent
     * @param personatges characters in pitch / party
     * @return order of the characters
     */
    private int[] getCharacterOrder(Personatge[] personatges) {

        String[] names = new String[personatges.length];
        int[] values = new int[personatges.length];


        for (int i = 0; i < personatges.length; i++) {
            names[i] = personatges[i].getNomPersonatge();
            values[i]  = personatges[i].getInitiative();
            //order.put(personatges[i].getNomPersonatge(),personatges[i].throwD12());
        }

        /*
        * Here comes the part of sorting values to return id sorted by Inititive;
        * */
        // sort values to merge them lately
        quickSort(values, names,0, values.length - 1);

        //order.put(names,values);
        this.characterOrder = Arrays.stream(names).toList();

        return values;
    }

    /**
     * function that generate life points of the criatures in combat
     * @param index indicates the number of the combat, if != 0 party lifepoints won't change
     * @throws FileNotFoundException if file is not found
     */
    private void generateLifePoints(int index) throws FileNotFoundException {
        if (index == 0) {
            party.setLifePoints(); // set the initial life points to the party characters
        }
        this.actualLifePoints = mergeLifePoints(party.getLifePoints(), combat.getMonstersLifePoints(monsterOrder));
        this.partyLifePoints = party.getLifePoints();
    }

    /**
     * function that merges the life points of the monsters and the life points of both monsters and characters
     * @param lifePoints life points of the characters in party
     * @param monstersLifePoints life points of the monsters
     * @return life points sorted as the same way as the initiative order
     * @throws FileNotFoundException if file is not found
     */
    private int[] mergeLifePoints(int[] lifePoints, int[] monstersLifePoints) throws FileNotFoundException {
        int[] array = new int[lifePoints.length + monstersLifePoints.length];

        for (int i = 0; i < array.length; i++) {
            // get the character name from the OrderList
            String name = characterOrder.get(i);

            // if character -> search his lifePoints (from party)
            if (!isMonster(name)) {
                array[i] = party.getLifePoints(name);
            } else {
                // if Monster -> search his lifepoints
                array[i] = getMonsterLifePoints(name);
            }

        }

        return array;
    }

    /**
     * function that gets the life points / hit points of a monster given its name
     * @param monsterName name of the monster
     * @return lifepoints of the monster
     * @throws FileNotFoundException if the file is not found
     */
    public int getMonsterLifePoints(String monsterName) throws FileNotFoundException{
        List<Monster> monsters = listMonsters();
        int value = 0;
        for (Monster monster: monsters) {
            if (monster.getName().equals(monsterName)) {
                value = monster.getHitPoints();
            }
        }
        return value;
    }

    /**
     * function that returns the life points of the party
     * @return party life points
     */
    public int[] getPartyLifePoints() {
        return partyLifePoints;
    }

    /**
     * function that given an attacker, returns the index of the receiver
     * @param attacker intdex of attacker (in characterOrder array)
     * @return index of the receiver (in characterOrder array)
     * @throws FileNotFoundException if the file is not found
     */
    public int getReceiver(int attacker) throws FileNotFoundException{
        int receiver = 0;
        String attackerName = characterOrder.get(attacker);
        // get attacker type (character/monster)
        boolean isMonster =  isMonster(attackerName);

        // if character -> attack Monster
        if (!isMonster) {
            // get a random character  -- Has to be a Monster
            if (!allMonsterDies()) {
                Random random = new Random();
                receiver = random.nextInt(0, characterOrder.size());
                while (!isMonster(characterOrder.get(receiver)) || isInconscient(receiver)) {
                    receiver = random.nextInt(characterOrder.size());
                }
            } else {
                receiver = -1; // value that indicates all monsters are dead so you can't attack any
            }
        }
        // if Monster -> attack Character
        if (isMonster) {
            // get a random character  -- Has to be a Character
            if (!allCharactersDies()) {
                Random random = new Random();
                receiver = random.nextInt(0, characterOrder.size());
                while (isMonster(characterOrder.get(receiver)) || isInconscient(receiver)) {
                    receiver = random.nextInt(characterOrder.size());
                }
            } else {
                receiver = -2;
            }
        }

        return receiver;
    }

    /**
     * function that indicates if a criature in pitch is conscient or not
     * @param receiver index of the criature
     * @return ture if criature is inconscient
     */
    public boolean isInconscient(int receiver) {
        return actualLifePoints[receiver] <= 0;
    }

    /**
     * function that indicates if a criature is or not a monster
     * @param attackerName name of the criature
     * @return true if the criature is a monster
     * @throws FileNotFoundException if the file is not found
     */
    public boolean isMonster(String attackerName) throws FileNotFoundException{
        MonsterDAO monsterDAO = new MonsterDAO();
        List<Monster> monsters = monsterDAO.readMonstersFromJson();

        boolean found = false;
        for (Monster monster: monsters) {
            if (monster.getName().equals(attackerName)) {
                found = true;
                break;
            }
        }
        return found;
    }

    /**
     * function that returns the character order (names)
     * @return character order
     */
    public List<String> getCharacterOrder() {
        return characterOrder;
    }

    /**
     * function that simulates an atac done by a criature (character/ monster)
     * @param name name of the criature
     * @return damage dealed
     * @throws FileNotFoundException if the file is not found
     */
    public int simulateAttack(String name) throws FileNotFoundException{
        int damage;
        if (isMonster(name)) {
            // search in monsters
            damage = combat.getMonster(name).atac();
        } else {
            // search in party
            damage = party.tryToAttack(name);
        }
       return damage;
    }

    /**
     * function that tries to attack(character type)
     * @return value of the attack
     */
    public int tryAttack() {
        int impact = switch (party.getPersonatges()[0].throwD10()) {
            case 1 -> 0;
            case 10 -> 2;
            default -> 1;
        };
        return impact;
    }

    /**
     * function that given a name, returns the type of attack that is going to be
     * @param name name of the criature
     * @return type of attack
     * @throws FileNotFoundException if the file is not found
     */
    public String getTypeOfAttack(String name) throws FileNotFoundException{
        String type = "";
        if (isMonster(name)) {
             type = getCombat().getMonster(name).getDamageType();
        } else {
            for (int i = 0; i < getParty().getPersonatges().length; i++) {
                if (getParty().getPersonatges()[i].getNomPersonatge().equals(name)) {
                     type = getParty().getPersonatges()[i].getDamageType();
                }
            }
        }
        return type;
    }

    /**
     * function that update life points in combat
     * @param damage damage dealed
     * @param receiver index of the criature that recives the damage
     * @param typeOfAttack type of damage done
     */
    public void updateLifePoints(int damage, int receiver, String typeOfAttack) {
        actualLifePoints[receiver] = actualLifePoints[receiver] - damage;

        if (actualLifePoints[receiver] < 0) {
            actualLifePoints[receiver] = 0;
        }


    }

    /**
     * function that returns the actual life points of all criatures
     * @return actual life points
     */
    public int[] getActualLifePoints() {
        return actualLifePoints;
    }

    /**
     * function that indicates if the combat is finished
     * @return integer that indicates the state of the combat (No ended: 0, player wins: 1, monster wins: 2)
     * @throws FileNotFoundException if the file is not found
     */
    public int endOfCombat() throws FileNotFoundException{
        // 0 == No End Of Combat
        int value = 0;

        if(allMonsterDies()) {
            // 1 == Player Wins
            value = 1;
        } else {
            if (allCharactersDies()) {
                // 2 == Monster Wins
                value = 2;
            }
        }

        return value;
    }

    /**
     * function that indicates if party is defeated
     * @return true if party is defeated
     * @throws FileNotFoundException if the file is not found
     */
    private boolean allCharactersDies() throws FileNotFoundException{
        boolean value = true; // initially all monsters are dead
        for (int i = 0; i < actualLifePoints.length; i++) {
            if (!isMonster(characterOrder.get(i)) && actualLifePoints[i] > 0) {
                // if we find a criature in pitch that is a Character and its still alive (lifepoints > 0)
                value = false; // NoEndOfCombat
            }
        }

        return value;
    }

    /**
     * function that indicates if all monsters are defeated
     * @return true if all monsters are defeated
     * @throws FileNotFoundException if the file is not found
     */
    private boolean allMonsterDies() throws FileNotFoundException{
        boolean value = true; // initially all monsters are dead
        for (int i = 0; i < actualLifePoints.length; i++) {
            if (isMonster(characterOrder.get(i)) && actualLifePoints[i] > 0) {
                // if we find a criature in pitch that is a Monster and its still alive (lifepoints > 0)
                value = false; // NoEndOfCombat
            }
        }

        return value;
    }

    /**
     * function that returns the actual party life points
     * @return actual party life points
     * @throws FileNotFoundException if the file is not found
     */
    public int[] getActualPartyLifePoints() throws FileNotFoundException {
        int[] lifePoints = new int[getPartyLifePoints().length];
        // for each character in party:
        for (int i = 0; i < getParty().getPersonatges().length; i++) {
            // search character in characterOrder
            for (int j = 0; j < characterOrder.size(); j++) {
                // when we found the character:
                if (party.getPersonatges()[i].getNomPersonatge().equals(characterOrder.get(j))) {
                    // then -> update the lifepoints[i]
                    lifePoints[i] = getActualLifePoints()[j];
                }
            }
        }

        return lifePoints;
    }

    /**
     * function that calculates xp points to be winned by characters
     * @return xp points
     * @throws FileNotFoundException if the file is not found
     */
    public int calculateXPPoints() throws FileNotFoundException {
        int sum = 0;
        for (String s : monsterOrder) {
            sum += getMonsterXP(s);
        }
        return sum;
    }

    /**
     * function that returns the xp points that a monsters gives when it dies
     * @param monsterName name of the monster
     * @return xp points
     * @throws FileNotFoundException if the file is not found
     */
    private int getMonsterXP(String monsterName) throws FileNotFoundException{
        List<Monster> monsters = listMonsters();
        int value = 0;
        for (Monster monster: monsters) {
            if (monster.getName().equals(monsterName)) {
                value = monster.getExperience();
            }
        }
        return value;
    }

    /**
     * function that updates xp points of the characters in party
     * @param name name of the character
     * @param xp xp to be added
     * @return true if character levels up
     */
    public boolean updateXPPoints(String name, int xp) {
        Personatge personatge = getCharacter(name);
        boolean levelup = levelUp(personatge.getXpPoints(), xp);


        personatge.increaseXPPoints(xp);
        return levelup;
    }

    /**
     * function that given the actual experience and the new xp to be added, indicates if the character levels up
     * @param xpPoints actual xp of the character
     * @param xp new xp to be added
     * @return ture if level up
     */
    private boolean levelUp(int xpPoints, int xp) {
        int level = xpPoints / 100; // actual level
        int newLevel = (xpPoints + xp) / 100; // new level
        return newLevel > level;
    }

    /**
     * function that given the name of the character, returns the character
     * @param name name of the character
     * @return character to be returned
     */
    public   Personatge getCharacter(String name) {
        int index = 0;
        for (int i = 0; i < party.getPersonatges().length; i++) {
            if (party.getPersonatges()[i].getNomPersonatge().equals(name)) {
                index = i;
            }
        }
        return party.getPersonatges()[index];
    }

    /**
     * function that given the name of a character returns its experience
     * @param name name of the character
     * @return xp
     */
    public int getCharacterXP(String name) {
        Personatge personatge = getCharacter(name);
        return personatge.getNivellInicial();
    }

    /**
     * function that given the name of a character returns its heal points
     * @param name name of the character
     * @return heal points
     */
    public int getHealPoints(String name) {
        Personatge personatge = getCharacter(name);
        return personatge.restStageAction();
    }

    /**
     * function that given the name of a character returns its rest stage action
     * @param name name of the character
     * @return rest stage action
     */
    public String getRestStageAction(String name) {
        Personatge personatge = getCharacter(name);
        return personatge.getRestAction();
    }

    /**
     * function that adds life points to the character in party
     * @param index index of the character
     * @param healPoints heal points to add
     * @param name name of the character
     */
    public void addLifePoints(int index, int healPoints, String name) {
        actualLifePoints[index] = actualLifePoints[index] + healPoints;
        // this is to not overpass lifepoints
        if (actualLifePoints[index] > getParty().getLifePoints(name)) {
            actualLifePoints[index] = getParty().getLifePoints(name);
        }
    }

    /**
     * function that reads all monsters disponible
     * @throws FileNotFoundException if the file is not found
     */
    public void readMonsters() throws FileNotFoundException {
        MonsterDAO monsterDAO = new MonsterDAO();
        List<Monster> monsters = monsterDAO.readMonstersFromJson();
    }

    /**
     * function that creates a new combat
     * @param combatIndex index of the combat
     * @param monsters list of monters of the combat
     * @param monsterQuantity array of monsters quantity
     * @return new combat
     * @throws FileNotFoundException if the file is not found
     */
    public Combat createCombat(int combatIndex, List<String> monsters, List<Integer> monsterQuantity) throws FileNotFoundException{
        MonsterDAO monsterDAO = new MonsterDAO();
        List<Monster> monsterList = new ArrayList<>();

        for (String monsterName: monsters) {
            monsterList.add(monsterDAO.getMonster(monsterName));
        }
        int[] quantity = new int[monsterQuantity.size()];
        for (int i = 0; i < monsterQuantity.size(); i++) {
            quantity[i] = monsterQuantity.get(i);
        }

        return new Combat(combatIndex, monsterList, quantity);
    }

    /**
     * Function that updates a combat
     * @param index index of the combat
     * @param monsters new list of monsters
     * @param quantity new list of quantity of each monster
     */
    public void updateCombat(int index, List<String> monsters, List<Integer> quantity) {
        // 1st Remove monster form List
        combat.getMonsters().remove(index);

        // 2nd Remove monster amount from monsterQuantity[]
        removeElementFromArray(combat, index);

        // update lists
        monsters.remove(index);
        quantity.remove(index);
    }
}
