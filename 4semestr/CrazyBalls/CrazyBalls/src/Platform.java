
import javafx.event.Event;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

public class Platform extends MovableObject{


    private int direction = 0;
    private int width;    // В процентах от размера окна
    private int height;

    Platform(double xl, double yl, double xr, double yr, int velocity, Color color, GraphicsContext gc)
    {
        super(xl,yl,xr,yr,color,velocity,gc);

    }
    public void setDirection(int direction){this.direction = direction;}//Direction == 1 ->right,direction == -1 -> left
    public void checkBallCollision(){};
    public void render(){
        this.gc.setFill(this.color);
        this.gc.fillRect(this.xl , this.yl , this.xr - this.xl, this.yr - this.yl);
    };
    public void update(){this.move();};
    public void eventHandler(Event event){
       if(event instanceof KeyEvent)
       {
           if(((KeyEvent) event).getCode()== KeyCode.LEFT)
           {
               this.direction = -1;
           }
           if(((KeyEvent) event).getCode()== KeyCode.RIGHT)
           {
               this.direction = 1;
           }
       }
    }

    @Override
    public void move() {
        this.xl =this.xl+this.direction*this.velocity;
        this.xr =this.xr+this.direction*this.velocity;

    }
}
