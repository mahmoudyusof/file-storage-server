import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
  public static void main(String[] args) throws IOException {

    Socket s = new Socket("127.0.0.1", 5000);
    DataInputStream dis = new DataInputStream(s.getInputStream());
    DataOutputStream dos = new DataOutputStream(s.getOutputStream());
    InputStream is = s.getInputStream();
    FileOutputStream fr;

    Scanner scan = new Scanner(System.in);
    String msg = "";
    String res = "";
    res = dis.readUTF();
    System.out.print("\u001B[32m" + res + "\u001B[0m$ ");
    while (true) {
      msg = scan.nextLine();
      if (msg.equals("quit")) {
        dos.writeUTF(msg);
        dos.flush();
        System.out.println("bye!");
        break;
      } else if (msg.split(" ")[0].equals("download")) {
        dos.writeUTF("download " + msg.split(" ")[1]);
        dos.flush();

        res = dis.readUTF();

        try {
          int size = Integer.parseInt(res);
          String fileName = msg.split(" ")[2];
          fr = new FileOutputStream(System.getProperty("user.dir") + File.separator + fileName);
          byte b[] = new byte[size];
          is.read(b, 0, b.length);
          fr.write(b, 0, b.length);
          fr.close();

          res = dis.readUTF();
          System.out.println(res);
          System.out.flush();
        } catch (NumberFormatException e) {
          System.out.println(res);
          System.out.flush();
        }

      } else if (msg.split(" ")[0].equals("upload")) {
        dos.writeUTF("upload " + msg.split(" ")[2]);
        dos.flush();

        String fileName = msg.split(" ")[1];
        File file = new File(fileName);

        if (!file.exists() || file.isDirectory()) {
          dos.writeUTF("No such file");
        } else {
          FileInputStream fis = new FileInputStream(file);
          byte b[] = new byte[(int) file.length()];
          fis.read(b, 0, b.length);
          fis.close();

          dos.writeUTF(String.format("%d", b.length));
          OutputStream os = s.getOutputStream();
          os.write(b);
          os.flush();
        }
        res = dis.readUTF();
        System.out.println(res);
        System.out.flush();

      } else {
        dos.writeUTF(msg);
        dos.flush();
        res = dis.readUTF();
        System.out.println(res);
        System.out.flush();
      }

      dos.writeUTF("cwd");
      dos.flush();
      res = dis.readUTF(); // this returns the current working directory
      System.out.print("\u001B[32m" + res + "\u001B[0m$ ");

    }

    dis.close();
    dos.close();
    s.close();
    scan.close();
  }
}