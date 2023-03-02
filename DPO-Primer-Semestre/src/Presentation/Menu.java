package Presentation;

import Business.*;

import java.util.*;

import static java.lang.Boolean.FALSE;

public class Menu {
    public Menu(){}

    public void simpleRPG() {
        /*
         System.out.println("____  _  _      ____  _     _____   ____  ____  _____");
        System.out.println("/ ___\/ \/ \__/|/  __\/ \   /  __/  /  __\/  __\/  __/");
        System.out.println("|    \| || |\/|||  \/|| |   |  \    |  \/||  \/|| |  _");
        System.out.println("\___ || || |  |||  __/| |_/\|  /_   |    /|  __/| |_//");
        System.out.println("\____/\_/\_/  \|\_/   \____/\____\  \_/\_\\_/   \____\");
        */
    }

    /**
     *  Function that shows the initial menu and the options available to the user
     * @param min integer that describes the minimum value option
     * @param max integer that describes the maximum value option
     * @param ableToPlay boolean that indicates if player is able to play an adventure
     * @return integer that indicates the option choosed
     */
    public int InitialMenu(int min, int max, boolean ableToPlay) {
        System.out.println("The tavern keeper looks at you and says:");
        System.out.println("Welcome adventurer! How can i help you?\n");

        System.out.println("\t1) Character creation");
        System.out.println("\t2) List characters");
        System.out.println("\t3) Create an adventure");
        if (ableToPlay) {
            System.out.println("\t4) Start an adventure");
            System.out.println("\t5) Exit");

            System.out.print("\nYour answer: ");
            return askOption(min, max);
        } else {
            System.out.println("\t4) Start an adventure (disabled: create 3 characters first)");
            System.out.println("\t5) Exit");

            System.out.print("\nYour answer: ");
            int option = askOption(min, max);
            while (option == 4) {
                System.out.print("Incorrect value, please try again: ");
                option = askOption(min, max);
            }
            return option;
        }

    }

    /**
     * Function that asks a number between a minimum and a maximum value and returns de option
     * @param min minimum value available
     * @param max maximum value available
     * @return option choosed
     */
    private int askOption(int min, int max) {
        int option;
        Scanner scanner = new Scanner(System.in);
        try {
            option = scanner.nextInt();
            if (inRange(option, min, max) == FALSE) {
                System.out.print("Incorrect value, please try again: ");
                option = askOption(min, max);
            }
        }   catch (InputMismatchException e) {
            System.out.print("Incorrect value, please try again: ");
            option = askOption(min,max);
        }

        return option;
    }

    /**
     * Funcion that indicates if a number is between two numbers
     * @param option number provided
     * @param minValue minimum value
     * @param maxValue maximum value
     * @return boolean that indicates if the number is between values
     */
    private boolean inRange(int option, int minValue, int maxValue) {
        return option >= minValue && option <= maxValue;
    }

    /**
     * Show error message while opening file
     * @param fileName name of the file
     */
    public void errorLoadingFiles(String fileName) {
        System.out.println("Error: The " + fileName + " file can't be accessed.");
    }

    /**
     * shows message indicating that the file was successfully loaded
     */
    public void dataSuccessfullyLoaded() {
        System.out.println("Data was successfully loaded.");
    }

    /**
     * shows an exit message
     */
    public void exitMessage() {
        promptTavernKeeper();
        System.out.println("Are you leaving already? See you soon, adventurer.");
    }

    /**
     * shows tavern keeper message
     */
    public void promptTavernKeeper() {
        System.out.print("Tavern keeper: ");
    }

    /**
     * shows message while creating a new character
     */
    public void createCharacter() {
        promptTavernKeeper();
        System.out.println("Oh, so you are new to this land.");
        System.out.println("What's your name?\n");
    }

    /**
     * Function that asks the name of the character to create
     * @return name of the character
     */
    public String askCharacterName() {
        Scanner scanner = new Scanner(System.in);
        String name;
        System.out.print("-> Enter your name: ");
        try {
            name = scanner.nextLine();
            if (correctNameFormat(name) == FALSE) {
                System.out.println("Please, provide a correct format for name");
                name = askCharacterName();
            }
        } catch (InputMismatchException exception) {
            promptTavernKeeper();
            System.out.println("Please, provide a correct format for name");
            name = askCharacterName();
        }


        return toCorrectFormat(name);
    }

    /**
     * Function that converts the name written to the correct format established if necessary
     * @param name name choosen previously
     * @return name correctly formatted
     */
    private String toCorrectFormat(String name) {
        name = name.toLowerCase();
        String[] parts = name.split(" ");
        String correctedFormat = "";

        for (int i = 0; i < parts.length; i++) {
            correctedFormat += parts[i].toUpperCase().charAt(0);
            correctedFormat += parts[i].substring(1) + " ";
        }
        correctedFormat = correctedFormat.trim();
        return correctedFormat;
    }

    /**
     * Function that indicates if the name follows the format established
     * @param name name provided by the user
     * @return true, if the name follows the rules. False, if not
     */
    private boolean correctNameFormat(String name) {
        char[] chars = name.toCharArray();
        boolean correctFormat = true;
        for (int i = 0; i < chars.length; i++) {
            if (Character.isAlphabetic(chars[i]) == FALSE && chars[i] != ' ') {
                correctFormat = FALSE;
            }
        }

        return correctFormat;
    }

    /**
     * function that shows a message indicating that the name is not available
     */
    public void nameNotDisponible() {
        promptTavernKeeper();
        System.out.println("Adventurer, this name is not yet available");
    }

    /**
     * Function that asks the name of the player creating the character
     * @param name name of the character
     * @return name of the player
     */
    public String askPlayerName(String name) {
        Scanner scanner = new Scanner(System.in);
        promptTavernKeeper();
        System.out.println("Hello, " + name + ", be welcome.");
        System.out.println("And now, if I may break the fourth wall, who is your Player?");
        System.out.print("\n-> Enter player's name: ");

        return scanner.nextLine();
    }

    /**
     * Function that asks the level of the character to create
     * @return level of the character
     */
    public int askLevel() {

        System.out.println();
        promptTavernKeeper();
        System.out.println("I see, I see...");
        System.out.println("Now, are you an experienced adventurer? ");

        System.out.print("\n-> Enter the character's level [1..10]: ");

        return askOption(1,10);
    }

    /**
     * Function that shows a message indicating the level chosen and generating stats
     * @param level level provided (integer)
     */
    public void generatingStats(int level) {
        System.out.println();
        promptTavernKeeper();
        System.out.println("Oh, so you are level " + level + "!");
        System.out.println("Great, let me take a closer look at you...");
        System.out.println("\nGenerating your stats...\n");
    }

    /**
     * Shows the stat generated and the dice rolled value
     * @param string stat type (body, mind, spirit)
     * @param firstValue first value rolled
     * @param secondValue second value rolled
     */
    public void showSingleStat(String string, int firstValue, int secondValue) {
        int sum = firstValue + secondValue;
        System.out.println(string + ":   You rolled " + sum + " (" + firstValue + " and "+ secondValue + ").");
    }

    /**
     * List all the stats of the user
     * @param body body stat value
     * @param mind mind stat value
     * @param spirit spirit stat value
     */
    public void listStats(int body, int mind, int spirit) {

        System.out.println("\nYour stats are:");
        System.out.println(" -Body: " + body);
        System.out.println(" -Mind: " + mind);
        System.out.println(" -Spirit: " + spirit);
    }

    /**
     * Function that asks the name of the player to show it's characters created
     * @return name of the player
     */
    public String listCharactersMessage() {
        Scanner scanner = new Scanner(System.in);
        promptTavernKeeper();
        System.out.println("Lads! The Boss wants to see you, come here!");
        System.out.println("Who Piques your Interest?\n");
        System.out.print("-> Enter the name of the Player to filter: ");
        return scanner.nextLine();
    }

    /**
     * List the names of the characters created by a player
     * @param names list of names of the characters
     */
    public void listNames(List<String> names) {
        System.out.println("You watch as all adventurers get up from their chairs and approach you.\n");
        for (int i = 0; i < names.size(); i++) {
            System.out.println(" " + (i+1) + ". " + names.get(i));
        }

        System.out.println("\n 0. back\n");
    }

    /**
     * Function that asks the user which character does it want to meet
     * @param characterNames list of names of the character
     * @return character index in the list provided by parameters
     */
    public int meetCharacter(List<String> characterNames) {
        System.out.print("Who would you like to meet [0.." + (characterNames.size()) + "]: ");

        int option = askOption(0, characterNames.size());
        System.out.println();
        promptTavernKeeper();

        if (option != 0) {
            System.out.println("Hey " + characterNames.get(option - 1) + " get here; the boss wants to see you!\n");
        }
        return option;
    }

    /**
     * Shows the attribute of the player
     * @param camp attribute selected
     * @param nomPersonatge name of the character
     */
    public void showStringAtribute(String camp, String nomPersonatge) {
        System.out.println("* " + camp + nomPersonatge);
    }

    /**
     * Shows the attribute value
     * @param camp attribute selected
     * @param level value of the attribute
     */
    public void showIntegerAtribute(String camp, int level) {
        System.out.println("* " + camp + level);
    }

    /**
     * shows an error message while listing characters
     */
    public void errorListingCharacters() {
        promptTavernKeeper();
        System.out.println("\nWe haven't found any character with this criteria");
        nextLine();
    }

    /**
     * Function that asks the user to delete a character and asks to introduce the name of tha character again
     * @param nomPersonatge name of the character
     * @return name provided by the user
     */
    public String askToDeleteCharacter(String nomPersonatge) {
        System.out.println();
        System.out.println("[Enter name to delete or press enter to cancel]");
        System.out.print("Do you want to delete " + nomPersonatge + "? ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    /**
     * shows message that indicates that the character has to leave
     */
    public void characterHasToLeave() {
        promptTavernKeeper();
        System.out.println("I'm sorry kiddo, but you have to leave.\n");
    }

    /**
     * Shows message that a character has leaved
     * @param nomPersonatge name of the character
     */
    public void characterLeftTheGuild(String nomPersonatge) {
        System.out.println("Character " + nomPersonatge + " has left the Guild.");
    }

    /**
     * shows message of planning an adventure
     */
    public void planningAnAdventure() {
        promptTavernKeeper();
        System.out.println("Planning an adventure? Good luck with that!\n");
    }

    /**
     * Function that asks the name of the adventure
     * @return name of the adventure
     */
    public String nameAdventure() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("-> Name your adventure: ");
        return scanner.nextLine();
    }

    /**
     * Shows error message, adventure already exists
     */
    public void adventureNameNotAble() {
        promptTavernKeeper();
        System.out.println("This Adventure already exists!");
    }

    /**
     * shows message of correct adventure name
     * @param adventureName adventure name
     */
    public void adventureNameCorrect(String adventureName) {
        promptTavernKeeper();
        System.out.println("You plan to undertake " + adventureName + ", really?");
        System.out.println("How long will that take?\n");
    }

    /**
     * Function that asks the amount of encounters of the adventure
     * @param maxEncounters integer that indicates the maximum amount available of encounters
     * @param maxErrors maximum number of errors possible
     * @return amount of encounters
     */
    public int askEncounters(int maxEncounters, int maxErrors) {
        System.out.print("-> How many encounters do you want [1.." + maxEncounters + "]: ");
        int times = 0;
        return askOption(1, maxEncounters, maxErrors, times);
    }

    /**
     * Overload function of askOption, with amount of fail times
     * @param min min value possible
     * @param max max value possible
     * @param maxErrors max errors available
     * @param times times asked the function
     * @return option value (0 if fail)
     */
    private int askOption(int min, int max, int maxErrors, int times) {
        int option = 0;
        Scanner scanner = new Scanner(System.in);

        times++;
        if (times <= maxErrors) {
            try {
                option = scanner.nextInt();

                if (inRange(option, min, max) == FALSE) {
                    if (times < maxErrors) {
                        System.out.print("Incorrect value, please try again: ");
                    }
                    option = askOption(min, max, maxErrors, times);

                }
            } catch (InputMismatchException e) {
                if (times < maxErrors) {
                    System.out.print("Incorrect value, please try again: ");
                }
                option = askOption(min, max, maxErrors, times);

            }
            return option;
        } else {
            return 0;
        }
    }

    /**
     * shows message of amount of encounters
     * @param encounters number of encounters
     */
    public void nEncounters(int encounters) {
        promptTavernKeeper();
        System.out.println( encounters + " encounters? That is too much for me...");
        System.out.println();
    }

    /**
     * Shows the menu of the encounter
     * @param encounterIndex index of the encounter
     * @param maxEncounters max encounters for this adventure
     * @param monsters list of monsters at the moment
     * @param quantity amount of monsters at the moment
     */
    public void encounterMenu(int encounterIndex, int maxEncounters, List<String> monsters, List<Integer> quantity) {
        System.out.println("\n* Encounter " + encounterIndex + " / " + maxEncounters);
        System.out.println("* Monsters in encounter:");
        if (monsters.isEmpty()) {
            System.out.println("\t# Empty\n");
        } else {
            for (int i = 0; i < monsters.size(); i++) {
                System.out.println("\t" + (i+1) + ". " + monsters.get(i) + "(x" + quantity.get(i) + ")");
            }
            System.out.println();
        }
    }

    /**
     * menu of the combat estate
     * @return option choosen
     */
    public int combatManagerMenu() {
        System.out.println("1. Add monster");
        System.out.println("2. Remove monster");
        System.out.println("3. Continue\n");

        System.out.print("-> Enter an option [1..3]: ");
        return askOption(1,3);
    }

    /**
     * shows the list of monsters and their challenge
     * @param monsterNames list of monsters
     * @param challenge list of challenges of the monsters
     */
    public void showMonsterList(List<String> monsterNames, List<String> challenge) {
        for (int i = 0; i < monsterNames.size(); i++) {
            System.out.println((i+1) + ". " + monsterNames.get(i) + " (" + challenge.get(i) + ")");
        }
        System.out.println();
    }

    /**
     * function that asks the monster index to add
     * @param max maxValue possible
     * @return index of the monster
     */
    public int askMonsterIndex(int max) {
        System.out.print("-> Choose a monster to add [1.." + max + "]: ");
        return askOption(1, max) - 1; // substract 1 for easily operations in controller
    }

    /**
     * function that asks the monster quantity
     * @param monsterName name of the monster
     * @return amount of monsters of that type
     */
    public int askMonsterQuantity(String monsterName) {
        System.out.print("-> How many " + monsterName + "(s) do you want to add: ");
        return askOption(1);
    }

    /**
     * asks amount of monsters to remove
     * @param max max value possible
     * @return amount of monsters to remove
     */
    public int askMonsterQuantity(int max) {
        System.out.print("-> Which monster do you want to delete: ");
        return askOption(1, max);
    }

    /**
     * Shows the name and the quantity of monsters removed
     * @param name name of the monster
     * @param amount amount of monsters removed
     */
    public void deletedFromEncounter(String name, int amount) {
        System.out.println(amount + " " + name + " were removed from the encounter.");
    }

    /**
     * overload of function askOption with no max value
     * @param min min value possible
     * @return value provided
     */
    private int askOption(int min) {
        int option;
        Scanner scanner = new Scanner(System.in);
        try {
            option = scanner.nextInt();
            if (option < min) {
                System.out.print("Incorrect value, please try again: ");
                option = askOption(min);
            }
        }   catch (InputMismatchException e) {
            System.out.print("Incorrect value, please try again: ");
            option = askOption(min);
        }

        return option;
    }

    /**
     * shows error message of no added monsters
     */
    public void errorNoAddedMonsters() {
        promptTavernKeeper();
        System.out.println("Error, you haven't added any monsters already!\n");
    }

    /**
     * Menu of playing an adventure
     * @param adventures list of adventure names
     * @return index of the adventure to play
     */
    public int playAdventureMenu(List<String> adventures) {
        promptTavernKeeper();
        System.out.println("So, you are looking to go on an adventure?");
        System.out.println("Where do you fancy going?\n");

        System.out.println("Available adventures: ");
        for (int i = 0; i < adventures.size(); i++) {
            System.out.println("\t" +(i+1) + "." + adventures.get(i));
        }

        System.out.print("\n-> Choose an adventure: ");
        return askOption(1, adventures.size());
    }

    /**
     * Function that asks the size of the party
     * @param adventureName name of th eadventure to play
     * @return size of the party
     */
    public int askPartySize(String adventureName) {
        promptTavernKeeper();
        System.out.println(adventureName + "it is!");

        System.out.print("-> Choose a number of characters [3..5]: ");
        return askOption(3,5);
    }

    /**
     * shows message of party size
     * @param partySize party size
     */
    public void askCharacterMessage(int partySize) {
        promptTavernKeeper();
        System.out.println("Great, " + partySize + " it is.");
        System.out.println("Who among these lads shall join you?\n");
    }

    /**
     * shows party information (data)
     * @param party Party choosed
     * @param actual actual index of character to add to party
     * @param size max amount of characters in party
     */
    public void showParty(Party party, int actual, int size) {
        System.out.println("---------------------------");
        System.out.println("Your Party (" + actual + "/" + size + ")");

        for (int i = 0; i < party.getPersonatges().length; i++) {
            if (party.getPersonatges()[i] == null) {
                System.out.println((i+1) + ". Empty");
            } else {
                System.out.println((i + 1) + ". " + party.getPersonatges()[i].getNomPersonatge());
            }
        }
        System.out.println("---------------------------");
    }

    /**
     * Function that lists all characters and asks the user to pick one of them
     * @param allCharacters list of characters
     * @param index index off the character in party
     * @return index of the character in list of characters
     */
    public int listCharacters(List<String> allCharacters, int index) {
        System.out.println("Available characters: ");
        for (int i = 0; i < allCharacters.size(); i++) {
            System.out.println("\t" + (i+1) + ". " + allCharacters.get(i));
        }

        System.out.print("\n-> Choose character " + index + " in your party: ");
        return askOption(1, allCharacters.size());
    }

    /**
     * shows error message
     * @param nomPersonatge name of the character
     */
    public void characterAlreadyInParty(String nomPersonatge) {
        promptTavernKeeper();
        System.out.println("Error, " + nomPersonatge + " is already in your party!\n");
    }

    /**
     * Shows message, adventure will start soon
     * @param name name of the adventure
     */
    public void startAdventure(String name) {
        promptTavernKeeper();
        System.out.println("Great, good luck on your adventure lads!\n");
        System.out.println("The " + name + " will start soon...");
    }

    /**
     * start of combat menu
     * @param index
     * @param combat
     */
    public void startOfCombat(int index, Combat combat) {
        System.out.println("--------------------");
        System.out.println("Starting encounter " + (index+1) + ":");
        for (int i = 0; i < combat.getMonsters().size(); i++) {
            System.out.println(" - " + combat.getMonstersQuantity()[i] + "x " + combat.getMonsters().get(i).getName());
        }
        System.out.println("--------------------\n\n");
    }


    /**
     * shows the preparation stage title
     * @param party Party choosed
     */

    public void preparationStage(Party party) {
        System.out.println("-------------------------");
        System.out.println("*** Preparation stage ***");
        System.out.println("-------------------------");

        for (int i = 0; i < party.getPersonatges().length; i++) {
            // this sout has to be changed as we increase the amount of character types, but by now it's ok
            System.out.println(party.getPersonatges()[i].getNomPersonatge() + " uses Self-Motivated. Their Spirit increases in +1.");
        }
        System.out.println();
    }


    /**
     * shows the list of particpants of the adventure in their initiative order
     * @param order list of the participants
     * @param orderValue value of initiative
     */

    public void initiativeOrder(List<String> order, int[] orderValue) {
        System.out.println("Rolling initiative...");
        for (int i = 0; i < order.size(); i++) {

            System.out.println("- " + orderValue[i] + "  " + order.get(i));
        }
    }

    /**
     * shows title of combat stage
     */

    public void combatStage() {
        System.out.println("---------------------");
        System.out.println("*** Combat stage ***");
        System.out.println("---------------------");
    }

    /**
     * shows the characters and their stats of the party
     * @param party Party choosed
     * @param index number of round
     * @param actualLifepoints actual life points of each character
     * @param staticLifePoints maximum life points of each character
     */
    public void partyStats(Party party, int index, int[] actualLifepoints, int[] staticLifePoints) {
        System.out.println("Round " + index + ":");
        System.out.println("Party:");

        for (int i = 0; i < party.getPersonatges().length; i++) {
            System.out.print("  - " + party.getPersonatges()[i].getNomPersonatge() + "  ");
            System.out.println(actualLifepoints[i] + " / " + staticLifePoints[i] + " hit points");
        }
        System.out.println();
    }

    /**
     * choose a random number 0 to 10
     * @return the number
     */
    public int throwD10() {
        Dice dice = new Dice("D10", 10);
        return dice.throwDice();
    }

    /**
     * show the combat actions
     * @param attacker name of the attacker
     * @param receiver name of the receiver
     * @param isMonster boolean to know if it is a monster
     */


    public void combatActions(String attacker, String receiver, boolean isMonster){
        if (isMonster) {
            System.out.println(attacker + " attacks " + receiver + ".");
        } else {
            System.out.println(attacker + " attacks " + receiver + " with Sword Slash.");
        }
    }

    /**
     * shows the final result of the combat (completed/failed)
     * @param party Party choosed
     * @param actualLifePoints Actual life points of the characters
     * @param name name of the adventure
     */
    public void finalResult(Party party, int[] actualLifePoints, String name){
        int win = 0;
        for (int i = 0; i < party.getPersonatges().length; i++){
            win = win + actualLifePoints[i];
        }
        if (win > 0){
            //restStage();
            System.out.println("Congratulations, your party completed " + name);

        } else{
            System.out.println("Tavern keeper: Lad, wake up. Yes, your party fell unsconscious.");
            System.out.println("Don't worry, you are safe back at the Tavern.");
        }
    }

    /**
     * jumps to the next line (usefull to have a good design)
     */

    public void nextLine() {
        System.out.println("");
    }

    /**
     * shows the attack damge and the type of damage of a character/monster to another.
     * @param damage damage of the attack
     * @param typeOfAttack type of attack
     * @param critical boolean that says if it is a critical damage or not
     */
    public void attackHits(int damage, String typeOfAttack, boolean critical) {
        if (critical) {
            System.out.println("Critical hit and deals " + damage + " " + typeOfAttack + " damage.");
        } else {
            System.out.println("Hits and deals " + damage + " " + typeOfAttack + " damage.");
        }
        System.out.println();
    }

    /**
     * if an attack fails it shows an error
     * @param damage damage of the attack
     * @param typeOfAttack type of attack
     */

    public void attackFails(int damage, String typeOfAttack) {
        System.out.println("Fails and deals " + damage + " " + typeOfAttack + " damage.\n");
    }

    /**
     * shows a message when a monster dies
     * @param name name of the monster
     */
    public void monsterDies(String name) {
        System.out.println(name + " dies.\n");
    }

    /**
     * shows a message when a character dies
     * @param name name of the character
     */
    public void characterIsInconscient(String name) {
        System.out.println(name + " falls unconscious.\n");
    }

    /**
     * shows a message when a round finishes
     * @param roundIndex number of round
     */
    public void endOfRound(int roundIndex) {
        System.out.println("End of round " + roundIndex + ".");
    }

    /**
     * shows that message when all characters are dead
     */
    public void TPU() {
        promptTavernKeeper();
        System.out.println("Lad, wake up. Yes, your party fell unconscious. ");
        System.out.println("Don't worry, you are safe back at the Tavern.");
        nextLine();
    }

    /**
     * shows that message when all enemies are dead
     */
    public void enemiesDefeated() {
        System.out.println("All enemies are defeated.");
        nextLine();
    }

    /**
     * shows the rest stage title
     */
    public void shortRestStage() {
        System.out.println("------------------------");
        System.out.println("*** Short rest stage ***");
        System.out.println("------------------------");
    }

    /**
     * shows the experience won by characters
     * @param name name of character
     * @param xp experience of character
     * @param levelUp boolean that says if the character levels up
     * @param level level of the character
     */
    public void updateXPPoints(String name, int xp, boolean levelUp, int level) {
        if (levelUp) {
            System.out.println(name + " gains " + xp + " xp. " + name + " levels up. They are now lvl " + level + "!");
        } else {
            System.out.println(name + " gains " + xp + " xp.");
        }

    }

    /**
     * shows a message when a character is dead in the rest stage
     * @param name name of the character
     */
    public void unconsciousCharacterRestStage(String name) {
        System.out.println(name + " is unconscious.");
    }

    /**
     * shows the heal points depending on the rest action of the conscious characters
     * @param name name of the character
     * @param restAction the rest action
     * @param healPoints the heal points
     */
    public void consciousCharacterRestStage(String name, String restAction, int healPoints) {
        System.out.println(name + " uses " + restAction + ". Heals " + healPoints + " hit points.");
    }

    /**
     * shows a message when you reach the limit of bosses put in an adventure
     */
    public void bossLimitReached() {
        promptTavernKeeper();
        System.out.println("You have reached the amount of Bosses already!");
        nextLine();
    }

    /**
     * shows a message that continues with creating the character class
     */
    public void nextClass() {
        nextLine();
        promptTavernKeeper();
        System.out.println("Looking good!");
        System.out.println("And, lastly, ?\n");
    }

    /**
     * asks the class of the character
     * @return returns the class
     */
    public String askClass(){
        Scanner scanner = new Scanner(System.in);
        String className;
        System.out.print("-> Enter the character's initial class [Adventurer, Cleric, Wizard]: ");
        try{
            className = toCorrectFormat(scanner.nextLine());
            if (!className.equals("Adventurer") && !className.equals("Cleric") && !className.equals("Wizard")){
                System.out.println("Please, provide a correct format for name");
                className = askClass();
            }
        } catch (InputMismatchException exception){
            System.out.println("Please, provide a correct format for name");
            className = askClass();
        }
        return className;
    }

    /**
     * reassigns the character class depending on his level
     * @param level level of the character
     * @param className0 class name of the previous function
     * @return returns the definitive character class
     */
    public String classMessage(int level, String className0){
        String className = null;
        if(className0.equals("Adventurer")){
            if(level >= 4 && level <= 7){
                className = "Warrior";
            } else if(level >= 8){
                className = "Champion";
            } else{
                className = className0;
            }
        } else if(className0.equals("Cleric")){
            if(level >= 5){
                className = "Paladin";
            }
            else {
                className = className0;
            }
        } else{
            className = className0;
        }

        promptTavernKeeper();
        System.out.println("Any decent party needs one of those.");
        System.out.println("I guess that means you're a " + className + " by now, nice!\n");

        return className;
    }

    /**
     * shows the final message after creating a character
     * @param name name of the character
     */
    public void finalMessage(String name){
        System.out.println("The new character " + name + " has been created.\n");
    }
}
