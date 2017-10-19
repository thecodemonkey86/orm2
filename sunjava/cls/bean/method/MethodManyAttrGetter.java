package sunjava.cls.bean.method;

import sunjava.Types;
import sunjava.cls.Attr;
import sunjava.cls.JavaCls;
import sunjava.cls.JavaGenericClass;
import sunjava.cls.Method;
import sunjava.cls.expression.Expressions;
import sunjava.cls.expression.NewOperator;
import sunjava.cls.instruction.IfBlock;
import sunjava.lib.IList;
import util.StringUtil;

public class MethodManyAttrGetter extends Method{
	protected Attr a;
	
	public MethodManyAttrGetter(Attr a) {
		super(Public,null, "get"+StringUtil.ucfirst(a.getName()));
//		
		setReturnType(new IList(((JavaGenericClass)a.getType()).getElementType()));
		this.a = a;
//		setConstQualifier(true);
	}

	@Override
	public void addImplementation() {
		addThrowsException(Types.SqlException);
		JavaCls parent = (JavaCls) this.parent;
//		parent.addImport("ormtest.repository.BeanRepository");
		_if(Expressions.not(parent.getAttrByName("loaded"))).thenBlock()
//			._callMethodInstr(_this(), "load");
		.addInstr( Types.BeanRepository.callStaticMethod("load"+parent.getName(), _this()).asInstruction());
		
		IfBlock ifAttrIsNotNull= _if(a.isNotNull());
		ifAttrIsNotNull.thenBlock().
			_return(new NewOperator(Types.arraylist(((JavaGenericClass)a.getType()).getElementType()),  a));
		ifAttrIsNotNull.elseBlock()._return(Expressions.Null);
	}

}

