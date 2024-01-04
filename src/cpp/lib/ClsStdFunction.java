package cpp.lib;

import cpp.Namespaces;
import cpp.core.Cls;
import cpp.core.Type;
import util.CodeUtil2;

public class ClsStdFunction extends Cls {

	Type returnType;
	Type[] argTypes;
	
	public ClsStdFunction(Type returnType,Type...argTypes) {
		super("function",false);
		setUseNamespace(Namespaces.std);
		this.returnType=returnType;
		this.argTypes=argTypes;
	}
	
	@Override
	public String toDeclarationString() {
		String[] argTypes = new String[this.argTypes.length];
		for(int i=0;i<argTypes.length;i++) {
			argTypes[i] = this.argTypes[i].toDeclarationString();
		}
		return useNamespace+"::"+ type+CodeUtil2.abr(returnType.toDeclarationString()+CodeUtil2.parentheses(CodeUtil2.commaSep(argTypes)));
	}
	

}
