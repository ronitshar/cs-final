import java.io.*;
import javax.json.*;
import java.net.*;
import java.util.*;

public class NBATier {

    public static void main(String[] args) throws IOException{
        URL url = new URL("http://data.nba.net/10s/prod/v1/2018/players.json");
        InputStream is = url.openStream();
		JsonReader reader = Json.createReader(new InputStreamReader(is, "UTF-8"));
        JsonObject jo = reader.readObject();
        JsonObject jo1 = jo.getJsonObject("_internal");
        System.out.println("Stats last updated: " + jo1.getString("pubDateTime"));
        JsonArray jo2 = jo.getJsonObject("league").getJsonArray("standard");
        ArrayList<String> keys = new ArrayList<String>();
        ArrayList<String> fnames = new ArrayList<String>();
        ArrayList<String> lnames = new ArrayList<String>();
        ArrayList<Double> points = new ArrayList<Double>();
        ArrayList<Double> pointsSorted = new ArrayList<Double>();
        ArrayList<Double> fgp = new ArrayList<Double>();
        for (int i = 0; i < 497; i++) {
            JsonObject player = jo2.getJsonObject(i);
            String a = player.getString("personId");
            keys.add(a);
        }
        pointsSorted.add(0, 0.0);
        System.out.print("Progress: ");
        for (int i = 0; i < keys.size(); i++) {
            URL url2 = new URL("http://data.nba.net/10s/prod/v1/2018/players/" + keys.get(i) +"_profile.json");
            InputStream is2 = url2.openStream();
            JsonReader reader2 = Json.createReader(new InputStreamReader(is2, "UTF-8"));
            JsonObject ja = reader2.readObject();
            JsonObject ja1 = ja.getJsonObject("league");
            JsonObject ja2 = ja1.getJsonObject("standard");
            JsonObject ja3 = ja2.getJsonObject("stats");
            JsonObject ja4 = ja3.getJsonObject("latest");
            double pts = Double.parseDouble(ja4.getString("ppg"));
            if (pts > pointsSorted.get(0)) {
                pointsSorted.add(pts);
                Collections.sort(pointsSorted);
                if (pointsSorted.size() > 10) {
                    pointsSorted.remove(0);
                }
                points.add(pts);
                JsonObject player = jo2.getJsonObject(i);
                fnames.add(player.getString("firstName"));
                lnames.add(player.getString("lastName"));
                fgp.add(Double.parseDouble(ja4.getString("fgp")));
            }
            
            if (i == 0) {
                System.out.print("|");
            } else if (i % 35 == 0) {
                System.out.print("|");
            } else if (i == 496) {
                System.out.print("|");
            } else {

            }
        }
        System.out.println();
        ArrayList<Double> effpost = new ArrayList<Double>();
        ArrayList<Double> eps = new ArrayList<Double>();
        for (int i = 0; i < pointsSorted.size(); i++) {
            int index = points.indexOf(pointsSorted.get(i));
            effpost.add(pointsSorted.get(i) / (0.01 * fgp.get(i)));
            eps.add(effpost.get(i));
        }
        Collections.sort(eps);
        int j = 1;
        System.out.println("Ranking of NBA Players by Scoring Potential:");
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