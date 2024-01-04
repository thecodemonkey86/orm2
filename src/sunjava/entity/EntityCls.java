package sunjava.entity;

import java.util.List;

import database.Database;
import database.column.Column;
import database.relation.AbstractRelation;
import database.relation.ManyRelation;
import database.relation.OneRelation;
import database.relation.OneToManyRelation;
import database.table.Table;
import sunjava.core.Attr;
import sunjava.core.Constructor;
import sunjava.core.JavaCls;
import sunjava.core.Param;
import sunjava.core.Type;
import sunjava.core.Types;
import sunjava.core.expression.Expression;
import sunjava.core.expression.StaticAccessExpression;
import sunjava.core.method.MethodAttributeGetter;
import sunjava.entity.method.EntityConstructor;
import sunjava.entity.method.EntityEqualsMethod;
import sunjava.entity.method.MethodAddManyToManyRelatedBean;
import sunjava.entity.method.MethodAddManyToManyRelatedBeanInternal;
import sunjava.entity.method.MethodAddRelatedEntity;
import sunjava.entity.method.MethodAddRelatedEntityInternal;
import sunjava.entity.method.MethodAttrGetter;
import sunjava.entity.method.MethodColumnAttrSetNull;
import sunjava.entity.method.MethodColumnAttrSetter;
import sunjava.entity.method.MethodColumnAttrSetterInternal;
import sunjava.entity.method.MethodCreateNew;
import sunjava.entity.method.MethodGetFieldName;
import sunjava.entity.method.MethodHasAddedManyToMany;
import sunjava.entity.method.MethodHasRemovedManyToMany;
import sunjava.entity.method.MethodHasUpdate;
import sunjava.entity.method.MethodHashCode;
import sunjava.entity.method.MethodIsModified;
import sunjava.entity.method.MethodManyAttrGetter;
import sunjava.entity.method.MethodOneRelationAttrGetter;
import sunjava.entity.method.MethodOneRelationAttrSetter;
import sunjava.entity.method.MethodOneRelationBeanIsNull;
import sunjava.entity.method.MethodRemoveManyToManyRelatedBean;
import sunjava.entity.method.MethodSetAutoIncrementId;
import sunjava.entitypk.PkMultiColumnType;
import sunjava.orm.DatabaseTypeMapper;
import sunjava.orm.OrmUtil;
import util.CodeUtil2;
import util.StringUtil;

public class EntityCls extends JavaCls {


	static Database database;
	static DatabaseTypeMapper typeMapper;
	static JavaCls sqlQueryCls;

	protected static String entityClsPackage;


	public static void setSqlQueryCls(JavaCls sqlQueryCls) {
		EntityCls.sqlQueryCls = sqlQueryCls;
	}

	public static JavaCls getSqlQueryCls() {
		return sqlQueryCls;
	}

	public static void setEntityClsPackage(String entityClsPackage) {
		EntityCls.entityClsPackage = entityClsPackage;
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

	//protected Struct structPk;
	protected Type pkType;
	protected FetchListHelperClass fetchListHelper;

	public FetchListHelperClass getFetchListHelperCls() {
		return fetchListHelper;
	}

	private void addImports() {
		addImport("sql.orm.model.BaseEntity");
	}

	@Override
	public void addImport(String i) {
		super.addImport(i);
	}

	private void addAttributes(List<Column> allColumns) {


		for(OneRelation r:oneRelations) {
			OneAttr attr = new OneAttr(r);
			addAttr(attr);
			addMethod(new MethodOneRelationAttrGetter(attr,true));	
			addMethod(new MethodOneRelationBeanIsNull(r));
			addMethod(new MethodOneRelationAttrSetter( attr, true,r.isPartOfPk())); // internal setter
			addMethod(new MethodOneRelationAttrSetter( attr, false,r.isPartOfPk())); // public setter
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
		}

		for(ManyRelation r:manyRelations) {
			ManyAttr attr = new ManyAttr(r);
			addAttr(attr);
			addMethod(new MethodManyAttrGetter(attr));
			
			Attr manyRelAdded = new Attr(Types.arraylist(Types.getRelationForeignPrimaryKeyType(r)) ,attr.getName()+"Added");
			addAttr(manyRelAdded);
			addMethod(new MethodAttributeGetter(manyRelAdded));
			Attr manyRelRemoved = new Attr(Types.arraylist(Types.getRelationForeignPrimaryKeyType(r)) ,attr.getName()+"Removed");
			addAttr(manyRelRemoved);
			addMethod(new MethodAttributeGetter(manyRelRemoved));
			
			addMethod(new MethodAddManyToManyRelatedBean(r, new Param(attr.getElementType(), "entity")));
			addMethod(new MethodAddManyToManyRelatedBeanInternal(r, new Param(attr.getElementType(), "entity")));

			addMethod(new MethodRemoveManyToManyRelatedBean(r, new Param(attr.getElementType(), "entity")));
			addMethod(new MethodHasRemovedManyToMany(r));
			addMethod(new MethodHasAddedManyToMany(r));
		}


		for(Column col:allColumns) {

			if (!col.hasOneRelation()

					) {
				Attr attr = new Attr(EntityCls.getTypeMapper().getTypeFromDbDataType(col.getDbType(), col.isNullable()), col.getCamelCaseName());
				addAttr(attr);
				addMethod(new MethodAttrGetter(attr,false));	
				addMethod(new MethodColumnAttrSetter(this,col,attr));
				addMethod(new MethodColumnAttrSetterInternal(this,col,attr));
				if (col.isNullable()) {
					addMethod(new MethodColumnAttrSetNull(this, col, attr));
				}

				addMethod(new MethodGetFieldName(col));
				addMethod(new MethodGetFieldName(col, true));
				if (!col.isPartOfPk()) {

					Attr attrModified = new Attr(Types.Bool, col.getCamelCaseName()+"Modified");
					addAttr(attrModified);
					addMethod(new MethodIsModified(attrModified));
				}
			}
			else {
				Attr attr = new Attr(EntityCls.getTypeMapper().getTypeFromDbDataType(col.getDbType(), col.isNullable()), col.getCamelCaseName());
				addAttr(attr);
				addMethod(new MethodAttrGetter(attr,false));	
			}

			if (col.isAutoIncrement()) {
				addMethod(new MethodSetAutoIncrementId());
			}

		}
		if (tbl.getPrimaryKey().isMultiColumn()) {
			pkType = new PkMultiColumnType("Pk"+tbl.getUc1stCamelCaseName(),entityClsPackage+".pk", tbl); 
			for(Column col: tbl.getPrimaryKey().getColumns()) {
				Attr attrPrev = new Attr(EntityCls.getTypeMapper().getTypeFromDbDataType(col.getDbType(), col.isNullable()), col.getCamelCaseName()+"Previous");
				addAttr(attrPrev);
				addMethod(new MethodAttrGetter(attrPrev, false));
			}
		} else {
			if (tbl.getPrimaryKey().getColumns().size()==0) {
				throw new RuntimeException("pk info missing for "+getName());
			}
			Column col= tbl.getPrimaryKey().getFirstColumn();
			Attr attrPrev = new Attr(EntityCls.getTypeMapper().getTypeFromDbDataType(col.getDbType(), col.isNullable()), col.getCamelCaseName()+"Previous");
			addAttr(attrPrev);
			addMethod(new MethodAttrGetter(attrPrev, false));
			pkType =typeMapper.columnToType(col);
		}
	}

	public EntityCls(Table tbl,List<OneToManyRelation> manyRelations,List<OneRelation> oneRelations, List<ManyRelation> manyToManyRelations) {
		super(CodeUtil2.uc1stCamelCase(tbl.getName()), entityClsPackage);
		this.tbl = tbl;
		this.oneToManyRelations= manyRelations;
		this.oneRelations = oneRelations;
		this.manyRelations = manyToManyRelations;
	}


	public Constructor getConstructor() {
		return super.getConstructors().get(0);
	}

	public void addDeclarations() {
		superclass = Types.BaseEntity;
		
		addConstructor(new EntityConstructor(tbl.getPrimaryKey().isAutoIncrement(),false,tbl.getColumnsWithoutPrimaryKey())); 
		addConstructor(new EntityConstructor(tbl.getPrimaryKey().isAutoIncrement(),true,tbl.getColumnsWithoutPrimaryKey())); 


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
		addMethod(new MethodHashCode(tbl));
		addMethod(new MethodHasUpdate());
		addImports();
	}


	@Override
	public void addMethodImplementations() {

		fetchListHelper = new FetchListHelperClass(this);
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
	public Type getPkType() {
		return pkType;
	}

	@Override
	protected void addClassHeaderCode(StringBuilder sb) {

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
	 * @param beanExpression
	 * @param col
	 * @param skipOneRelationEntity true: avoid (potentially) loading 1:1/n:1-relation entity, use redundant primary key field in BeanClass which owns 1:1/n:1-relation  
	 * @return
	 */
	public static Expression accessAttrGetterByColumn(Expression beanExpression, Column col, boolean skipOneRelationEntity) {
		if (col.hasOneRelation()) {
			try{
				Attr attr = ((EntityCls) beanExpression.getType()).getOneRelationAttr(col.getOneRelation());
				if(skipOneRelationEntity) {
					return beanExpression.callMethod("get"+StringUtil.ucfirst(col.getCamelCaseName()));
				} else {
					return beanExpression.accessAttrGetter(attr).callMethod("get"+col.getOneRelationMappedColumn().getUc1stCamelCaseName());
				}
			} catch(Exception e) {
				e.printStackTrace();
				System.out.println(col);
				throw e;
			}
		} else {
			return beanExpression.accessAttrGetter(((EntityCls) beanExpression.getType()).getAttrByName(col.getCamelCaseName()));
		}
	}

	/**
	 * 
	 * @param beanExpression
	 * @param col
	 * @return
	 */
	public static Expression accessColumnAttrOrEntity(Expression beanExpression, Column col) {
		if (col.hasOneRelation()) {
			try{
				Attr attr = ((EntityCls) beanExpression.getType()).getOneRelationAttr(col.getOneRelation());
				return beanExpression.accessAttrGetter(attr);
			} catch(Exception e) {
				e.printStackTrace();
				System.out.println(col);
				throw e;
			}
		} else {
			return beanExpression.accessAttrGetter(((EntityCls) beanExpression.getType()).getAttrByName(col.getCamelCaseName()));
		}
	}
	
	/**
	 * 
	 * @param beanExpression
	 * @param col
	 * @return
	 */
	public static Expression accessColumnAttrOrEntityPrevious(Expression beanExpression, Column col) {
		if (!col.isPartOfPk()) {
			throw new IllegalArgumentException();
		}
		return beanExpression.accessAttr(col.getCamelCaseName()+"Previous");
	}
	
	/**
	 * 
	 * @param beanExpression
	 * @param col
	 * @return
	 */
	public static Expression accessIsColumnAttrOrEntityModified(Expression beanExpression, Column col) {
		if (col.hasOneRelation()) {
			try{
				String attrName = OrmUtil.getOneRelationDestAttrName(col.getOneRelation())+"Modified";
				return beanExpression.callMethod("is"+StringUtil.ucfirst(attrName));
			} catch(Exception e) {
				e.printStackTrace();
				System.out.println(col);
				throw e;
			}
		} else {
			return beanExpression.callMethod("is"+ col.getUc1stCamelCaseName()+"Modified");
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

	public static String getRelatedBeanMethodName(AbstractRelation r) {
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

	public static Expression accessPrimaryKeyPreviousAttrGetterByColumn(Expression beanExpression, Column col) {
		if (col.hasOneRelation()) {
			try{
				return beanExpression.callMethod("get"+StringUtil.ucfirst(col.getCamelCaseName()+"Previous"));
			} catch(Exception e) {
				e.printStackTrace();
				System.out.println(col);
				throw e;
			}
		} else {
			return beanExpression.accessAttrGetter(((EntityCls) beanExpression.getType()).getAttrByName(col.getCamelCaseName()+"Previous"));
		}
	}

	public boolean hasRelations() {
		return !manyRelations.isEmpty() && !oneRelations.isEmpty() && !oneToManyRelations.isEmpty();
	}
}
