package david.naor.com.memorygame.Data;

public class Score {
    private String name;
    private double locationX;
    private double locationY;
    private int score;

    public Score (){
        setName("");
        setLocation(0,0);
        setScore(0);
    }

    public Score(String name, double locationX, double locationY, int score){
        setName(name);
        setLocation(locationX, locationY);
        setScore(score);
    }

    public void setName (String name){
        this.name = name;
    }

    public void setLocation (double locationX, double locationY){
        this.locationX = locationX;
        this.locationY = locationY;
    }

    public void setScore (int score){
        this.score = score;
    }

    public  String  getName(){
        return this.name;
    }

    public  double  getLocationX(){
        return this.locationX;
    }
    public  double  getLocationY(){
        return this.locationY;
    }
    public  int  getScore(){
        return this.score;
    }
}
