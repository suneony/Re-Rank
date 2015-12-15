import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
	public static String getProperty(String key){
		Properties config=new Properties();
		InputStream configStream=null;
		try {
			configStream=new FileInputStream("config.properties");
			config.load(configStream);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				configStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return config.getProperty(key);
	}
}
