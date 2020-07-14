package commands;

public class Commands {
  public static Command touch = new Command() {
    public void run() {
      System.out.println("Don't fucking touch me, bruh!");
    }
  };
}