package php.entity.method;

import database.column.Column;
import database.relation.OneToManyRelation;
import database.relation.PrimaryKey;
import php.core.Attr;
import php.core.Param;
import php.core.PhpCls;
import php.core.Types;
import php.core.expression.ArrayInitExpression;
import php.core.expression.Expression;
import php.core.expression.Var;
import php.core.method.Method;
import php.entity.Entities;
import php.entitypk.method.MethodPkHash;
import php.orm.OrmUtil;
import util.StringUtil;

public class MethodAddRelatedBeanInternal extends Method {

	protected OneToManyRelation rel;
	
	public MethodAddRelatedBeanInternal(OneToManyRelation r, Param p) {
		super(Public, Types.Void, getMethodName(r) );
		addParam(p);
		rel=r;
	}
	
	
	
	public static String getMethodName(OneToManyRelation r) {
		return "add"+StringUtil.ucfirst(OrmUtil.getManyRelationDestAttrNameSingular(r))+"Internal";
	}
	

	@Override
	public void addImplementation() {
		PhpCls parent = (PhpCls) this.parent;
		Attr a=parent.getAttrByName(OrmUtil.getOneToManyRelationDestAttrName(rel));
		_if(a.isNull()).addIfInstr(a.assign(new ArrayInitExpression()));
		Param pBean = getParam("entity");
		PrimaryKey pk = rel.getDestTable().getPrimaryKey();
		if(rel.getDestTable().getPrimaryKey().isMultiColumn()) {
			Expression[] e1PkArgs = new Expression[pk.getColumnCount()];
			int i=0;
			for(Column colPk : pk) {
				e1PkArgs[i++] = pBean.callAttrGetter(colPk.getCamelCaseName());
			}
			
			Var relPk = _declareNew(Entities.get( rel.getDestTable()).getPkType(), "relPk", e1PkArgs);
			addInstr(a.arrayIndexSet(relPk.callMethod(MethodPkHash.getMethodName()),pBean));
		} else {
			addInstr(a.arrayIndexSet(pBean.callAttrGetter(rel.getDestTable().getPrimaryKey().getFirstColumn().getCamelCaseName()),pBean));
		}
	}
	
	public static MethodAddRelatedBeanInternal prototype() {
		return new MethodAddRelatedBeanInternal(null, null);
	}

}
