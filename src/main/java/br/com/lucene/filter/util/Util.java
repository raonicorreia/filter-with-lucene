package br.com.lucene.filter.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLConnection;

public class Util {

    private static final String TEMP_FILE = "temp.pdf";

    public static File getFile(String path) throws IOException {
        if (path != null && !path.isEmpty()) {
            if (path.startsWith("http")) {
                if (createFileTemporary(path)) {
                    return new File(TEMP_FILE);
                } else {
                    throw new IOException("Não foi possível acessar arquivo: " + path);
                }
            } else {
                return new File(path);
            }
        }
        throw new IOException("Arquivo inválido: " + path);
    }

    private static boolean createFileTemporary(String path) throws IOException {
        URL url = new URL(path);
        byte[] bytes = new byte[1024];
        int bytesLength;
        FileOutputStream fileOutputStream = new FileOutputStream(TEMP_FILE);
        URLConnection urlConn = url.openConnection();
        if (urlConn.getContentType().equalsIgnoreCase("application/pdf")) {
            try {
                InputStream inputStream = url.openStream();
                while ((bytesLength = inputStream.read(bytes)) != -1) {
                    fileOutputStream.write(bytes, 0, bytesLength);
                }
                fileOutputStream.flush();
                fileOutputStream.close();
                inputStream.close();
                return true;
            } catch (ConnectException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }
}