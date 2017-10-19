package php.cls;

import php.Types;
import php.lib.LibOperator;

public class PhpStringConcatOperator extends LibOperator {

	public PhpStringConcatOperator() {
		super(".", Types.String, false);
	}

}
