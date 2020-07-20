import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;
import commands.Commands;

public class Server {
  public static void main(String[] args) throws IOException {
    ServerSocket ss = new ServerSocket(5000);
    System.out.println("Server running on localhost:5000 ...");

    while (true) {
      try {
        Socket s = ss.accept();
        System.out.println("Client Connected.");
        Commands commands = new Commands(s);
        ClientHandler ch = new ClientHandler(s, commands);
        Thread th = new Thread(ch);
        th.start();
      } catch (Exception e) {
        break;
      }
    }
    ss.close();
  }
}