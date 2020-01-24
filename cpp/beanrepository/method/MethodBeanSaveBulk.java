package cpp.beanrepository.method;

import java.util.ArrayList;
import java.util.List;

import cpp.Types;
import cpp.CoreTypes;
import cpp.bean.BeanCls;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import cpp.core.QStringPlusEqOperator;
import cpp.core.Type;
import cpp.core.expression.BinaryOperatorExpression;
import cpp.core.expression.Expression;
import cpp.core.expression.Expressions;
import cpp.core.expression.IntExpression;
import cpp.core.expression.ParenthesesExpression;
import cpp.core.expression.Var;
import cpp.core.instruction.ForeachLoop;
import cpp.core.instruction.IfBlock;
import cpp.lib.ClsBaseRepository;
import cpp.lib.ClsQVariant;
import cpp.lib.ClsQVariantList;
import cpp.lib.ClsQVector;
import cpp.orm.OrmUtil;
import database.column.Column;
import database.relation.ManyRelation;
import util.CodeUtil2;

public class MethodBeanSaveBulk extends Method {

//	protected boolean overloadCascadeSaveRelations;
	protected BeanCls bean;
	protected Param pBeans;
	
	
	public MethodBeanSaveBulk(BeanCls bean
//			, boolean overloadCascadeSaveRelations
			) {
		super(Public, Types.Void, "save");
//		if (!overloadCascadeSaveRelations)
//			this.addParam(new Param(Types.Bool, "cascadeSaveRelations"));
//		this.setVirtualQualifier(true);
//		this.overloadCascadeSaveRelations = overloadCascadeSaveRelations;
		pBeans = addParam(Types.qvector(bean.toSharedPtr()).toConstRef(), "beans");
		this.bean = bean;
	}

	

	@Override
	public void addImplementation() {
//		if (overloadCascadeSaveRelations) {
//			addInstr(new StaticMethodCall(bean, parent.getMethod("save"), BoolExpression.FALSE).asInstruction()) ;
//			
//		} else {
//			addInstr(new StaticMethodCall(parent.getSuperclass(), parent.getMethod("save" ), getParam("cascadeSaveRelations")).asInstruction()) ;
			
		 	
		
			addInstr(_this().callMethodInstruction(ClsBaseRepository.bulkSave,pBeans));
		
			// TODO FIXME one to many relations @see BeanCls.getAllManyRelations
			List<ManyRelation> manyRelations = bean.getManyToManyRelations();
			if(!manyRelations.isEmpty()) {
				ForeachLoop foreach = _foreach(new Var(bean.toSharedPtr().toConstRef(), "bean"), pBeans);
				for(ManyRelation r:manyRelations) {
					if (r.getDestColumnCount()==0) {
						throw new RuntimeException();
					}
					ArrayList<String> columnsIn=new ArrayList<>();
					ArrayList<String> mappingColumnsMatching=new ArrayList<>();
					for(int i=0;i<r.getSourceColumnCount();i++) {
						mappingColumnsMatching.add(r.getSourceMappingColumn(i).getEscapedName()+" = ?");
					}
					for(int i=0;i<r.getDestColumnCount();i++) {
						columnsIn.add( r.getDestMappingColumn(i).getEscapedName() );
					}
					
					// removed
					Expression attrManyToManyRemoved = foreach.getVar().callAttrGetter(OrmUtil.getManyRelationDestAttrName(r)+"Removed" );
					IfBlock ifRemoveBeans = foreach._if(Expressions.not(attrManyToManyRemoved
							.callMethod("empty")
							));
					String sql="delete from "+r.getMappingTable().getEscapedName();
					
					
					
					sql += " where " ;
	//				
					sql += CodeUtil2.concat(mappingColumnsMatching, " and ");
					sql += " and " + (columnsIn.size()>1? CodeUtil2.parentheses(CodeUtil2.concat(columnsIn, ",")):columnsIn.get(0));
					sql += " in (%1)";
					Var varDeleteSql = ifRemoveBeans.thenBlock()._declare(Types.QString, "deleteSql",QString.fromStringConstant(sql));
					Type foreachRemoveElementType = ((ClsQVector) attrManyToManyRemoved.getType()).getElementType();
					Var varParamsForeachRemove = ifRemoveBeans.thenBlock()._declare( CoreTypes.QVariantList, "params");
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
							ifRemoveBeans.thenBlock().addInstr(varParamsForeachRemove.callMethodInstruction(ClsQVariantList.append, Types.QVariant.callStaticMethod(ClsQVariant.fromValue, foreach.getVar().callAttrGetter(col.getCamelCaseName()))));
						}
					} else {
						Expression e = foreach.getVar().callAttrGetter( bean.getTbl().getPrimaryKey().getFirstColumn().getCamelCaseName());
						ifRemoveBeans.thenBlock().addInstr(varParamsForeachRemove.callMethodInstruction(ClsQVariantList.append,e.getType().equals(Types.QVariant) ?e : Types.QVariant.callStaticMethod(ClsQVariant.fromValue,e) ));
					}
					
					ForeachLoop foreachAttrRemove = ifRemoveBeans.thenBlock()._foreach(new Var(foreachRemoveElementType.isPrimitiveType()?foreachRemoveElementType: foreachRemoveElementType.toConstRef(), "rem"), attrManyToManyRemoved);
				
					if (r.getDestTable().getPrimaryKey().isMultiColumn()) {
						for(Column col:r.getDestTable().getPrimaryKey().getColumns()) {
							Expression e = foreachAttrRemove.getVar().accessAttr(col.getCamelCaseName());
							foreachAttrRemove.addInstr(varParamsForeachRemove.callMethodInstruction(ClsQVariantList.append, e.getType().equals(Types.QVariant) ?e : Types.QVariant.callStaticMethod(ClsQVariant.fromValue,e) ));
						}
					} else {
						foreachAttrRemove.addInstr(varParamsForeachRemove.callMethodInstruction(ClsQVariantList.append, Types.QVariant.callStaticMethod(ClsQVariant.fromValue, foreachAttrRemove.getVar())));
					}
					
					foreachAttrRemove.addInstr(new BinaryOperatorExpression(varPlaceholdersForeachRemove, new QStringPlusEqOperator(), QString.fromStringConstant(bean.getTbl().getPrimaryKey().isMultiColumn() ? ","+CodeUtil2.parentheses( CodeUtil2.strMultiply("?", ",", r.getDestTable().getPrimaryKey().getColumns().size())):",?" )).asInstruction() );
					ifRemoveBeans.thenBlock()._callMethodInstr(_this().accessAttr("sqlCon"), "execute", varDeleteSql.callMethod("arg", varPlaceholdersForeachRemove.callMethod("mid", new IntExpression(1))), varParamsForeachRemove);
					
					// added
					
	//				ArrayList<String> columnsInsert=new ArrayList<>();
	//				for(int i=0;i<r.getSourceColumnCount();i++) {
	//					columnsInsert.add( r.getSourceMappingColumn(i).getEscapedName() );
	//				}
	//				for(int i=0;i<r.getDestColumnCount();i++) {
	//					columnsInsert.add( r.getDestMappingColumn(i).getEscapedName() );
	//				}
					
					Expression attrManyToManyAdded = foreach.getVar().callAttrGetter(OrmUtil.getManyRelationDestAttrName(r)+"Added" );
					IfBlock ifAddBeans = foreach._if(Expressions.not(attrManyToManyAdded
							.callMethod("empty")
							));
					
					if(BeanCls.getDatabase().supportsMultiRowInsert()) {
						String sqlAdded= BeanCls.getDatabase().supportsInsertOrIgnore() ?
								
								BeanCls.getDatabase().sqlInsertOrIgnoreMultiRow(r.getMappingTable(),"%1") :
									
									BeanCls.getDatabase().sqlInsertMultiRow(r.getMappingTable(),"%1");
									;
						Var varAddSql = ifAddBeans.thenBlock()._declare(Types.QString, "addedSql",QString.fromStringConstant(sqlAdded));
						Type foreachAddElementType = ((ClsQVector) attrManyToManyAdded.getType()).getElementType();
						Var varParamsForeachAdd = ifAddBeans.thenBlock()._declare( CoreTypes.QVariantList, "params");
						Var varPlaceholdersForeachAdd = ifAddBeans.thenBlock()._declare( Types.QString, "placeholders");
						ifAddBeans.thenBlock()._callMethodInstr(varPlaceholdersForeachAdd, "reserve", attrManyToManyAdded.callMethod("size").binOp("*", new IntExpression(2)));
						ifAddBeans.thenBlock()._callMethodInstr(varParamsForeachAdd, "reserve", attrManyToManyAdded.callMethod("size"));
						
						ForeachLoop foreachAttrAdd = ifAddBeans.thenBlock()._foreach(new Var(foreachAddElementType.isPrimitiveType()?foreachAddElementType: foreachAddElementType.toConstRef(), "rem"),  attrManyToManyAdded);
						if (bean.getTbl().getPrimaryKey().isMultiColumn()) {
							for(Column col:bean.getTbl().getPrimaryKey().getColumns()) {
								Expression e =foreach.getVar().callAttrGetter(  col.getCamelCaseName());
								foreachAttrAdd.addInstr(varParamsForeachAdd.callMethodInstruction(ClsQVariantList.append, e.getType().equals(Types.QVariant) ? e : Types.QVariant.callStaticMethod(ClsQVariant.fromValue, e)  ));
							}
						} else {
							Expression e =foreach.getVar().callAttrGetter(  bean.getTbl().getPrimaryKey().getFirstColumn().getCamelCaseName());
							foreachAttrAdd.addInstr(varParamsForeachAdd.callMethodInstruction(ClsQVariantList.append, e.getType().equals(Types.QVariant) ? e : Types.QVariant.callStaticMethod(ClsQVariant.fromValue, e)));
						}
						if (r.getDestTable().getPrimaryKey().isMultiColumn()) {
							for(Column col:r.getDestTable().getPrimaryKey().getColumns()) {
								Expression e = foreachAttrAdd.getVar().accessAttr(col.getCamelCaseName()); 
								foreachAttrAdd.addInstr(varParamsForeachAdd.callMethodInstruction(ClsQVariantList.append, e.getType().equals(Types.QVariant) ? e : Types.QVariant.callStaticMethod(ClsQVariant.fromValue, e)  ));
							}
						} else {
							foreachAttrAdd.addInstr(varParamsForeachAdd.callMethodInstruction(ClsQVariantList.append, foreachAttrAdd.getVar().getType().equals(Types.QVariant) ? foreachAttrAdd.getVar() :  Types.QVariant.callStaticMethod(ClsQVariant.fromValue,foreachAttrAdd.getVar())));
						}
						int propertyColumnCount=0;
						for(Column col:r.getMappingTable().getAllColumns()) {
													
							if(!col.isPartOfPk()) {
								propertyColumnCount++;
								foreachAttrAdd.addInstr(varParamsForeachAdd.callMethodInstruction(ClsQVariantList.append, BeanCls.getDatabaseMapper().getColumnDefaultValueExpression(col)));
							}
						}
						
					//	foreachAttrAdd.addInstr(varParamsForeachAdd.callMethodInstruction(ClsQVariantList.append, _this().accessAttr(new Attr( bean.getPkType(),  bean.getTbl().getPrimaryKey().getFirstColumn().getCamelCaseName()))));
		//				foreachAttrAdd.addInstr(varParamsForeachAdd.callMethodInstruction(ClsQVariantList.append, foreachAttrAdd.getVar()));
						foreachAttrAdd.addInstr(new BinaryOperatorExpression(varPlaceholdersForeachAdd, new QStringPlusEqOperator(), QString.fromStringConstant(","+ CodeUtil2.parentheses( CodeUtil2.strMultiply("?", ",", r.getSourceColumnCount()+r.getDestColumnCount()+propertyColumnCount)) )).asInstruction() );
	//					ifAddBeans.thenBlock().addInstr(new cpp.core.instruction.ScClosedInstruction("qDebug()<<addedSql.arg(placeholders.mid(1))"));
						ifAddBeans.thenBlock()._callMethodInstr(_this().accessAttr("sqlCon"), "execute", varAddSql.callMethod("arg", varPlaceholdersForeachAdd.callMethod("mid", new IntExpression(1))), varParamsForeachAdd);
					} else {
						throw new RuntimeException("not implemented");
					}
					
				}
			}
//		}

	}

}