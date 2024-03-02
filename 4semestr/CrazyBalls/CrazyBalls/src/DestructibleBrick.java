import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.*;
import java.beans.Visibility;

public class DestructibleBrick extends DisplayObject {


    private int Durability;

    DestructibleBrick(double xl, double yl,double xr,double yr, Color color, int Durability,GraphicsContext gc)
    {
        super(xl,yl,xr,yr,color,gc);
        this.Durability = Durability;
    }

    public void render(){
        if(isVisible()) {
                if(this.Durability>=75)
                {
                    this.gc.setFill(Color.BLACK);
                    this.gc.fillRect(this.xl, this.yl, this.xr - this.xl, this.yr - this.yl);
                    this.gc.setFill(Color.BROWN);
                    this.gc.fillRect(this.xl + 1, this.yl + 1, this.xr - this.xl - 2, this.yr - this.yl - 2);

                }
                else if(this.Durability>=50){
                    this.gc.setFill(Color.BLACK);
                    this.gc.fillRect(this.xl, this.yl, this.xr - this.xl, this.yr - this.yl);
                    this.gc.setFill(Color.ORANGE);
                    this.gc.fillRect(this.xl + 1, this.yl + 1, this.xr - this.xl - 2, this.yr - this.yl - 2);

                }
                else if(this.Durability>=25)
                {
                    this.gc.setFill(Color.BLACK);
                    this.gc.fillRect(this.xl, this.yl, this.xr - this.xl, this.yr - this.yl);
                    this.gc.setFill(Color.BLUE);
                    this.gc.fillRect(this.xl + 1, this.yl + 1, this.xr - this.xl - 2, this.yr - this.yl - 2);

                } else if (this.Durability>=1) {

                    this.gc.setFill(Color.BLACK);
                    this.gc.fillRect(this.xl, this.yl, this.xr - this.xl, this.yr - this.yl);
                    this.gc.setFill(Color.GREEN);
                    this.gc.fillRect(this.xl + 1, this.yl + 1, this.xr - this.xl - 2, this.yr - this.yl - 2);

                }
            }

    };
    public void update(){
        if(this.Durability==0) this.changeVisibility();
    }
    public int getDurability(){return this.Durability;};
    public void changeVisibility(){
        this.visible = !this.visible;
    }
    public void changeDurability(int Durability)
    {
        this.Durability = this.Durability -Durability;
    }
    public boolean isDestroyed (){return false;}

}
