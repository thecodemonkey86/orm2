package cpp.entityrepository.method;

import java.util.ArrayList;
import cpp.Types;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QStringLiteral;
import cpp.core.expression.Expression;
import cpp.core.expression.QVectorInitList;
import cpp.entity.EntityCls;
import cpp.lib.ClsQVariant;
import cpp.lib.ClsSql;
import cpp.util.ClsDbPool;
import database.column.Column;
import util.CodeUtil2;

public class MethodEntityRemove extends Method {

	protected EntityCls bean;
	protected boolean overloadCascadeDeleteRelations;
	protected Param pBean;
	protected Param pSqlCon;
	
	public MethodEntityRemove(EntityCls bean,
			 boolean overloadCascadeDeleteRelations
			) {
		super(Public, Types.Void, "remove");
		if (overloadCascadeDeleteRelations)
			this.addParam(new Param(Types.Bool, "overloadCascadeDeleteRelations"));
//		this.setVirtualQualifier(true);
		this.overloadCascadeDeleteRelations = overloadCascadeDeleteRelations;
		pBean = addParam(bean.toSharedPtr().toConstRef(), "entity");
		pSqlCon = addParam(Types.QSqlDatabase.toConstRef(),"sqlCon",ClsDbPool.instance.callStaticMethod(ClsDbPool.getDatabase));
		this.bean = bean;
		setStatic(true);
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
						varParams = new QVectorInitList(Types.QVariant);
						for(Column colPk : bean.getTbl().getPrimaryKey().getColumns()) {
							Expression e = pBean.callAttrGetter(colPk.getCamelCaseName());
							
							((QVectorInitList)varParams).addExpression(e.getType().equals(Types.QVariant) ?e : Types.QVariant.callStaticMethod(ClsQVariant.fromValue,e));
						}
					}
					
				 addInstr(Types.Sql.callStaticMethod(ClsSql.execute, pSqlCon, QStringLiteral.fromStringConstant(sql), varParams).asInstruction());
				} else {
					throw new RuntimeException("not implemented");
				}
				
//		}

	}

}
