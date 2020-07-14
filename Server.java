import java.net.*;
import java.util.HashMap;
import java.io.*;
import commands.*;

public class Server {
  public static void main(String[] args) throws IOException {
    ServerSocket ss = new ServerSocket(5000);
    System.out.println("Server running on localhost:5000 ...");
    Socket s = ss.accept();
    System.out.println("Client Connected.");

    Commands.init();

    HashMap<String, Command> CMDs = Commands.CMDs;

    DataInputStream dis = new DataInputStream(s.getInputStream());
    DataOutputStream dos = new DataOutputStream(s.getOutputStream());

    String line = "";
    String[] splits;

    while (true) {
      line = dis.readUTF();
      splits = line.split(" ");

      if (splits[0].equals("quit")) {
        dos.writeUTF("bye!");
        break;
      } else {
        if (CMDs.containsKey(splits[0])) {
          if (splits.length > 1) {
            Commands.setPath(splits[1]);
          }
          dos.writeUTF(CMDs.get(splits[0]).run());
        } else {
          dos.writeUTF("This command doesn't exist");
        }
        dos.flush();
      }
    }

    dos.close();
    dis.close();
    s.close();
    ss.close();

  }
}