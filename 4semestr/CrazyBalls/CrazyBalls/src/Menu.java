import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;

public class Menu extends  DisplayObject {
    SettingsWindow settings;
    Button startGameBtn;
    Button settingsBtn;
    Button closeBtn;
    int height;
    int width;
    menuItem startGameButtom;
    menuItem exitGameButton;
    menuItem settingsButton;
    public void update() {

    }
    public void render(){
        settingsButton.render();
        startGameButtom.render();
        exitGameButton.render();
    }


    Menu(double xl, double yl,double xr,double yr, Color backgroundColor,GraphicsContext gc){
        super(xl,yl,xr,yr,backgroundColor,gc);
        double buttonHeight = (this.yr-this.yl)/5;
        double buttonWidth = this.xr-this.xl;
        startGameButtom = new menuItem(xl,yl,xr,yl+buttonHeight,Color.GRAY,gc,"Start Game");
        settingsButton = new menuItem(xl,yl+2*buttonHeight,xr,yl+3*buttonHeight,Color.GRAY,gc,"Settings");
        exitGameButton =  new menuItem(xl,yl+4*buttonHeight,xr,yl+5*buttonHeight,Color.GRAY,gc,"Exit Game");
    }

}
