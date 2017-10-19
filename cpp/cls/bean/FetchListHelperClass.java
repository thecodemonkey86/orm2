package cpp.cls.bean;

import java.util.ArrayList;
import java.util.List;

import model.AbstractRelation;
import cpp.Struct;
import cpp.Types;
import cpp.cls.Attr;
import cpp.cls.Type;

public class FetchListHelperClass extends Struct{

	public FetchListHelperClass(BeanCls parent) {
		super(parent.getName()+"FetchListHelper");
		Attr b1 = new Attr(parent.toSharedPtr(), "b1");
		addAttr(b1);
//		addMethod(new Method(Method.Public, b1.getType().toRef(), "get"+StringUtil.ucfirst(b1.getName())) {
//
//			@Override
//			public void addImplementation() {
//				_return(b1);
//			}
//			
//		});
		
		List<AbstractRelation> manyRelations = new ArrayList<>();
		manyRelations.addAll(parent.getOneToManyRelations());
		manyRelations.addAll(parent.getManyToManyRelations());
		
		//int bCount=2;
		for(AbstractRelation r:manyRelations) {
			Type beanPk=Types.getRelationForeignPrimaryKeyType(r);
			if(r.getDestTable().getPrimaryKey().isMultiColumn()) {
				parent.addForwardDeclaredClass(beanPk.getName());
			}
			Attr attrSet = new Attr(Types.qset(beanPk), r.getAlias()+"Set");
//			addMethod(new Method(Method.Public, attrSet.getType().toRef(), "get"+StringUtil.ucfirst(attrSet.getName())) {
//
//				@Override
//				public void addImplementation() {
//					_return(attrSet);
//				}
//				
//			});
			addAttr(attrSet);
		}
	}
	
	/*@Override
	public String toSourceString() {
		StringBuilder sb=new StringBuilder();
		
//		addSourceCode(sb);
//		for(Constructor c:constructors) {
//			CodeUtil.writeLine(sb, c);
//		}
//		if (destructor !=null)
//			CodeUtil.writeLine(sb, destructor);
//		
//		for(Method m:methods) {
//			CodeUtil.writeLine(sb, m);
//		}
		
		for(Attr a:attrs) {
			if (a.isStatic())
				CodeUtil.writeLine(sb, CodeUtil.sp(a.getType(),getName()+"::"+a.getName(),"=",a.getInitValue(),";"));
		}
//		if (nonMemberMethods!=null) {
//			for(Method m:nonMemberMethods) {
//				CodeUtil.writeLine(sb, m.toString());
//			}
//		} 
//		if (nonMemberOperators!=null) {
//			for(Operator op:nonMemberOperators) {
//				CodeUtil.writeLine(sb, op.toString());
//			}
//		}
		return sb.toString();
	}*/

	
	
}
