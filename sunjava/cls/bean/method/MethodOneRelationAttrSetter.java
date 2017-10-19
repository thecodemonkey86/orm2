package sunjava.cls.bean.method;

import model.Column;
import model.OneRelation;
import sunjava.cls.Attr;
import sunjava.cls.bean.OneAttr;
import sunjava.cls.expression.BoolExpression;
import sunjava.cls.method.MethodAttributeSetter;


public class MethodOneRelationAttrSetter extends MethodAttributeSetter {

	protected boolean internal; 
	protected boolean partOfPrimaryKey;
	
	public MethodOneRelationAttrSetter(Attr attr, boolean internal, boolean partOfPrimaryKey) {
		super(attr);
		this.internal = internal;
		if (internal ) {
			this.name = this.name + "Internal";
		}
		this.partOfPrimaryKey = partOfPrimaryKey;
	}

	
	@Override
	public void addImplementation() {
		super.addImplementation();
		OneRelation r = ((OneAttr) attr).getRelation();
		if (this.partOfPrimaryKey) {
			for(int i=0;i<r.getColumnCount();i++) {
				Column destCol = r.getColumns(i).getValue2();
				Column srcCol = r.getColumns(i).getValue1();
				addInstr( _this().assignAttr(srcCol.getCamelCaseName()+"Previous",  _this().accessAttr(srcCol.getCamelCaseName())));
				addInstr( _this().assignAttr(srcCol.getCamelCaseName(), getParam(attr.getName()).callMethod("get"+destCol.getUc1stCamelCaseName())));
				if (this.internal) {
					this.returnType = getParent();
				}
			}
			if (!this.internal) {
				addInstr(_this().assignAttr("primaryKeyModified",BoolExpression.TRUE));
			}
		} else {
			for(int i=0;i<r.getColumnCount();i++) {
				Column destCol = r.getColumns(i).getValue2();
				Column srcCol = r.getColumns(i).getValue1();
				addInstr( _this().assignAttr(srcCol.getCamelCaseName(), getParam(attr.getName()).callMethod("get"+destCol.getUc1stCamelCaseName())));
				if (!this.internal) {
					addInstr(_this().assignAttr(attr.getName()+"Modified",BoolExpression.TRUE));
				} else {
					this.returnType = getParent();
					
				}
			}
		}
		if (this.internal) {
			_return(_this());
		}
	}
}
