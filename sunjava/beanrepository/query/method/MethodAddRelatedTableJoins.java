package sunjava.beanrepository.query.method;

import java.util.ArrayList;

import codegen.CodeUtil;
import database.relation.ManyRelation;
import database.relation.OneRelation;
import database.relation.OneToManyRelation;
import sunjava.bean.BeanCls;
import sunjava.bean.Beans;
import sunjava.beanrepository.ClsBeanRepository;
import sunjava.core.JavaString;
import sunjava.core.Method;
import sunjava.core.Types;
import sunjava.core.expression.Expression;
import util.CodeUtil2;


public class MethodAddRelatedTableJoins extends Method {
	
	protected BeanCls bean;
	
	public MethodAddRelatedTableJoins(BeanCls cls) {
		super(Public, Types.Void, "addRelatedTableJoins");
//		addParam(new Param(Types.beanQuery(cls), "query"));
//		setStatic(true);
		this.bean = cls;
		setOverrideAnnotation(true);
	}

	@Override
	public void addImplementation() {
//		Expression query = getParam("query");
		Expression query = _this();
			
		for(OneRelation r:bean.getOneRelations()) {
			//parent.addImport(Beans.get(r.getDestTable()).getImport());
			ArrayList<String> joinConditions=new ArrayList<>();
			for(int i=0;i<r.getColumnCount();i++) {
				joinConditions.add(CodeUtil.sp("b1."+r.getColumns(i).getValue1().getEscapedName(),'=',r.getAlias()+"."+ r.getColumns(i).getValue2().getEscapedName()));
			}
			
			query = query.callMethod("leftJoin", Types.BeanRepository.callStaticMethod(ClsBeanRepository.getMethodNameGetTableName(Beans.get(r.getDestTable()))),JavaString.stringConstant(r.getAlias()), JavaString.stringConstant(CodeUtil2.concat(joinConditions," AND ")));
		}
		for(OneToManyRelation r:bean.getOneToManyRelations()) {
			//parent.addImport(Beans.get(r.getDestTable()).getImport());
			ArrayList<String> joinConditions=new ArrayList<>();
			for(int i=0;i<r.getColumnCount();i++) {
				joinConditions.add(CodeUtil.sp("b1."+r.getColumns(i).getValue1().getEscapedName(),'=',r.getAlias()+"."+ r.getColumns(i).getValue2().getEscapedName()));
			}
			
			query = query.callMethod("leftJoin", Types.BeanRepository.callStaticMethod(ClsBeanRepository.getMethodNameGetTableName(Beans.get(r.getDestTable()))),JavaString.stringConstant(r.getAlias()), JavaString.stringConstant(CodeUtil2.concat(joinConditions," AND ")));
		}
		for(ManyRelation r:bean.getManyRelations()) {
			//parent.addImport(Beans.get(r.getDestTable()).getImport());
			ArrayList<String> joinConditions=new ArrayList<>();
			for(int i=0;i<r.getSourceColumnCount();i++) {
				joinConditions.add(CodeUtil.sp("b1."+r.getSourceEntityColumn(i).getEscapedName(),'=',r.getAlias("mapping")+"."+ r.getSourceMappingColumn(i).getEscapedName()));
			}
			
			query = query.callMethod("leftJoin", JavaString.stringConstant(r.getMappingTable().getName()),JavaString.stringConstant(r.getAlias("mapping")), JavaString.stringConstant(CodeUtil2.concat(joinConditions," AND ")));
			
			joinConditions.clear();
			for(int i=0;i<r.getDestColumnCount();i++) {
				joinConditions.add(CodeUtil.sp(r.getAlias("mapping")+"."+r.getDestMappingColumn(i).getEscapedName(),'=',r.getAlias()+"."+r.getDestEntityColumn(i).getEscapedName() ));
			}
			
			query = query.callMethod("leftJoin", Types.BeanRepository.callStaticMethod(ClsBeanRepository.getMethodNameGetTableName(Beans.get(r.getDestTable()))),JavaString.stringConstant(r.getAlias()), JavaString.stringConstant(CodeUtil2.concat(joinConditions," AND ")));
			
		}
		
		if (!bean.getOneRelations().isEmpty() || !bean.getOneToManyRelations().isEmpty() || !bean.getManyRelations().isEmpty()) {
			addInstr(query.asInstruction());
		}
		
	}
	
	@Override
	public boolean includeIfEmpty() {
		return true;
	}

}
