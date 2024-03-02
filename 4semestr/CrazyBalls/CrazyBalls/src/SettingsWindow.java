import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;

public class SettingsWindow extends DisplayObject {
    int height;
    int width;

    public SettingsWindow(int x, int y, Color color,GraphicsContext gc) {
        super(x, y, 0,0,color,gc);
    }

    public void draw(){}
    Button windowSizeBtn;
    Button volumeBtn;

    public void changeVolume(){}

    public void changeWindowSize(){}

}
