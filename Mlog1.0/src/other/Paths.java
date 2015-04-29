package other;

/**
 * Constants class for Paths in the application
 * 
 * @author Umut G�kbayrak
 */
public class Paths {
	private static String prefix = "/";
	private static String logFolder;
	private static String cfgFolder;
	private static String libFolder;
	private static String resFolder;
	private static String clsFolder;
	private static String dbFolder;
	private static String backupFolder;
    private static String webXmlFolder;//web.xml的地址
	public static String getBackupFolder() {
		return backupFolder;
	}

	public static void setBackupFolder(String backupFolder) {
		Paths.backupFolder = backupFolder;
	}

	public static String getCfgFolder() {
		return cfgFolder;
	}

	public static void setCfgFolder(String cfgFolder) {
		Paths.cfgFolder = cfgFolder;
	}

	public static String getClsFolder() {
		return clsFolder;
	}

	public static void setClsFolder(String clsFolder) {
		Paths.clsFolder = clsFolder;
	}

	public static String getDbFolder() {
		return dbFolder;
	}

	public static void setDbFolder(String dbFolder) {
		Paths.dbFolder = dbFolder;
	}

	public static String getLibFolder() {
		return libFolder;
	}

	public static void setLibFolder(String libFolder) {
		Paths.libFolder = libFolder;
	}

	public static String getLogFolder() {
		return logFolder;
	}

	public static void setLogFolder(String logFolder) {
		Paths.logFolder = logFolder;
	}

	public static String getPrefix() {
		return prefix;
	}

	public static void setPrefix(String prefix) {
		Paths.prefix = prefix;
	}

	public static String getResFolder() {
		return resFolder;
	}

	public static void setResFolder(String resFolder) {
		Paths.resFolder = resFolder;
	}

	public static String getWebXmlFolder() {
		return webXmlFolder;
	}

	public static void setWebXmlFolder(String webXmlFolder) {
		Paths.webXmlFolder = webXmlFolder;
	}
	
}
