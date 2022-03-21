package cpp.jsonentity.method;

import util.StringUtil;
import cpp.Types;
import cpp.core.Attr;
import cpp.core.Method;
import cpp.core.Param;
import cpp.lib.ClsQList;
import cpp.orm.OrmUtil;
import database.relation.OneToManyRelation;

public class MethodAddRelatedBean extends Method {

	protected OneToManyRelation rel;
	Param pBean;
	
	public MethodAddRelatedBean(OneToManyRelation r, Param p) {
		super(Public, Types.Void, getMethodName(r));
		pBean = addParam(p);
		rel=r;
	}

	@Override
	public void addImplementation() {
		Attr a=parent.getAttrByName(OrmUtil.getOneToManyRelationDestAttrName(rel));
		addInstr(a.callMethod("append",pBean).asInstruction());
//		addInstr(parent.getAttrByName("_added"+StringUtil.ucfirst(a.getName())).callMethod("append",getParam("entity")).asInstruction());

		
		if(!pBean.getType().getName().startsWith(ClsQList.CLSNAME)) // TODO overloaded with QVector
		for(int i=0;i < rel.getColumnCount(); i++) {
			addInstr(pBean.callSetterMethodInstruction(rel.getDestMappingColumn(i).getCamelCaseName(), _this().callAttrGetter(rel.getColumns(i).getValue1().getCamelCaseName())));
		}
	}

	public static String getMethodName(OneToManyRelation r) {
		return "add"+StringUtil.ucfirst(OrmUtil.getOneToManyRelationDestAttrNameSingular(r));
	}
}
