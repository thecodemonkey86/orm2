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
import cpp.util.ClsDbPool;
import database.column.Column;
import util.CodeUtil2;

public class MethodEntityRemove extends Method {

	protected EntityCls entity;
	protected boolean overloadCascadeDeleteRelations;
	protected Param pEntity;
	protected Param pSqlCon;
	
	public MethodEntityRemove(EntityCls entity,
			 boolean overloadCascadeDeleteRelations
			) {
		super(Public, Types.Void, getMethodName());
		if (overloadCascadeDeleteRelations)
			this.addParam(new Param(Types.Bool, "overloadCascadeDeleteRelations"));
//		this.setVirtualQualifier(true);
		this.overloadCascadeDeleteRelations = overloadCascadeDeleteRelations;
		pEntity = addParam(entity.toConstRef(), "entity");
		pSqlCon = addParam(Types.QSqlDatabase.toConstRef(),"sqlCon",ClsDbPool.instance.callStaticMethod(ClsDbPool.getDatabase));
		this.entity = entity;
		setStatic(true);
	}

	

	@Override
	public void addImplementation() {
				if(!overloadCascadeDeleteRelations) {
					ArrayList<String> pkCondition = new ArrayList<>();
					
					for(Column colPk : entity.getTbl().getPrimaryKey().getColumns()) {
						pkCondition.add(colPk.getEscapedName()+"=?");
					}
					
					
					String sql = EntityCls.getDatabase().sqlDelete(entity.getTbl(), CodeUtil2.concat(pkCondition, " AND "));
					Expression varParams = null;
					
					if(pkCondition.size() == 1) {
						varParams = pEntity.callAttrGetter(entity.getTbl().getPrimaryKey().getFirstColumn().getCamelCaseName());
					} else {
						varParams = new QListInitList(Types.QVariant);
						for(Column colPk : entity.getTbl().getPrimaryKey().getColumns()) {
							Expression e = pEntity.callAttrGetter(colPk.getCamelCaseName());
							
							((QListInitList)varParams).addExpression(e.getType().equals(Types.QVariant) ?e : ClsQVariant.fromValue(e));
						}
					}
					
				 addInstr(Types.Sql.callStaticMethod(ClsSql.execute, pSqlCon, QStringLiteral.fromStringConstant(sql), varParams).asInstruction());
				} else {
					throw new RuntimeException("not implemented");
				}
				
//		}

	}



	public static String getMethodName() {
		return "remove";
	}

}
