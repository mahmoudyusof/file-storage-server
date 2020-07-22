package commands;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.HashMap;

public class Commands {

  public HashMap<String, Command> CMDs = new HashMap<>();
  public DataOutputStream dos;
  public DataInputStream dis;
  public Socket s;

  public static final String RESET = "\u001B[0m";
  public static final String BLACK = "\u001B[30m";
  public static final String CYAN = "\u001B[36m";

  public static final String YELLOW_BG = "\u001B[43m";

  public Commands(Socket clientSocket) throws IOException {
    this.CMDs.put("touch", this.touch);
    this.CMDs.put("mkdir", this.mkdir);
    this.CMDs.put("clear", this.clear);
    this.CMDs.put("ls", this.listDir);
    this.CMDs.put("cwd", this.getWorkingDir);
    this.CMDs.put("rm", this.removeFile);
    this.CMDs.put("cd", this.changeDirectory);
    this.CMDs.put("mv", this.rename);
    this.CMDs.put("cp", this.copy);
    this.CMDs.put("download", this.download);
    this.CMDs.put("upload", this.upload);
    this.CMDs.put("help", this.help);

    this.dos = new DataOutputStream(clientSocket.getOutputStream());
    this.dis = new DataInputStream(clientSocket.getInputStream());
    this.s = clientSocket;
  }

  private String srcPath = "";
  private String targetPath = "";
  public String cwd = "/home/mahmoud/projects/java-stuff/FileStorageServer/server/";

  private Command help = new Command() {
    public void run() throws IOException {
      dos.writeUTF(
          CYAN + "\ntouch " + RESET + YELLOW_BG + BLACK + "<filename>" + RESET + " => create new file\n" 
          + CYAN + "mkdir " + RESET + YELLOW_BG + BLACK + "<dirname>" + RESET + " => create new directory\n" 
          + CYAN + "ls " + RESET + YELLOW_BG + BLACK + "[<dirname>]" + RESET + " => list directory content\n" 
          + CYAN + "cwd " + RESET + "=> print current working directory\n" 
          + CYAN + "rm " + RESET + YELLOW_BG + BLACK + "<name>" + RESET + " => remove file or directory\n"
          + CYAN + "cd " + RESET + YELLOW_BG + BLACK + "<dirname>" + RESET + " => change working directory\n"
          + CYAN + "mv " + RESET + YELLOW_BG + BLACK + "<srcFilePath>" + RESET + " " + YELLOW_BG + BLACK + "<targetFilePath> " + RESET + " => move file\n"
          + CYAN + "cp " + RESET + YELLOW_BG + BLACK + "<srcFilePath>" + RESET + " " + YELLOW_BG + BLACK + "<targetFilePath> " + RESET + " => copy file\n"
          + CYAN + "download " + RESET + YELLOW_BG + BLACK + "<srcFileOnServer>" + RESET + " " + YELLOW_BG + BLACK + "<targetPathOnClient> " + RESET + " => download file\n" 
          + CYAN + "upload " + RESET + YELLOW_BG + BLACK + "<srcFileOnClient>" + RESET + " " + YELLOW_BG + BLACK + "<targetPathOnServer> " + RESET + " => upload file\n"
          + CYAN + "quit\n");
    }
  };

  private Command touch = new Command() {
    public void run() throws IOException {
      if (srcPath.equals("")) {
        dos.writeUTF("Please provide a file name");
        return;
      }
      try {
        File file = new File(cwd + srcPath);
        if (file.createNewFile()) {
          dos.writeUTF("File created successfully!");
        } else {
          dos.writeUTF("File already exists");
        }
      } catch (Exception e) {
        dos.writeUTF(e.getMessage());
      }
    }
  };

  private Command mkdir = new Command() {
    public void run() throws IOException {
      if (srcPath.equals("")) {
        dos.writeUTF("Please provide a directory name");
        return;
      }
      try {
        File dir = new File(cwd + srcPath);
        if (dir.mkdirs()) {
          dos.writeUTF("Directory created successfully!");
        } else {
          dos.writeUTF("Directory already exists");
        }
      } catch (Exception e) {
        dos.writeUTF(e.getMessage());
      }
    }
  };

  private Command listDir = new Command() {
    public void run() throws IOException {
      try {
        File dirToList = new File(cwd + srcPath);
        String[] items = dirToList.list();
        if (items == null) {
          dos.writeUTF("No such file or directory");
          return;
        }
        String response = "";
        for (String item : items) {
          response += item + "  ";
        }
        dos.writeUTF(response);
      } catch (Exception e) {
        dos.writeUTF(e.getMessage());
      }
    }
  };

  private Command getWorkingDir = new Command() {
    public void run() throws IOException {
      dos.writeUTF(cwd);
    }
  };

  private Command removeFile = new Command() {
    public void run() throws IOException {
      if (srcPath.equals("")) {
        dos.writeUTF("Please provide a file name");
        return;
      }
      try {
        File toDelete = new File(cwd + srcPath);
        String response = toDelete.isDirectory() ? "Direcotry deleted Successfully" : "File deleted successfully";
        this.delete(toDelete);
        dos.writeUTF(response);
      } catch (Exception e) {
        dos.writeUTF(e.getMessage());
      }
    }

    private void delete(File file) throws IOException {
      if (file.isDirectory()) {
        if (file.list().length == 0) {
          file.delete();
        } else {
          String files[] = file.list();
          for (String temp : files) {
            File fileDelete = new File(file, temp);

            delete(fileDelete);
          }
          if (file.list().length == 0) {
            file.delete();
          }
        }
      } else {
        file.delete();
      }
    }
  };

  private Command changeDirectory = new Command() {
    public void run() throws IOException {
      try {
        File dir = new File(cwd + srcPath);
        if (dir.isDirectory()) {
          cwd = dir.getCanonicalPath() + File.separator;
          dos.writeUTF("Changed Directory!");
        } else {
          dos.writeUTF("No such directory");
        }
      } catch (Exception e) {
        dos.writeUTF(e.getMessage());
      }
    }
  };

  private Command rename = new Command() {
    public void run() throws IOException {
      try {
        File src = new File(cwd + srcPath);
        File trgt = new File(cwd + targetPath);
        if (!src.exists()) {
          dos.writeUTF("No such file or directory!");
          return;
        }
        if (trgt.exists()) {
          dos.writeUTF("Target file name already exists!");
          return;
        }
        if (src.renameTo(trgt)) {
          dos.writeUTF("File moved successfully");
        } else {
          dos.writeUTF("Couldn't move file");
        }
      } catch (Exception e) {
        dos.writeUTF("Couldn't move file");
      }
    }
  };

  private Command copy = new Command() {
    public void run() throws IOException {
      try {
        File src = new File(cwd + srcPath);
        File trgt = new File(cwd + targetPath);
        if (!src.exists()) {
          dos.writeUTF("No such file or directory!");
          return;
        }
        if (trgt.exists()) {
          dos.writeUTF("Target file name already exists!");
          return;
        }

        Files.copy(src.toPath(), trgt.toPath());
        dos.writeUTF("File moved successfully");

      } catch (Exception e) {
        dos.writeUTF("Couldn't move file");
      }
    }
  };

  private Command download = new Command() {
    public void run() throws IOException {
      try {
        File file = new File(cwd + srcPath);
        if (!file.exists() || file.isDirectory()) {
          dos.writeUTF("No such file on server");
          return;
        }
        FileInputStream fr = new FileInputStream(cwd + srcPath);
        byte b[] = new byte[(int) file.length()];
        fr.read(b, 0, b.length);
        fr.close();

        OutputStream os = s.getOutputStream();
        dos.writeUTF(String.format("%d", b.length));
        dos.flush();

        os.write(b, 0, b.length);
        os.flush();
        dos.writeUTF("successful download");
        dos.flush();
      } catch (Exception e) {
        dos.writeUTF(e.getMessage());
        dos.flush();
      }
    }
  };

  private Command upload = new Command() {
    public void run() throws IOException {
      try {
        int size = Integer.parseInt(dis.readUTF());
        FileOutputStream fr = new FileOutputStream(cwd + srcPath);
        InputStream is = s.getInputStream();
        byte b[] = new byte[size];
        is.read(b, 0, b.length);
        fr.write(b, 0, b.length);
        fr.close();

        dos.writeUTF("File uploaded successfully!");
      } catch (NumberFormatException e) {
        dos.writeUTF("No such file on client");
      }
    }
  };

  private Command clear = new Command() {
    public void run() throws IOException {
      dos.writeUTF("\033[H\033[2J");
    }
  };

  public void setPath(String src, String trgt) {
    this.srcPath = src;
    this.targetPath = trgt;
  }

}