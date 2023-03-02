package Business;

import java.util.Arrays;

public class Monster {
    private final String name;
    private final String challenge;
    private final int experience;
    private final int hitPoints;
    private final int initiative;
    private final String damageDice;
    private final String damageType;


    public Monster(String name, String challenge, int experience, int hitPoints, int initiative, String damageDice, String damageType) {

        this.name = name;
        this.challenge = challenge;
        this.experience = experience;
        this.hitPoints = hitPoints;
        this.initiative = initiative;
        this.damageDice = damageDice;
        this.damageType = damageType;
    }

    public String getName() {
        return name;
    }

    public int getExperience() {
        return experience;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public int getInitiative() {
        return initiative;
    }

    public String getChallenge() {
        return challenge;
    }

    public String getDamageDice() {
        return damageDice;
    }

    public String getDamageType() {
        return damageType;
    }

    /**
     * function that simulates monster throwing d12 dice
     * @return d12 dice value obtained by throwing it
     */
    public int throwD12() {
        Dice dice = new Dice("D12", 12);
        return dice.throwDice();
    }

    /**
     * function that simulates monsters atacking
     * @return returns damage to be dealed by monster
     */
    public int atac() {
        // 1st get dice
        String damageDice = getDamageDice();
        int diceFaces = atoi(damageDice, 1);

        Dice dice = new Dice(diceFaces);

        return dice.throwDice();
    }

    /**
     * function that converts ascii to integer
     * @param str string to parse int
     * @param index start of parsing string
     * @return value obtained
     */
    private static int atoi(String str, int index) {
        // atoi
        int result = 0;

        for (int i = index; i < str.length(); i++)
            result = result * 10 + str.charAt(i) - '0';
        return result;
    }
}
