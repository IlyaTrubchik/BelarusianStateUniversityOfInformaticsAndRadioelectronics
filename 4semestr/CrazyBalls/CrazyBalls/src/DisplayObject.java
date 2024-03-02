import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.Serializable;
import java.util.ArrayList;


public abstract class DisplayObject  implements Serializable {
    protected int x;
    protected int y;
    protected transient Color color;

    boolean visible =true;
    protected  int width;
    protected int height;
    protected double xl;
    protected double yl;
    protected double xr;
    protected double yr;
    protected transient GraphicsContext gc;


    public DisplayObject(double xl, double yl,double xr,double yr,Color color,GraphicsContext gc) {
        this.xl = xl;
        this.yl = yl;
        this.xr = xr;
        this.yr = yr;
        this.gc = gc;
        this.color =color;
    }
    public  void setGc(GraphicsContext gc){
        this.gc = gc;
    }
    protected void render(){}
    protected void update(){}
    public boolean isVisible(){return this.visible;}
    public CollisionEvent checkCollisions(ArrayList<DisplayObject> obj){return null;}
}