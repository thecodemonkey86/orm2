package cpp.entityquery.method;

import cpp.Types;
import cpp.core.Method;
import cpp.core.QString;
import cpp.core.expression.Expression;
import cpp.core.expression.Expressions;
import cpp.core.expression.InlineIfExpression;
import cpp.core.expression.Var;
import cpp.core.instruction.Instruction;
import cpp.entity.EntityCls;
import cpp.entity.method.MethodGetAllSelectFields;
import cpp.entity.method.MethodGetSelectFields;
import cpp.entity.method.MethodGetTableName;
import cpp.entityquery.ClsEntityQuerySelect;

public class MethodToStringSelectFirebird extends Method{

	EntityCls entity;
	
	public MethodToStringSelectFirebird(EntityCls entity) {
		super(Method.Public, Types.QString, "toString");
		setConstQualifier(true);
		this.entity = entity;
	}

	@Override
	public void addImplementation() {
		Var mainBeanAlias = _declare(Types.QString, "mainBeanAlias", QString.fromStringConstant("e1"));
		Expression selectFields = entity.hasRelations() ? new InlineIfExpression(Expressions.not(_this().accessAttr(ClsEntityQuerySelect.lazyLoading)), entity.callStaticMethod(MethodGetAllSelectFields.getMethodName(), mainBeanAlias ), entity.callStaticMethod(MethodGetSelectFields.getMethodName(), mainBeanAlias )) : entity.callStaticMethod(MethodGetSelectFields.getMethodName(), mainBeanAlias );
		Expression tableExpr = entity.callStaticMethod(MethodGetTableName.getMethodName(), mainBeanAlias );
		
		String limitExpr="limitResults > 0 ? QStringLiteral(\"FIRST %1 SKIP %2 \").arg(QString::number(limitResults), QString::number(resultOffset>-1?resultOffset:0)) : QString()";
		
		addInstr(new Instruction() {
			@Override
			public String toString() {
				
				//ifNotLazyLoading.thenBlock()._assign(_this().accessAttr(ClsBeanQuerySelect.selectFields),  this.entity.callStaticMethod(MethodGetAllSelectFields.getMethodName(), attrMainBeanAlias ));
				//ifNotLazyLoading.elseBlock()._assign(_this().accessAttr(ClsBeanQuerySelect.selectFields),  this.entity.callStaticMethod(MethodGetSelectFields.getMethodName(), attrMainBeanAlias ));
				return "QString query = QStringLiteral(\"SELECT %3%1 FROM %2\").arg("+selectFields+", "+tableExpr+", ("+limitExpr+"));\r\n" +
						"\r\n" + 
						"        for(const QString &joinTable:joinTables) {\r\n" + 
						"            query+=joinTable;\r\n" + 
						"        }\r\n" + 
						"\r\n" + 
						"        if (!conditions.empty()) {\r\n" + 
						"\r\n" + 
						"            if (!limitOffsetCondition.isEmpty() && (limitResults > 0 || resultOffset > -1)) {\r\n" + 
						"                query += QStringLiteral(\" WHERE (\");\r\n" + 
						"            } else {\r\n" + 
						"                query += QStringLiteral(\" WHERE \");\r\n" + 
						"            }\r\n" + 
						"\r\n" + 
						"            for(const QString &cond: conditions) {\r\n" + 
						"                query += cond;\r\n" + 
						"            }\r\n" + 
						"        }\r\n" + 
						"\r\n" + 
						"        if (group.size()>0) {\r\n" + 
						"            query += QStringLiteral(\" GROUP BY %1\").arg(group.at(0));\r\n" + 
						"            for(int i=1;i<group.size();i++) {\r\n" + 
						"                query += QStringLiteral(\", %1\").arg(group.at(i));\r\n" + 
						"            }\r\n" + 
						"        }\r\n" + 
						"        if (limitResults > 0 || resultOffset > -1) {\r\n" + 
						"            if(!limitOffsetCondition.isEmpty()){ \r\n"+
						"            if (!conditions.empty()) {\r\n" + 
						"                query += QStringLiteral(\") AND %1\").arg("+  MethodToStringSelectFirebird.this.entity.getName() +"::getLimitQueryString(limitResults,resultOffset,limitOffsetCondition));\r\n" + 
						"            } else {\r\n" + 
						"                query += QStringLiteral(\" WHERE %1\").arg("+ MethodToStringSelectFirebird.this.entity.	getName() +"::getLimitQueryString(limitResults,resultOffset,limitOffsetCondition));\r\n" + 
						"            }\r\n" + 
						"             query += QStringLiteral(\" ORDER BY \");\r\n" +
						"             for(auto order : this->orderByExpressions) {\r\n" + 
						"               query += QStringLiteral(\"%1,\").arg(order);\r\n" + 
						"             }\r\n" + 				
						"             query += this->orderByPrimaryKey();\r\n" +
						"            } else {\r\n" +
						"             query += QStringLiteral(\" ORDER BY \");\r\n" +
						"             for(auto order : this->orderByExpressions) {\r\n" + 
						"               query += QStringLiteral(\"%1,\").arg(order);\r\n" + 
						"             }\r\n" + 				
						"             query += this->orderByPrimaryKey();\r\n" +
						"            }\r\n" + 
						"        } else {\r\n" + 
						"             query += QStringLiteral(\" ORDER BY \");\r\n" +
						"             for(auto order : this->orderByExpressions) {\r\n" + 
						"               query += QStringLiteral(\"%1,\").arg(order);\r\n" + 
						"             }\r\n" + 				
						"             query += this->orderByPrimaryKey();\r\n" +
						"            }\r\n" +	
						
						"        return query;";
			}
		});
		
	}

}
