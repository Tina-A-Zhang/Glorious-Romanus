package unsw.gloriaromanus;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Load {

    static Game game;

    /**
     * loading game class
     * 
     * @param game   the game to load
     * @throws Exception
     */
    public Load(Game game) throws Exception {
        String fileName = "SaveGame.json";
        FileInputStream fis = new FileInputStream(fileName);
        ObjectInputStream ois = new ObjectInputStream(fis);
        game = (Game) ois.readObject();
        ois.close();
    }

}
