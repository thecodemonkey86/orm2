package php.entity.method;

import database.relation.ManyRelation;
import php.core.Attr;
import php.core.Param;
import php.core.PhpCls;
import php.core.Types;
import php.core.expression.ArrayInitExpression;
import php.core.method.Method;
import php.orm.OrmUtil;
import util.StringUtil;

public class MethodAddManyToManyRelatedEntityInternal extends Method {

	protected ManyRelation rel;
	
	public MethodAddManyToManyRelatedEntityInternal(ManyRelation r, Param p) {
		super(Public, Types.Void, getMethodName(r) );
		addParam(p);
		rel=r;
	}
	

	public static String getMethodName(ManyRelation r) {
		return "add"+StringUtil.ucfirst(OrmUtil.getManyRelationDestAttrNameSingular(r))+"Internal";
	}
	


	@Override
	public void addImplementation() {
		PhpCls parent = (PhpCls) this.parent;
		Attr a=parent.getAttrByName(OrmUtil.getManyRelationDestAttrName(rel));
		_if(a.isNull()).addIfInstr(a.assign(new ArrayInitExpression()));
		Param pEntity = getParam("entity");
		if(rel.getDestTable().getPrimaryKey().isMultiColumn()) {
			throw new RuntimeException("unimplemented");
		} else {
			addInstr(a.arrayIndexSet(pEntity.callAttrGetter(rel.getDestTable().getPrimaryKey().getFirstColumn().getCamelCaseName()),pEntity));
		}
//		addInstr(parent.getAttrByName("_added"+StringUtil.ucfirst(a.getName())).callMethod("append",getParam("entity")).asInstruction());
	}
	
	public static MethodAddManyToManyRelatedEntityInternal prototype() {
		return new MethodAddManyToManyRelatedEntityInternal(null, null);
	}

}
