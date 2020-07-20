import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import commands.Commands;

public class ClientHandler implements Runnable {
  private Socket s;
  private Commands commands;
  private InputStream is;
  private OutputStream os;
  private DataInputStream dis;
  private DataOutputStream dos;

  ClientHandler(Socket clientSocket, Commands commands) throws IOException {
    this.s = clientSocket;
    this.commands = commands;
    this.is = this.s.getInputStream();
    this.os = this.s.getOutputStream();
    this.dis = new DataInputStream(this.is);
    this.dos = new DataOutputStream(this.os);
  }

  public void run() {
    String line = "";
    String[] splits;
    // String response = "";
    try {
      this.dos.writeUTF(this.commands.cwd);

      while (true) {
        line = dis.readUTF();
        splits = line.split(" ");

        if (splits[0].equals("quit")) {
          break;
        } else {
          if (this.commands.CMDs.containsKey(splits[0])) {

            if (splits.length > 2) {
              this.commands.setPath(splits[1], splits[2]);
            } else if (splits.length > 1) {
              this.commands.setPath(splits[1], "");
            }

            this.commands.CMDs.get(splits[0]).run();
            // dos.writeUTF(response);
            this.commands.setPath("", "");
          } else {
            dos.writeUTF("This command doesn't exist");
          }
        }
      }
    } catch (IOException e) {

    }
  }
}