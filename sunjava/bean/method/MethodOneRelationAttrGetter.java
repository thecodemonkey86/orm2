package sunjava.bean.method;

import sunjava.core.Attr;
import sunjava.core.Types;

public class MethodOneRelationAttrGetter extends MethodAttrGetter{

	public MethodOneRelationAttrGetter(Attr a, boolean loadIfNotLoaded) {
		super(a, loadIfNotLoaded);
	}

	@Override
	public void addImplementation() {
		addThrowsException(Types.SqlException);
		super.addImplementation();
	}
}
