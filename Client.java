import java.io.IOException;
import java.io.PrintStream;
import java.net.*;
import java.util.Scanner;

public class Client {
  public static void main(String[] args) throws IOException {
    Socket s = new Socket("127.0.0.1", 5000);
    Scanner scan = new Scanner(System.in);
    String msg = "";
    PrintStream p = new PrintStream(s.getOutputStream());
    while (true) {
      msg = scan.nextLine();
      p.println(msg);
      if (msg.equals("quit")) {
        break;
      }
    }
  }
}