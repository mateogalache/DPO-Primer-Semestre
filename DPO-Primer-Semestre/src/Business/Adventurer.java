package Business;

public class Adventurer extends Personatge{
    /**
     * Constructor of class Adventurer
     * @param nomPersonatge name of the character
     * @param nomJugador name of the player
     * @param nivellInicial initial level of the character
     * @param mind mind stat of the character
     * @param body body stat of the character
     * @param spirit spirit stat of the character
     * @param tipusPersonatge class of the character
     */
    public Adventurer(String nomPersonatge, String nomJugador, int nivellInicial, int mind, int body, int spirit, String tipusPersonatge) {
        super(nomPersonatge, nomJugador, nivellInicial, mind, body, spirit, tipusPersonatge);

    }

    /**
     * Constructor of the class adventurer when a character is given
     * @param personatge character to create
     */
    public Adventurer(Personatge personatge) {
        super(personatge.getNomPersonatge(), personatge.getNomJugador(), personatge.getXpPoints(), personatge.getMind(), personatge.getBody(), personatge.getSpirit(), personatge.getTipusPersonatge());

    }

    /**
     * function that runs the adventurer support action
     */
    public void supportAction() {
        increaseStat("spirit", 1);
    }

    /**
     * Function that generates the initial life points of the adventurer
     * @return lifepoints
     */
    @Override
    public int generateLifePoints() {
        int body = this.getBody();
        return (body + 10) * this.getNivellInicial();
    }

    /**
     * function that runs the adventurer atac
     * @return damage to do
     */
    public int atac() {
        return throwD6() + this.getBody();
    }

    /**
     * Function that simulates an adventurer throwing a D6 dice
     * @return value of the dice
     */
    private int throwD6() {
        Dice dice = new Dice("D6", 6);
        return dice.throwDice();
    }

    /**
     * function that gets the damage type of the adventurer
     * @return damage type
     */
    public String getDamageType() {
        return "physical";
    }

    /**
     * Function that simulates the rest stage action of the adventurers
     * @return rest stage action value
     */
    public int restStageAction() {
        return throwD8() + getMind();
    }
    /**
     * Function that simulates an adventurer throwing a D8 dice
     * @return value of the dice
     */
    private int throwD8() {
        Dice dice = new Dice("D8",8);
        return dice.throwDice();
    }

    /**
     * function that gets the rest action to be done by the adventurer
     * @return rest action
     */
    public String getRestAction() {
        return "Bandage time";
    }
    /**
     * Function gets the initiative value by throwing a D12 dice
     * @return value of the dice
     */
    @Override
    public int getInitiative() {
        return throwD12();
    }
}
