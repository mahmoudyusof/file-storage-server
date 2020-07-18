package commands;

import java.io.IOException;

public interface Command {
  public void run() throws IOException;
}