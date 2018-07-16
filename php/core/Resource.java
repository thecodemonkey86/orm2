package php.core;

public class Resource extends Type{

	public Resource() {
		super("resource");
	}

	@Override
	public boolean typeHinting() {
		return false;
	}
}
