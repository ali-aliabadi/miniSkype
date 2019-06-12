package client;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;

public class Test {

    public static void main(String[] args) throws JsonProcessingException {

        /**
         * fields must be private.
         *
         */

        Massage msg = new Massage(false, "Login", 13, CommandsType.Login);
        msg.getTestArrayListOfLonges().add(31L);
        msg.getTestArrayListOfLonges().add(-5432L);
        msg.getTestArrayListOfLonges().add(98766L);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(msg);

        System.out.println(json);

        ////////////////////////////////////////    now reverse    /////////////////////////////////////////////////

        /**
         * class must have default constractor
         */

        ObjectMapper mapper = new ObjectMapper();

        try {

            // Convert JSON string from file to Object
            Massage user = mapper.readValue(json, Massage.class);
            System.out.println(user.getTestArrayListOfLonges());
            System.out.println(user.getCommand());
            System.out.println(user.getCommandType());
            System.out.println(user.getTestInt());
            System.out.println(user.getTestString());

        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
