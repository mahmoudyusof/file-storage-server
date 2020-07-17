package commands;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Commands {

  public static HashMap<String, Command> CMDs = new HashMap<>();

  public static void init() {
    CMDs.put("touch", touch);
    CMDs.put("mkdir", mkdir);
    CMDs.put("clear", clear);
    CMDs.put("ls", listDir);
    CMDs.put("cwd", getWorkingDir);
    CMDs.put("rm", removeFile);
    CMDs.put("cd", changeDirectory);
    CMDs.put("mv", rename);
  }

  private static String srcPath = "";
  private static String targetPath = "";
  public static String cwd = "/home/mahmoud/projects/java-stuff/FileStorageServer/ignore/";

  private static Command touch = new Command() {
    public String run() {
      if (srcPath.equals("")) {
        return "Please provide a file name";
      }
      try {
        File file = new File(cwd + srcPath);
        if (file.createNewFile()) {
          return "File created successfully!";
        } else {
          return "File already exists";
        }
      } catch (Exception e) {
        return e.getMessage();
      }
    }
  };

  private static Command mkdir = new Command() {
    public String run() {
      if (srcPath.equals("")) {
        return "Please provide a directory name";
      }
      try {
        File dir = new File(cwd + srcPath);
        if (dir.mkdirs()) {
          return "Directory created successfully!";
        } else {
          return "Directory already exists";
        }
      } catch (Exception e) {
        return e.getMessage();
      }
    }
  };

  private static Command listDir = new Command() {
    public String run() {
      try {
        File dirToList = new File(cwd + srcPath);
        String[] items = dirToList.list();
        if (items == null) {
          return "No such file or directory";
        }
        String response = "";
        for (String item : items) {
          response += item + "  ";
        }
        return response;
      } catch (Exception e) {
        return e.getMessage();
      }
    }
  };

  private static Command getWorkingDir = new Command() {
    public String run() {
      return cwd;
    }
  };

  private static Command removeFile = new Command() {
    public String run() {
      if (srcPath.equals("")) {
        return "Please provide a file name";
      }
      try {
        File toDelete = new File(cwd + srcPath);
        String response = toDelete.isDirectory() ? "Direcotry deleted Successfully" : "File deleted successfully";
        this.delete(toDelete);
        return response;
      } catch (Exception e) {
        return e.getMessage();
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
    public String run() {
      try {
        File dir = new File(cwd + srcPath);
        if (dir.isDirectory()) {
          cwd = dir.getCanonicalPath() + File.separator;
          return "Changed Directory!";
        } else {
          return "No such directory";
        }
      } catch (Exception e) {
        return e.getMessage();
      }
    }
  };

  private static Command rename = new Command() {
    public String run() {
      try {
        File src = new File(cwd + srcPath);
        File trgt = new File(cwd + targetPath);
        if (!src.exists()) {
          return "No such file or directory!";
        }
        if (trgt.exists()) {
          return "Target file name already exists!";
        }
        if (src.renameTo(trgt)) {
          return "File moved successfully";
        } else {
          return "Couldn't move file";
        }
      } catch (Exception e) {
        return "Couldn't move file";
      }
    }
  };

  private static Command clear = new Command() {
    public String run() {
      return "\033[H\033[2J";
    }
  };

  public static void setPath(String src, String trgt) {
    srcPath = src;
    targetPath = trgt;
  }

}