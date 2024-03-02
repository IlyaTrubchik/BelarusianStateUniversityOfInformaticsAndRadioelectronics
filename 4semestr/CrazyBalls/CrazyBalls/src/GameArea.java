
import java.io.Serializable;
import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
public class GameArea extends  DisplayObject implements Serializable {

    private static final int ROW_COUNT = 10;
    private static final int COL_COUNT = 10;
    int height;
    int width;
    ArrayList<DisplayObject> displayObjects = new ArrayList<>();
    MessageBox message;
    ScoreBoard scoreBoard;
    Platform platform;

    GameArea(double xl,double yl,double xr,double yr,Color backgroundColor,GraphicsContext gc)
    {
        super(xl,yl,xr,yr,backgroundColor,gc);
    }
    public void render(){
        this.gc.setFill(Color.WHITE);
        this.gc.fillRect(xl,yl,xr-xl,yr-yl);
        for(int i = 0;i<this.displayObjects.size();i++) {
            this.displayObjects.get(i).render();
        }
    };
    public void update(){
        for(int i = 0;i<this.displayObjects.size();i++) {
            this.displayObjects.get(i).update();
        }
    };
    public boolean checkCollisions(){
        for(int i = 0;i<this.displayObjects.size();i++) {
            if(this.displayObjects.get(i) instanceof MovableObject) {
                ((MovableObject) this.displayObjects.get(i)).eventHandler
                        (this.displayObjects.get(i).checkCollisions(this.displayObjects));
            }
        }
        return false;
    };
    public void initialize(){
        //Bricks initialize
        double areaHeight = (this.yr-this.yl);
        double areaWidth =  (this.xr-this.xl);
        double brickWidth = (areaWidth)/COL_COUNT;
        double brickHeight =  (areaHeight/3)/ROW_COUNT;
        double xl = (this.xl);
        double yl = (this.yl);
        double xr = xl+brickWidth;
        double yr = yl+brickHeight;

        for(int i  =0 ;i<ROW_COUNT;i++)
        {
            for(int j  = 0 ;j<COL_COUNT;j++)
            {
                this.displayObjects.add(new DestructibleBrick(xl,yl,xr,yr, Color.GRAY,100,this.gc));
                xl=xr;
                xr = xl+brickWidth;
            }
            xl = (this.xl);
            xr = xl+brickWidth;
            yl = yr;
            yr=yl+brickHeight;
        }
        /// Borders
        this.displayObjects.add(new IndestructibleBrick(this.xl,this.yl-10,this.xr,this.yl,Color.BLACK,this.gc));
        this.displayObjects.add(new IndestructibleBrick(this.xl-10,this.yl,this.xl,this.yr,Color.BLACK,this.gc));
        this.displayObjects.add(new IndestructibleBrick(this.xr,this.yl,this.xr+10,this.yr,Color.BLACK,this.gc));
        this.displayObjects.add(new IndestructibleBrick(this.xl,this.yr,this.xr,this.yr+10,Color.BLACK,this.gc));
        //Ball adding
        this.displayObjects.add(new Ball((this.xr-this.xl)/2,this.yr-3*brickHeight,(this.xr-this.xl)/2+brickHeight,this.yr-2*brickHeight,2,Color.BLUE,this.gc));
        //Platform adding
        this.platform = new Platform((this.xr-this.xl)/2,this.yr-2*brickHeight,(this.xr-this.xl)/2+2*brickWidth,this.yr-brickHeight,5,Color.RED,this.gc);
        this.displayObjects.add(this.platform);

    }

}

