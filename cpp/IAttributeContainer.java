package cpp;

import cpp.core.Attr;

public interface IAttributeContainer {
	public Attr getAttrByName(String name);
	public Attr getAttr(Attr prototype);
}
