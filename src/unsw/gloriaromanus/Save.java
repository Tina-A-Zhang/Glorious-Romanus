package unsw.gloriaromanus;

import java.io.File;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Save {
    public Save(Game g) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File("SavedGame.json"), g);
            String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(g);
            System.out.println(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

