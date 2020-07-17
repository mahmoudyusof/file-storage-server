import java.net.*;
import java.io.*;
import commands.*;

public class Server {
  public static void main(String[] args) throws IOException {
    ServerSocket ss = new ServerSocket(5000);
    System.out.println("Server running on localhost:5000 ...");
    Socket s = ss.accept();
    System.out.println("Client Connected.");

    Commands.init();

    DataInputStream dis = new DataInputStream(s.getInputStream());
    DataOutputStream dos = new DataOutputStream(s.getOutputStream());

    String line = "";
    String[] splits;
    // String response = "";

    dos.writeUTF(Commands.cwd);

    while (true) {
      line = dis.readUTF();
      splits = line.split(" ");

      if (splits[0].equals("quit")) {
        dos.writeUTF("bye!");
        break;
      } else {
        if (Commands.CMDs.containsKey(splits[0])) {

          if (splits.length > 2) {
            Commands.setPath(splits[1], splits[2]);
          } else if (splits.length > 1) {
            Commands.setPath(splits[1], "");
          }

          Commands.CMDs.get(splits[0]).run(dos);
          // dos.writeUTF(response);
          Commands.setPath("", "");
        } else {
          dos.writeUTF("This command doesn't exist");
        }
        dos.writeUTF(Commands.cwd);
        dos.flush();
      }
    }

    dos.close();
    dis.close();
    s.close();
    ss.close();

  }
}