package cpp.cls.bean.repo.method;

import generate.CodeUtil2;

import java.util.ArrayList;
import java.util.List;

import model.Column;
import model.ManyRelation;
import cpp.Types;
import cpp.cls.Method;
import cpp.cls.Param;
import cpp.cls.QString;
import cpp.cls.QStringPlusEqOperator;
import cpp.cls.Type;
import cpp.cls.bean.BeanCls;
import cpp.cls.expression.BinaryOperatorExpression;
import cpp.cls.expression.Expression;
import cpp.cls.expression.Expressions;
import cpp.cls.expression.IntExpression;
import cpp.cls.expression.ParenthesesExpression;
import cpp.cls.expression.Var;
import cpp.cls.instruction.ForeachLoop;
import cpp.cls.instruction.IfBlock;
import cpp.lib.ClsBaseRepository;
import cpp.lib.ClsQVector;
import cpp.orm.OrmUtil;

public class MethodBeanSave extends Method {

//	protected boolean overloadCascadeSaveRelations;
	protected BeanCls bean;
	
	
	
	public MethodBeanSave(BeanCls bean
//			, boolean overloadCascadeSaveRelations
			) {
		super(Public, Types.Void, "save");
//		if (!overloadCascadeSaveRelations)
//			this.addParam(new Param(Types.Bool, "cascadeSaveRelations"));
//		this.setVirtualQualifier(true);
//		this.overloadCascadeSaveRelations = overloadCascadeSaveRelations;
		addParam(new Param(bean.toSharedPtr().toConstRef(), "bean"));
		this.bean = bean;
	}

	

	@Override
	public void addImplementation() {
		Param pBean = getParam("bean");
		
//		if (overloadCascadeSaveRelations) {
//			addInstr(new StaticMethodCall(bean, parent.getMethod("save"), BoolExpression.FALSE).asInstruction()) ;
//			
//		} else {
//			addInstr(new StaticMethodCall(parent.getSuperclass(), parent.getMethod("save" ), getParam("cascadeSaveRelations")).asInstruction()) ;
			
			addInstr(_this().callMethodInstruction(ClsBaseRepository.saveBean,pBean));
		
			List<ManyRelation> manyRelations = bean.getManyToManyRelations();
			
			
			for(ManyRelation r:manyRelations) {
				if (r.getDestColumnCount()==0) {
					throw new RuntimeException();
				}
				// removed
				Expression attrManyToManyRemoved = pBean.callAttrGetter(OrmUtil.getManyRelationDestAttrName(r)+"Removed" );
				IfBlock ifRemoveBeans = _if(Expressions.not(attrManyToManyRemoved
						.callMethod("empty")
						));
				String sql="delete from "+r.getMappingTable().getEscapedName();
				
				ArrayList<String> columnsIn=new ArrayList<>();
				ArrayList<String> mappingColumnsMatching=new ArrayList<>();
				for(int i=0;i<r.getSourceColumnCount();i++) {
					mappingColumnsMatching.add(r.getSourceMappingColumn(i).getEscapedName()+" = ?");
				}
				for(int i=0;i<r.getDestColumnCount();i++) {
					columnsIn.add( r.getDestMappingColumn(i).getEscapedName() );
				}
				
				sql += " where " ;
//				
				sql += CodeUtil2.concat(mappingColumnsMatching, " and ");
				sql += " and " + (columnsIn.size()>1? CodeUtil2.parentheses(CodeUtil2.concat(columnsIn, ",")):columnsIn.get(0));
				sql += " in (%1)";
				Var varDeleteSql = ifRemoveBeans.thenBlock()._declare(Types.QString, "deleteSql",QString.fromStringConstant(sql));
				Type foreachRemoveElementType = ((ClsQVector) attrManyToManyRemoved.getType()).getElementType();
				Var varParamsForeachRemove = ifRemoveBeans.thenBlock()._declare( Types.QVariantList, "params");
				Var varPlaceholdersForeachRemove = ifRemoveBeans.thenBlock()._declare( Types.QString, "placeholders");
				
				Expression exprReservePlaceholders = attrManyToManyRemoved.callMethod("size"); // n ?s + (n-1) commas, minus one see below
				if (r.getDestTable().getPrimaryKey().isMultiColumn()) {
					IntExpression exprColCount= new IntExpression( r.getDestTable().getPrimaryKey().getColumns().size());
					exprReservePlaceholders = exprReservePlaceholders.binOp("*", new ParenthesesExpression(new IntExpression(2).binOp("*", exprColCount).binOp("+", new IntExpression(2)))); // for: , ( ) | 3 - 1 = 2
					ifRemoveBeans.thenBlock()._callMethodInstr(varParamsForeachRemove, "reserve", attrManyToManyRemoved.callMethod("size").binOp("*", exprColCount));
				} else {
					exprReservePlaceholders = exprReservePlaceholders.binOp("*", new IntExpression(2));
				}
				
				ifRemoveBeans.thenBlock()._callMethodInstr(varPlaceholdersForeachRemove, "reserve",exprReservePlaceholders );
				
				//
				
				if (bean.getTbl().getPrimaryKey().isMultiColumn()) {
					for(Column col:bean.getTbl().getPrimaryKey().getColumns()) {
						ifRemoveBeans.thenBlock().addInstr(varParamsForeachRemove.callMethodInstruction("append", pBean.callAttrGetter(col.getCamelCaseName())));
					}
				} else {
					ifRemoveBeans.thenBlock().addInstr(varParamsForeachRemove.callMethodInstruction("append", pBean.callAttrGetter( bean.getTbl().getPrimaryKey().getFirstColumn().getCamelCaseName())));
				}
				
				ForeachLoop foreachAttrRemove = ifRemoveBeans.thenBlock()._foreach(new Var(foreachRemoveElementType.isPrimitiveType()?foreachRemoveElementType: foreachRemoveElementType.toConstRef(), "rem"), attrManyToManyRemoved);
			
				if (r.getDestTable().getPrimaryKey().isMultiColumn()) {
					for(Column col:r.getDestTable().getPrimaryKey().getColumns()) {
						foreachAttrRemove.addInstr(varParamsForeachRemove.callMethodInstruction("append", foreachAttrRemove.getVar().accessAttr(col.getCamelCaseName())));
					}
				} else {
					foreachAttrRemove.addInstr(varParamsForeachRemove.callMethodInstruction("append", foreachAttrRemove.getVar()));
				}
				
				foreachAttrRemove.addInstr(new BinaryOperatorExpression(varPlaceholdersForeachRemove, new QStringPlusEqOperator(), QString.fromStringConstant(bean.getTbl().getPrimaryKey().isMultiColumn() ? ","+CodeUtil2.parentheses( CodeUtil2.strMultiply("?", ",", r.getDestTable().getPrimaryKey().getColumns().size())):",?" )).asInstruction() );
				ifRemoveBeans.thenBlock()._callMethodInstr(_this().accessAttr("sqlCon"), "execute", varDeleteSql.callMethod("arg", varPlaceholdersForeachRemove.callMethod("mid", new IntExpression(1))), varParamsForeachRemove);
				
				// added
				
				ArrayList<String> columnsInsert=new ArrayList<>();
				for(int i=0;i<r.getSourceColumnCount();i++) {
					columnsInsert.add( r.getSourceMappingColumn(i).getEscapedName() );
				}
				for(int i=0;i<r.getDestColumnCount();i++) {
					columnsInsert.add( r.getDestMappingColumn(i).getEscapedName() );
				}
				
				Expression attrManyToManyAdded = pBean.callAttrGetter(OrmUtil.getManyRelationDestAttrName(r)+"Added" );
				IfBlock ifAddBeans = _if(Expressions.not(attrManyToManyAdded
						.callMethod("empty")
						));
				
				if(BeanCls.getDatabase().supportsMultiRowInsert()) {
					String sqlAdded=BeanCls.getDatabase().sqlInsertOrIgnoreMultiRow(r.getMappingTable(), columnsInsert,"%1");
					Var varAddSql = ifAddBeans.thenBlock()._declare(Types.QString, "addedSql",QString.fromStringConstant(sqlAdded));
					Type foreachAddElementType = ((ClsQVector) attrManyToManyAdded.getType()).getElementType();
					Var varParamsForeachAdd = ifAddBeans.thenBlock()._declare( Types.QVariantList, "params");
					Var varPlaceholdersForeachAdd = ifAddBeans.thenBlock()._declare( Types.QString, "placeholders");
					ifAddBeans.thenBlock()._callMethodInstr(varPlaceholdersForeachAdd, "reserve", attrManyToManyAdded.callMethod("size").binOp("*", new IntExpression(2)));
					ifAddBeans.thenBlock()._callMethodInstr(varParamsForeachAdd, "reserve", attrManyToManyAdded.callMethod("size"));
					
					ForeachLoop foreachAttrAdd = ifAddBeans.thenBlock()._foreach(new Var(foreachAddElementType.isPrimitiveType()?foreachAddElementType: foreachAddElementType.toConstRef(), "rem"),  attrManyToManyAdded);
					if (bean.getTbl().getPrimaryKey().isMultiColumn()) {
						for(Column col:bean.getTbl().getPrimaryKey().getColumns()) {
							foreachAttrAdd.addInstr(varParamsForeachAdd.callMethodInstruction("append", pBean.callAttrGetter(  col.getCamelCaseName())));
						}
					} else {
						foreachAttrAdd.addInstr(varParamsForeachAdd.callMethodInstruction("append", pBean.callAttrGetter(  bean.getTbl().getPrimaryKey().getFirstColumn().getCamelCaseName())));
					}
					if (r.getDestTable().getPrimaryKey().isMultiColumn()) {
						for(Column col:r.getDestTable().getPrimaryKey().getColumns()) {
							foreachAttrAdd.addInstr(varParamsForeachAdd.callMethodInstruction("append", foreachAttrAdd.getVar().accessAttr(col.getCamelCaseName())));
						}
					} else {
						foreachAttrAdd.addInstr(varParamsForeachAdd.callMethodInstruction("append", foreachAttrAdd.getVar()));
					}
					
				//	foreachAttrAdd.addInstr(varParamsForeachAdd.callMethodInstruction("append", _this().accessAttr(new Attr( bean.getPkType(),  bean.getTbl().getPrimaryKey().getFirstColumn().getCamelCaseName()))));
	//				foreachAttrAdd.addInstr(varParamsForeachAdd.callMethodInstruction("append", foreachAttrAdd.getVar()));
					foreachAttrAdd.addInstr(new BinaryOperatorExpression(varPlaceholdersForeachAdd, new QStringPlusEqOperator(), QString.fromStringConstant(","+ CodeUtil2.parentheses( CodeUtil2.strMultiply("?", ",", r.getSourceColumnCount()+r.getDestColumnCount())) )).asInstruction() );
					ifAddBeans.thenBlock().addInstr(new cpp.cls.instruction.ScClosedInstruction("qDebug()<<addedSql.arg(placeholders.mid(1))"));
					ifAddBeans.thenBlock()._callMethodInstr(_this().accessAttr("sqlCon"), "execute", varAddSql.callMethod("arg", varPlaceholdersForeachAdd.callMethod("mid", new IntExpression(1))), varParamsForeachAdd);
				} else {
					throw new RuntimeException("not implemented");
				}
				
			}
//		}

	}

}
