package cpp.entityrepository.method;

import java.util.ArrayList;
import cpp.Types;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QStringLiteral;
import cpp.core.expression.Expression;
import cpp.core.expression.QListInitList;
import cpp.entity.EntityCls;
import cpp.lib.ClsQVariant;
import cpp.lib.ClsSql;
import database.column.Column;
import util.CodeUtil2;

public class MethodEntityRemove extends Method {

	protected EntityCls bean;
	protected boolean overloadCascadeDeleteRelations;
	protected Param pBean;
	
	public MethodEntityRemove(EntityCls bean,
			 boolean overloadCascadeDeleteRelations
			) {
		super(Public, Types.Void, getMethodName());
		if (overloadCascadeDeleteRelations)
			this.addParam(new Param(Types.Bool, "overloadCascadeDeleteRelations"));
//		this.setVirtualQualifier(true);
		this.overloadCascadeDeleteRelations = overloadCascadeDeleteRelations;
		pBean = addParam(bean.toConstRef(), "entity");
		this.bean = bean;
	}

	

	@Override
	public void addImplementation() {
				if(!overloadCascadeDeleteRelations) {
					ArrayList<String> pkCondition = new ArrayList<>();
					
					for(Column colPk : bean.getTbl().getPrimaryKey().getColumns()) {
						pkCondition.add(colPk.getEscapedName()+"=?");
					}
					
					
					String sql = EntityCls.getDatabase().sqlDelete(bean.getTbl(), CodeUtil2.concat(pkCondition, " AND "));
					Expression varParams = null;
					
					if(pkCondition.size() == 1) {
						varParams = pBean.callAttrGetter(bean.getTbl().getPrimaryKey().getFirstColumn().getCamelCaseName());
					} else {
						varParams = new QListInitList(Types.QVariant);
						for(Column colPk : bean.getTbl().getPrimaryKey().getColumns()) {
							Expression e = pBean.callAttrGetter(colPk.getCamelCaseName());
							
							((QListInitList)varParams).addExpression(e.getType().equals(Types.QVariant) ?e : Types.QVariant.callStaticMethod(ClsQVariant.fromValue,e));
						}
					}
					
				 addInstr(Types.Sql.callStaticMethod(ClsSql.execute,  _this().accessAttr("sqlCon"), QStringLiteral.fromStringConstant(sql), varParams).asInstruction());
				} else {
					throw new RuntimeException("not implemented");
				}
				
//		}

	}
	
	public static String getMethodName() {
		return "remove";
	}

}
