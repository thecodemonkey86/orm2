package php;

public class Php7_1 extends Php {

	@Override
	public boolean supportsTypeHints() {
		return true;
	}

	@Override
	public boolean supportsNullableReturnTypes() {
		return true;
	}

}
