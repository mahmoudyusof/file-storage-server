package commands;

import java.io.File;
import java.util.HashMap;

public class Commands {

  public static HashMap<String, Command> CMDs = new HashMap<>();

  public static void init() {
    CMDs.put("touch", touch);
    CMDs.put("mkdir", mkdir);
    CMDs.put("clear", clear);
    CMDs.put("ls", listDir);
    CMDs.put("cwd", getWorkingDir);
  }

  private static String path = "";

  private static Command touch = new Command() {
    public String run() {
      if (path.equals("")) {
        return "Please provide a file name";
      }
      try {
        File file = new File(path);
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
      if (path.equals("")) {
        return "Please provide a directory name";
      }
      try {
        File dir = new File(path);
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
      if (path.equals("")) {
        path = getWorkingDir.run();
      }
      try {
        File dirToList = new File(path);
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
      return System.getProperty("user.dir");
    }
  };

  private static Command clear = new Command() {
    public String run() {
      return "\033[H\033[2J";
    }
  };

  public static void setPath(String name) {
    path = name;
  }

}