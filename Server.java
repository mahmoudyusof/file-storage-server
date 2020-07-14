import java.net.*;
import java.util.HashMap;
import java.io.*;
import commands.*;

public class Server {
  public static void main(String[] args) throws IOException {
    ServerSocket ss = new ServerSocket(5000);
    Socket s = ss.accept();

    HashMap<String, Command> CMDs = new HashMap<>();
    CMDs.put("touch", Commands.touch);

    DataInputStream dis = new DataInputStream(s.getInputStream());
    DataOutputStream dos = new DataOutputStream(s.getOutputStream());

    String msg = "";

    while (true) {
      msg = dis.readUTF();

      if (msg.equals("quit")) {
        dos.writeUTF("bye!");
        break;
      } else {
        if (CMDs.containsKey(msg)) {
          CMDs.get(msg).run();
        } else {
          dos.writeUTF("This command doesn't exist");
          dos.flush();
        }
      }
    }

    dos.close();
    dis.close();
    s.close();
    ss.close();

  }
}