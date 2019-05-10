import java.io.*;
import javax.json.*;
import java.net.*;
import java.util.*;

public class NBATier {

    public static void main(String[] args) throws IOException{
        System.out.println("Enter the abbreviation of a team: ");
        Scanner console = new Scanner(System.in);
        String team = console.next();
        team = team.toUpperCase();
        URL url = new URL("http://site.api.espn.com/apis/site/v2/sports/basketball/nba/teams");
        InputStream is = url.openStream();
		JsonReader reader = Json.createReader(new InputStreamReader(is, "UTF-8"));
        JsonObject jo = reader.readObject();
        JsonObject jop = jo.getJsonArray("sports").getJsonObject(0);
        System.out.println(jop.getString("name"));

    }

}