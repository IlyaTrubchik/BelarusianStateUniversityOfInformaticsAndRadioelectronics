public class PlayerInfo {
    private String name = "DEFAULT";
    private int score = 0;
    private String drawingWord = "UNKNOWN";
    private byte[] playerImage;
    String answer = "";

    public String getName(){
        return this.name;
    }
    public int getScore(){
        return this.score;
    }
    public String getDrawingWord(){
        return this.drawingWord;
    }
    public byte[] getPlayerImage(){
        return this.playerImage;
    }
    public void setDrawingWord(String word)
    {
        this.drawingWord  = word;
    }
    public void setPlayerImage(byte[] image)
    {
        this.playerImage = image;
    }
    public void incScore(int i)
    {
        this.score+=i;
    }
    public void setPlayerName(String name)
    {
        this.name = name;
    }
    public void setScore(int score)
    {
        this.score =score;
    }
}
