import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ScoreBoard extends  DisplayObject{
    ScoreBoard(int x, int y, Color color,GraphicsContext gc)
    {
        super(x,y,0,0,color,gc);
    }


    Score score;

    public boolean isGameOver(){
       return false;
    }
    public void draw(){};


}
