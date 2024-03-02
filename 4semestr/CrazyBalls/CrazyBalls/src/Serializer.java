import javafx.scene.canvas.GraphicsContext;

import java.io.*;

public class Serializer {
    public static void Save(GameArea gameArea) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("E:/game.txt"));
        out.writeObject(gameArea);
        try {
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static GameArea Load(GraphicsContext gc) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream("E:/game.txt"));
        GameArea game = (GameArea) in.readObject();
        game.gc = gc;
        for (DisplayObject obj: game.displayObjects) {
            obj.setGc(gc);
        }
        in.close();
        return game;
    }
}
