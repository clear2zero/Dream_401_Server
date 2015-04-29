package other;


public abstract class BaseRandomProvider implements IRandomProvider {

	@Override
	public String genRandom() {

		return genRandom(IRandomProvider.length);
	}

	@Override
	public abstract String genRandom(int length);

}
