import java.io.File;
import java.io.IOException;

class Server {
  public static void main(String[] args) {
    try {
      String fileName = args.length > 0 ? args[0] : "FileName.txt";
      File fileObj = new File("ignore/" + fileName);
      if (fileObj.createNewFile()) {
        System.out.println("File Created Successfully");
      } else {
        System.out.println("File with this name already exists");
      }
    } catch (IOException e) {
      System.out.println("Something Went Wrong");
    }
  }
}