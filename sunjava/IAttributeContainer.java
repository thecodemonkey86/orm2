package sunjava;

import sunjava.cls.Attr;

public interface IAttributeContainer {
	public Attr getAttrByName(String name);
	public Attr getAttr(Attr prototype);
	public Attr addAttr(Attr attr);
}
