import java.io.*;
import javax.json.*;
import java.net.*;
import java.util.*;

public class NBATier {

    public static void main(String[] args) throws IOException{
        URL url = new URL("http://data.nba.net/10s/prod/v1/2018/players.json"); // getting each player's NBA key
        InputStream is = url.openStream();
		JsonReader reader = Json.createReader(new InputStreamReader(is, "UTF-8"));
        JsonObject jo = reader.readObject();
        JsonObject jo1 = jo.getJsonObject("_internal");
        System.out.println("Stats last updated: " + jo1.getString("pubDateTime"));
        JsonArray jo2 = jo.getJsonObject("league").getJsonArray("standard");
        ArrayList<String> keys = new ArrayList<String>(); // array for key
        ArrayList<String> fnames = new ArrayList<String>(); // array for names
        ArrayList<String> lnames = new ArrayList<String>();
        ArrayList<Double> points = new ArrayList<Double>();
        ArrayList<Double> pointsSorted = new ArrayList<Double>(); // sorted points array
        ArrayList<Double> fgp = new ArrayList<Double>();
        for (int i = 0; i < 497; i++) {
            JsonObject player = jo2.getJsonObject(i); 
            String a = player.getString("personId"); // getting each player's key
            keys.add(a);
        }
        pointsSorted.add(0, 0.0);
        System.out.print("Progress: "); // show progress because program takes so long
        for (int i = 0; i < keys.size(); i++) {
            URL url2 = new URL("http://data.nba.net/10s/prod/v1/2018/players/" + keys.get(i) +"_profile.json"); // accessing each players stats
            InputStream is2 = url2.openStream();
            JsonReader reader2 = Json.createReader(new InputStreamReader(is2, "UTF-8"));
            JsonObject ja = reader2.readObject();
            JsonObject ja1 = ja.getJsonObject("league");
            JsonObject ja2 = ja1.getJsonObject("standard");
            JsonObject ja3 = ja2.getJsonObject("stats");
            JsonObject ja4 = ja3.getJsonObject("latest");
            double pts = Double.parseDouble(ja4.getString("ppg")); // get ppg
            if (pts > pointsSorted.get(0)) {
                pointsSorted.add(pts); // add to a sorted array
                Collections.sort(pointsSorted);
                if (pointsSorted.size() > 10) { // sort array, removing if there are more than 10 elements
                    pointsSorted.remove(0);
                }
                points.add(pts); // adds points to a second array, whose index matches the index of the player's name
                JsonObject player = jo2.getJsonObject(i);
                fnames.add(player.getString("firstName")); // add names
                lnames.add(player.getString("lastName"));
                fgp.add(Double.parseDouble(ja4.getString("fgp"))); // add FG%
            }
            
            if (i == 0) {
                System.out.print("|"); // progress bar update
            } else if (i % 35 == 0) {
                System.out.print("|");
            } else if (i == 496) {
                System.out.print("|");
            } else {

            }
        }
        System.out.println();
        ArrayList<Double> effpost = new ArrayList<Double>(); // sort by our ranking points, this is unsorted
        ArrayList<Double> eps = new ArrayList<Double>(); // sorted ranking points list
        for (int i = 0; i < pointsSorted.size(); i++) {
            int index = points.indexOf(pointsSorted.get(i));
            effpost.add(pointsSorted.get(i) / 1 - Math.pow((0.01 * fgp.get(i)), 2)); // formula to determine ranking points
            eps.add(effpost.get(i)); // sorted list
        }
        Collections.sort(eps); // sort ranking points from greatest to least
        int j = 1;
        System.out.println("Ranking of NBA Players by Scoring Potential:"); // output loop
        for (int i = eps.size() - 1; i >= 0; i--) {
            int index = effpost.indexOf(eps.get(i));
            int index2 = points.indexOf(pointsSorted.get(index));
            String fnamez = fnames.get(index2);
            String lnamez = lnames.get(index2);
            int ppoints = (int)(Math.round(eps.get(i)));
            System.out.print(j);
            j++;
            System.out.print(" " + fnamez + " " + lnamez+ ": ");
            System.out.print(ppoints);
            System.out.println(" points");
        }


    }

}