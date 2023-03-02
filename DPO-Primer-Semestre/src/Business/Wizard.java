package Business;

public class Wizard extends Personatge {

    public Wizard(String nomPersonatge, String nomJugador, int nivellInicial, int mind, int body, int spirit, String tipusPersonatge) {
        super(nomPersonatge, nomJugador, nivellInicial, mind, body, spirit, tipusPersonatge);
    }
    @Override
    public void supportAction() {
        // insert code
    }

    @Override
    public int generateLifePoints() {
        // insert code
        return 0;
    }

    @Override
    public int atac() {
        // insert code
        return 0;
    }

    @Override
    public int restStageAction() {
        // insert code
        return 0;
    }

    @Override
    public String getDamageType() {
        // insert code
        return null;
    }

    @Override
    public String getRestAction() {
        // insert code
        return null;
    }

    @Override
    public int getInitiative() {
        // insert code
        return 0;
    }
}
