package php.cls;

import java.util.ArrayList;

import codegen.CodeUtil;
import php.cls.expression.Expression;
import php.cls.instruction.Instruction;

public abstract class Constructor extends Method {
	
	protected ArrayList<Expression> passToSuperConstructor;
	
	public Constructor() {
		super(Public,null,null);
		params = new ArrayList<>();
		instructions = new ArrayList<>();
		
	}
	
	
	public void addPassToSuperConstructor(Expression p) {
		if (passToSuperConstructor==null)
			passToSuperConstructor=new ArrayList<>();
		this.passToSuperConstructor.add(p);
	}
	
	@Override
	public String toString() {
		ArrayList<String> params=new ArrayList<>();
		for(Param p:this.params) {
			params.add(p.toDeclarationString());
		}
		
		StringBuilder sb=new StringBuilder(CodeUtil.sp(visibility,"function"  ,"__construct")+CodeUtil.parentheses(CodeUtil.commaSep(params))
				);
		
		
			
			sb.append("{\n");
			
if (passToSuperConstructor!=null) {
				
	CodeUtil.writeLine(sb,"parent::__construct" + CodeUtil.parentheses(CodeUtil.commaSep(passToSuperConstructor))+";");
			}
		for(Object i:instructions) {
			
			CodeUtil.writeLine(sb,i);
		}
		sb.append("}");
		
		
		return sb.toString();
	}
	

}
