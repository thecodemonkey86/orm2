package php.entity.method;

import php.core.Attr;
import php.core.PhpArray;
import php.core.PhpCls;
import php.core.PhpFunctions;
import php.core.PhpPseudoGenericClass;
import php.core.Types;
import php.core.expression.ArrayInitExpression;
import php.core.expression.Expressions;
import php.core.instruction.IfBlock;
import php.core.method.Method;
import util.StringUtil;

public class MethodManyAttrGetter extends Method{
	protected Attr a;
	
	public MethodManyAttrGetter(Attr a) {
		super(Public,null, "get"+StringUtil.ucfirst(a.getName()));
//		
		setReturnType(new PhpArray(((PhpPseudoGenericClass)a.getType()).getElementType()));
		this.a = a;
//		setConstQualifier(true);
	}

	@Override
	public void addImplementation() {
		PhpCls parent = (PhpCls) this.parent;
//		parent.addImport("ormtest.repository.EntityRepository");
		_if(Expressions.not(parent.getAttrByName("loaded"))).thenBlock()
//			._callMethodInstr(_this(), "load");
		.addInstr( Types.EntityRepository.callStaticMethod("load"+parent.getName(), _this()).asInstruction());
		
		IfBlock ifAttrIsNotNull= _if(a.isNotNull());
		ifAttrIsNotNull.thenBlock().
			_return(PhpFunctions.array_values.call(a));
		ifAttrIsNotNull.elseBlock()._return(new ArrayInitExpression());
	}

}

