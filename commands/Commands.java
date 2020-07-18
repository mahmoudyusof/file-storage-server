package commands;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.util.HashMap;

public class Commands {

  public static HashMap<String, Command> CMDs = new HashMap<>();
  public static DataOutputStream dos;

  public static void init(Socket clientSocket) throws IOException {
    CMDs.put("touch", touch);
    CMDs.put("mkdir", mkdir);
    CMDs.put("clear", clear);
    CMDs.put("ls", listDir);
    CMDs.put("cwd", getWorkingDir);
    CMDs.put("rm", removeFile);
    CMDs.put("cd", changeDirectory);
    CMDs.put("mv", rename);
    CMDs.put("cp", copy);

    dos = new DataOutputStream(clientSocket.getOutputStream());
  }

  private static String srcPath = "";
  private static String targetPath = "";
  public static String cwd = "/home/mahmoud/projects/java-stuff/FileStorageServer/ignore/";

  private static Command touch = new Command() {
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

  private static Command mkdir = new Command() {
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

  private static Command listDir = new Command() {
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

  private static Command getWorkingDir = new Command() {
    public void run() throws IOException {
      dos.writeUTF(cwd);
    }
  };

  private static Command removeFile = new Command() {
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

  private static Command changeDirectory = new Command() {
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

  private static Command rename = new Command() {
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

  private static Command copy = new Command() {
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

  private static Command clear = new Command() {
    public void run() throws IOException {
      dos.writeUTF("\033[H\033[2J");
    }
  };

  public static void setPath(String src, String trgt) {
    srcPath = src;
    targetPath = trgt;
  }

}