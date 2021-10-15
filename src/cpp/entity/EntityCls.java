package cpp.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import codegen.CodeUtil;
import config.cpp.CppOrmConfig;
import util.CodeUtil2;
import util.StringUtil;
import cpp.Types;
import cpp.CoreTypes;
import cpp.core.Attr;
import cpp.core.Cls;
import cpp.core.Constructor;
import cpp.core.Destructor;
import cpp.core.Method;
import cpp.core.Operator;
import cpp.core.Param;
import cpp.core.Struct;
import cpp.core.TplCls;
import cpp.core.Type;
import cpp.core.expression.Expression;
import cpp.core.expression.StaticAccessExpression;
import cpp.core.method.MethodStaticAttributeSetter;
import cpp.entity.method.EntityConstructor;
import cpp.entity.method.EntityDestructor;
import cpp.entity.method.MethodAddInsertParamForRawExpression;
import cpp.entity.method.MethodAddManyToManyRelatedEntity;
import cpp.entity.method.MethodAddManyToManyRelatedEntityInternal;
import cpp.entity.method.MethodAddRelatedEntity;
import cpp.entity.method.MethodAddRelatedEntityInternal;
import cpp.entity.method.MethodAddRelatedTableJoins;
import cpp.entity.method.MethodAllFieldNames;
import cpp.entity.method.MethodAttrGetter;
import cpp.entity.method.MethodColumnAttrSetNull;
import cpp.entity.method.MethodColumnAttrSetter;
import cpp.entity.method.MethodColumnAttrSetterInternal;
import cpp.entity.method.MethodCopyFields;
import cpp.entity.method.MethodFileImportColumnSetter;
import cpp.entity.method.MethodGetAllSelectFields;
import cpp.entity.method.MethodGetFieldName;
import cpp.entity.method.MethodGetFieldNameAlias;
import cpp.entity.method.MethodGetInsertFields;
import cpp.entity.method.MethodGetInsertParams;
import cpp.entity.method.MethodGetInsertValuePlaceholders;
import cpp.entity.method.MethodGetLastItem;
import cpp.entity.method.MethodGetLimitQueryString;
import cpp.entity.method.MethodGetManyRelatedAtIndex;
import cpp.entity.method.MethodGetManyRelatedCount;
import cpp.entity.method.MethodGetPrimaryKeyColumns;
import cpp.entity.method.MethodGetSelectFields;
import cpp.entity.method.MethodGetTableName;
import cpp.entity.method.MethodGetTableNameAlias;
import cpp.entity.method.MethodGetUpdateCondition;
import cpp.entity.method.MethodGetUpdateConditionParams;
import cpp.entity.method.MethodGetUpdateFields;
import cpp.entity.method.MethodGetValueByName;
import cpp.entity.method.MethodIsNullOrEmpty;
import cpp.entity.method.MethodManyAttrGetter;
import cpp.entity.method.MethodOneRelationAttrSetter;
import cpp.entity.method.MethodOneRelationEntityIsNull;
import cpp.entity.method.MethodQHashEntity;
import cpp.entity.method.MethodQHashEntitySharedPtr;
import cpp.entity.method.MethodQHashPkStruct;
import cpp.entity.method.MethodRemoveAllManyRelatedEntities;
import cpp.entity.method.MethodRemoveAllOneToManyRelatedEntities;
import cpp.entity.method.MethodRemoveManyToManyRelatedEntity;
import cpp.entity.method.MethodSetAutoIncrementId;
import cpp.entity.method.MethodUnload;
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

public class EntityCls extends Cls {

	private static final String BEAN_PARAM_NAME = "entity";
	public static final String BEGIN_CUSTOM_CLASS_MEMBERS = "/*BEGIN_CUSTOM_CLASS_MEMBERS*/";
	public static final String END_CUSTOM_CLASS_MEMBERS = "/*END_CUSTOM_CLASS_MEMBERS*/";
	public static final String BEGIN_CUSTOM_PREPROCESSOR = "/*BEGIN_CUSTOM_PREPROCESSOR*/";
	public static final String END_CUSTOM_PREPROCESSOR = "/*END_CUSTOM_PREPROCESSOR*/";
	public static final String APILEVEL = "3.8.2";
	
	static Database database;
	static DatabaseTypeMapper mapper;
	public static final String repository = "repository";
	private static String modelPath, repositoryPath;
	static CppOrmConfig cfg;
	
	public static void setCfg(CppOrmConfig cfg) {
		EntityCls.cfg = cfg;
	}
	private ArrayList<String> customHeaderCode, customSourceCode, customPreprocessorCode;
	private Map<String, SetterValidator> columnValidators;
	
	public static CppOrmConfig getCfg() {
		return cfg;
	}
	
	public static void setModelPath(String modelPath) {
		EntityCls.modelPath = modelPath;
		if(!EntityCls.modelPath.endsWith("/")) {
			EntityCls.modelPath+="/";
		}
	}
	
	public static String getRepositoryPath() {
		return repositoryPath;
	}
	
	public static void setRepositoryPath(String repositoryPath) {
		EntityCls.repositoryPath = repositoryPath;
		if(!EntityCls.repositoryPath.endsWith("/")) {
			EntityCls.repositoryPath+="/";
		}
	}
	
	public static void setTypeMapper(DatabaseTypeMapper mapper) {
		EntityCls.mapper = mapper;
	}
	
	public static void setDatabase(Database database) {
		EntityCls.database = database;
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
	private static final char NEWLINE='\n';
	
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
				addForwardDeclaredClass( (Cls) ((TplCls)attr.getClassType()).getElementType());
				addMethod(new MethodAttrGetter(attr,true));	
				addMethod(new MethodOneRelationEntityIsNull(r,true));
				addMethod(new MethodOneRelationEntityIsNull(r,false));
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
			//Attr attrManyToManyAdded = new Attr(Types.qvector(Types.getRelationForeignPrimaryKeyType(r)) ,attr.getName()+"Added");
			//addAttr(attrManyToManyAdded);
			//addMethod(new MethodAttributeGetter(attrManyToManyAdded));
			
			//Attr attrManyToManyRemoved = new Attr(Types.qvector(Types.getRelationForeignPrimaryKeyType(r)) ,attr.getName()+"Removed");
			//addAttr(attrManyToManyRemoved);
			//addMethod(new MethodAttributeGetter(attrManyToManyRemoved));
			addIncludeHeader(attr.getClassType().getIncludeHeader());
			addForwardDeclaredClass( (Cls) ((TplCls) (Cls) attr.getElementType()).getElementType());
			addMethod(new MethodManyAttrGetter(attr));
			addMethod(new MethodAddRelatedEntity(r, new Param(attr.getElementType().toConstRef(), BEAN_PARAM_NAME)));
			//addMethod(new MethodAddRelatedBean(r, new Param(Types.qvector(attr.getElementType()).toConstRef(), BEAN_PARAM_NAME)));
			addMethod(new MethodAddRelatedEntityInternal(r, new Param(attr.getElementType().toConstRef(), BEAN_PARAM_NAME)));
			addMethod(new MethodAddRelatedEntityInternal(r, new Param(Types.qvector(attr.getElementType()).toConstRef(), BEAN_PARAM_NAME)));
			addMethod(new MethodGetManyRelatedAtIndex(attr, r));
			addMethod(new MethodGetManyRelatedCount(attr, r));
			addMethod(new MethodRemoveAllOneToManyRelatedEntities(r));
			//addMethod(new MethodReplaceAllManyRelatedEntities(r));
			addMethod(new MethodGetLastItem(attr.getElementType(), r));
		}
		
		for(ManyRelation r:manyRelations) {
			ManyAttr attr = new ManyAttr(r);
			addAttr(attr);
			addIncludeHeader(attr.getClassType().getIncludeHeader());
			addForwardDeclaredClass( (Cls) ((TplCls) (Cls) attr.getElementType()).getElementType());
			addMethod(new MethodManyAttrGetter(attr));
//			Attr attrManyToManyAdded = new Attr(Types.qvector(Types.getRelationForeignPrimaryKeyType(r)) ,attr.getName()+"Added");
//			addAttr(attrManyToManyAdded);
//			addMethod(new MethodAttributeGetter(attrManyToManyAdded));
			
			//Attr attrManyToManyRemoved = new Attr(Types.qvector(Types.getRelationForeignPrimaryKeyType(r)) ,attr.getName()+"Removed");
			//addAttr(attrManyToManyRemoved);
			//addMethod(new MethodAttributeGetter(attrManyToManyRemoved));
			addMethod(new MethodAddManyToManyRelatedEntity(r, new Param(attr.getElementType().toConstRef(), BEAN_PARAM_NAME)));
			//addMethod(new MethodAddManyToManyRelatedBean(r, new Param(Types.qvector(attr.getElementType()).toConstRef(), BEAN_PARAM_NAME)));
			addMethod(new MethodAddManyToManyRelatedEntityInternal(r, new Param(attr.getElementType().toConstRef(), BEAN_PARAM_NAME)));
			addMethod(new MethodAddManyToManyRelatedEntityInternal(r, new Param(Types.qvector(attr.getElementType()).toConstRef(), BEAN_PARAM_NAME)));
			
			addMethod(new MethodRemoveManyToManyRelatedEntity(r));
			addMethod(new MethodRemoveAllManyRelatedEntities(r));
		}
		
		Type nullstring = Types.nullable(Types.QString);
//		structPk.setScope(name);
		for(Column col:allColumns) {
			if(col.isFileImportEnabled()) {
				Attr attr = new Attr(Types.QString, col.getCamelCaseName()+"FilePath");
				addAttr(attr);
				addMethod(new MethodFileImportColumnSetter(attr,col));
				addMethod(new MethodGetFieldName(col));
				addMethod(new MethodGetFieldNameAlias(col, true));
				addMethod(new MethodGetFieldNameAlias(col, false));
				addMethod(new MethodGetFieldName(col, true));
			} else	if (!col.hasOneRelation()
					
					) {
				Attr attr = new Attr(EntityCls.getDatabaseMapper().getTypeFromDbDataType(col.getDbType(), col.isNullable()), col.getCamelCaseName());
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
					attrInsertExpression.setStatic(true);
					addMethod(new MethodStaticAttributeSetter(attrInsertExpression));
					Attr attrInsertParams = new Attr(Attr.Protected, Types.QVariantList,"insertParamsForRawExpression"+col.getUc1stCamelCaseName(),null,false);
					addAttr(attrInsertParams);
					addMethod(new MethodAddInsertParamForRawExpression(col));
				}
			} else {
				Attr attr = new Attr(EntityCls.getDatabaseMapper().getTypeFromDbDataType(col.getDbType(), col.isNullable()), col.getCamelCaseName());
				addAttr(attr);
				addMethod(new MethodAttrGetter(attr,false));	
			}
			
//			if (col.isPartOfPk()) {
//				
//			}
			
		}
		addMethod(new MethodSetAutoIncrementId(getTbl().getPrimaryKey().isAutoIncrement()));
		//for(OneToManyRelation r:oneToManyRelations) {
		//	addMethod(new MethodToggleAddRemoveRelatedEntity(r));
		//}
		//for(ManyRelation r:manyRelations) {
		//	addMethod(new MethodToggleAddRemoveRelatedEntity(r));
		//}
//		addAttr(new RepositoryAttr());
	}
	
	public EntityCls(Table tbl,List<OneToManyRelation> manyRelations,List<OneRelation> oneRelations, List<ManyRelation> manyToManyRelations) {
		super(CodeUtil2.uc1stCamelCase(tbl.getName()));
		this.tbl = tbl;
		this.oneToManyRelations= manyRelations;
		this.oneRelations = oneRelations;
		this.manyRelations = manyToManyRelations;
		classDocumentation = String.format("/**\n * @brief auto-generated entity class representing the %s database table\n*/", tbl.getName());
	}
	
	public Constructor getConstructor() {
		return super.getConstructors().get(0);
	}
	
	public void addDeclarations() {
		Constructor c=new EntityConstructor(tbl.getPrimaryKey().isAutoIncrement(),tbl.getColumnsWithoutPrimaryKey());
		addSuperclass(Types.BaseEntity);
		addConstructor(c); 
		Destructor d = new EntityDestructor(this);
		setDestructor(d);
		
	//	addPreprocessorInstruction("#define " + getName()+ " "+CodeUtil2.uc1stCamelCase(tbl.getName()));
		addIncludeHeader(Types.BaseEntity.getIncludeHeader());
		addIncludeLib(Types.QString);
		addIncludeLib(CoreTypes.QVariant);
		addIncludeLib(Types.QDate);
		addIncludeLib("memory");
		addIncludeHeader("nullable");
				
//		Attr aTableName = new Attr(Attr.Public, Types.ConstCharPtr, "TABLENAME",constCharPtr(tbl.getName()),true);
//		addAttr(aTableName);
		
		addMethod(new MethodGetTableName());
		addMethod(new MethodGetTableNameAlias());
//		addMethod(new MethodGetTableNameInternal());
		//addIncludeHeader("entityquery");
		addIncludeHeader(repositoryPath + Types.EntityRepository.getName().toLowerCase());
		addForwardDeclaredClass(Types.EntityRepository);
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
		if(cfg.isEnableGetValueByName()) {
			addMethod(new MethodGetValueByName());
			addMethod(new MethodAllFieldNames());
		}
		
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
//			addIncludeHeader("entityhash");
		}  
		addOperator(new EntityEqualsOperator(this, tbl.getPrimaryKey()));
		addOperator(new EntitySharedPtrEqualsOperator(this, tbl.getPrimaryKey()));
		addNonMemberMethod(new MethodQHashEntity(this, tbl.getPrimaryKey()));
		addNonMemberMethod(new MethodQHashEntitySharedPtr(this, tbl.getPrimaryKey()));
		addNonMemberOperator(new NonMemberOperatorEntityEquals(this, tbl.getPrimaryKey()));
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
		return obj instanceof EntityCls && type.equals(((EntityCls)obj).type);
	}

	@Override
	public int hashCode() {
		return type.hashCode();
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
				throw e;
			}
		} else {
			return expression.callAttrGetter(col.getCamelCaseName());
		}
	}
	
 
	
	public static String getAccessMethodNameByColumn(Column col) {
		return "get"+col.getUc1stCamelCaseName();
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
		sb.append(NEWLINE).append(BEGIN_CUSTOM_CLASS_MEMBERS).append(NEWLINE);
		if(customHeaderCode != null) {
			for(String cc : customHeaderCode) {
				sb.append(cc.trim()).append(NEWLINE);
			}
		}
		sb.append(NEWLINE).append(END_CUSTOM_CLASS_MEMBERS).append(NEWLINE);

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
				Attr attrPrev = new Attr(EntityCls.getDatabaseMapper().getTypeFromDbDataType(col.getDbType(), col.isNullable()), col.getCamelCaseName()+"Previous");
				addAttr(attrPrev);
				getStructPk().addAttr(  new Attr(EntityCls.getDatabaseMapper().columnToType(col), col.getCamelCaseName()));
			}
		} else {
			if (tbl.getPrimaryKey().getColumns().size()==0) {
				throw new RuntimeException("pk info missing for "+type);
			}
			Column col= tbl.getPrimaryKey().getFirstColumn();
			Attr attrPrev = new Attr(EntityCls.getDatabaseMapper().getTypeFromDbDataType(col.getDbType(), col.isNullable()), col.getCamelCaseName()+"Previous");
			addAttr(attrPrev);
			pkType =EntityCls.getDatabaseMapper().columnToType(col);
		}
		
	}
	
	@Override
	protected void addBeforeHeader(StringBuilder sb) {
		CodeUtil.writeLine(sb, "/*DO NOT MODIFY OUTSIDE CUSTOM MEMBERS SECTIONS (DELIMITED BY COMMENTS BEGIN_CUSTOM_CLASS_MEMBERS AND END_CUSTOM_CLASS_MEMBERS + BEGIN_CUSTOM_PREPROCESSOR AND END_CUSTOM_PREPROCESSOR). ALL OTHER MODIFICATIONS WILL BE LOST ON REBUILDING ORM CLASSES*/");
		CodeUtil.writeLine(sb, "/*Dies ist eine automatisch generierte Datei des C++ ORM Systems https://github.com/thecodemonkey86/orm2*/");
		CodeUtil.writeLine(sb, "/*Generator (Java-basiert): https://github.com/thecodemonkey86/orm2*/");
		CodeUtil.writeLine(sb, "/*Abhängigkeiten (C++ libraries): https://github.com/thecodemonkey86/libcpporm, https://github.com/thecodemonkey86/QtCommonLibs2, https://github.com/thecodemonkey86/SqlUtil3*/");
		CodeUtil.writeLine(sb, "/*API Level " + APILEVEL + "*/\n");
		
	}
	
	public void setColumnValidators(Map<String, SetterValidator> columnValidators) {
		this.columnValidators = columnValidators;
	}
	
	public SetterValidator getColumnValidator(String col) {
		return columnValidators.get(col);
	}
	
	public boolean hasColumnValidator(String col) {
		return columnValidators != null && columnValidators.containsKey(col);
	}

}
