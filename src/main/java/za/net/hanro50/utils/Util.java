package za.net.hanro50.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Util {
    public static void pipe(InputStream is, OutputStream os) throws IOException {
        int n;
        byte[] buffer = new byte[1024];
        while ((n = is.read(buffer)) > -1) {
            os.write(buffer, 0, n); // Don't allow any extra bytes to creep in, final write
        }
        os.close();
    }

    public static String readFile(File path)
            throws IOException {
        return readFile(path.getAbsolutePath());
    }

    public static String readFile(File path, Charset encoding)
            throws IOException {
        return readFile(path.getAbsolutePath(), encoding);
    }

    public static String readFile(String path)
            throws IOException {
        return readFile(path, Charset.defaultCharset());
    }

    public static String readFile(String path, Charset encoding)
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}
