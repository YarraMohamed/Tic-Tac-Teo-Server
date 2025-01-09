package database;

public class Player {
    private int ID;
    private String Name;
    private String Email;
    private String Password;
    private int Score ;
    private boolean Active;

    public Player (){
    }
    
    public Player(String Name,String Email,String Password){
        this.Name=Name;
        this.Email=Email;
        this.Password=Password;
    }
    
    public Player(int ID,String Name,String Email,String Password,int Score, boolean Active){
        this.ID=ID;
        this.Name=Name;
        this.Email=Email;
        this.Password=Password;
        this.Score=Score;
        this.Active=Active;
    }
    
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public int getScore() {
        return Score;
    }

    public void setScore(int Score) {
        this.Score = Score;
    }

    public boolean isActive() {
        return Active;
    }

    public void setActive(boolean Active) {
        this.Active = Active;
    }
}
