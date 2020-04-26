package cpp.core;

public class CopyConstructor extends Constructor {

	public CopyConstructor(Cls cls) {
		addParam(cls.toConstRef(),"");
	}
	
	@Override
	public void addImplementation() {
	}
	
	
	@Override
	public boolean hasOutputSourceCode() {
		return false;
	}
	
	@Override
	public String toHeaderString() {
		String s=super.toHeaderString();
		return s.substring(0,s.length()-1)+"= default;";
	}
}
