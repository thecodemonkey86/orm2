package sunjava.cls.bean.method;

import sunjava.Types;
import sunjava.cls.Attr;

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
