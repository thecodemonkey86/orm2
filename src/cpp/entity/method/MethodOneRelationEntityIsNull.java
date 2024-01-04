package cpp.entity.method;

import util.StringUtil;
import util.pg.PgCppUtil;
import cpp.Types;
import cpp.core.Method;
import cpp.core.expression.BinaryOperatorExpression;
import cpp.core.expression.Expressions;
import cpp.lib.LibEqualsOperator;
import database.relation.OneRelation;

public class MethodOneRelationEntityIsNull extends Method{
	OneRelation r;
	boolean internal;
	
	public MethodOneRelationEntityIsNull(OneRelation r) {
		this(r, false);
	}
	
	public MethodOneRelationEntityIsNull(OneRelation r, boolean internal ) {
		super(Public, Types.Bool, getMethodName(r,internal));
		setConstQualifier(true);
		this.r=r;
		this.internal = internal;
	}

	@Override
	public void addImplementation() {
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
