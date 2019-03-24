package php.core.expression;

import java.util.ArrayList;
import java.util.List;

import codegen.CodeUtil;
import php.core.Type;
import php.core.Types;
import util.Pair;

public class AssocArrayInitExpression extends Expression{

	private List<Pair<String, Expression>> elems;
	
	public AssocArrayInitExpression(List<Pair<String, Expression> >elems) {
		this.elems = elems;
	}
	
	public AssocArrayInitExpression() {
		elems = new ArrayList<>();
	}
	
	public void addElement(Pair<String, Expression>  s) {
		elems.add(s);
	}
	
	@Override
	public Type getType() {
		return Types.array(elems.get(0).getValue2().getType());
	}

	@Override
	public String toString() {
		
		ArrayList<String> elems = new ArrayList<>(this.elems.size());
		for(Pair<String, Expression> e : this.elems) {
			elems.add(CodeUtil.sp(new PhpStringLiteral(e.getValue1()),"=>",e.getValue2(),"\n"));
		}
		
		return "array" + CodeUtil.parentheses( CodeUtil.commaSep(elems) );
	}

}
