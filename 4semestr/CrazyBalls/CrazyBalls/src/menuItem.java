import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class menuItem  extends DisplayObject{

    String text;
    public menuItem(double xl, double yl, double xr, double yr, Color color, GraphicsContext gc,String text) {
        super(xl, yl, xr, yr, color, gc);
        this.text = text;
    }
    public void render(){
        this.gc.setFill(Color.BLACK);
        this.gc.fillRect(this.xl, this.yl, this.xr - this.xl, this.yr - this.yl);
        this.gc.setFill(Color.GREEN);
        this.gc.fillRect(this.xl + 1, this.yl + 1, this.xr - this.xl - 2, this.yr - this.yl - 2);
        gc.setFill(Color.RED);
        gc.setFont(Font.font("Arial", 24));
        gc.fillText(this.text, this.xl+(this.xr-this.xl)/2, this.yr-(this.yr-this.yl)/2);
    };
    public void update(){
    };
}
