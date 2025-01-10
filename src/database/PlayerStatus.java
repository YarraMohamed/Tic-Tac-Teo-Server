package database;

public class PlayerStatus {
    private String name;
    private boolean active;
    private boolean busy;
    
    public PlayerStatus(){
        name="";
        active=false;
        busy=false;
    }
    
    public PlayerStatus(String name,boolean isActive,boolean isBusy){
        this.name=name;
        this.active=isActive;
        this.busy=isBusy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
