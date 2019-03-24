package php.core;

import php.lib.LibOperator;

public class PhpStringConcatOperator extends LibOperator {

	public PhpStringConcatOperator() {
		super(".", Types.String, false);
	}

}
