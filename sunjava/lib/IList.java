package sunjava.lib;

import sunjava.cls.JavaGenericInterface;
import sunjava.cls.Type;

public class IList extends JavaGenericInterface {

	public IList(Type element) {
		super("List", element, "java.util");
	}

}
