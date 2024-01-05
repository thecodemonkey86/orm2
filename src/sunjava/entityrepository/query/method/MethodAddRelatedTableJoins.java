package sunjava.entityrepository.query.method;

import java.util.ArrayList;

import codegen.CodeUtil;
import database.relation.ManyRelation;
import database.relation.OneRelation;
import database.relation.OneToManyRelation;
import sunjava.core.JavaString;
import sunjava.core.Method;
import sunjava.core.Types;
import sunjava.core.expression.Expression;
import sunjava.entity.Entities;
import sunjava.entity.EntityCls;
import sunjava.entityrepository.ClsEntityRepository;
import util.CodeUtil2;


public class MethodAddRelatedTableJoins extends Method {
	
	protected EntityCls entity;
	
	public MethodAddRelatedTableJoins(EntityCls cls) {
		super(Public, Types.Void, "addRelatedTableJoins");
//		addParam(new Param(Types.beanQuery(cls), "query"));
//		setStatic(true);
		this.entity = cls;
		setOverrideAnnotation(true);
	}

	@Override
	public void addImplementation() {
//		Expression query = getParam("query");
		Expression query = _this();
			
		for(OneRelation r:entity.getOneRelations()) {
			//parent.addImport(Beans.get(r.getDestTable()).getImport());
			ArrayList<String> joinConditions=new ArrayList<>();
			for(int i=0;i<r.getColumnCount();i++) {
				joinConditions.add(CodeUtil.sp("e1."+r.getColumns(i).getValue1().getEscapedName(),'=',r.getAlias()+"."+ r.getColumns(i).getValue2().getEscapedName()));
			}
			
			query = query.callMethod("leftJoin", Types.BeanRepository.callStaticMethod(ClsEntityRepository.getMethodNameGetTableName(Entities.get(r.getDestTable()))),JavaString.stringConstant(r.getAlias()), JavaString.stringConstant(CodeUtil2.concat(joinConditions," AND ")));
		}
		for(OneToManyRelation r:entity.getOneToManyRelations()) {
			//parent.addImport(Beans.get(r.getDestTable()).getImport());
			ArrayList<String> joinConditions=new ArrayList<>();
			for(int i=0;i<r.getColumnCount();i++) {
				joinConditions.add(CodeUtil.sp("e1."+r.getColumns(i).getValue1().getEscapedName(),'=',r.getAlias()+"."+ r.getColumns(i).getValue2().getEscapedName()));
			}
			
			query = query.callMethod("leftJoin", Types.BeanRepository.callStaticMethod(ClsEntityRepository.getMethodNameGetTableName(Entities.get(r.getDestTable()))),JavaString.stringConstant(r.getAlias()), JavaString.stringConstant(CodeUtil2.concat(joinConditions," AND ")));
		}
		for(ManyRelation r:entity.getManyRelations()) {
			//parent.addImport(Beans.get(r.getDestTable()).getImport());
			ArrayList<String> joinConditions=new ArrayList<>();
			for(int i=0;i<r.getSourceColumnCount();i++) {
				joinConditions.add(CodeUtil.sp("e1."+r.getSourceEntityColumn(i).getEscapedName(),'=',r.getAlias("mapping")+"."+ r.getSourceMappingColumn(i).getEscapedName()));
			}
			
			query = query.callMethod("leftJoin", JavaString.stringConstant(r.getMappingTable().getName()),JavaString.stringConstant(r.getAlias("mapping")), JavaString.stringConstant(CodeUtil2.concat(joinConditions," AND ")));
			
			joinConditions.clear();
			for(int i=0;i<r.getDestColumnCount();i++) {
				joinConditions.add(CodeUtil.sp(r.getAlias("mapping")+"."+r.getDestMappingColumn(i).getEscapedName(),'=',r.getAlias()+"."+r.getDestEntityColumn(i).getEscapedName() ));
			}
			
			query = query.callMethod("leftJoin", Types.BeanRepository.callStaticMethod(ClsEntityRepository.getMethodNameGetTableName(Entities.get(r.getDestTable()))),JavaString.stringConstant(r.getAlias()), JavaString.stringConstant(CodeUtil2.concat(joinConditions," AND ")));
			
		}
		
		if (!entity.getOneRelations().isEmpty() || !entity.getOneToManyRelations().isEmpty() || !entity.getManyRelations().isEmpty()) {
			addInstr(query.asInstruction());
		}
		
	}
	
	@Override
	public boolean includeIfEmpty() {
		return true;
	}

}
