package app;

import static spark.Spark.get;
import static spark.Spark.post;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Random;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Application {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/java36a";
        String user = "postgres";
        String password = "postgres";
        
        get("/lists", (req, res) -> {
            JsonArray result = new JsonArray();
            try (Connection con = DriverManager.getConnection(url, user, password);
            PreparedStatement pst = con.prepareStatement("SELECT * FROM list");
            ResultSet rs = pst.executeQuery()) {

                while (rs.next()) {
                    System.out.print(rs.getInt(1));
                    System.out.print(": ");
                    System.out.println(rs.getString(2));
                    JsonObject list = new JsonObject();

                    list.addProperty("id", rs.getInt(1));
                    list.addProperty("title", rs.getString(2));
                    list.addProperty("tasks", rs.getString(3));
                    System.out.println(list.toString());
                    res.status(200);
                    result.add(list);
                }
            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(Application.class.getName());
                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
            System.out.print(req.queryParams("username"));
            return result;
        });

        post("/tasks/create", (req, res) -> {
            Random rand = new Random(); 

            String title = req.queryParams("title");
            String body = req.queryParams("body");
            try (Connection con = DriverManager.getConnection(url, user, password);
            PreparedStatement pst = con.prepareStatement("INSERT INTO task(id, list, title, body) VALUES("+ rand.nextInt(100000000) + " " + 2 + " " + " " + title + " " + body + ") returning *;");
            ResultSet rs = pst.executeQuery()) {

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(Application.class.getName());
                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
            return "Added successfully";
        });

    }
}