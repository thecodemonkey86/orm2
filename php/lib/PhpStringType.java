package php.lib;

import java.util.ArrayList;

import php.core.PhpCls;
import php.core.expression.PhpStringPlusEqOperator;

public class PhpStringType extends PhpCls {

	public PhpStringType() {
		super("string", null);
		
	}

	public void addDeclarations() {
		operators = new ArrayList<>();
		operators.add(new PhpStringPlusEqOperator());
	}
}
