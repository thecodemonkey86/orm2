package cpp.entity.method;

import util.StringUtil;

import java.util.ArrayList;

import codegen.CodeUtil;
import cpp.Types;
import cpp.core.Attr;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import cpp.core.expression.Var;
import cpp.entity.EntityCls;
import cpp.lib.ClsQVariantList;
import cpp.lib.ClsQList;
import cpp.lib.ClsSql;
import cpp.orm.OrmUtil;
import cpp.util.ClsDbPool;
import database.column.Column;
import database.relation.ManyRelation;

public class MethodRemoveAllManyRelatedEntities extends Method {

	protected ManyRelation rel;
	protected Param pSqlCon;
	
	public static String getMethodName(ManyRelation r) {
		return "removeAll"+StringUtil.ucfirst(OrmUtil.getManyRelationDestAttrName(r));
	}
	
	public MethodRemoveAllManyRelatedEntities(ManyRelation r) {
		super(Public, Types.Void, getMethodName(r));
		rel=r;
		pSqlCon = addParam(Types.QSqlDatabase.toConstRef(),"sqlCon",ClsDbPool.instance.callStaticMethod(ClsDbPool.getDatabase));
	}

	@Override
	public void addImplementation() {
		EntityCls parent = (EntityCls) this.parent;
		Attr a=parent.getAttrByName(OrmUtil.getManyRelationDestAttrName(rel));
		addInstr(a.callMethod(ClsQList.clear).asInstruction());
		
		ArrayList<String> columns = new ArrayList<>();
		Var varParams = _declare(Types.QVariantList, "params");
		
		for(int i=0;i<rel.getSourceColumnCount();i++) {
			columns.add(rel.getSourceMappingColumn(i).getEscapedName()+"=?");
		}
		
		for(Column colPk : rel.getSourceTable().getPrimaryKey()) {
			_callMethodInstr(varParams, ClsQVariantList.append,parent.accessThisAttrGetterByColumn(colPk));
		}
		
		String sql = String.format("delete from %s where %s", rel.getMappingTable().getEscapedName(), CodeUtil.concat(columns," and "));
		
		addInstr(Types.Sql.callStaticMethod(ClsSql.execute, pSqlCon,QString.fromStringConstant(sql),varParams).asInstruction());
		
		
		
	}

}
