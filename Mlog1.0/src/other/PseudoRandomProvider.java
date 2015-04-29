package other;

import java.util.Random;

import org.bouncycastle.util.encoders.Hex;

/**
 * 
 * @author shenqn
 * 
 */

public class PseudoRandomProvider extends SoftWareRandonProvider {

	@Override
	public String genRandom(int length) {
		Random random = new Random();
		byte[] data = new byte[length];
		random.nextBytes(data);
		return new String(Hex.encode(data));
	}

}
