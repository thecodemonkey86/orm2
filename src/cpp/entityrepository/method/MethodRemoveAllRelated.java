package cpp.entityrepository.method;

import java.util.ArrayList;

import cpp.Types;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import cpp.core.expression.Var;
import cpp.entity.EntityCls;
import cpp.lib.ClsQVariant;
import cpp.lib.ClsQVariantList;
import cpp.lib.ClsSql;
import cpp.orm.OrmUtil;
import cpp.util.ClsDbPool;
import database.column.Column;
import database.relation.IManyRelation;
import util.StringUtil;

public class MethodRemoveAllRelated extends Method {

	protected IManyRelation rel;
	protected EntityCls entity;
	protected Param pSqlCon;
	
	public MethodRemoveAllRelated(EntityCls entity, IManyRelation r) {
		super(Public, Types.Void, "removeAllRelated"
				+ StringUtil.ucfirst(OrmUtil.getManyRelationDestAttrNameSingular(r)) + "From" + entity.getName());
		this.rel = r;
		
		this.entity = entity;
		setStatic(true);
	}

	@Override
	public boolean includeIfEmpty() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void addImplementation() {
		ArrayList<Param> fieldParams = new ArrayList<>();
		for(Column colPk : entity.getTbl().getPrimaryKey())		{
			fieldParams.add( addParam(entity.getAttrByName(colPk.getCamelCaseName()).getType(), colPk.getCamelCaseName()));
			}
		pSqlCon = addParam(Types.QSqlDatabase.toConstRef(),"sqlCon",ClsDbPool.instance.callStaticMethod(ClsDbPool.getDatabase));
		ArrayList<String> placeholders = new ArrayList<>();
		
		for (int i = 0; i < rel.getDestColumnCount(); i++) {
			placeholders.add(rel.getDestMappingColumn(i).getEscapedName()+"=?");
		}
		String sql = String.format("delete from %s where %s", rel.getDestTable().getEscapedName(),
				 String.join(" and ", placeholders)  );
		Var varDeleteSql = _declareInitConstructor(Types.QString, "deleteSql", QString.fromStringConstant(sql));
		Var varParams = _declare(Types.QVariantList, "params");
		
		for(Param p : fieldParams)		{
		  _callMethodInstr(varParams, ClsQVariantList.append, ClsQVariant.fromValue( p));
		}
		
		addInstr(Types.Sql.callStaticMethod(ClsSql.execute, pSqlCon, varDeleteSql, varParams)
				.asInstruction());
	}

}
