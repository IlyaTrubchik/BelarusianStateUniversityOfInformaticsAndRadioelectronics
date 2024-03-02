import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.event.Event;

import java.util.ArrayList;

public class Ball extends MovableObject{

    private double dx;
    private double dy;
    private double x;
    private double y;
    private int damage=0;
    private int velocity;
    private double angle = 45;


    public void ChangeSpeed(int velocity)
    {
        this.velocity = velocity;
    }
    public void render(){
       this.gc.setFill(this.color);
       this.gc.fillOval(this.xl,this.yl,this.xr-this.xl,this.yr-this.yl);
    };
    public void update(){
        this.move();
    };
    public int getDamage(){return this.damage;};

    public void eventHandler(Event event)
    {
        if(event!=null && event instanceof CollisionEvent)
        {
            switch (((CollisionEvent) event).getCollisionType()){
                case CollisionEvent.COLLISION_TOP -> {this.dy=-this.dy;}
                case CollisionEvent.COLLISION_BOTTOM -> {this.dy=-this.dy;}
                case CollisionEvent.COLLISION_LEFT -> {this.dx = -this.dx;break;}
                case CollisionEvent.COLLISION_RIGHT-> {this.dx = -this.dx;break;}
            }
        }
    }


    public CollisionEvent checkCollisions(ArrayList<DisplayObject> obj){
        for(int i = 0 ;i<obj.size();i++)
               {
                   DisplayObject currObj = obj.get(i);
                   if(currObj.isVisible() && currObj != this) {
                       if (currObj != this) {
                           if( this.xl<=currObj.xr && this.xr>=currObj.xl && this.yl<=currObj.yr && this.yr>=currObj.yl){
                              if (this.yl <= currObj.yr && this.yr >= currObj.yr){//TOp collide
                                  return new CollisionEvent(CollisionEvent.COLLISION_TOP);
                              }
                              else if(this.yl <= currObj.yl && this.yr >= currObj.yl)//Bottom collide
                              {
                                  return new CollisionEvent(CollisionEvent.COLLISION_BOTTOM);
                              }
                              else if(this.xl <= currObj.xr && this.xr >= currObj.xr) //Right collide
                              {
                                  return new CollisionEvent(CollisionEvent.COLLISION_RIGHT);
                              }
                              else if(this.xl <= currObj.xl && this.xr >= currObj.xl) //Left collide
                              {
                                    return new CollisionEvent(CollisionEvent.COLLISION_LEFT);
                              }
                           }
                       }
                   }
               }
        return null;
    }
    Ball (double xl, double yl,double xr,double yr, int velocity, Color color,GraphicsContext gc)
    {
        super(xl,yl,xr,yr,color,velocity,gc);
        this.dx = velocity*Math.cos(this.angle);
        this.dy = velocity*Math.sin(this.angle);
        this.x = xl+(xl-xr)/2;
        this.y = yl+(yr-yl)/2;
    }

    @Override
    public void move() {
        this.xl =this.xl+dx;
        this.yl =this.yl+dy;
        this.xr =this.xr+dx;
        this.yr =this.yr+dy;
    }
}
