package Business;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public abstract class Personatge {

    @SerializedName(value = "name") private String nomPersonatge;
    @SerializedName(value = "player")private String nomJugador;
    @SerializedName(value = "xp")private int xpPoints;
    @SerializedName(value = "body")private int body;
    @SerializedName(value = "mind")private int mind;
    @SerializedName(value = "spirit")private int spirit;
    @SerializedName(value = "class")private String tipusPersonatge;


    /**
     * Constructor of Personatge class
     * @param nomPersonatge name of the character
     * @param nomJugador name of the player
     * @param xpPoints experience of character
     * @param mind mind points of the character
     * @param body body points of the character
     * @param spirit spirit points of the character
     * @param tipusPersonatge type of character(class)
     */
    public Personatge(String nomPersonatge, String nomJugador, int xpPoints, int mind, int body, int spirit, String tipusPersonatge) {
        this.nomPersonatge = nomPersonatge;
        this.nomJugador = nomJugador;
        this.xpPoints = xpPoints;
        this.mind = mind;
        this.body = body;
        this.spirit = spirit;
        this.tipusPersonatge = tipusPersonatge;
    }

    /**
     * Function that gets the body points of the character
     * @return the body points
     */
    public int getBody() {
        return this.body;
    }

    /**
     * Function that gets the mind points of the character
     * @return the mind points
     */
    public int getMind() {
        return this.mind;
    }

    /**
     * Funciton that gets the initial level of the character
     * @return the initial level
     */
    public int getNivellInicial() {
        return xpPoints/100 + 1;
    }

    /**
     * Funciton that gets the spirit points
     * @return the spirit points
     */
    public int getSpirit() {
        return spirit;
    }

    /**
     * Function that gets the character name
     * @return the character name
     */
    public String getNomPersonatge() {
        return this.nomPersonatge;
    }

    /**
     * Function that gets the character type (class)
     * @return the type of character
     */
    public String getTipusPersonatge() {
        return tipusPersonatge;
    }

    /**
     * Function that gets the experince points of the character
     * @return the experience points
     */
    public int getXpPoints() {
        return xpPoints;
    }

    /**
     * Funciton that gets the name of the player
     * @return the player's name
     */
    public String getNomJugador() {
        return nomJugador;
    }

    /**
     * Function that initiates the support action
     */
    public abstract void supportAction();

    /**
     * Funciton that generates the life points of a character
     * @return the life points
     */
    public abstract int generateLifePoints();

    /**
     * Function that increase the stats of the character
     * @param type type of the stat to be increased
     * @param amount amount of increment
     */
    public void increaseStat(String type, int amount) {
        switch (type) {
            case "body": {
                setBody(getBody() + amount);
                break;
            }
            case "mind": {
                setMind(getMind() + amount);
                break;
            }
            case "spirit": {
                setSpirit(getSpirit() + amount);
                break;
            }
        }
    }

    /**
     * Function that gets spirit stat with constructor
     * @param spirit spirit stat of the character
     */
    public void setSpirit(int spirit) {
        this.spirit = spirit;
    }

    /**
     * Funciton that gets body stat with constructor
     * @param body body stat of the constructor
     */
    public void setBody(int body) {
        this.body = body;
    }

    /**
     * Funciton that gets mind stat with constructor
     * @param mind mind stat of the character
     */
    public void setMind(int mind) {
        this.mind = mind;
    }

    /**
     * Function that get a random number 1 to 12
     * @return int of the number
     */
    public int throwD12() {
        Dice dice = new Dice("D12", 12);
        return dice.throwDice();
    }

    /**
     * Function that initiates the attack
     * @return in of the attack damage
     */
    public abstract int atac();

    /**
     * Function that get random number 1 to 10
     * @return int of the number
     */
    public int throwD10() {
        Dice dice = new Dice("D10", 10);
        return dice.throwDice();
    }

    /**
     * Function that initiates the rest stage
     * @return int of number of heal points
     */
    public abstract int restStageAction();

    /**
     * Function that gets the type of damage
     * @return string of the damage type
     */
    public abstract String getDamageType();

    /**
     * Function that increase the experience points of the character
     * @param xp experience to be added to the actual experience
     */
    public void increaseXPPoints(int xp) {
        int actualXP = getXpPoints();
        setXpPoints(actualXP + xp);
        // updateLvl

    }

    /**
     * Function that gets xp points by contructor
     * @param xpPoints Experience points of the character
     */
    private void setXpPoints(int xpPoints) {
        this.xpPoints = xpPoints;
    }


    /**
     * Function that gets the action to be done in rest stage
     * @return string of the action
     */
    public abstract String  getRestAction();

    /**
     * Function that get the number of initiative
     * @return int of the initiative number
     */
    public abstract int getInitiative();
}
