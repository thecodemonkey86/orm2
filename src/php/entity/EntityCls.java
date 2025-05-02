package php.entity;

import java.util.List;

import database.Database;
import database.column.Column;
import database.relation.AbstractRelation;
import database.relation.ManyRelation;
import database.relation.OneRelation;
import database.relation.OneToManyRelation;
import database.table.Table;
import php.core.Attr;
import php.core.Param;
import php.core.PhpCls;
import php.core.Type;
import php.core.Types;
import php.core.expression.Expression;
import php.core.expression.StaticAccessExpression;
import php.core.method.MethodAttributeGetter;
import php.core.method.MethodAttributeSetter;
import php.entity.method.EntityConstructor;
import php.entity.method.EntityEqualsMethod;
import php.entity.method.MethodAddManyToManyRelatedEntity;
import php.entity.method.MethodAddManyToManyRelatedEntityInternal;
import php.entity.method.MethodAddRelatedEntity;
import php.entity.method.MethodAddRelatedEntityInternal;
import php.entity.method.MethodAttrGetter;
import php.entity.method.MethodClearModified;
import php.entity.method.MethodColumnAttrSetNull;
import php.entity.method.MethodColumnAttrSetter;
import php.entity.method.MethodColumnAttrSetterInternal;
import php.entity.method.MethodCreateNew;
import php.entity.method.MethodGetFieldName;
import php.entity.method.MethodGetFieldsAsAssocArray;
import php.entity.method.MethodGetPrimaryKeyFields;
import php.entity.method.MethodHasAddedManyToMany;
import php.entity.method.MethodHasRemovedManyToMany;
import php.entity.method.MethodHasUpdate;
import php.entity.method.MethodIsModified;
import php.entity.method.MethodManyAttrGetter;
import php.entity.method.MethodOneRelationAttrGetter;
import php.entity.method.MethodOneRelationAttrSetter;
import php.entity.method.MethodOneRelationEntityIsNull;
import php.entity.method.MethodRemoveAllManyToManyRelatedEntities;
import php.entity.method.MethodRemoveAllOneToManyRelatedEntities;
import php.entity.method.MethodRemoveManyToManyRelatedEntity;
import php.entity.method.MethodSetAutoIncrementId;
import php.entity.method.MethodSetPrimaryKey;
import php.entity.method.MethodSetValue;
import php.entitypk.PkMultiColumnType;
import php.entityrepository.helper.FetchListHelperClass;
import php.orm.DatabaseTypeMapper;
import php.orm.OrmUtil;
import util.CodeUtil2;
import util.StringUtil;

public class EntityCls extends PhpCls {


	static Database database;
	static DatabaseTypeMapper typeMapper;
	static PhpCls sqlQueryCls;
	protected static String entityNamespace;
	protected static String entityRepoNamespace;
	public final static String API_LEVEL="2.5.0";
	
	public static void setEntityRepoNamespace(String entityRepoClsNamespace) {
		EntityCls.entityRepoNamespace = entityRepoClsNamespace;
	}

	public static void setSqlQueryCls(PhpCls sqlQueryCls) {
		EntityCls.sqlQueryCls = sqlQueryCls;
	}

	public static PhpCls getSqlQueryCls() {
		return sqlQueryCls;
	}

	public static void setEntityNamespace(String entityPackage) {
		EntityCls.entityNamespace = entityPackage;
	}

	public static void setDatabase(Database database) {
		EntityCls.database = database;
	}

	public static Database getDatabase() {
		return database;
	}

	public static DatabaseTypeMapper getTypeMapper() {
		return typeMapper;
	}

	public static void setTypeMapper(DatabaseTypeMapper typeMapper) {
		EntityCls.typeMapper = typeMapper;
	}

	protected Table tbl;
	protected List<OneToManyRelation> oneToManyRelations;
	protected List<OneRelation> oneRelations;
	protected List<ManyRelation> manyRelations;

	
	protected Type pkType;
	protected FetchListHelperClass fetchListHelper;

	public FetchListHelperClass getFetchListHelperCls() {
		return fetchListHelper;
	}


	private void addAttributes(List<Column> allColumns) {


		for(OneRelation r:oneRelations) {
			OneAttr attr = new OneAttr(r);
			addAttr(attr);
			addMethod(new MethodOneRelationAttrGetter(attr,true));	
			addMethod(new MethodOneRelationEntityIsNull(r));
			addMethod(new MethodOneRelationAttrSetter( attr, true)); // internal setter
			addMethod(new MethodOneRelationAttrSetter( attr, false)); // public setter
			if (!r.isPartOfPk()) {
				Attr attrModified = new Attr(Types.Bool, attr.getName()+"Modified");
				addAttr(attrModified);
				addMethod(new MethodIsModified(attrModified));
			}
		}
		for(OneToManyRelation r:oneToManyRelations) {
			ManyAttr attr = new ManyAttr(r);
			addAttr(attr);
			addMethod(new MethodManyAttrGetter(attr));
			addMethod(new MethodAddRelatedEntity(r, new Param(attr.getElementType(), "entity")));
			addMethod(new MethodAddRelatedEntityInternal(r, new Param(attr.getElementType(), "entity")));
			Attr manyRelAdded = new Attr(Types.array(Entities.get(r.getDestTable())) ,attr.getName()+"Added");
			addAttr(manyRelAdded);
			
			Attr manyRelRemoved = new Attr(Types.array(Entities.get(r.getDestTable())) ,attr.getName()+"Removed");
			addAttr(manyRelRemoved);
			
			addMethod(new MethodRemoveAllOneToManyRelatedEntities(r)
					);
		}

		for(ManyRelation r:manyRelations) {
			ManyAttr attr = new ManyAttr(r);
			addAttr(attr);
			addMethod(new MethodManyAttrGetter(attr));
			
			Attr manyRelAdded = new Attr(Types.array(Entities.get(r.getDestTable())) ,attr.getName()+"Added");
			addAttr(manyRelAdded);
			
			addMethod(new MethodAttributeGetter(manyRelAdded));
			Attr manyRelRemoved = new Attr(Types.array(Entities.get(r.getDestTable())) ,attr.getName()+"Removed");
			addAttr(manyRelRemoved);
			addMethod(new MethodAttributeGetter(manyRelRemoved));
			
			addMethod(new MethodAddManyToManyRelatedEntity(r, new Param(attr.getElementType(), "entity")));
			addMethod(new MethodAddManyToManyRelatedEntityInternal(r, new Param(attr.getElementType(), "entity")));

			addMethod(new MethodRemoveManyToManyRelatedEntity(r, new Param(attr.getElementType(), "entity")));
			addMethod(new MethodHasRemovedManyToMany(r));
			addMethod(new MethodHasAddedManyToMany(r));
			
			addMethod(new MethodRemoveAllManyToManyRelatedEntities(r));
		}
		addMethod(new MethodSetPrimaryKey(this));
		
		for(Column col:allColumns) {

			if (!col.hasOneRelation()

					) {
				Attr attr = new Attr(EntityCls.getTypeMapper().getTypeFromDbDataType(col), col.getCamelCaseName());
				addAttr(attr);
				addMethod(new MethodAttrGetter(attr,false));	
				if(!col.isPartOfPk()) {
					addMethod(new MethodColumnAttrSetter(this,col,attr));
				}
				addMethod(new MethodColumnAttrSetterInternal(this,col,attr));
				if (col.isNullable()) {
					addMethod(new MethodColumnAttrSetNull(this, col, attr));
				}

				addMethod(new MethodGetFieldName(col));
				
				if (!col.isPartOfPk()) {

					Attr attrModified = new Attr(Types.Bool, col.getCamelCaseName()+"Modified");
					addAttr(attrModified);
					addMethod(new MethodIsModified(attrModified));
				}
				if(col.isRawValueEnabled()) {
					Attr attrInsertExpression = new Attr(Attr.Protected, Types.String,"insertExpression"+col.getUc1stCamelCaseName(), null,false);
					addAttr(attrInsertExpression);
					addMethod(new MethodAttributeSetter(attrInsertExpression));
					addMethod(new MethodAttributeGetter(attrInsertExpression));
					/*Attr attrInsertParams = new Attr(Attr.Protected, Types.array(Types.String),"insertParamsForRawExpression"+col.getUc1stCamelCaseName(),null,false);
					addAttr(attrInsertParams);
					addMethod(new MethodAddInsertParamForRawExpression(col));*/
				}
			}
			else {
				Attr attr = new Attr(EntityCls.getTypeMapper().getTypeFromDbDataType(col), col.getCamelCaseName());
				addAttr(attr);
				addMethod(new MethodAttrGetter(attr,false));	
			}

			if (col.isAutoIncrement()) {
				addMethod(new MethodSetAutoIncrementId());
			}

		}
		
		addMethod(new MethodGetPrimaryKeyFields(this));
		
	}

	public EntityCls(Table tbl,List<OneToManyRelation> manyRelations,List<OneRelation> oneRelations, List<ManyRelation> manyToManyRelations) {
		super(CodeUtil2.uc1stCamelCase(tbl.getName()), entityNamespace);
		this.tbl = tbl;
		this.oneToManyRelations= manyRelations;
		this.oneRelations = oneRelations;
		this.manyRelations = manyToManyRelations;
		
		if (tbl.getPrimaryKey().isMultiColumn()) {
			pkType = new PkMultiColumnType("Pk"+tbl.getUc1stCamelCaseName(),entityNamespace+"\\Pk", tbl);
			
			for(Column col: tbl.getPrimaryKey().getColumns()) {
				Attr attrPrev = new Attr(EntityCls.getTypeMapper().getTypeFromDbDataType(col), col.getCamelCaseName()+"Previous");
				addAttr(attrPrev);
				addMethod(new MethodAttrGetter(attrPrev, false));
			}
			
		} else {
			if (tbl.getPrimaryKey().getColumns().size()==0) {
				throw new RuntimeException("pk info missing for "+getName());
			}
			Column col= tbl.getPrimaryKey().getFirstColumn();
			Attr attrPrev = new Attr(EntityCls.getTypeMapper().getTypeFromDbDataType(col), col.getCamelCaseName()+"Previous");
			addAttr(attrPrev);
			addMethod(new MethodAttrGetter(attrPrev, false));
			pkType =typeMapper.columnToType(col);
			
		}
	}



	public void addDeclarations() {
		superclass = Types.BaseEntity;
		
		setConstructor(new EntityConstructor(tbl.getPrimaryKey().isAutoIncrement(),tbl.getColumnsWithoutPrimaryKey())); 

addMethod(new MethodSetValue());
		addAttributes(tbl.getAllColumns());
		//		List<Column> cols = tbl.getColumns(!tbl.getPrimaryKey().isAutoIncrement());
		//		List<Column> allCols = tbl.getColumns(true);
		//		addMethod(new MethodGetInsertFields(cols));
		//		addMethod(new MethodGetInsertValuePlaceholders(cols.size()));
		//		addMethod(new MethodGetUpdateFields(tbl.getColumnsWithoutPrimaryKey(),tbl.getPrimaryKey()));
		//		addMethod(new MethodGetUpdateConditionParams(tbl.getPrimaryKey()));
		//		addMethod(new MethodGetUpdateCondition(tbl.getPrimaryKey()));
		//		addMethod(new MethodGetSelectFields(allCols));
		//		addMethod(new MethodGetAllSelectFields(allCols));
		//		addMethod(new MethodGetByRecordStatic(allCols,this));
		//		addMethod(new MethodAddRelatedTableJoins(this));
		//		addMethod(new MethodGetPrimaryKeyColumns(tbl.getPrimaryKey()));
		//		addMethod(new MethodGetLimitQueryString(tbl.getPrimaryKey()));

		//		if (manyRelations.size()>0) {
		//			addMethod(new MethodSave(true));
		//			addMethod(new MethodSave(false));
		//		}
		addMethod(new MethodCreateNew(this));
		addMethod(new EntityEqualsMethod(this, tbl.getPrimaryKey()));
		addMethod(new MethodHasUpdate());
		addMethod(new MethodClearModified());
		addMethod(new MethodGetFieldsAsAssocArray(this));
		
		
		fetchListHelper = new FetchListHelperClass(this, entityRepoNamespace);
	}


	@Override
	public void addMethodImplementations() {

		if(tbl.getPrimaryKey().isMultiColumn()) {
			((PhpCls) pkType).addMethodImplementations();
		}
		
		fetchListHelper.addMethodImplementations();
		super.addMethodImplementations();

	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof EntityCls && getName().equals(((EntityCls)obj).getName());
	}

	@Override
	public int hashCode() {
		return getName().hashCode();
	}


	public Attr getManyRelationAttr(OneRelation r) {
		return getAttrByName(CodeUtil2.plural(r.getDestTable().getCamelCaseName()));
	}

	public Attr getOneRelationAttr(OneRelation r) {
		try {
			//			return getAttrByName(r.getDestTable().getCamelCaseName());
			return getAttrByName(OrmUtil.getOneRelationDestAttrName(r));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(r);
			throw e;

		}
	}

	 @Override
	protected void addCodeBeforeClassDefinition(StringBuilder sb) {
		CodeUtil2.writeLine(sb, "/* AUTOMATISCH GENERIERTE DATEI */");
		CodeUtil2.writeLine(sb,"/* aktivweb ORM System */");
		CodeUtil2.writeLine(sb,"/* API Level "+API_LEVEL+" */");
	}

	public StaticAccessExpression accessStaticAttribute(String attrName) {
		return new StaticAccessExpression(this, getStaticAttribute(attrName));
	}

	public Expression accessThisAttrByColumn(Column col) {
		return accessThisAttrByColumn(col, true);
	}

	public Expression accessThisAttrByColumn(Column col, boolean skipOneRelationEntity) {
		if (col.hasOneRelation()) {
			try{
				Attr attr = getOneRelationAttr(col.getOneRelation());
				if(skipOneRelationEntity) {
					return _this().accessAttr(getAttrByName(col.getCamelCaseName()));
				}else {
					return _this().accessAttr(attr).callMethod("get"+col.getOneRelationMappedColumn().getUc1stCamelCaseName());
				}
			} catch(Exception e) {
				e.printStackTrace();
				System.out.println(col);
				throw e;
			}
		} else {
			return _this().accessAttr(getAttrByName(col.getCamelCaseName()));
		}
	}

	public Expression accessThisColumnAttrOrEntity(Column col) {
		if (col.hasOneRelation()) {
			try{
				Attr attr = getOneRelationAttr(col.getOneRelation());
				return _this().accessAttr(attr);
			} catch(Exception e) {
				e.printStackTrace();
				System.out.println(col);
				throw e;
			}
		} else {
			return _this().accessAttr(getAttrByName(col.getCamelCaseName()));
		}
	}

	/**
	 * difference to {@code accessColumnAttrOrEntity}: if one relation entity, automatically calls the getter method for the mapped key column 
	 * 
	 * @param entityExpression
	 * @param col
	 * @param skipOneRelationEntity true: avoid (potentially) loading 1:1/n:1-relation entity, use redundant primary key field in EntityClass which owns 1:1/n:1-relation  
	 * @return
	 */
	public static Expression accessAttrGetterByColumn(Expression entityExpression, Column col, boolean skipOneRelationEntity) {
		if (col.hasOneRelation()) {
			try{
				Attr attr = ((EntityCls) entityExpression.getType()).getOneRelationAttr(col.getOneRelation());
				if(skipOneRelationEntity) {
					return entityExpression.callMethod("get"+StringUtil.ucfirst(col.getCamelCaseName()));
				} else {
					return entityExpression.accessAttrGetter(attr).callMethod("get"+col.getOneRelationMappedColumn().getUc1stCamelCaseName());
				}
			} catch(Exception e) {
				e.printStackTrace();
				System.out.println(col);
				throw e;
			}
		} else {
			return entityExpression.accessAttrGetter(((EntityCls) entityExpression.getType()).getAttrByName(col.getCamelCaseName()));
		}
	}

	/**
	 * 
	 * @param entityExpression
	 * @param col
	 * @return
	 */
	public static Expression accessColumnAttrOrEntity(Expression entityExpression, Column col) {
		if (col.hasOneRelation()) {
			try{
				Attr attr = ((EntityCls) entityExpression.getType()).getOneRelationAttr(col.getOneRelation());
				return entityExpression.accessAttrGetter(attr);
			} catch(Exception e) {
				e.printStackTrace();
				System.out.println(col);
				throw e;
			}
		} else {
			return entityExpression.accessAttrGetter(((EntityCls) entityExpression.getType()).getAttrByName(col.getCamelCaseName()));
		}
	}
	
	/**
	 * 
	 * @param entityExpression
	 * @param col
	 * @return
	 */
	public static Expression accessColumnAttrOrEntityPrevious(Expression entityExpression, Column col) {
		if (!col.isPartOfPk()) {
			throw new IllegalArgumentException();
		}
		return entityExpression.accessAttr(col.getCamelCaseName()+"Previous");
	}
	
	/**
	 * 
	 * @param entityExpression
	 * @param col
	 * @return
	 */
	public static Expression accessIsColumnAttrOrEntityModified(Expression entityExpression, Column col) {
		if (col.hasOneRelation()) {
			try{
				String attrName = OrmUtil.getOneRelationDestAttrName(col.getOneRelation())+"Modified";
				return entityExpression.callMethod("is"+StringUtil.ucfirst(attrName));
			} catch(Exception e) {
				e.printStackTrace();
				System.out.println(col);
				throw e;
			}
		} else {
			return entityExpression.callMethod("is"+ col.getUc1stCamelCaseName()+"Modified");
		}
	}

	public List<OneRelation> getOneRelations() {
		return oneRelations;
	}

	public List<OneToManyRelation> getOneToManyRelations() {
		return oneToManyRelations;
	}

	public List<ManyRelation> getManyRelations() {
		return manyRelations;
	}

	public Table getTbl() {
		return tbl;
	}

	public RepositoryAttr getRepositoryAttr() {
		return (RepositoryAttr) getAttrByName("repository");
	}

	public List<ManyRelation> getManyToManyRelations() {
		return manyRelations;
	}

	public static String getAddRelatedEntityMethodName(AbstractRelation r) {
		if (r instanceof OneToManyRelation) {
			return "add"+StringUtil.ucfirst(OrmUtil.getManyRelationDestAttrNameSingular((OneToManyRelation) r))+"Internal";
		} else  if (r instanceof ManyRelation) {
			return "add"+StringUtil.ucfirst(OrmUtil.getManyRelationDestAttrNameSingular((ManyRelation) r))+"Internal";
		} else {
			throw new IllegalArgumentException();
		}
	}

	
	public static Expression getPkExpression(Expression e, Column colPk) {
		EntityCls cls= (EntityCls) e.getType();
		if (colPk.hasOneRelation()) {
			try{

				Attr attr = cls.getOneRelationAttr(colPk.getOneRelation());
				return e.accessAttr(cls.getAttr(attr)).callMethod("get"+colPk.getOneRelationMappedColumn().getUc1stCamelCaseName());
			} catch(Exception ex) {
				ex.printStackTrace();
				System.out.println(colPk);
				throw ex;
			}
		} else {
			return e.accessAttr(cls.getAttrByName(colPk.getCamelCaseName()));
		}
	}

	public static Expression accessPrimaryKeyPreviousAttrGetterByColumn(Expression entityExpression, Column col) {
		if (col.hasOneRelation()) {
			try{
				return entityExpression.callMethod("get"+StringUtil.ucfirst(col.getCamelCaseName()+"Previous"));
			} catch(Exception e) {
				e.printStackTrace();
				System.out.println(col);
				throw e;
			}
		} else {
			return entityExpression.accessAttrGetter(((EntityCls) entityExpression.getType()).getAttrByName(col.getCamelCaseName()+"Previous"));
		}
	}
	
	public Type getPkType() {
		if(pkType == null) {
			System.out.println(getName());
			throw new NullPointerException();
		}
		return pkType;
	}
}
