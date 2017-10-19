package php.cls.bean.method;

import php.PhpArray;
import php.Types;
import php.cls.Attr;
import php.cls.PhpCls;
import php.cls.PhpPseudoGenericClass;
import php.cls.Method;
import php.cls.expression.ArrayInitExpression;
import php.cls.expression.Expressions;
import php.cls.instruction.IfBlock;
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
//		parent.addImport("ormtest.repository.BeanRepository");
		_if(Expressions.not(parent.getAttrByName("loaded"))).thenBlock()
//			._callMethodInstr(_this(), "load");
		.addInstr( Types.BeanRepository.callStaticMethod("load"+parent.getName(), _this()).asInstruction());
		
		IfBlock ifAttrIsNotNull= _if(a.isNotNull());
		ifAttrIsNotNull.thenBlock().
			_return(new ArrayInitExpression());
		ifAttrIsNotNull.elseBlock()._return(Expressions.Null);
	}

}

