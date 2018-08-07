package grid.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * TextDatReader.java 2013-9-9 16:47:09
 * 
 * @Author George Bourne
 */
public class TextDatReader {
	public static String read(String path) throws IOException {
		File file = new File(path);
		try (InputStreamReader reader = new InputStreamReader(new FileInputStream(file), Charset.forName("utf8"))) {
		    char[] buffer = new char[(int) file.length()];
		    reader.read(buffer);
		    return new String(buffer);
		}
	}
}
