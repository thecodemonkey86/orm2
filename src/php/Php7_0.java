package php;

public class Php7_0 extends Php {

	@Override
	public boolean supportsTypeHints() {
		return true;
	}

	@Override
	public boolean supportsNullableTypeHint() {
		return false;
	}

}
