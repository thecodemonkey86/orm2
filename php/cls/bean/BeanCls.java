package php.cls.bean;

import java.util.List;

import php.Types;
import php.cls.Attr;
import php.cls.PhpCls;
import php.cls.Type;
import php.cls.Param;
import php.cls.bean.method.BeanConstructor;
import php.cls.bean.method.BeanEqualsMethod;
import php.cls.bean.method.MethodAddManyToManyRelatedBean;
import php.cls.bean.method.MethodAddManyToManyRelatedBeanInternal;
import php.cls.bean.method.MethodAddRelatedBean;
import php.cls.bean.method.MethodAddRelatedBeanInternal;
import php.cls.bean.method.MethodAttrGetter;
import php.cls.bean.method.MethodColumnAttrSetNull;
import php.cls.bean.method.MethodColumnAttrSetter;
import php.cls.bean.method.MethodColumnAttrSetterInternal;
import php.cls.bean.method.MethodCreateNew;
import php.cls.bean.method.MethodGetFieldName;
import php.cls.bean.method.MethodHasAddedManyToMany;
import php.cls.bean.method.MethodHasRemovedManyToMany;
import php.cls.bean.method.MethodHasUpdate;
import php.cls.bean.method.MethodIsModified;
import php.cls.bean.method.MethodManyAttrGetter;
import php.cls.bean.method.MethodOneRelationAttrGetter;
import php.cls.bean.method.MethodOneRelationAttrSetter;
import php.cls.bean.method.MethodOneRelationBeanIsNull;
import php.cls.bean.method.MethodRemoveManyToManyRelatedBean;
import php.cls.bean.method.MethodSetAutoIncrementId;
import php.cls.bean.pk.PkMultiColumnType;
import php.cls.bean.repo.helper.FetchListHelperClass;
import php.cls.expression.Expression;
import php.cls.expression.StaticAccessExpression;
import php.cls.method.MethodAttributeGetter;
import php.orm.DatabaseTypeMapper;
import php.orm.OrmUtil;
import util.StringUtil;
import model.AbstractRelation;
import model.Column;
import model.Database;
import model.ManyRelation;
import model.OneToManyRelation;
import model.OneRelation;
import model.Table;
import generate.CodeUtil2;

public class BeanCls extends PhpCls {


	static Database database;
	static DatabaseTypeMapper typeMapper;
	static PhpCls sqlQueryCls;

	protected static String beanNamespace;
	protected static String beanRepoNamespace;

	public static void setBeanRepoNamespace(String beanRepoClsNamespace) {
		BeanCls.beanRepoNamespace = beanRepoClsNamespace;
	}

	public static void setSqlQueryCls(PhpCls sqlQueryCls) {
		BeanCls.sqlQueryCls = sqlQueryCls;
	}

	public static PhpCls getSqlQueryCls() {
		return sqlQueryCls;
	}

	public static void setBeanNamespace(String beanPackage) {
		BeanCls.beanNamespace = beanPackage;
	}

	public static void setDatabase(Database database) {
		BeanCls.database = database;
	}

	public static Database getDatabase() {
		return database;
	}

	public static DatabaseTypeMapper getTypeMapper() {
		return typeMapper;
	}

	public static void setTypeMapper(DatabaseTypeMapper typeMapper) {
		BeanCls.typeMapper = typeMapper;
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
			addMethod(new MethodAddRelatedBean(r, new Param(attr.getElementType(), "bean")));
			addMethod(new MethodAddRelatedBeanInternal(r, new Param(attr.getElementType(), "bean")));
		}

		for(ManyRelation r:manyRelations) {
			ManyAttr attr = new ManyAttr(r);
			addAttr(attr);
			addMethod(new MethodManyAttrGetter(attr));
			
			Attr manyRelAdded = new Attr(Types.array(Types.Mixed) ,attr.getName()+"Added");
			addAttr(manyRelAdded);
			addMethod(new MethodAttributeGetter(manyRelAdded));
			Attr manyRelRemoved = new Attr(Types.array(Types.Mixed) ,attr.getName()+"Removed");
			addAttr(manyRelRemoved);
			addMethod(new MethodAttributeGetter(manyRelRemoved));
			
			addMethod(new MethodAddManyToManyRelatedBean(r, new Param(attr.getElementType(), "bean")));
			addMethod(new MethodAddManyToManyRelatedBeanInternal(r, new Param(attr.getElementType(), "bean")));

			addMethod(new MethodRemoveManyToManyRelatedBean(r, new Param(attr.getElementType(), "bean")));
			addMethod(new MethodHasRemovedManyToMany(r));
			addMethod(new MethodHasAddedManyToMany(r));
		}


		for(Column col:allColumns) {

			if (!col.hasOneRelation()

					) {
				Attr attr = new Attr(BeanCls.getTypeMapper().getTypeFromDbDataType(col.getDbType()), col.getCamelCaseName());
				addAttr(attr);
				addMethod(new MethodAttrGetter(attr,false));	
				addMethod(new MethodColumnAttrSetter(this,col,attr));
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
			}
			else {
				Attr attr = new Attr(BeanCls.getTypeMapper().getTypeFromDbDataType(col.getDbType()), col.getCamelCaseName());
				addAttr(attr);
				addMethod(new MethodAttrGetter(attr,false));	
			}

			if (col.isAutoIncrement()) {
				addMethod(new MethodSetAutoIncrementId());
			}

		}
		if (tbl.getPrimaryKey().isMultiColumn()) {
			pkType = new PkMultiColumnType("Pk"+tbl.getUc1stCamelCaseName(),beanNamespace+"\\Pk", tbl); 
			for(Column col: tbl.getPrimaryKey().getColumns()) {
				Attr attrPrev = new Attr(BeanCls.getTypeMapper().getTypeFromDbDataType(col.getDbType()), col.getCamelCaseName()+"Previous");
				addAttr(attrPrev);
				addMethod(new MethodAttrGetter(attrPrev, false));
			}
		} else {
			if (tbl.getPrimaryKey().getColumns().size()==0) {
				throw new RuntimeException("pk info missing for "+getName());
			}
			Column col= tbl.getPrimaryKey().getFirstColumn();
			Attr attrPrev = new Attr(BeanCls.getTypeMapper().getTypeFromDbDataType(col.getDbType()), col.getCamelCaseName()+"Previous");
			addAttr(attrPrev);
			addMethod(new MethodAttrGetter(attrPrev, false));
			pkType =typeMapper.columnToType(col);
		}
	}

	public BeanCls(Table tbl,List<OneToManyRelation> manyRelations,List<OneRelation> oneRelations, List<ManyRelation> manyToManyRelations) {
		super(CodeUtil2.uc1stCamelCase(tbl.getName()), beanNamespace);
		this.tbl = tbl;
		this.oneToManyRelations= manyRelations;
		this.oneRelations = oneRelations;
		this.manyRelations = manyToManyRelations;
	}



	public void addDeclarations() {
		superclass = Types.BaseBean;
		
		setConstructor(new BeanConstructor(tbl.getPrimaryKey().isAutoIncrement(),tbl.getColumnsWithoutPrimaryKey())); 


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
		addMethod(new BeanEqualsMethod(this, tbl.getPrimaryKey()));
		addMethod(new MethodHasUpdate());
		fetchListHelper = new FetchListHelperClass(this, beanRepoNamespace);
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
		return obj instanceof BeanCls && getName().equals(((BeanCls)obj).getName());
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
				Attr attr = ((BeanCls) beanExpression.getType()).getOneRelationAttr(col.getOneRelation());
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
			return beanExpression.accessAttrGetter(((BeanCls) beanExpression.getType()).getAttrByName(col.getCamelCaseName()));
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
				Attr attr = ((BeanCls) beanExpression.getType()).getOneRelationAttr(col.getOneRelation());
				return beanExpression.accessAttrGetter(attr);
			} catch(Exception e) {
				e.printStackTrace();
				System.out.println(col);
				throw e;
			}
		} else {
			return beanExpression.accessAttrGetter(((BeanCls) beanExpression.getType()).getAttrByName(col.getCamelCaseName()));
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

	public static String getAddRelatedBeanMethodName(AbstractRelation r) {
		if (r instanceof OneToManyRelation) {
			return "add"+StringUtil.ucfirst(OrmUtil.getManyRelationDestAttrNameSingular((OneToManyRelation) r))+"Internal";
		} else  if (r instanceof ManyRelation) {
			return "add"+StringUtil.ucfirst(OrmUtil.getManyRelationDestAttrNameSingular((ManyRelation) r))+"Internal";
		} else {
			throw new IllegalArgumentException();
		}
	}

	
	public static Expression getPkExpression(Expression e, Column colPk) {
		BeanCls cls= (BeanCls) e.getType();
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
			return beanExpression.accessAttrGetter(((BeanCls) beanExpression.getType()).getAttrByName(col.getCamelCaseName()+"Previous"));
		}
	}
	
	public Type getPkType() {
		return pkType;
	}
}
