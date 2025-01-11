package database;

public class PlayerStatus {
    
    private int playerID;
    private boolean active;
    private boolean busy;
    
    public PlayerStatus(int playerID){
        this.playerID = playerID;
        active=false;
        busy=false;
    }
   
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean isActive) {
        this.active = isActive;
    }

    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean isBusy) {
        this.busy = isBusy;
    }
}
