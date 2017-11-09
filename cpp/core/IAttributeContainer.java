package cpp.core;

public interface IAttributeContainer {
	public Attr getAttrByName(String name);
	public Attr getAttr(Attr prototype);
}
