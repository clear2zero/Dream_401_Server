package other;



/**
 * 
 * @author shenqn
 * 
 */
public class RandomProviderManager {

	public static IRandomProvider randomProvider = null;
	static {
		try {
			/*String provider = SettingControllerFactory
			.getMailboxSettingController().getSetting( "mail.randomProvider").getValue();*/
			String provider = "other.PseudoRandomProvider";//"mail.randomProvider";
			randomProvider = (IRandomProvider) Class.forName(provider)
					.newInstance();

		} catch (Exception e) {

			throw new RuntimeException(e);
		}
	}

	public static IRandomProvider getRandromProvider() {

		return randomProvider;
	}
}
