package commands;

import java.io.File;
import java.util.HashMap;

public class Commands {

  public static HashMap<String, Command> CMDs = new HashMap<>();

  public static void init() {
    CMDs.put("touch", touch);
    CMDs.put("mkdir", mkdir);
    CMDs.put("clear", clear);
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
      try {
        if (path.equals("")) {
          return "Please provide a directory name";
        }
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

  private static Command clear = new Command() {
    public String run() {
      return "\033[H\033[2J";
    }
  };

  public static void setPath(String name) {
    path = name;
  }

}