package Presentation;

import Business.*;
import Business.Personatge;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import static java.lang.Boolean.FALSE;

public class Controller {
    //private PersonatgeManager personatgeManager;

    private Menu menu;
    private static final String CHARACTERS_FILE_NAME = "characters.json";
    private static final String MONSTERS_FILE_NAME = "monsters.json";
    private static final String ADVENTURES_FILE_NAME = "adventures.json";
    private static final int MAX_ENCOUNTERS = 4;
    private static final int MAX_ERRORS = 3;

    /**
     * Function that calls the menu class and checks the option of the menu is correct
     * @throws IOException
     */
    public void run() throws IOException{
        menu = new Menu();
        int option = 0;
        if(loadData()) {
            menu.dataSuccessfullyLoaded();
            while (option != 5) {
                boolean ableToPlay = ableToPlayAdventures();
                option = menu.InitialMenu(1, 5, ableToPlay);
                switchOperation(option);
                //menu.nextLine();
            }

        }
    }

    /**
     * Function that call all the funcitons depending the option chosed in the menu with a switch
     * @param option option chosen in the menu
     * @throws IOException
     */
    private void switchOperation(int option) throws IOException {
            PersonatgeManager personatgeManager = new PersonatgeManager();
            switch (option) {
            case 1: {
                menu.createCharacter();
                String name = menu.askCharacterName();
                if (personatgeManager.checkIfCharacterNameExists(name) == FALSE) {

                    String playerName = menu.askPlayerName(name);
                    int level = menu.askLevel();
                    menu.generatingStats(level);

                    int[] bodyValues = personatgeManager.generateStat();
                    menu.showSingleStat("body", bodyValues[0], bodyValues[1]);

                    int[] mindValues = personatgeManager.generateStat();
                    menu.showSingleStat("mind", mindValues[0], mindValues[1]);

                    int[] spiritValues = personatgeManager.generateStat();
                    menu.showSingleStat("spirit", spiritValues[0], spiritValues[1]);

                    int body = bodyValues[2];
                    int mind = mindValues[2];
                    int spirit = spiritValues[2];

                    menu.listStats(body, mind, spirit);

                    menu.nextClass();
                    String className0 = menu.askClass();
                    String className = menu.classMessage(level,className0);
                    menu.finalMessage(name);

                    personatgeManager.createAdventurer(name, playerName, level, body, mind, spirit, className);
                } else {
                    menu.nameNotDisponible();
                }
                break;
            }
            case 2: {
                // function that asks the player's name to
                String player = menu.listCharactersMessage();

                // function that gets the list of names that we'll show (take care it can be null)
                List<String> characterNames = listCharacters(player, personatgeManager);
                if (characterNames != null) {
                    int characterIndex = menu.meetCharacter(characterNames);

                    // operem si l'opcio no és "back"
                    if (characterIndex > 0) {
                        characterIndex--; // substract 1 to operate easily

                        listCharacterInfo(personatgeManager, characterIndex, characterNames);

                        // we offer the chance to delete character
                        // askToDeleteCharacter mostrara els missatges de si volem borrar i gestionara si hem triat 'si' o 'no'
                        boolean delete = askToDeleteCharacter(personatgeManager.getCharacterFromIndex(characterIndex, characterNames).getNomPersonatge());
                        if (delete) {
                            // deleteCharacter pasara al personatge manager i posteriorment al personatge DAO el nom del personatge a esborrar
                            menu.characterLeftTheGuild(personatgeManager.getCharacterFromIndex(characterIndex, characterNames).getNomPersonatge());
                            personatgeManager.deleteCharacter(personatgeManager.getCharacterFromIndex(characterIndex, characterNames).getNomPersonatge());
                        }
                    }
                }
                break;
            }
            case 3: {
                // we firstAsk the adventure's Name
                menu.planningAnAdventure();
                String adventureName = menu.nameAdventure();
                if (checkAdventureName(adventureName)) {
                    // continue with the process
                    menu.adventureNameCorrect(adventureName);

                    // ask amount of encounters for the Adventure
                    int encounters = menu.askEncounters(MAX_ENCOUNTERS, MAX_ERRORS);
                    if (encounters != 0) {
                        menu.nEncounters(encounters);
                        // create every Encounter
                        AdventureManager adventureManager = new AdventureManager();

                        createAdventure(adventureName, adventureManager, encounters);
                    }
                } else {
                    // show error message and exit
                    menu.adventureNameNotAble();
                }
                break;
            }
            case 4: {
                // Play an adventure
                AdventureManager adventureManager = new AdventureManager();

                int index = menu.playAdventureMenu(adventureManager.getAdventureNames()) -1; // ask the adventure we are going to play

                // 1st init party
                int partySize = menu.askPartySize(adventureManager.getAdventures().get(index).getName()); // ask the size of the party
                menu.askCharacterMessage(partySize);
                Party party = createParty(partySize, personatgeManager);

                // 2nd start to play the Adventure
                menu.startAdventure(adventureManager.getAdventures().get(index).getName());
                playAdventure(party, adventureManager, index);

                break;
            }
            case 5: {
                menu.exitMessage();
                break;
            }
        }
    }

    /**
     * Function that calls all the functions that plays the adventure
     * @param party Party chosed to start the adventure
     * @param adventureManager Manager of the adventure
     * @param index number of adventure
     * @throws IOException
     */
    private void playAdventure(Party party, AdventureManager adventureManager, int index) throws IOException {
        // now you are playing an adventure with your party!

        // loop for each combat
        CombatManager combatManager = new CombatManager();
        combatManager.initParty(party);
        for (int i = 0; i < adventureManager.getAdventures().get(index).getNumberOfCombats(); i++) {
            // startOfCombat Message
            menu.startOfCombat(i, adventureManager.getAdventures().get(index).getCombats().get(i));

            // Preparation Stage
            preparationStage(party);
            menu.preparationStage(party);


            combatManager.startCombat(adventureManager.getAdventures().get(index).getCombats().get(i), i);

            menu.initiativeOrder(combatManager.getInitiativeOrder(),combatManager.getInitiativeOrderValues());

            // start of combat
            menu.combatStage();

            int indexOfCombat = 1;
            boolean endOfCombat = false;

            int roundIndex = 1;
            while (!endOfCombat) {
                boolean endOfRound = false;
                menu.partyStats(party, indexOfCombat, combatManager.getActualPartyLifePoints(), combatManager.getParty().getLifePoints());

                // atac while !endOfRound
                int attacker = 0;
                while (!endOfRound) {

                    int receiver = combatManager.getReceiver(attacker);

                        List<String> criatures = combatManager.getCharacterOrder();

                        // show damageDealer and damageReceiver
                        menu.combatActions(criatures.get(attacker), criatures.get(receiver), combatManager.isMonster(criatures.get(attacker)));

                        // simulate attack Action
                        int damage = simulateAttackAction(combatManager, criatures, attacker, 0);

                        updateLifePoints(damage, receiver, combatManager, attacker, criatures);

                        // iterate attacker index

                    if (combatFinished(combatManager.endOfCombat())) {
                        displayEndOfCombatMessage(combatManager.endOfCombat(), roundIndex);
                        endOfRound = true;
                        endOfCombat = true;

                    } else {
                        attacker++;
                        if (attacker == combatManager.getCharacterOrder().size()) {
                            menu.endOfRound(roundIndex);
                            endOfRound = true;
                            roundIndex++;

                        }
                    }
                }
                indexOfCombat++;
            }


            updateExperience(combatManager);
            menu.nextLine();

            if (combatManager.endOfCombat() == 2) {
                // means that the party has lost
                break; // stop the loop of each combat
            } else  {
                // characters increaselifepoints
                increaseActualLifePoints(combatManager.getCharacterOrder(), combatManager);
            }
        }

        if (combatManager.endOfCombat() != 2) {
            menu.nextLine();
            menu.finalResult(party, party.getLifePoints(),adventureManager.getAdventures().get(index).getName());
        }



    }

    /**
     * Function that checks if characters are dead or not in the rest stage to get heal
     * @param criatures list of the criatures names
     * @param combatManager combat manager to call each character that is in the combat
     * @throws FileNotFoundException
     */
    private void increaseActualLifePoints(List<String> criatures, CombatManager combatManager) throws FileNotFoundException {
        int index = 0;
        for (String name: criatures) {
            if (!combatManager.isMonster(name)) {
                if (!combatManager.isInconscient(index)) {
                    // get information to display
                    String restAction = combatManager.getRestStageAction(name);
                    int healPoints = combatManager.getHealPoints(name);
                    // update values
                    combatManager.addLifePoints(index,healPoints, name);
                    // show message
                    menu.consciousCharacterRestStage(name, restAction, healPoints);
                } else {
                    // show message
                    menu.unconsciousCharacterRestStage(name);
                }
            }
            index++;
        }
    }

    /**
     * Function that updates the experience of the character and checks if it has to level up
     * @param combatManager combat manager to call each character that is in the combat
     * @throws FileNotFoundException
     */
    private void updateExperience(CombatManager combatManager) throws FileNotFoundException{
        int xp = combatManager.calculateXPPoints();
        // for each criature on pitch
        for (int i = 0; i < combatManager.getCharacterOrder().size(); i++) {
            // if it's a character
             if (!combatManager.isMonster(combatManager.getCharacterOrder().get(i))) {
                 // update it's experience and display message
                 boolean levelUp = combatManager.updateXPPoints(combatManager.getCharacterOrder().get(i), xp);
                 int level = combatManager.getCharacterXP(combatManager.getCharacterOrder().get(i));

                 menu.updateXPPoints(combatManager.getCharacterOrder().get(i), xp, levelUp, level);

             }
        }
    }

    /**
     * Function that calls the messages for the end of the combat
     * @param endOfCombat if it is 1 the player won, if it is 2 the player lost
     * @param roundIndex number of round
     */
    private void displayEndOfCombatMessage(int endOfCombat, int roundIndex) {
        if (endOfCombat == 1) {
            menu.endOfRound(roundIndex);
            menu.enemiesDefeated();

            // brief short rest menu
            menu.shortRestStage();
        }

        if (endOfCombat == 2) {
            menu.TPU();
        }
    }

    /**
     * Function that checks if the combat has finished
     * @param endOfCombat int that if is different to 0 it means the combat finished
     * @return
     */
    private boolean combatFinished(int endOfCombat) {
        return endOfCombat != 0;
    }

    /**
     * Updates the life points of the character/monster when they get hit and checks if dies or not.
     * @param damage damage of the attack
     * @param receiver  damage received
     * @param combatManager combat manager to select character/monster that are in the combat
     * @param attacker  number of the attacker
     * @param criatures list of the criatures
     * @throws FileNotFoundException
     */
    private void updateLifePoints(int damage, int receiver, CombatManager combatManager, int attacker, List<String> criatures) throws FileNotFoundException {
        // Update damage receiver lifepoints
        combatManager.updateLifePoints(damage, receiver, combatManager.getTypeOfAttack(criatures.get(attacker)));
        // Check if its inconscient
        if (combatManager.isInconscient(receiver)) {
            if (combatManager.isMonster(criatures.get(receiver))) {
                menu.monsterDies(criatures.get(receiver));
            } else {
                menu.characterIsInconscient(criatures.get(receiver));
            }
        }

    }

    /**
     * Function that call the combat action and check if the attack fails or not.
     * @param combatManager combat manager that selects the characters/monster in the combat
     * @param criatures list of the criatures
     * @param attacker number of the attacker
     * @param damage damage of the attack
     * @return
     * @throws FileNotFoundException
     */
    private int simulateAttackAction(CombatManager combatManager, List<String> criatures, int attacker, int damage) throws FileNotFoundException {
        int impact = combatManager.tryAttack();

        if (impact > 0) {
            // show attack done effectively
            damage = combatManager.simulateAttack(criatures.get(attacker)) * impact;
            menu.attackHits(damage, combatManager.getTypeOfAttack(criatures.get(attacker)), impact == 2);
        } else {
            // show attack failed
            menu.attackFails(damage, combatManager.getTypeOfAttack(criatures.get(attacker)));
        }

        return damage;
    }

    /**
     * Function that get all the conscious characters/monsters of the adventure party.
     * @param party Party chose
     */
    private void preparationStage(Party party) {
        // for each character
        for (int i = 0; i < party.getPersonatges().length; i++) {
            // if it's conscient:
            if (party.getConscient()[i]) {
                party.getPersonatges()[i].supportAction();
            }
        }
    }

    /**
     * Function that creates the adventure party and calls the functions that show the party.
     * @param partySize size of the party
     * @param personatgeManager manager of the character to select the ones in the party
     * @return
     * @throws FileNotFoundException
     */
    private Party createParty(int partySize, PersonatgeManager personatgeManager) throws FileNotFoundException{
        Party party = new Party(partySize);

        for (int i = 0; i < partySize; i++) {
            // 1st show Actual status of Party
            menu.showParty(party, i, partySize);
            // 2nd ask user to select the character to add
            int character = menu.listCharacters(personatgeManager.getAllCharacters(), i + 1) - 1;

            // 3rd check i character already exists in party
            boolean alreadyExists = false;
            Personatge personatge = personatgeManager.getCharacterFromIndex(character, personatgeManager.getAllCharacters());
            alreadyExists = personatgeManager.inListCharacter(personatge.getNomPersonatge(), Arrays.asList(party.getPersonatges()));

            while (alreadyExists) {
                menu.characterAlreadyInParty(personatge.getNomPersonatge());
                character = menu.listCharacters(personatgeManager.getAllCharacters(), i + 1) - 1;
                personatge = personatgeManager.getCharacterFromIndex(character, personatgeManager.getAllCharacters());
                alreadyExists = personatgeManager.inListCharacter(personatge.getNomPersonatge(), Arrays.asList(party.getPersonatges()));
            }

            // 4rt we create the "Personatge" as the type it is (by now, it will only be a "Warrior")
            Personatge finalCharacter = initCharacter(personatge);

            // 5ft add the character previously selected to the party
            party.setPersonatge(finalCharacter, i);
            party.setConscient(i, true);
        }
        // final print of the party status
        menu.showParty(party, partySize, partySize);

        return party;
    }

    /**
     * Function that returns a new character if it's an adventurer
     * @param personatge the character
     * @return
     */
    private Personatge initCharacter(Personatge personatge) {
        // function that will init the character as the type it is
        if (personatge.getTipusPersonatge().equals("Adventurer")) {
            return new Adventurer(personatge);
        } else {
            // this might cause an error in the future, but not by now
            // in next Fases it'll be a switch
            return null;
        }
    }

    /**
     * Function that creates adventure and the number of combat in depending on encounters
     * @param adventureName name of the adventure
     * @param adventureManager adventure manager
     * @param encounters number of encounters that the user selects
     * @throws IOException
     */
    private void createAdventure(String adventureName, AdventureManager adventureManager, int encounters) throws IOException{


        int index = adventureManager.getAdventures().size();


        // for each encounter/combat
        for (int i = 0; i < encounters; i++) {
            //
            CombatManager combatManager = new CombatManager();
            createCombat(adventureManager, combatManager, i, encounters);

        }

        adventureManager.createAdventure(adventureName, adventureManager.getCombats());
    }

    /**
     * Function that creates the combat
     * @param adventureManager adventure manager
     * @param combatManager combat manager
     * @param combatIndex number of combat
     * @param encounters number of encounters
     * @throws FileNotFoundException
     */
    private void createCombat(AdventureManager adventureManager, CombatManager combatManager, int combatIndex, int encounters) throws FileNotFoundException{

        List<String> monsters = new ArrayList<>();
        List<Integer> monsterQuantity = new ArrayList<>();
        boolean empty = true;
        int option = 0;
        // while we don't want to stop adding/removing monsters || we haven't add any monster
        while (option != 3 || combatManager.getCombat().getMonsters().isEmpty()) {
            menu.encounterMenu(combatIndex+1, encounters, monsters, monsterQuantity);
            option = menu.combatManagerMenu();
            switch (option) {
                case 1: { // add Monsters

                    // first list all the monsters
                    menu.showMonsterList(combatManager.listMonsterNames(), combatManager.listMonsterChallenges());
                    // then ask monster index
                    int monsterIndex = menu.askMonsterIndex(combatManager.listMonsters().size());

                    boolean add = addMonstersToCombat(monsters, monsterIndex);
                    if (add) {
                        // after ask monster quantity
                        int monsterAmount = menu.askMonsterQuantity(combatManager.listMonsters().get(monsterIndex).getName());
                        int length = monsterQuantity.size();

                        addMonsterQuantityToCombat(monsterQuantity, monsters, monsterIndex, monsterAmount, combatManager);

                        // Now we have the list of Monsters and it's Quantity

                        combatManager.setCombat(combatManager.createCombat(combatIndex, monsters, monsterQuantity));

                        empty = false;
                    }
                    break;
                }
                case 2: { // remove Monsters
                    if (empty) {
                        // Error: You haven't added monsters already
                        menu.errorNoAddedMonsters();
                    } else {
                        menu.encounterMenu(combatIndex+1, encounters, monsters, monsterQuantity);
                        int index = menu.askMonsterQuantity(combatManager.getCombat().getMonsters().size()) - 1;
                        menu.deletedFromEncounter(combatManager.getCombat().getMonsters().get(index).getName(), combatManager.getCombat().getMonstersQuantity()[index]);
                       // combatManager.setCombat(combatManager.); //= updateEncounter(combat);
                        combatManager.updateCombat(index, monsters, monsterQuantity);
                        if (combatManager.getCombat().getMonsters().size() == 0) {
                            empty = true;
                        }

                    }
                    break;
                }
                case 3: { // continue
                    if (monsters.isEmpty()) {
                        //Error: You haven't added monsters already
                        menu.errorNoAddedMonsters();
                    } else {
                        //combats.add(createEncounter(i));
                        ///combats.add(combat);
                        adventureManager.addCombat(combatManager.getCombat()); // add new combat to the list
                    }
                    break;
                }
            }
        }
    }

    /**
     * Function that add the number of monsters and checks if it is a boos and if it is in the list
     * @param monsterQuantity list of number of each monster
     * @param monsters list of the monster names
     * @param monsterIndex number of the monster in the list
     * @param monsterAmount monster amount put by user
     * @param combatManager combat manager
     * @throws FileNotFoundException
     */
    private void addMonsterQuantityToCombat(List<Integer> monsterQuantity, List<String> monsters, int monsterIndex, int monsterAmount, CombatManager combatManager) throws FileNotFoundException {
        // get the name of the monster to operate with him
        String monsterName = combatManager.listMonsters().get(monsterIndex).getName();
        // check that the monster is not a boss
        if (combatManager.isBoss(monsterName) && monsterAmount > 1) {
            // show error message and exit
            menu.bossLimitReached();
            // search index in list
            int index = 0;
            for (int j = 0; j < monsters.size() ; j++) {
                if (monsters.get(j).equals(monsterName)) {
                    index = j;
                }
            }

            monsters.remove(index);
        } else {
            // search the monster in the list and update
            boolean found = false;
                for (int i = 0; i < monsterQuantity.size(); i++) {
                    if (monsterName.equals(monsters.get(i))) {
                        found = true;
                        // update the QuantityList
                        monsterQuantity.set(i,monsterQuantity.get(i) + monsterAmount);
                    }
                }

            if (!found) {
                monsterQuantity.add(monsterAmount);
            }

        }
    }

    /**
     * Function that checks if a monster con be added or not to the combat
     * @param monsters list of monsters
     * @param monsterIndex number of monster in the list
     * @return returns the boolean 'add' that says if a monster can be added or not
     * @throws FileNotFoundException
     */
    private boolean addMonstersToCombat(List<String> monsters, int monsterIndex) throws FileNotFoundException{
        // Variables:
        CombatManager combatManager = new CombatManager();
        boolean boss;
        boolean add = true;
        // now check either it's or not a boss
        boss = combatManager.isBoss(combatManager.listMonsterNames().get(monsterIndex));
        if (boss) {
            // check amount of Bosses in List
            //int nBosses = combatManager.getAmountOfBosses(monsters);
            if (combatManager.getAmountOfBosses(monsters) >= 1) {
                // show error message, boss limit already reached
                menu.bossLimitReached();
                add = false;
            }
        }

        // now check that it don't exist already
        if (monsters.contains(combatManager.listMonsterNames().get(monsterIndex)) == FALSE && add) {
            monsters.add(combatManager.listMonsterNames().get(monsterIndex));
        }

        return add;
    }

    /**
     * Function that checks if the adventure name is correct
     * @param adventureName adventure name
     * @return boolean that says if it's correct or not
     * @throws FileNotFoundException
     * @throws IOException
     */
    private boolean checkAdventureName(String adventureName) throws FileNotFoundException, IOException{
        AdventureManager adventureManager = new AdventureManager();
        return adventureManager.checkName(adventureName);
    }

    /**
     * Function that calls functions that show message when the user want to delete a character
     * @param nomPersonatge name of the character
     * @return returns if the character is goign to be deleted or not
     */
    private boolean askToDeleteCharacter(String nomPersonatge) {
        boolean answer = false;
        String response = menu.askToDeleteCharacter(nomPersonatge);

        // To make the program case Insensitive, we pass both strings to UpperCase
        response = response.toUpperCase();
        nomPersonatge = nomPersonatge.toUpperCase();

        if (response.equals(nomPersonatge)) {
            answer = true;
            menu.characterHasToLeave();
        }

        return answer;
    }

    /**
     * Function that shows the information of the character selected
     * @param personatgeManager character manager
     * @param characterIndex number of character
     * @param characterNames list of the character names
     * @throws FileNotFoundException
     */
    private void listCharacterInfo(PersonatgeManager personatgeManager, int characterIndex, List<String> characterNames) throws FileNotFoundException{

        menu.showStringAtribute("Name:   ", personatgeManager.getCharacterFromIndex(characterIndex, characterNames).getNomPersonatge());
        menu.showStringAtribute("Player: ", personatgeManager.getCharacterFromIndex(characterIndex, characterNames).getNomJugador());
        menu.showStringAtribute("Class:  ", personatgeManager.getCharacterFromIndex(characterIndex, characterNames).getTipusPersonatge());
        menu.showIntegerAtribute("Level:  ", personatgeManager.getCharacterFromIndex(characterIndex, characterNames).getNivellInicial());
        menu.showIntegerAtribute("XP:     ", personatgeManager.getCharacterFromIndex(characterIndex, characterNames).getXpPoints());
        menu.showIntegerAtribute("Body:   ", personatgeManager.getCharacterFromIndex(characterIndex, characterNames).getBody());
        menu.showIntegerAtribute("Mind:   ", personatgeManager.getCharacterFromIndex(characterIndex, characterNames).getMind());
        menu.showIntegerAtribute("Spirit: ", personatgeManager.getCharacterFromIndex(characterIndex, characterNames).getSpirit());
    }


    /**
     * Function that calls the 'showing character list' function depending if the list has names or not
     * @param playerName name of the plyer
     * @param personatgeManager character manager
     * @return returns the list of the names of the characters
     * @throws FileNotFoundException
     */
    private List<String> listCharacters(String playerName, PersonatgeManager personatgeManager) throws FileNotFoundException{
        List<String> names = new ArrayList<>();
        if (Objects.equals(playerName, "")) {
            names = personatgeManager.getAllCharacters();

        } else {
            names = personatgeManager.getCharactersFromPlayer(playerName);

        }
        if (names != null) {
            menu.listNames(names);
        }
        if (names == null) {
            menu.errorListingCharacters();
        }
        return names;
    }


    /**
     * Function that checks if the data from json can be read or not.
     */
    private boolean loadData() {
        boolean character;
        boolean monster;
        boolean adventures;
        // Carreguem el fitxer de personatges
        try {
            PersonatgeManager manager = new PersonatgeManager();
            manager.getAllCharacters();
            // carrguem el fitxer de Monstres
            character = true;

        } catch (FileNotFoundException e) {
            menu.errorLoadingFiles(CHARACTERS_FILE_NAME);
            character = false;
        }

        try {
            CombatManager combatManager = new CombatManager();
            combatManager.readMonsters();
            monster = true;
        } catch (FileNotFoundException exception) {
            menu.errorLoadingFiles(MONSTERS_FILE_NAME);
            monster = false;
        }

        // Carreguem el fitxer d'aventures
        try {
            AdventureManager adventureManager = new AdventureManager();
            adventureManager.readAdventures();
            adventures = true;
        } catch (FileNotFoundException e) {
            menu.errorLoadingFiles(ADVENTURES_FILE_NAME);
            adventures = false;
        }
        return character && monster && adventures;
    }


    /**
     * Function that checks if the player can play adventures
     * @return if the adventure could have been created
     * @throws FileNotFoundException
     */
    private boolean ableToPlayAdventures() throws FileNotFoundException{
        AdventureManager adventureManager = new AdventureManager();
        return adventureManager.ableToPlayAdventure();
    }

}

