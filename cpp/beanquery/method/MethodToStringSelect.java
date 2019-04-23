package cpp.beanquery.method;

import cpp.Types;
import cpp.bean.BeanCls;
import cpp.bean.method.MethodGetAllSelectFields;
import cpp.bean.method.MethodGetSelectFields;
import cpp.bean.method.MethodGetTableName;
import cpp.beanquery.ClsBeanQuerySelect;
import cpp.core.Method;
import cpp.core.QString;
import cpp.core.expression.Expression;
import cpp.core.expression.Expressions;
import cpp.core.expression.InlineIfExpression;
import cpp.core.expression.Var;
import cpp.core.instruction.Instruction;

public class MethodToStringSelect extends Method{

	BeanCls bean;
	
	public MethodToStringSelect(BeanCls bean) {
		super(Method.Public, Types.QString, "toString");
		setConstQualifier(true);
		this.bean = bean;
	}

	@Override
	public void addImplementation() {
		Var mainBeanAlias = _declare(Types.QString, "mainBeanAlias", QString.fromStringConstant("b1"));
		InlineIfExpression selectFields = new InlineIfExpression(Expressions.not(_this().accessAttr(ClsBeanQuerySelect.lazyLoading)), bean.callStaticMethod(MethodGetAllSelectFields.getMethodName(), mainBeanAlias ), bean.callStaticMethod(MethodGetSelectFields.getMethodName(), mainBeanAlias ));
		Expression tableExpr = bean.callStaticMethod(MethodGetTableName.getMethodName(), mainBeanAlias );
		addInstr(new Instruction() {
			@Override
			public String toString() {
				
				//ifNotLazyLoading.thenBlock()._assign(_this().accessAttr(ClsBeanQuerySelect.selectFields),  this.bean.callStaticMethod(MethodGetAllSelectFields.getMethodName(), attrMainBeanAlias ));
				//ifNotLazyLoading.elseBlock()._assign(_this().accessAttr(ClsBeanQuerySelect.selectFields),  this.bean.callStaticMethod(MethodGetSelectFields.getMethodName(), attrMainBeanAlias ));
				return "QString query = QStringLiteral(\"SELECT %1 FROM %2\").arg("+selectFields+", "+tableExpr+");\r\n" +
						"\r\n" + 
						"        for(const QString &joinTable:joinTables) {\r\n" + 
						"            query+=joinTable;\r\n" + 
						"        }\r\n" + 
						"\r\n" + 
						"        if (!conditions.empty()) {\r\n" + 
						"\r\n" + 
						"            if (limitResults > 0 || resultOffset > -1) {\r\n" + 
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
						"            if (!conditions.empty()) {\r\n" + 
						"                query += QStringLiteral(\") AND %1\").arg("+  MethodToStringSelect.this.bean.getName() +"::getLimitQueryString(limitResults,resultOffset,limitOffsetCondition));\r\n" + 
						"            } else {\r\n" + 
						"                query += QStringLiteral(\" WHERE %1\").arg("+ MethodToStringSelect.this.bean.	getName() +"::getLimitQueryString(limitResults,resultOffset,limitOffsetCondition));\r\n" + 
						"            }\r\n" + 
						"        }\r\n" + 
						"        query += QStringLiteral(\" ORDER BY \");\r\n" + 
						"        for(auto order : this->orderByExpressions) {\r\n" + 
						"            query += QStringLiteral(\"%1,\").arg(order);\r\n" + 
						"        }\r\n" + 
						"        query += this->orderByPrimaryKey();\r\n" + 
						"        return query;";
			}
		});
		
	}

}
