package cpp.bean.method;

import cpp.Types;
import cpp.bean.BeanCls;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import cpp.core.expression.InlineIfExpression;
import cpp.core.expression.IntExpression;
import cpp.core.expression.QChar;
import cpp.core.expression.Var;
import cpp.core.instruction.IfBlock;
import cpp.core.instruction.Instruction;
import cpp.lib.ClsQString;
import database.relation.PrimaryKey;

public class MethodGetLimitQueryString extends Method {
	PrimaryKey pk;
	//Param pLimitOffsetOrderBy;
	
	public MethodGetLimitQueryString(PrimaryKey pk) {
		super(Public, Types.QString, "getLimitQueryString");
		this.pk = pk;
		setStatic(true);
		addParam(new Param(Types.LongLong, "limit"));
		addParam(new Param(Types.LongLong, "offset"));
		addParam(new Param(Types.QString.toConstRef(), "condition"));
		//pLimitOffsetOrderBy = addParam(new Param(Types.QString.toConstRef(), "limitOffsetOrderBy"));
	}

	@Override
	public void addImplementation() {
		String mainBeanAlias = "b1.";
		BeanCls bean = (BeanCls) parent;
		StringBuilder sql = new StringBuilder();
		if (pk.isMultiColumn()) {
			sql.append('(').append(mainBeanAlias).append(pk.getFirstColumn().getEscapedName());
			for(int i=1;i<pk.getColumnCount();i++) {
				sql.append(',').append(mainBeanAlias).append(pk.getColumn(i).getEscapedName());
			}
			sql.append(')');
		} else {
			sql.append(mainBeanAlias).append(pk.getFirstColumn().getEscapedName());
		}
		sql.append(" IN (SELECT ").append(pk.getFirstColumn().getEscapedName());
		for(int i=1;i<pk.getColumnCount();i++) {
			sql.append(',').append(pk.getColumn(i).getEscapedName());
		}
		sql.append(" FROM ").append(bean.getTbl().getEscapedName()).append(" WHERE %1" );
		
		/*if (!limitOffsetOrderBy.isEmpty()) {\r\n" + 
						"                query += QStringLiteral(\" ORDER BY %1\").arg(limitOffsetOrderBy);\r\n" + 
						"            }\r\n" +*/
		
		
		Var varSql  = _declareInitConstructor(returnType, "sql", QString.fromStringConstant(sql.toString()).callMethod("arg", new InlineIfExpression(getParam("condition").callMethod("isEmpty"), QString.fromStringConstant(BeanCls.getDatabase().getBooleanExpressionTrue()), getParam("condition")) ));
		Param paramLimit = getParam("limit");
		Param paramOffset = getParam("offset");
		//IfBlock ifNotLimitOffsetOrderByIsEmpty = _ifNot(pLimitOffsetOrderBy.callMethod(ClsQString.isEmpty));
		//ifNotLimitOffsetOrderByIsEmpty.thenBlock().addInstr(varSql.binOp("+=",QString.fromStringConstant(" ORDER BY %1").callMethod(ClsQString.arg, pLimitOffsetOrderBy)).asInstruction());
		
		IfBlock ifIsSetLimit = _if(paramLimit.greaterThan(new IntExpression(-1)));
		ifIsSetLimit.addIfInstr(varSql.binOp("+=", QString.fromStringConstant(" LIMIT %1").callMethod("arg", paramLimit)).asInstruction());
		IfBlock ifIsSetOffset = _if(paramOffset.greaterThan(new IntExpression(-1)));
		ifIsSetOffset.addIfInstr(varSql.binOp("+=", QString.fromStringConstant(" OFFSET %1").callMethod("arg", paramOffset)).asInstruction());
		
		
		addInstr( varSql.binOp("+=", QChar.fromChar(')')).asInstruction());
		_return(varSql);
	}
	
	
}
