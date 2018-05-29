package finley.gmair.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

public class MachineProperties {
	private Logger logger = LoggerFactory.getLogger(MachineProperties.class);
	
	private static Properties props = new Properties();
	
	static {
		InputStream input = MachineProperties.class.getClassLoader().getResourceAsStream("machine.properties");
		try {
			props.load(input);
		} catch (Exception e) {

		}
	}
	
	private MachineProperties() {
		super();
	}
	
	public static String getValue(String key) {
		return props.getProperty(key);
	}
}
