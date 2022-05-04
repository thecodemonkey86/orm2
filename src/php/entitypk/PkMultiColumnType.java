package php.entitypk;

import database.column.Column;
import database.table.Table;
import php.core.Attr;
import php.core.Constructor;
import php.core.Param;
import php.core.PhpCls;
import php.core.method.MethodAttributeGetter;
import php.core.method.MethodAttributeSetter;
import php.entity.EntityCls;
import php.entitypk.method.MethodPkEquals;

public class PkMultiColumnType extends PhpCls{

	public PkMultiColumnType(String name,String namespace,Table tbl) {
		super(name,namespace);
		Constructor constr = new Constructor() {
			
			@Override
			public void addImplementation() {
				for(Column col: tbl.getPrimaryKey().getColumns()) {
				 addInstr(_this().assignAttr(col.getCamelCaseName(),getParam(col.getCamelCaseName()
							)));
				}
				
			}
		};
		
		setConstructor(constr);
		for(Column col: tbl.getPrimaryKey().getColumns()) {
			Param p = constr.addParam(new Param(EntityCls.getTypeMapper().columnToType( col), col.getCamelCaseName()));
			Attr attr = addAttr(new Attr(p.getType(), col.getCamelCaseName()));
			addMethod(new MethodAttributeSetter(attr));
			addMethod(new MethodAttributeGetter(attr));
			
		}
		
		addMethod(new MethodPkEquals(tbl,this));
	}

}
