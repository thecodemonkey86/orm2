package cpp.cls.bean;

import java.util.List;

import util.StringUtil;
import model.AbstractRelation;
import model.Column;
import model.Database;
import model.ManyRelation;
import model.OneToManyRelation;
import model.OneRelation;
import model.Table;
import generate.CodeUtil2;
import cpp.Struct;
import cpp.Types;
import cpp.cls.Attr;
import cpp.cls.Cls;
import cpp.cls.Constructor;
import cpp.cls.Destructor;
import cpp.cls.Method;
import cpp.cls.Operator;
import cpp.cls.Param;
import cpp.cls.Type;
import cpp.cls.bean.method.BeanDestructor;
import cpp.cls.bean.method.BeanConstructor;
import cpp.cls.bean.method.MethodAddManyToManyRelatedBean;
import cpp.cls.bean.method.MethodAddManyToManyRelatedBeanInternal;
import cpp.cls.bean.method.MethodAddRelatedBean;
import cpp.cls.bean.method.MethodAddRelatedBeanInternal;
import cpp.cls.bean.method.MethodAddRelatedTableJoins;
import cpp.cls.bean.method.MethodAttrGetter;
import cpp.cls.bean.method.MethodColumnAttrSetter;
import cpp.cls.bean.method.MethodColumnAttrSetNull;
import cpp.cls.bean.method.MethodColumnAttrSetterInternal;
import cpp.cls.bean.method.MethodGetAllSelectFields;
import cpp.cls.bean.method.MethodGetFieldName;
import cpp.cls.bean.method.MethodGetInsertFields;
import cpp.cls.bean.method.MethodGetInsertParams;
import cpp.cls.bean.method.MethodGetInsertValuePlaceholders;
import cpp.cls.bean.method.MethodGetLimitQueryString;
import cpp.cls.bean.method.MethodGetPrimaryKeyColumns;
import cpp.cls.bean.method.MethodGetSelectFields;
import cpp.cls.bean.method.MethodGetTableName;
import cpp.cls.bean.method.MethodGetTableNameAlias;
import cpp.cls.bean.method.MethodGetTableNameInternal;
import cpp.cls.bean.method.MethodGetUpdateCondition;
import cpp.cls.bean.method.MethodGetUpdateConditionParams;
import cpp.cls.bean.method.MethodGetUpdateFields;
import cpp.cls.bean.method.MethodManyAttrGetter;
import cpp.cls.bean.method.MethodOneRelationAttrSetter;
import cpp.cls.bean.method.MethodOneRelationBeanIsNull;
import cpp.cls.bean.method.MethodQHashBean;
import cpp.cls.bean.method.MethodQHashBeanSharedPtr;
import cpp.cls.bean.method.MethodQHashPkStruct;
import cpp.cls.bean.method.MethodRemoveManyToManyRelatedBean;
import cpp.cls.bean.method.MethodSetAutoIncrementId;
import cpp.cls.expression.Expression;
import cpp.cls.expression.StaticAccessExpression;
import cpp.cls.method.MethodAttributeGetter;
import cpp.orm.DatabaseTypeMapper;
import cpp.orm.OrmUtil;

public class BeanCls extends Cls {

//	public static final String attrPkFieldName = "pkFieldName";
//	public static final String attrPkFieldNames = "pkFieldNames";
	
	static Database database;
	static DatabaseTypeMapper mapper;
	public static final String repository = "repository";
	private static String modelPath, repositoryPath;
	
	public static void setModelPath(String modelPath) {
		BeanCls.modelPath = modelPath;
		if(!BeanCls.modelPath.endsWith("/")) {
			BeanCls.modelPath+="/";
		}
	}
	
	public static String getRepositoryPath() {
		return repositoryPath;
	}
	
	public static void setRepositoryPath(String repositoryPath) {
		BeanCls.repositoryPath = repositoryPath;
		if(!BeanCls.repositoryPath.endsWith("/")) {
			BeanCls.repositoryPath+="/";
		}
	}
	
	public static void setTypeMapper(DatabaseTypeMapper mapper) {
		BeanCls.mapper = mapper;
	}
	
	public static void setDatabase(Database database) {
		BeanCls.database = database;
	}
	
	public static Database getDatabase() {
		return database;
	}
		
	public static String getModelPath() {
		return modelPath;
	}
	
	protected Table tbl;
	protected List<OneToManyRelation> oneToManyRelations;
	protected List<OneRelation> oneRelations;
	protected List<ManyRelation> manyRelations;
	
	//protected Struct structPk;
	protected Type pkType;
	protected Struct fetchListHelper;
	
	public Struct getFetchListHelperCls() {
		return fetchListHelper;
	}
	
	private void addAttributes(List<Column> allColumns) {
		addAttr(new RepositoryAttr());
		for(OneRelation r:oneRelations) {
			OneAttr attr = new OneAttr(r);
				addAttr(attr);
				addIncludeHeader(attr.getElementType().getName().toLowerCase());
				addForwardDeclaredClass(attr.getType().getName());
				addMethod(new MethodAttrGetter(attr,true));	
				addMethod(new MethodOneRelationBeanIsNull(r,true));
				addMethod(new MethodOneRelationBeanIsNull(r,false));
//				addMethod(new MethodAttrSetterInternal(this, attr)); 
				addMethod(new MethodOneRelationAttrSetter( this,r, true)); // internal setter
				addMethod(new MethodOneRelationAttrSetter( this,r, false)); // public setter
				if (!r.isPartOfPk()) {
					Attr attrModified = new Attr(Types.Bool, attr.getName()+"Modified");
					addAttr(attrModified);
				}
				
//				for(Column col:r.getDestTable().getPrimaryKey().getColumns()) {
//					addMethod(new MethodManyDelegateGetter(attr,col,r.getDestTable().getUc1stCamelCaseName()));	
//				}
		}
		for(OneToManyRelation r:oneToManyRelations) {
			ManyAttr attr = new ManyAttr(r);
			addAttr(attr);
			addIncludeHeader(attr.getClassType().getIncludeHeader());
			addForwardDeclaredClass(attr.getElementType().getName());
			addMethod(new MethodManyAttrGetter(attr));
			addMethod(new MethodAddRelatedBean(r, new Param(attr.getElementType().toConstRef(), "bean")));
			addMethod(new MethodAddRelatedBeanInternal(r, new Param(attr.getElementType().toConstRef(), "bean")));
		}
		
		for(ManyRelation r:manyRelations) {
			ManyAttr attr = new ManyAttr(r);
			addAttr(attr);
			addIncludeHeader(attr.getClassType().getIncludeHeader());
			addForwardDeclaredClass(attr.getElementType().getName());
			addMethod(new MethodManyAttrGetter(attr));
			Attr attrManyToManyAdded = new Attr(Types.qvector(Types.getRelationForeignPrimaryKeyType(r)) ,attr.getName()+"Added");
			addAttr(attrManyToManyAdded);
			addMethod(new MethodAttributeGetter(attrManyToManyAdded));
			
			Attr attrManyToManyRemoved = new Attr(Types.qvector(Types.getRelationForeignPrimaryKeyType(r)) ,attr.getName()+"Removed");
			addAttr(attrManyToManyRemoved);
			addMethod(new MethodAttributeGetter(attrManyToManyRemoved));
			addMethod(new MethodAddManyToManyRelatedBean(r, new Param(attr.getElementType().toConstRef(), "bean")));
			addMethod(new MethodAddManyToManyRelatedBeanInternal(r, new Param(attr.getElementType().toConstRef(), "bean")));
			
			addMethod(new MethodRemoveManyToManyRelatedBean(r, new Param(attr.getElementType().toConstRef(), "bean")));
		}
		
		
//		structPk.setScope(name);
		for(Column col:allColumns) {
						
			if (!col.hasOneRelation()
					
					) {
				Attr attr = new Attr(BeanCls.getDatabaseMapper().getTypeFromDbDataType(col.getDbType(), col.isNullable()), col.getCamelCaseName());
				addAttr(attr);
				addMethod(new MethodAttrGetter(attr,false));	
				addMethod(new MethodColumnAttrSetter(this,col,attr));
				addMethod(new MethodColumnAttrSetterInternal(this,col,attr));
				if (col.isNullable()) {
					addMethod(new MethodColumnAttrSetNull(this, col, attr, true));
					addMethod(new MethodColumnAttrSetNull(this, col, attr, false));
				}
				
				addMethod(new MethodGetFieldName(col));
				addMethod(new MethodGetFieldName(col, true));
				if (!col.isPartOfPk()) {
					
					Attr attrModified = new Attr(Types.Bool, col.getCamelCaseName()+"Modified");
					addAttr(attrModified);
				}
			} else {
				Attr attr = new Attr(BeanCls.getDatabaseMapper().getTypeFromDbDataType(col.getDbType(), col.isNullable()), col.getCamelCaseName());
				addAttr(attr);
				addMethod(new MethodAttrGetter(attr,false));	
			}
			
			if (col.isAutoIncrement()) {
				addMethod(new MethodSetAutoIncrementId());
			}
//			if (col.isPartOfPk()) {
//				
//			}
			
		}
		if (tbl.getPrimaryKey().isMultiColumn()) {
			pkType = new Struct("Pk"+tbl.getUc1stCamelCaseName()); 
			for(Column col: tbl.getPrimaryKey().getColumns()) {
				Attr attrPrev = new Attr(BeanCls.getDatabaseMapper().getTypeFromDbDataType(col.getDbType(), col.isNullable()), col.getCamelCaseName()+"Previous");
				addAttr(attrPrev);
				getStructPk().addAttr(!col.hasOneRelation() ? getAttrByName(col.getCamelCaseName() ) : new Attr(BeanCls.getDatabaseMapper().columnToType(col), col.getCamelCaseName()));
			}
		} else {
			if (tbl.getPrimaryKey().getColumns().size()==0) {
				throw new RuntimeException("pk info missing for "+name);
			}
			Column col= tbl.getPrimaryKey().getFirstColumn();
			Attr attrPrev = new Attr(BeanCls.getDatabaseMapper().getTypeFromDbDataType(col.getDbType(), col.isNullable()), col.getCamelCaseName()+"Previous");
			addAttr(attrPrev);
			pkType =BeanCls.getDatabaseMapper().columnToType(col);
		}
		
		
//		addAttr(new RepositoryAttr());
	}
	
	public BeanCls(Table tbl,List<OneToManyRelation> manyRelations,List<OneRelation> oneRelations, List<ManyRelation> manyToManyRelations) {
		super(CodeUtil2.uc1stCamelCase(tbl.getName()));
		this.tbl = tbl;
		this.oneToManyRelations= manyRelations;
		this.oneRelations = oneRelations;
		this.manyRelations = manyToManyRelations;
	}
	
	/*public void breakPointerCircles() {
		if (getName().equals("Track")) {
			System.out.println();
		}
		for(Relation r:oneRelations) {
			Attr a= getAttr( new OneAttr(r));
			if (a!=null&& a.getType() instanceof SharedPtr) {
				SharedPtr sp = (SharedPtr) a.getType();
				for(Attr ra: ((BeanCls)sp.getElementType()).attrs) {
					if (ra.getType() instanceof ClsQVector) {
						ClsQVector v=(ClsQVector) ra.getType();
						if (((SharedPtr) v.getElementType()).getElementType() == this) {
							sp.setWeak();
							break;
						}
					}
					
					
				}
			}
		}
	}*/
	
	public Constructor getConstructor() {
		return super.getConstructors().get(0);
	}
	
	public void addDeclarations() {
		Constructor c=new BeanConstructor(tbl.getPrimaryKey().isAutoIncrement(),tbl.getColumnsWithoutPrimaryKey());
		addSuperclass(Types.BaseBean);
		addConstructor(c); 
		Destructor d = new BeanDestructor(this);
		setDestructor(d);
		
	//	addPreprocessorInstruction("#define " + getName()+ " "+CodeUtil2.uc1stCamelCase(tbl.getName()));
		addIncludeHeader("basebean");
		addIncludeLib(Types.QString);
		addIncludeLib(Types.QVariant);
		addIncludeLib(Types.QDate);
		addIncludeLib("memory");
		addIncludeHeader("nullable");
		
//		Attr aTableName = new Attr(Attr.Public, Types.ConstCharPtr, "TABLENAME",constCharPtr(tbl.getName()),true);
//		addAttr(aTableName);
		
		addMethod(new MethodGetTableName());
		addMethod(new MethodGetTableNameAlias());
		addMethod(new MethodGetTableNameInternal());
		addIncludeHeader("beanquery");
		addIncludeHeader(repositoryPath + "beanrepository");
		addForwardDeclaredClass(Types.BeanRepository.getName());
		addIncludeHeader("util/orderedset");
		addAttributes(tbl.getAllColumns());
		addForwardDeclaredClass(getName());
		List<Column> cols = tbl.getColumns(!tbl.getPrimaryKey().isAutoIncrement());
		List<Column> allCols = tbl.getColumns(true);
		addMethod(new MethodGetInsertFields(cols));
		addMethod(new MethodGetInsertValuePlaceholders(cols.size()));
		addMethod(new MethodGetInsertParams(cols));
		addMethod(new MethodGetUpdateFields(tbl.getColumnsWithoutPrimaryKey(),tbl.getPrimaryKey()));
		addMethod(new MethodGetUpdateConditionParams(tbl.getPrimaryKey()));
		addMethod(new MethodGetUpdateCondition(tbl.getPrimaryKey()));
//		addMethod(new MethodGetById(oneRelations,manyRelations, tbl, this));
		addMethod(new MethodGetSelectFields(allCols));
		addMethod(new MethodGetAllSelectFields(allCols));
//		addMethod(new MethodGetFromRecordStatic(allCols,this));
//		addMethod(new MethodFetchList(oneRelations, manyRelations, this, tbl.getPrimaryKey()));
//		addMethod(new MethodCreateQuery(this));
		addMethod(new MethodAddRelatedTableJoins(this));
		//addMethod(new MethodBeanLoad(oneRelations, oneToManyRelations,manyRelations, tbl));
		addMethod(new MethodGetPrimaryKeyColumns(tbl.getPrimaryKey()));
		addMethod(new MethodGetLimitQueryString(tbl.getPrimaryKey()));
		//addMethod(new MethodLoad2Levels(oneRelations, oneToManyRelations,manyRelations, tbl));
		
//		if (manyRelations.size()>0) {
//			addMethod(new MethodBeanSave(true));
//			addMethod(new MethodBeanSave(false));
//		}
//		addMethod(new LibMethod(new ClsSql(), "sqlCon"));
//		addMethod(new MethodCreateNew(this));
//		addMethod(new MethodLoadCollection(oneRelations, manyRelations, this, tbl.getPrimaryKey()));
		if (tbl.getPrimaryKey().isMultiColumn()) {
			addNonMemberMethod(new MethodQHashPkStruct(getStructPk(), tbl.getPrimaryKey()));
			
			addNonMemberOperator(new StructPkEqOperator(getStructPk()));
//			BeanHashFunctions.instance.addMethod(new MethodQHashPkStruct(structPk, tbl.getPrimaryKey()));
//			BeanHashFunctions.instance.addMethod(new MethodQHashBean(this, tbl.getPrimaryKey()));
//			BeanHashFunctions.instance.addIncludeHeader(getName().toLowerCase());
//			BeanHashFunctions.instance.addOperator(new StructPkEqOperator(structPk));
//			addIncludeHeader("beanhash");
		} else {
			System.out.println();
		}
		addOperator(new BeanEqualsOperator(this, tbl.getPrimaryKey()));
		addOperator(new BeanSharedPtrEqualsOperator(this, tbl.getPrimaryKey()));
		addNonMemberMethod(new MethodQHashBean(this, tbl.getPrimaryKey()));
		addNonMemberMethod(new MethodQHashBeanSharedPtr(this, tbl.getPrimaryKey()));
		addNonMemberOperator(new NonMemberOperatorBeanEquals(this, tbl.getPrimaryKey()));
//		addForwardDeclaredClass(Types.BeanRepository.getName());
	}
	
	@Override
	public void addOperator(Operator op) {
		super.addOperator(op);
		op.setParent(this);
	}
	
	@Override
	public void addMethodImplementations() {
		
//		if (!manyRelations.isEmpty()) {
			fetchListHelper = new FetchListHelperClass(this);
//		}
		
		super.addMethodImplementations();
		if (nonMemberMethods !=null) {
			for(Method m : nonMemberMethods) {
				m.addImplementation();
			}
		}
		if (nonMemberOperators!=null) {
			for(Operator o : nonMemberOperators) {
				o.addImplementation();
			}
		}
//		if (fetchListHelper!=null)
//			fetchListHelper.addMethodImplementations();
		
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof BeanCls && name.equals(((BeanCls)obj).name);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	
	
	@Override
	protected void addHeaderCodeBeforeClassDefinition(StringBuilder sb) {
		if (tbl.getPrimaryKey().isMultiColumn()) {
			sb.append(getStructPk().toSourceString()).append('\n');
		}
//		if (fetchListHelper!=null) {
//			sb.append(fetchListHelper.toHeaderString()).append('\n').append('\n');
//		}
		if (fetchListHelper!=null) {
			sb.append(fetchListHelper.toSourceString()).append('\n').append('\n');
		}
	}

	@Override
	protected void addBeforeSourceCode(StringBuilder sb) {
		super.addBeforeSourceCode(sb);
//		if (structPk!=null) {
//			sb.append(structPk.toSourceString()).append('\n');
//		}
		
//		if (fetchListHelper!=null) {
//			sb.append(fetchListHelper.toSourceString()).append('\n').append('\n');
//		}
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
	
	public Struct getStructPk() {
		return (Struct)pkType;
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
	
	public Expression accessThisAttrGetterByColumn(Column col) {
		if (col.hasOneRelation()) {
			try{
			Attr attr = getOneRelationAttr(col.getOneRelation());
			return _this().accessAttr(attr).callMethod("get"+col.getOneRelationMappedColumn().getUc1stCamelCaseName());
			} catch(Exception e) {
				e.printStackTrace();
				System.out.println(col);
				throw e;
			}
		} else {
			return _this().accessAttr(getAttrByName(col.getCamelCaseName()));
		}
	}
	
	public static Expression accessThisAttrGetterByColumn(Expression expression, Column col) {
		if (col.hasOneRelation()) {
			try{
			Expression attr = expression.callAttrGetter(OrmUtil.getOneRelationDestAttrName(col.getOneRelation()));
			return attr.callMethod("get"+col.getOneRelationMappedColumn().getUc1stCamelCaseName());
			} catch(Exception e) {
				e.printStackTrace();
				System.out.println(col);
				throw e;
			}
		} else {
			return expression.callAttrGetter(col.getCamelCaseName());
		}
	}
	
	// TODO relation
	private static String getAttrGetterMethodNameByColumn(Column col) {
//		if (colPk.hasOneRelation()) {
//			try{
//			return "get"+colPk.getOneRelationMappedColumn().getUc1stCamelCaseName();
//			} catch(Exception e) {
//				e.printStackTrace();
//				System.out.println(colPk);
//				throw e;
//			}
//		} else {
			return "get"+col.getUc1stCamelCaseName();
//		}
	}
	
	// TODO relation
	public static String getAccessMethodNameByColumn(Column col) {
		
//		if (colPk.hasOneRelation()) {
//			try{
//			return "get"+colPk.getOneRelationMappedColumn().getUc1stCamelCaseName();
//			} catch(Exception e) {
//				e.printStackTrace();
//				System.out.println(colPk);
//				throw e;
//			}
//		} else {
			return "get"+col.getUc1stCamelCaseName();
//		}
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
		return (RepositoryAttr) getAttrByName(repository);
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

	public static DatabaseTypeMapper getDatabaseMapper() {
		return mapper;
	}
}
