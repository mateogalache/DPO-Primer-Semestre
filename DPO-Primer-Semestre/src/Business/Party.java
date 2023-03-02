package Business;


public class Party {
    private Personatge[] personatges;
    private boolean[] conscient;

    private int[] lifePoints;

    /**
     * Function that creates the party depending on the size
     * @param size size of the party
     */
    public Party(int size) {
        this.personatges = new Personatge[size];
        this.conscient = new boolean[size];
    }

    /**
     * Function that gets the character
     * @return the character
     */
    public Personatge[] getPersonatges() {
        return personatges;
    }

  public void setPersonatges(Personatge[] personatges) {
        this.personatges = personatges;
    }

    public void setPersonatge(Personatge personatge, int index) {
        this.personatges[index] = personatge;
    }

    public void setConscient(int index, boolean state) {this.conscient[index] = state;}

    /**
     * Function that gets if a criature is conscient
     * @return boolean  if its conscient or not
     */
    public boolean[] getConscient() {
        return conscient;
    }

    /**
     * Function that set the life points of the character
     */
    public void setLifePoints() {
        int[] points = new int[personatges.length];
        for (int i = 0; i < personatges.length; i++) {
           points[i] = personatges[i].generateLifePoints();
        }

        this.lifePoints = points;
    }

    /**
     * Funciton that gets life points
     * @return life points
     */
    public int[] getLifePoints() {
        return lifePoints;
    }

    /**
     * Function that get the lifepoints of a character from his name
     * @param name name of the character
     * @return life points of the character
     */
    public int getLifePoints(String name) {
        int index = 0;

        for (int i = 0; i < personatges.length; i++) {
            if (personatges[i].getNomPersonatge().equals(name)) {
                index = i;
            }
        }

        return lifePoints[index];
    }

    /**
     * Function that starts an attack of character
     * @param name name of the character
     * @return int of the damage attack
     */
    public int tryToAttack(String name) {
        // 1st find player
        int index = 0;
        for (int i = 0; i < personatges.length; i++) {
            if (personatges[i].getNomPersonatge().equals(name)) {
                index = i;
            }
        }

        //2nd simulate attack
        return personatges[index].atac();
    }
}
