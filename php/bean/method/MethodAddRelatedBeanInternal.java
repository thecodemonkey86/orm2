package php.bean.method;

import database.column.Column;
import database.relation.OneToManyRelation;
import database.relation.PrimaryKey;
import php.bean.Beans;
import php.core.Attr;
import php.core.Param;
import php.core.PhpCls;
import php.core.PhpFunctions;
import php.core.Types;
import php.core.expression.ArrayInitExpression;
import php.core.expression.Expression;
import php.core.expression.Var;
import php.core.method.Method;
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
		Param pBean = getParam("bean");
		PrimaryKey pk = rel.getDestTable().getPrimaryKey();
		if(rel.getDestTable().getPrimaryKey().isMultiColumn()) {
			Expression[] b1PkArgs = new Expression[pk.getColumnCount()];
			int i=0;
			for(Column colPk : pk) {
				b1PkArgs[i++] = pBean.callAttrGetter(colPk.getCamelCaseName());
			}
			
			Var relPk = _declareNew(Beans.get( rel.getDestTable() ), "relPk", b1PkArgs);
			addInstr(a.arrayIndexSet(PhpFunctions.md5.call(PhpFunctions.serialize.call(relPk)),pBean));
		} else {
			addInstr(a.arrayIndexSet(pBean.callAttrGetter(rel.getDestTable().getPrimaryKey().getFirstColumn().getCamelCaseName()),pBean));
		}
	}
	
	public static MethodAddRelatedBeanInternal prototype() {
		return new MethodAddRelatedBeanInternal(null, null);
	}

}
