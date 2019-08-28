package php;

public class Php5NoTypeHints extends Php{

	@Override
	public boolean supportsTypeHints() {
		return false;
	}

	@Override
	public boolean supportsNullableTypeHint() {
		return false;
	}

}
