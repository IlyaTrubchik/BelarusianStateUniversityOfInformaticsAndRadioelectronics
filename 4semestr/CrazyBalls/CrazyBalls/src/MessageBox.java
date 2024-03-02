import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;

public class MessageBox extends DisplayObject {

    MessageBox(int x, int y, Color color, GraphicsContext gc)
    {
        super(x,y,0,0,color,gc);
    }


    Button okButton;
    public void showGameOver(){};
    public void showPlayerStatistics(){};
    public void showWin(){};
    public void display(){};
}
