package cpp.entity.method;

import java.util.ArrayList;

import codegen.CodeUtil;
import cpp.CoreTypes;
import cpp.Types;
import cpp.core.Attr;
import cpp.core.Method;
import cpp.core.Optional;
import cpp.core.Param;
import cpp.core.QString;
import cpp.core.expression.BoolExpression;
import cpp.core.expression.Expressions;
import cpp.core.instruction.IfBlock;
import cpp.entity.Entities;
import cpp.entityquery.method.MethodEntityQueryFetch;
import cpp.entityquery.method.MethodEntityQueryFetchOne;
import cpp.entityquery.method.MethodEntityQueryWhereEquals;
import cpp.entityrepository.method.MethodCreateQuerySelect;
import database.relation.AbstractRelation;
import database.relation.ManyRelation;
import database.relation.OneRelation;
import database.relation.OneToManyRelation;

public class MethodEnsureLoaded extends Method {


	AbstractRelation relation;
	ManyRelation manyToManyRelation;
	OneToManyRelation oneToManyRelation;
	OneRelation oneRelation;
	Attr a;
	Param pSqlCon;
	public static String getMethodName(AbstractRelation relation) {
		return "ensureLoaded"+relation.getIdentifier();
	}
	
	public MethodEnsureLoaded(Attr a,ManyRelation manyToManyRelation) {
		super(Protected, CoreTypes.Void, getMethodName((AbstractRelation)manyToManyRelation));
		this.manyToManyRelation=manyToManyRelation;
		this.relation=(AbstractRelation)manyToManyRelation;
		this.a=a;
		pSqlCon = addParam(Types.QSqlDatabase.toConstRef(), "sqlCon");
	}
	public MethodEnsureLoaded(Attr a,OneToManyRelation oneToManyRelation) {
		super(Protected, CoreTypes.Void, getMethodName((AbstractRelation)oneToManyRelation));
		this.oneToManyRelation=oneToManyRelation;
		this.relation=(AbstractRelation)oneToManyRelation;
		this.a=a;
		pSqlCon = addParam(Types.QSqlDatabase.toConstRef(), "sqlCon");
	}
	public MethodEnsureLoaded(Attr a,OneRelation oneRelation) {
		super(Protected, CoreTypes.Void, getMethodName((AbstractRelation)oneRelation));
		this.oneRelation=oneRelation;
		this.relation=oneRelation;
		this.a=a;
		pSqlCon = addParam(Types.QSqlDatabase.toConstRef(), "sqlCon");
	}
	@Override
	public void addImplementation() {
		Attr attrLoaded=parent.getAttrByName("loaded"+relation.getIdentifier());
		Attr attrQuery=parent.getAttrByName("query"+relation.getIdentifier());
		
		IfBlock ifNotLoaded = _if(/*Expressions.and(*/
				Expressions.not(attrLoaded)
//				Expressions.not(paramByName("noLoading"))
			//)
				
				
		);
		
		var pk=relation.getDestTable().getPrimaryKey();
		var exprQuery=Types.EntityRepository.callStaticMethod(MethodCreateQuerySelect.getMethodName(Entities.get(relation.getDestTable())));

		if(manyToManyRelation!=null) {
					var alias=manyToManyRelation.getMappingAlias();
			ArrayList<String> mappingJoinConditions=new ArrayList<String>();
			for(int i=0;i<manyToManyRelation.getSourceColumnCount();++i) {
				mappingJoinConditions.add("e1."+manyToManyRelation.getSourceEntityColumn(i).getEscapedName()+"="+alias+"."+manyToManyRelation.getDestMappingColumn(i).getEscapedName());
			}
			exprQuery=exprQuery
					.callMethod("join",QString.fromStringConstant(manyToManyRelation.getMappingTable().getEscapedName()+" "+alias),QString.fromStringConstant(CodeUtil.concat(mappingJoinConditions, " and ")));
			for(int i=0;i<manyToManyRelation.getSourceColumnCount();++i) {
				exprQuery=exprQuery.callMethod("where", QString.fromStringConstant(alias +"."+manyToManyRelation.getSourceMappingColumn(i).getEscapedName()+"=?" ),
						_this().callAttrGetter(manyToManyRelation.getDestEntityColumn(i).getCamelCaseName()));
			}
			exprQuery = exprQuery.callMethod(MethodEntityQueryFetch.METHOD_NAME, pSqlCon);
		} else if(oneToManyRelation!=null) {
			for(int i=0;i<oneToManyRelation.getColumnCount();++i) {
				var pkCol = pk.getColumn(i);
				exprQuery=exprQuery.callMethod(MethodEntityQueryWhereEquals.getMethodName(pkCol), _this().callAttrGetter(oneToManyRelation.getColumns(i).getValue1().getCamelCaseName()));
			}
			exprQuery = exprQuery.callMethod(MethodEntityQueryFetch.METHOD_NAME, pSqlCon);
		} else {
			for(int i=0;i<pk.getColumnCount();++i) {
				var pkCol = pk.getColumn(i);
				exprQuery=exprQuery.callMethod(MethodEntityQueryWhereEquals.getMethodName(pkCol), _this().callAttrGetter(oneRelation.getColumns(i).getValue1().getCamelCaseName()));
			}
			exprQuery = exprQuery.callMethod(MethodEntityQueryFetchOne.METHOD_NAME, pSqlCon);
		}
		
		
		IfBlock ifHasRelationQueryObject = ifNotLoaded.thenBlock()._if(attrQuery.callMethod(Optional.has_value)); 
		ifHasRelationQueryObject.thenBlock()._assign(a, attrQuery.callMethod(Optional.value).callMethod(oneRelation!=null ? MethodEntityQueryFetchOne.METHOD_NAME : MethodEntityQueryFetch.METHOD_NAME, pSqlCon));
		ifHasRelationQueryObject.elseBlock()._assign(a, exprQuery );
		//addInstr(attrQuery.callMethod(Optional.has_value)));
		ifNotLoaded.thenBlock()._assign(attrLoaded, BoolExpression.TRUE);

	}

}
