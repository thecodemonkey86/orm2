package php;

public class Php7_2 extends Php {

	@Override
	public boolean supportsTypeHints() {
		return true;
	}

	@Override
	public boolean supportsNullableTypeHint() {
		return true;
	}

}
