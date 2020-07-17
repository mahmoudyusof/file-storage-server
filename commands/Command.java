package commands;

import java.io.DataOutputStream;
import java.io.IOException;

public interface Command {
  public void run(DataOutputStream dos) throws IOException;
}