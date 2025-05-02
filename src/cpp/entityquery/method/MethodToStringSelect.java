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

public class MethodToStringSelect extends Method{

	EntityCls entity;
	
	public MethodToStringSelect(EntityCls entity) {
		super(Method.Public, Types.QString, "toString");
		setConstQualifier(true);
		this.entity = entity;
	}

	@Override
	public void addImplementation() {
		Var mainEntityAlias = _declare(Types.QString, "mainEntityAlias", QString.fromStringConstant("e1"));
		Expression selectFields = entity.hasRelations() ? new InlineIfExpression(Expressions.not(_this().accessAttr(ClsEntityQuerySelect.lazyLoading)), entity.callStaticMethod(MethodGetAllSelectFields.getMethodName(), mainEntityAlias ), entity.callStaticMethod(MethodGetSelectFields.getMethodName(), mainEntityAlias )) : entity.callStaticMethod(MethodGetSelectFields.getMethodName(), mainEntityAlias );
		Expression tableExpr = entity.callStaticMethod(MethodGetTableName.getMethodName(), mainEntityAlias );
		addInstr(new Instruction() {
			@Override
			public String toString() {
				
				return "QString query = QLatin1String(\"SELECT %1 FROM %2\").arg("+selectFields+", "+tableExpr+");\r\n" +
						"\r\n" + 
						"        for(const QString &joinTable:joinTables) {\r\n" + 
						"            query+=joinTable;\r\n" + 
						"        }\r\n" + 
						"\r\n" + 
						"        if (!conditions.empty()) {\r\n" + 
						"\r\n" + 
						"            if (!limitOffsetCondition.isEmpty() && (limitResults > 0 || resultOffset > -1)) {\r\n" + 
						"                query += QLatin1String(\" WHERE (\");\r\n" + 
						"            } else {\r\n" + 
						"                query += QLatin1String(\" WHERE \");\r\n" + 
						"            }\r\n" + 
						"\r\n" + 
						"            for(const QString &cond: conditions) {\r\n" + 
						"                query += cond;\r\n" + 
						"            }\r\n" + 
						"        }\r\n" + 
						"\r\n" + 
						"        if (group.size()>0) {\r\n" + 
						"            query += QLatin1String(\" GROUP BY %1\").arg(group.at(0));\r\n" + 
						"            for(int i=1;i<group.size();i++) {\r\n" + 
						"                query += QLatin1String(\", %1\").arg(group.at(i));\r\n" + 
						"            }\r\n" + 
						"        }\r\n" + 
						"        if (limitResults > 0 || resultOffset > -1) {\r\n" + 
						"            if(!limitOffsetCondition.isEmpty()){ \r\n"+
						"            if (!conditions.empty()) {\r\n" + 
						"                query += QLatin1String(\") AND %1\").arg("+  MethodToStringSelect.this.entity.getName() +"::getLimitQueryString(limitResults,resultOffset,limitOffsetCondition));\r\n" + 
						"            } else {\r\n" + 
						"                query += QLatin1String(\" WHERE %1\").arg("+ MethodToStringSelect.this.entity.	getName() +"::getLimitQueryString(limitResults,resultOffset,limitOffsetCondition));\r\n" + 
						"            }\r\n" + 
						"             query += QLatin1String(\" ORDER BY \");\r\n" +
						"             for(auto order : this->orderByExpressions) {\r\n" + 
						"               query += QLatin1String(\"%1,\").arg(order);\r\n" + 
						"             }\r\n" + 				
						"             query += this->orderByPrimaryKey();\r\n" +
						"            } else {\r\n" +
						"             query += QLatin1String(\" ORDER BY \");\r\n" +
						"             for(auto order : this->orderByExpressions) {\r\n" + 
						"               query += QLatin1String(\"%1,\").arg(order);\r\n" + 
						"             }\r\n" + 				
						"             query += this->orderByPrimaryKey();\r\n" +
						"             query += QLatin1String(\" LIMIT %1 OFFSET %2\").arg(QString::number(limitResults), QString::number(resultOffset));\r\n"+	
						"            }\r\n" + 
						"        } else {\r\n" + 
						"             query += QLatin1String(\" ORDER BY \");\r\n" +
						"             for(auto order : this->orderByExpressions) {\r\n" + 
						"               query += QLatin1String(\"%1,\").arg(order);\r\n" + 
						"             }\r\n" + 				
						"             query += this->orderByPrimaryKey();\r\n" +
						"            }\r\n" +	
						
						"        return query;";
			}
		});
		
	}

}
