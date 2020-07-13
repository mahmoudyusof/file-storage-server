import java.io.File;
import java.io.IOException;

class Server {
  public static void main(String[] args) {
    if (args.length > 0) {
      String command = args[0];
      switch (command) {
        case ("touch"):
          try {
            String fileName = args[1];
            File fileObj = new File(fileName);
            if (fileObj.createNewFile()) {
              System.out.println("File Created Successfully");
            } else {
              System.out.println("File with this name already exists");
            }
          } catch (IOException e) {
            System.out.println("Something Went Wrong");
            e.printStackTrace();
          }
      }
    }
  }
}