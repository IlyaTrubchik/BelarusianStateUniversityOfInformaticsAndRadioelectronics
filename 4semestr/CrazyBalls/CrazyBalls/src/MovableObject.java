import javafx.event.Event;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

abstract public class MovableObject extends DisplayObject{
    protected int velocity;
    MovableObject(double xl, double yl,double xr,double yr, Color color, int velocity,GraphicsContext gc)
    {
        super(xl,yl,xr,yr,color,gc);
        this.velocity = velocity;
    }

    public void eventHandler(Event event)
    {

    }
    public abstract void move();
}
