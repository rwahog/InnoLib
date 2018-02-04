import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class User_interface {
    static String command;
    public static void command(Connection conn, Scanner in) throws SQLException {

        command = GUI.read();
        switch (command){
            case("registration"):
                registration(conn, in);
                break;
            case("log in"):
                logIn(conn, in);
                break;
            case("exit"):
                GUI.exit();
            default:
                GUI.print("Wrong input");
                command(conn, in);
        }
    }

    public static void logIn(Connection conn, Scanner in) throws SQLException {
        GUI.print("Patron/Librarian");
        command = GUI.read();
        switch (command){
            case("Librarian"):
                Librarian librarian = new Librarian(conn, in);
                librarian.login();
                break;
            case("Patron"):
                Patron patron = new Patron(conn, in);
                patron.login();
                Patron_interface.command(conn, in, patron);
                break;
            default:
                GUI.print("Incorrect input");
                logIn(conn, in);
        }
        GUI.print("try again");
        logIn(conn, in);
    }

    public static void registration(Connection conn, Scanner in) throws SQLException {
        GUI.print("Patron/Librarian");
        command = GUI.read();
        switch (command){
            case("Librarian"):
                Librarian librarian = new Librarian(conn, in);
                librarian.read();
                break;
            case("Patron"):
                Patron patron = new Patron(conn, in);
                patron.read();
                Patron_interface.command(conn, in, patron);
                break;
            default:
                GUI.print("Incorrect input");
                registration(conn, in);
        }
        GUI.print("try again");
        registration(conn, in);
    }
}