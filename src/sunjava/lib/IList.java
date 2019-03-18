package sunjava.lib;

import sunjava.core.JavaGenericInterface;
import sunjava.core.Type;

public class IList extends JavaGenericInterface {

	public IList(Type element) {
		super("List", element, "java.util");
	}

}
