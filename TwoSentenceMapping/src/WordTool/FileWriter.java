package WordTool;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class FileWriter {
  public void writeFile(String pFilename, StringBuffer pData) throws IOException {
    FileOutputStream fos = new FileOutputStream(pFilename + ".csv", true);
    OutputStreamWriter out = new OutputStreamWriter(fos, "UTF-8");

    out.write(pData.toString());
    out.flush();
    out.close();
  }

  public void writeFile(String pFilename, ArrayList<String> strArr) throws IOException {
    FileOutputStream fos = new FileOutputStream(pFilename + ".txt", true);
    OutputStreamWriter out = new OutputStreamWriter(fos, "UTF-8");

    for (String str : strArr) {
      out.write(str+'\n');
    }
    out.flush();
    out.close();
  }

}
