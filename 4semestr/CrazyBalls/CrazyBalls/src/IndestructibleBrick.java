import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.*;

public class IndestructibleBrick extends DisplayObject {

    IndestructibleBrick(double xl, double yl,double xr,double yr, Color color,GraphicsContext gc)
    {
        super(xl,yl,xr,yr,color,  gc);
    }
    private int width;
    private int height;
    public void render(){
        this.gc.setFill(this.color);
        this.gc.fillRect(this.xl, this.yl, this.xr - this.xl, this.yr - this.yl);
    };


}
