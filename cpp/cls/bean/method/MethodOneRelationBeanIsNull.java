package cpp.cls.bean.method;

import pg.PgCppUtil;
import util.StringUtil;
import model.OneRelation;
import cpp.Types;
import cpp.cls.Method;
import cpp.cls.expression.Expressions;
import cpp.cls.expression.BinaryOperatorExpression;
import cpp.lib.LibEqualsOperator;

public class MethodOneRelationBeanIsNull extends Method{
	OneRelation r;
	boolean internal;
	
	public MethodOneRelationBeanIsNull(OneRelation r) {
		this(r, false);
	}
	
	public MethodOneRelationBeanIsNull(OneRelation r, boolean internal ) {
		super(Public, Types.Bool, getMethodName(r,internal));
		setConstQualifier(true);
		this.r=r;
		this.internal = internal;
	}

	@Override
	public void addImplementation() {
		//TODO not internal -> load if not loaded
		if (parent.getName().equals("Track")) {
			System.out.println("");
		}
		_return(
				new BinaryOperatorExpression(
						_this().accessAttr(PgCppUtil.getOneRelationDestAttrName(r)),
						new LibEqualsOperator(),
						Expressions.Nullptr)
				
				);
		
	}

	public static String getMethodName(OneRelation r) {
		return getMethodName(r, false);
	}
	
	public static String getMethodName(OneRelation r, boolean internal) {
		return "is"+StringUtil.ucfirst(PgCppUtil.getOneRelationDestAttrName(r))+"Null"+(internal?"Internal":"");
	}
}
