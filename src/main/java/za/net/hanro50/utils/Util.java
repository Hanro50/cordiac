package za.net.hanro50.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Util {
    public static void pipe(InputStream is, OutputStream os) throws IOException {
        int n;
        byte[] buffer = new byte[1024];
        while ((n = is.read(buffer)) > -1) {
            os.write(buffer, 0, n); // Don't allow any extra bytes to creep in, final write
        }
        os.close();
    }
}
