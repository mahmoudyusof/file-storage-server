import java.net.*;
import java.util.Scanner;
import java.io.IOException;

public class Server {
  public static void main(String[] args) throws IOException {
    ServerSocket ss = new ServerSocket(5000);
    Socket s = ss.accept();
    Scanner scan = new Scanner(s.getInputStream());
    String msg = "";
    while (true) {
      msg = scan.nextLine();
      if (msg.equals("quit")) {
        break;
      }
      System.out.println(msg);
    }

  }
}