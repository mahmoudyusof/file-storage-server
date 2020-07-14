import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Client {
  public static void main(String[] args) throws IOException {

    Socket s = new Socket("127.0.0.1", 5000);
    DataInputStream dis = new DataInputStream(s.getInputStream());
    DataOutputStream dos = new DataOutputStream(s.getOutputStream());

    Scanner scan = new Scanner(System.in);
    String msg = "";
    String res = "";

    while (true) {
      msg = scan.nextLine();
      dos.writeUTF(msg);
      dos.flush();

      res = dis.readUTF();
      System.out.println(res);
      if (res.equals("bye!")) {
        break;
      }
    }

    dis.close();
    dos.close();
    s.close();
    scan.close();
  }
}