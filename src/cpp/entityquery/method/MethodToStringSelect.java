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
import util.CodeUtil2;

public class MethodToStringSelect extends Method{

	EntityCls bean;
	
	public MethodToStringSelect(EntityCls bean) {
		super(Method.Public, Types.QString, "toString");
		setConstQualifier(true);
		this.bean = bean;
	}

	@Override
	public void addImplementation() {
		Var mainBeanAlias = _declare(Types.QString, "mainBeanAlias", QString.fromStringConstant("e1"));
		Expression selectFields = bean.hasRelations() ? new InlineIfExpression(Expressions.not(_this().accessAttr(ClsEntityQuerySelect.lazyLoading)), bean.callStaticMethod(MethodGetAllSelectFields.getMethodName(), mainBeanAlias ), bean.callStaticMethod(MethodGetSelectFields.getMethodName(), mainBeanAlias )) : bean.callStaticMethod(MethodGetSelectFields.getMethodName(), mainBeanAlias );
		Expression tableExpr = bean.callStaticMethod(MethodGetTableName.getMethodName(), mainBeanAlias );
		addInstr(new Instruction() {
			@Override
			public String toString() {
				
				//ifNotLazyLoading.thenBlock()._assign(_this().accessAttr(ClsBeanQuerySelect.selectFields),  this.bean.callStaticMethod(MethodGetAllSelectFields.getMethodName(), attrMainBeanAlias ));
				//ifNotLazyLoading.elseBlock()._assign(_this().accessAttr(ClsBeanQuerySelect.selectFields),  this.bean.callStaticMethod(MethodGetSelectFields.getMethodName(), attrMainBeanAlias ));
				return "QString query = QStringLiteral(\"SELECT %1 FROM %2\").arg("+selectFields+", "+tableExpr+");" + CodeUtil2.NEWLINE +
						CodeUtil2.NEWLINE + 
						"        for(const QString &joinTable:joinTables) {" + CodeUtil2.NEWLINE + 
						"            query+=joinTable;" + CodeUtil2.NEWLINE + 
						"        }" + CodeUtil2.NEWLINE + 
						CodeUtil2.NEWLINE + 
						"        if (!conditions.empty()) {" + CodeUtil2.NEWLINE + 
						CodeUtil2.NEWLINE + 
						"            if (!limitOffsetCondition.isEmpty() && (limitResults > 0 || resultOffset > -1)) {" + CodeUtil2.NEWLINE + 
						"                query += QStringLiteral(\" WHERE (\");" + CodeUtil2.NEWLINE + 
						"            } else {" + CodeUtil2.NEWLINE + 
						"                query += QStringLiteral(\" WHERE \");" + CodeUtil2.NEWLINE + 
						"            }" + CodeUtil2.NEWLINE + 
						CodeUtil2.NEWLINE + 
						"            for(const QString &cond: conditions) {" + CodeUtil2.NEWLINE + 
						"                query += cond;" + CodeUtil2.NEWLINE + 
						"            }" + CodeUtil2.NEWLINE + 
						"        }" + CodeUtil2.NEWLINE + 
						CodeUtil2.NEWLINE + 
						"        if (group.size()>0) {" + CodeUtil2.NEWLINE + 
						"            query += QStringLiteral(\" GROUP BY %1\").arg(group.at(0));" + CodeUtil2.NEWLINE + 
						"            for(int i=1;i<group.size();i++) {" + CodeUtil2.NEWLINE + 
						"                query += QStringLiteral(\", %1\").arg(group.at(i));" + CodeUtil2.NEWLINE + 
						"            }" + CodeUtil2.NEWLINE + 
						"        }" + CodeUtil2.NEWLINE + 
						"        if (limitResults > 0 || resultOffset > -1) {" + CodeUtil2.NEWLINE + 
						"            if(!limitOffsetCondition.isEmpty()){ " + CodeUtil2.NEWLINE +
						"            if (!conditions.empty()) {" + CodeUtil2.NEWLINE + 
						"                query += QStringLiteral(\") AND %1\").arg("+  MethodToStringSelect.this.bean.getName() +"::getLimitQueryString(limitResults,resultOffset,limitOffsetCondition));" + CodeUtil2.NEWLINE + 
						"            } else {" + CodeUtil2.NEWLINE + 
						"                query += QStringLiteral(\" WHERE %1\").arg("+ MethodToStringSelect.this.bean.	getName() +"::getLimitQueryString(limitResults,resultOffset,limitOffsetCondition));" + CodeUtil2.NEWLINE + 
						"            }" + CodeUtil2.NEWLINE + 
						"             query += QStringLiteral(\" ORDER BY \");" + CodeUtil2.NEWLINE +
						"             for(auto order : this->orderByExpressions) {" + CodeUtil2.NEWLINE + 
						"               query += QStringLiteral(\"%1,\").arg(order);" + CodeUtil2.NEWLINE + 
						"             }" + CodeUtil2.NEWLINE + 				
						"             query += this->orderByPrimaryKey();" + CodeUtil2.NEWLINE +
						"            } else {" + CodeUtil2.NEWLINE +
						"             query += QStringLiteral(\" ORDER BY \");" + CodeUtil2.NEWLINE +
						"             for(auto order : this->orderByExpressions) {" + CodeUtil2.NEWLINE + 
						"               query += QStringLiteral(\"%1,\").arg(order);" + CodeUtil2.NEWLINE + 
						"             }" + CodeUtil2.NEWLINE + 				
						"             query += this->orderByPrimaryKey();" + CodeUtil2.NEWLINE +
						"             query += QStringLiteral(\" LIMIT %1 OFFSET %2\").arg(QString::number(limitResults), QString::number(resultOffset));" + CodeUtil2.NEWLINE +	
						"            }" + CodeUtil2.NEWLINE + 
						"        } else {" + CodeUtil2.NEWLINE + 
						"             query += QStringLiteral(\" ORDER BY \");" + CodeUtil2.NEWLINE +
						"             for(auto order : this->orderByExpressions) {" + CodeUtil2.NEWLINE + 
						"               query += QStringLiteral(\"%1,\").arg(order);" + CodeUtil2.NEWLINE + 
						"             }" + CodeUtil2.NEWLINE + 				
						"             query += this->orderByPrimaryKey();" + CodeUtil2.NEWLINE +
						"            }" + CodeUtil2.NEWLINE +	
						
						"        return query;";
			}
		});
		
	}

}
