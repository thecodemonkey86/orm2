package cpp.entity.method;

import util.StringUtil;
import cpp.Types;
import cpp.core.Attr;
import cpp.core.Method;
import cpp.core.Param;
import cpp.lib.ClsQList;
import cpp.orm.OrmUtil;
import database.relation.OneToManyRelation;

public class MethodAddRelatedEntity extends Method {

	protected OneToManyRelation rel;
	Param pEntity;
	
	public MethodAddRelatedEntity(OneToManyRelation r, Param p) {
		super(Public, Types.Void, getMethodName(r));
		pEntity = addParam(p);
		rel=r;
	}

	@Override
	public void addImplementation() {
		Attr a=parent.getAttrByName(OrmUtil.getOneToManyRelationDestAttrName(rel));
		addInstr(a.callMethod("append",pEntity).asInstruction());
//		addInstr(parent.getAttrByName("_added"+StringUtil.ucfirst(a.getName())).callMethod("append",getParam("entity")).asInstruction());

		
		if(!pEntity.getType().getName().startsWith(ClsQList.CLSNAME))
		for(int i=0;i < rel.getColumnCount(); i++) {
			if(!rel.getDestMappingColumn(i).isPartOfPk()) {
				addInstr(pEntity.callSetterMethodInstruction(rel.getDestMappingColumn(i).getCamelCaseName(), _this().callAttrGetter(rel.getColumns(i).getValue1().getCamelCaseName())));
			} else {
				addInstr(pEntity.callMethodInstruction("set"+ rel.getDestMappingColumn(i).getUc1stCamelCaseName()+"Internal", _this().callAttrGetter(rel.getColumns(i).getValue1().getCamelCaseName())));
			}
			
		}
	}

	public static String getMethodName(OneToManyRelation r) {
		return "add"+StringUtil.ucfirst(OrmUtil.getOneToManyRelationDestAttrNameSingular(r));
	}
}
