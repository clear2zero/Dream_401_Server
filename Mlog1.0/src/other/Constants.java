package other;

import org.jasen.JasenScanner;

import com.jenkov.mrpersister.PersistenceManager;

/**
 * @author Umut Gokbayrak
 *
 */
public class Constants {
    public static final String GENERALERR = "/error/generalErr.do";
	public static PersistenceManager persistMan = new PersistenceManager();
	public static JasenScanner spamScanner = null;
	public static String ISAUTOADDCONT = "isAutoAddCont";
	public static String ctrlModeCommonCA="common";
	public static String ctrlModeHebca="hebca";
	public static String ISSMSINFORM="isSmsInform";
	public static String EHCACHE_NAME = "safemailcache";
}
