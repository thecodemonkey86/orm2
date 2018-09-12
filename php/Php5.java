package php;

public class Php5 extends Php {

	@Override
	public boolean supportsTypeHints() {
		return false;
	}

	@Override
	public boolean supportsNullableTypeHint() {
		return false;
	}

}
