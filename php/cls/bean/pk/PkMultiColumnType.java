package php.cls.bean.pk;

import model.Column;
import model.Table;
import php.cls.Attr;
import php.cls.Constructor;
import php.cls.PhpCls;
import php.cls.Param;
import php.cls.bean.BeanCls;
import php.cls.bean.pk.method.MethodPkEquals; 
import php.cls.method.MethodAttributeGetter;
import php.cls.method.MethodAttributeSetter;

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
			Param p = constr.addParam(new Param(BeanCls.getTypeMapper().columnToType( col), col.getCamelCaseName()));
			Attr attr = addAttr(new Attr(p.getType(), col.getCamelCaseName()));
			addMethod(new MethodAttributeSetter(attr));
			addMethod(new MethodAttributeGetter(attr));
			
		}
		
		addMethod(new MethodPkEquals(tbl,this));
	}

}
