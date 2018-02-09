package cpp.beanrepository.method;

import java.util.ArrayList;
import cpp.Types;
import cpp.bean.BeanCls;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QStringLiteral;
import cpp.core.expression.Expression;
import cpp.core.expression.QVectorInitList;
import database.column.Column;
import util.CodeUtil2;

public class MethodBeanRemove extends Method {

	protected BeanCls bean;
	protected boolean overloadCascadeDeleteRelations;
	
	
	public MethodBeanRemove(BeanCls bean,
			 boolean overloadCascadeDeleteRelations
			) {
		super(Public, Types.Void, "remove");
		if (overloadCascadeDeleteRelations)
			this.addParam(new Param(Types.Bool, "overloadCascadeDeleteRelations"));
//		this.setVirtualQualifier(true);
		this.overloadCascadeDeleteRelations = overloadCascadeDeleteRelations;
		addParam(new Param(bean.toSharedPtr().toConstRef(), "bean"));
		this.bean = bean;
	}

	

	@Override
	public void addImplementation() {
		Param pBean = getParam("bean");
				if(!overloadCascadeDeleteRelations) {
					ArrayList<String> pkCondition = new ArrayList<>();
					
					for(Column colPk : bean.getTbl().getPrimaryKey().getColumns()) {
						pkCondition.add(colPk.getEscapedName()+"=?");
					}
					
					
					String sql = BeanCls.getDatabase().sqlDelete(bean.getTbl(), CodeUtil2.concat(pkCondition, " AND "));
					Expression varParams = null;
					
					if(pkCondition.size() == 1) {
						varParams = pBean.callAttrGetter(bean.getTbl().getPrimaryKey().getFirstColumn().getCamelCaseName());
					} else {
						varParams = new QVectorInitList(Types.QVariant);
						for(Column colPk : bean.getTbl().getPrimaryKey().getColumns()) {
							((QVectorInitList)varParams).addExpression(pBean.callAttrGetter(colPk.getCamelCaseName()));
						}
					}
					
					_callMethodInstr(_this().accessAttr("sqlCon"), "execute", QStringLiteral.fromStringConstant(sql), varParams);
				} else {
					throw new RuntimeException("not implemented");
				}
				
//		}

	}

}
