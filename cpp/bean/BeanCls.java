package cpp.bean;

import java.util.ArrayList;
import java.util.List;

import codegen.CodeUtil;
import util.CodeUtil2;
import util.StringUtil;
import cpp.Types;
import cpp.CoreTypes;
import cpp.bean.method.BeanConstructor;
import cpp.bean.method.BeanDestructor;
import cpp.bean.method.MethodAddInsertParamForRawExpression;
import cpp.bean.method.MethodAddManyToManyRelatedBean;
import cpp.bean.method.MethodAddManyToManyRelatedBeanInternal;
import cpp.bean.method.MethodAddRelatedBean;
import cpp.bean.method.MethodAddRelatedBeanInternal;
import cpp.bean.method.MethodAddRelatedTableJoins;
import cpp.bean.method.MethodAttrGetter;
import cpp.bean.method.MethodColumnAttrSetNull;
import cpp.bean.method.MethodColumnAttrSetter;
import cpp.bean.method.MethodColumnAttrSetterInternal;
import cpp.bean.method.MethodCopyFields;
import cpp.bean.method.MethodGetAllSelectFields;
import cpp.bean.method.MethodGetFieldName;
import cpp.bean.method.MethodGetFieldNameAlias;
import cpp.bean.method.MethodGetInsertFields;
import cpp.bean.method.MethodGetInsertParams;
import cpp.bean.method.MethodGetInsertValuePlaceholders;
import cpp.bean.method.MethodGetLastItem;
import cpp.bean.method.MethodGetLimitQueryString;
import cpp.bean.method.MethodGetManyRelatedAtIndex;
import cpp.bean.method.MethodGetManyRelatedCount;
import cpp.bean.method.MethodGetPrimaryKeyColumns;
import cpp.bean.method.MethodGetSelectFields;
import cpp.bean.method.MethodGetTableName;
import cpp.bean.method.MethodGetTableNameAlias;
import cpp.bean.method.MethodGetTableNameInternal;
import cpp.bean.method.MethodGetUpdateCondition;
import cpp.bean.method.MethodGetUpdateConditionParams;
import cpp.bean.method.MethodGetUpdateFields;
import cpp.bean.method.MethodIsNullOrEmpty;
import cpp.bean.method.MethodManyAttrGetter;
import cpp.bean.method.MethodOneRelationAttrSetter;
import cpp.bean.method.MethodOneRelationBeanIsNull;
import cpp.bean.method.MethodQHashBean;
import cpp.bean.method.MethodQHashBeanSharedPtr;
import cpp.bean.method.MethodQHashPkStruct;
import cpp.bean.method.MethodRemoveAllManyRelatedBeans;
import cpp.bean.method.MethodRemoveManyToManyRelatedBean;
import cpp.bean.method.MethodReplaceAllManyRelatedBeans;
import cpp.bean.method.MethodSetAutoIncrementId;
import cpp.bean.method.MethodToggleAddRemoveRelatedBean;
import cpp.bean.method.MethodUnload;
import cpp.core.Attr;
import cpp.core.Cls;
import cpp.core.Constructor;
import cpp.core.Destructor;
import cpp.core.Method;
import cpp.core.Operator;
import cpp.core.Param;
import cpp.core.Struct;
import cpp.core.Type;
import cpp.core.expression.Expression;
import cpp.core.expression.StaticAccessExpression;
import cpp.core.method.MethodAttributeGetter;
import cpp.core.method.MethodAttributeSetter;
import cpp.orm.DatabaseTypeMapper;
import cpp.orm.OrmUtil;
import database.Database;
import database.column.Column;
import database.relation.AbstractRelation;
import database.relation.IManyRelation;
import database.relation.ManyRelation;
import database.relation.OneRelation;
import database.relation.OneToManyRelation;
import database.table.Table;

public class BeanCls extends Cls {

	private static final String BEAN_PARAM_NAME = "bean";
	public static final String BEGIN_CUSTOM_CLASS_MEMBERS = "/*BEGIN_CUSTOM_CLASS_MEMBERS*/";
	public static final String END_CUSTOM_CLASS_MEMBERS = "/*END_CUSTOM_CLASS_MEMBERS*/";
	public static final String BEGIN_CUSTOM_PREPROCESSOR = "/*BEGIN_CUSTOM_PREPROCESSOR*/";
	public static final String END_CUSTOM_PREPROCESSOR = "/*END_CUSTOM_PREPROCESSOR*/";
	public static final String APILEVEL = "2.0";
	
	static Database database;
	static DatabaseTypeMapper mapper;
	public static final String repository = "repository";
	private static String modelPath, repositoryPath;
	
	private ArrayList<String> customHeaderCode, customSourceCode, customPreprocessorCode;
	
	
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
	
	public void addCustomHeaderCode(String code) {
		if(this.customHeaderCode == null) {
			this.customHeaderCode = new ArrayList<>();
		}
		this.customHeaderCode.add(code);
	}
	
	public void addCustomSourceCode(String code) {
		if(this.customSourceCode == null) {
			this.customSourceCode = new ArrayList<>();
		}
		this.customSourceCode.add(code);
	}
		
	public void addCustomPreprocessorCode(String code) {
		if(this.customPreprocessorCode == null) {
			this.customPreprocessorCode = new ArrayList<>();
		}
		this.customPreprocessorCode.add(code);
	}
	private void addAttributes(List<Column> allColumns) {
		addAttr(new RepositoryAttr());
		for(OneRelation r:oneRelations) {
			OneAttr attr = new OneAttr(r);
				addAttr(attr);
				addIncludeHeader(attr.getElementType().getName().toLowerCase());
				addForwardDeclaredClass(attr.getClassType());
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
			Attr attrManyToManyAdded = new Attr(Types.qvector(Types.getRelationForeignPrimaryKeyType(r)) ,attr.getName()+"Added");
			addAttr(attrManyToManyAdded);
			addMethod(new MethodAttributeGetter(attrManyToManyAdded));
			
			Attr attrManyToManyRemoved = new Attr(Types.qvector(Types.getRelationForeignPrimaryKeyType(r)) ,attr.getName()+"Removed");
			addAttr(attrManyToManyRemoved);
			addIncludeHeader(attr.getClassType().getIncludeHeader());
			addForwardDeclaredClass((Cls) attr.getElementType());
			addMethod(new MethodManyAttrGetter(attr));
			addMethod(new MethodAddRelatedBean(r, new Param(attr.getElementType().toConstRef(), BEAN_PARAM_NAME)));
			//addMethod(new MethodAddRelatedBean(r, new Param(Types.qvector(attr.getElementType()).toConstRef(), BEAN_PARAM_NAME)));
			addMethod(new MethodAddRelatedBeanInternal(r, new Param(attr.getElementType().toConstRef(), BEAN_PARAM_NAME)));
			addMethod(new MethodAddRelatedBeanInternal(r, new Param(Types.qvector(attr.getElementType()).toConstRef(), BEAN_PARAM_NAME)));
			addMethod(new MethodGetManyRelatedAtIndex(attr, r));
			addMethod(new MethodGetManyRelatedCount(attr, r));
			addMethod(new MethodRemoveAllManyRelatedBeans(r));
			addMethod(new MethodReplaceAllManyRelatedBeans(r));
			addMethod(new MethodGetLastItem(attr.getElementType(), r));
		}
		
		for(ManyRelation r:manyRelations) {
			ManyAttr attr = new ManyAttr(r);
			addAttr(attr);
			addIncludeHeader(attr.getClassType().getIncludeHeader());
			addForwardDeclaredClass((Cls) attr.getElementType());
			addMethod(new MethodManyAttrGetter(attr));
			Attr attrManyToManyAdded = new Attr(Types.qvector(Types.getRelationForeignPrimaryKeyType(r)) ,attr.getName()+"Added");
			addAttr(attrManyToManyAdded);
			addMethod(new MethodAttributeGetter(attrManyToManyAdded));
			
			Attr attrManyToManyRemoved = new Attr(Types.qvector(Types.getRelationForeignPrimaryKeyType(r)) ,attr.getName()+"Removed");
			addAttr(attrManyToManyRemoved);
			addMethod(new MethodAttributeGetter(attrManyToManyRemoved));
			addMethod(new MethodAddManyToManyRelatedBean(r, new Param(attr.getElementType().toConstRef(), BEAN_PARAM_NAME)));
			//addMethod(new MethodAddManyToManyRelatedBean(r, new Param(Types.qvector(attr.getElementType()).toConstRef(), BEAN_PARAM_NAME)));
			addMethod(new MethodAddManyToManyRelatedBeanInternal(r, new Param(attr.getElementType().toConstRef(), BEAN_PARAM_NAME)));
			addMethod(new MethodAddManyToManyRelatedBeanInternal(r, new Param(Types.qvector(attr.getElementType()).toConstRef(), BEAN_PARAM_NAME)));
			
			addMethod(new MethodRemoveManyToManyRelatedBean(r, new Param(attr.getElementType().toConstRef(), BEAN_PARAM_NAME)));
			addMethod(new MethodRemoveAllManyRelatedBeans(r));
		}
		
		Type nullstring = Types.nullable(Types.QString);
//		structPk.setScope(name);
		for(Column col:allColumns) {
						
			if (!col.hasOneRelation()
					
					) {
				Attr attr = new Attr(BeanCls.getDatabaseMapper().getTypeFromDbDataType(col.getDbType(), col.isNullable()), col.getCamelCaseName());
				addAttr(attr);
				
				addMethod(new MethodAttrGetter(attr,false));	
				addMethod(new MethodColumnAttrSetter(col,attr));
				addMethod(new MethodColumnAttrSetterInternal(col,attr));
				if (col.isNullable()) {
					if(attr.getType().equals(nullstring))
						addMethod(new MethodIsNullOrEmpty(col));
					addMethod(new MethodColumnAttrSetNull(this, col, attr, true));
					addMethod(new MethodColumnAttrSetNull(this, col, attr, false));
				}
				
				addMethod(new MethodGetFieldName(col));
				addMethod(new MethodGetFieldNameAlias(col, true));
				addMethod(new MethodGetFieldNameAlias(col, false));
				addMethod(new MethodGetFieldName(col, true));
				if (!col.isPartOfPk()) {
					
					Attr attrModified = new Attr(Types.Bool, col.getCamelCaseName()+"Modified");
					addAttr(attrModified);
				}
				if(col.isRawValueEnabled()) {
					Attr attrInsertExpression = new Attr(Attr.Protected, Types.QString,"insertExpression"+col.getUc1stCamelCaseName(), null,false);
					addAttr(attrInsertExpression);
					addMethod(new MethodAttributeSetter(attrInsertExpression));
					Attr attrInsertParams = new Attr(Attr.Protected, Types.QVariantList,"insertParamsForRawExpression"+col.getUc1stCamelCaseName(),null,false);
					addAttr(attrInsertParams);
					addMethod(new MethodAddInsertParamForRawExpression(col));
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
		
		for(OneToManyRelation r:oneToManyRelations) {
			addMethod(new MethodToggleAddRemoveRelatedBean(r));
		}
		for(ManyRelation r:manyRelations) {
			addMethod(new MethodToggleAddRemoveRelatedBean(r));
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
		addIncludeLib(CoreTypes.QVariant);
		addIncludeLib(Types.QDate);
		addIncludeLib("memory");
		addIncludeHeader("nullable");
		
//		Attr aTableName = new Attr(Attr.Public, Types.ConstCharPtr, "TABLENAME",constCharPtr(tbl.getName()),true);
//		addAttr(aTableName);
		
		addMethod(new MethodGetTableName());
		addMethod(new MethodGetTableNameAlias());
		addMethod(new MethodGetTableNameInternal());
		//addIncludeHeader("beanquery");
		addIncludeHeader(repositoryPath + "beanrepository");
		addForwardDeclaredClass(Types.BeanRepository);
		addIncludeHeader(Types.orderedSet(null).getHeaderInclude());
		addAttributes(tbl.getAllColumns());
		addForwardDeclaredClass(this);
		List<Column> cols = tbl.getColumns(!tbl.getPrimaryKey().isAutoIncrement());
		List<Column> allCols = tbl.getColumns(true);
		addMethod(new MethodGetInsertFields(cols));
		addMethod(new MethodGetInsertValuePlaceholders(tbl));
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
		if(hasRelations())
			addMethod(new MethodAddRelatedTableJoins(this));
		//addMethod(new MethodBeanLoad(oneRelations, oneToManyRelations,manyRelations, tbl));
		addMethod(new MethodGetPrimaryKeyColumns(tbl.getPrimaryKey()));
		addMethod(new MethodGetLimitQueryString(tbl.getPrimaryKey()));
		addMethod(new MethodUnload(oneRelations, oneToManyRelations, manyRelations));
		addMethod(new MethodCopyFields(this));
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
	protected void addHeaderCodeBeforeClassDeclaration(StringBuilder sb) {
		super.addHeaderCodeBeforeClassDeclaration(sb);
		sb.append('\n').append(BEGIN_CUSTOM_PREPROCESSOR).append('\n');
		if(customPreprocessorCode != null) {
			
			for(String cc : customPreprocessorCode) {
				sb.append(cc.trim());
			}
			
		}
		sb.append('\n').append(END_CUSTOM_PREPROCESSOR).append('\n');
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
	protected void addAfterSourceCode(StringBuilder sb) {
		sb.append('\n').append(BEGIN_CUSTOM_CLASS_MEMBERS).append('\n');
		if(customSourceCode != null) {
			
			for(String cc : customSourceCode) {
				sb.append(cc.trim());
			}
			
		}
		sb.append('\n').append(END_CUSTOM_CLASS_MEMBERS).append('\n');
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
	//private static String getAttrGetterMethodNameByColumn(Column col) {
//		if (colPk.hasOneRelation()) {
//			try{
//			return "get"+colPk.getOneRelationMappedColumn().getUc1stCamelCaseName();
//			} catch(Exception e) {
//				e.printStackTrace();
//				System.out.println(colPk);
//				throw e;
//			}
//		} else {
		//	return "get"+col.getUc1stCamelCaseName();
//		}
	//}
	
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
	
	public List<IManyRelation> getAllManyRelations() {
		List<IManyRelation> list = new ArrayList<>(manyRelations);
		list.addAll(oneToManyRelations);
		return list;
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
	
	@Override
	protected void addClassHeaderCode(StringBuilder sb) {
		super.addClassHeaderCode(sb);
		sb.append('\n').append(BEGIN_CUSTOM_CLASS_MEMBERS).append('\n');
		if(customHeaderCode != null) {
			for(String cc : customHeaderCode) {
				sb.append(cc.trim());
			}
		}
		sb.append('\n').append(END_CUSTOM_CLASS_MEMBERS).append('\n');

	}
	
	
	public static String getRelatedBeanMethodName(AbstractRelation r) {
		 if (r instanceof OneToManyRelation) {
			return "add"+StringUtil.ucfirst(OrmUtil.getOneToManyRelationDestAttrNameSingular((OneToManyRelation) r))+"Internal";
		} else  if (r instanceof ManyRelation) {
			return "add"+StringUtil.ucfirst(OrmUtil.getManyRelationDestAttrNameSingular((ManyRelation) r))+"Internal";
		} else {
			throw new IllegalArgumentException();
		}
	}

	public static DatabaseTypeMapper getDatabaseMapper() {
		return mapper;
	}

	public boolean hasRelations() {
		return oneRelations.size() > 0 || oneToManyRelations.size() > 0 || manyRelations.size()>0;
	}

	public void setPrimaryKeyType() {
		if (tbl.getPrimaryKey().isMultiColumn()) {
			pkType = new Struct("Pk"+tbl.getUc1stCamelCaseName()); 
			for(Column col: tbl.getPrimaryKey().getColumns()) {
				Attr attrPrev = new Attr(BeanCls.getDatabaseMapper().getTypeFromDbDataType(col.getDbType(), col.isNullable()), col.getCamelCaseName()+"Previous");
				addAttr(attrPrev);
				getStructPk().addAttr(  new Attr(BeanCls.getDatabaseMapper().columnToType(col), col.getCamelCaseName()));
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
		
	}
	
	@Override
	protected void addBeforeHeader(StringBuilder sb) {
		CodeUtil.writeLine(sb, "/*Dies ist eine automatisch generierte Datei des C++ ORM Systems https://github.com/thecodemonkey86/orm2*/");
		CodeUtil.writeLine(sb, "/*Generator (Java-basiert): https://github.com/thecodemonkey86/orm2*/");
		CodeUtil.writeLine(sb, "/*Abhängigkeiten (C++ libraries): https://github.com/thecodemonkey86/libcpporm, https://github.com/thecodemonkey86/QtCommonLibs2, https://github.com/thecodemonkey86/SqlUtil2*/");
		CodeUtil.writeLine(sb, "/*API Level " + APILEVEL + "*/\n");
		
	}
}
