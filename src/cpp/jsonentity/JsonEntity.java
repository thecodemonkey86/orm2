package cpp.jsonentity;

import java.util.ArrayList;
import java.util.List;

import codegen.CodeUtil;
import cpp.CoreTypes;
import cpp.JsonTypes;
import cpp.Types;
import cpp.core.Attr;
import cpp.core.Cls;
import cpp.core.Constructor;
import cpp.core.Method;
import cpp.core.Operator;
import cpp.core.Param;
import cpp.core.Struct;
import cpp.core.Type;
import cpp.core.expression.BoolExpression;
import cpp.core.expression.Expression;
import cpp.core.expression.StaticAccessExpression;
import cpp.core.method.MethodAttributeGetter;
import cpp.core.method.MethodAttributeSetter;
import cpp.jsonentity.method.MethodAddInsertParamForRawExpression;
import cpp.jsonentity.method.MethodAddManyToManyRelatedEntity;
import cpp.jsonentity.method.MethodAddManyToManyRelatedEntityInternal;
import cpp.jsonentity.method.MethodAddRelatedBean;
import cpp.jsonentity.method.MethodAddRelatedEntityInternal;
import cpp.jsonentity.method.MethodColumnAttrSetNull;
import cpp.jsonentity.method.MethodColumnAttrSetter;
import cpp.jsonentity.method.MethodColumnAttrSetterInternal;
import cpp.jsonentity.method.MethodGetFieldName;
import cpp.jsonentity.method.MethodGetFieldNameAlias;
import cpp.jsonentity.method.MethodGetLastItem;
import cpp.jsonentity.method.MethodGetManyRelatedAtIndex;
import cpp.jsonentity.method.MethodGetManyRelatedCount;
import cpp.jsonentity.method.MethodIsNullOrEmpty;
import cpp.jsonentity.method.MethodLoadOneRelation;
import cpp.jsonentity.method.MethodManyAttrGetter;
import cpp.jsonentity.method.MethodOneRelationAttrSetter;
import cpp.jsonentity.method.MethodQHashPkStruct;
import cpp.jsonentity.method.MethodRemoveAllManyRelatedEntities;
import cpp.jsonentity.method.MethodReplaceAllManyRelatedEntities;
import cpp.jsonentity.method.MethodToJson;
import cpp.jsonentity.method.MethodToggleAddRemoveRelatedEntity;
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
import util.CodeUtil2;
import util.StringUtil;

/**
 * 
 * Remote entities with JSON representation
 *
 */
public class JsonEntity extends Cls {
	protected Table tbl;
	protected List<OneToManyRelation> oneToManyRelations;
	protected List<OneRelation> oneRelations;
	protected List<ManyRelation> manyRelations;
	public JsonEntity(Table tbl,List<OneToManyRelation> manyRelations,List<OneRelation> oneRelations, List<ManyRelation> manyToManyRelations) {
		super(CodeUtil2.uc1stCamelCase(tbl.getName()));
		this.tbl = tbl;		
		this.oneToManyRelations= manyRelations;
		this.oneRelations = oneRelations;
		this.manyRelations = manyToManyRelations;
		headerInclude=modelPath + "entities/"+type.toLowerCase();
	}
	
	private static final String ENTIITY_PARAM_NAME = "entity";
	public static final String BEGIN_CUSTOM_CLASS_MEMBERS = "/*BEGIN_CUSTOM_CLASS_MEMBERS*/";
	public static final String END_CUSTOM_CLASS_MEMBERS = "/*END_CUSTOM_CLASS_MEMBERS*/";
	public static final String BEGIN_CUSTOM_PREPROCESSOR = "/*BEGIN_CUSTOM_PREPROCESSOR*/";
	public static final String END_CUSTOM_PREPROCESSOR = "/*END_CUSTOM_PREPROCESSOR*/";
	public static final String APILEVEL = "1.3";
	
	static Database database;
	static DatabaseTypeMapper mapper;
	private static String modelPath, repositoryPath;
	
	private ArrayList<String> customHeaderCode, customSourceCode, customPreprocessorCode,customPreprocessorCodeInSource;
	
	
	public static void setModelPath(String modelPath) {
		JsonEntity.modelPath = modelPath;
		if(!JsonEntity.modelPath.endsWith("/")) {
			JsonEntity.modelPath+="/";
		}
	}
	
	public static String getRepositoryPath() {
		return repositoryPath;
	}
	
	public static void setRepositoryPath(String repositoryPath) {
		JsonEntity.repositoryPath = repositoryPath;
		if(!JsonEntity.repositoryPath.endsWith("/")) {
			JsonEntity.repositoryPath+="/";
		}
	}
	
	public static void setTypeMapper(DatabaseTypeMapper mapper) {
		JsonEntity.mapper = mapper;
	}
	
	public static void setDatabase(Database database) {
		JsonEntity.database = database;
	}
	
	public static Database getDatabase() {
		return database;
	}
		
	public static String getModelPath() {
		return modelPath;
	}
	
	
	
	//protected Struct structPk;
	protected Type pkType;
	
	
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
		for(OneRelation r:oneRelations) {
			OneAttr attr = new OneAttr(r);
				addAttr(attr);
				if(!attr.getElementType().equals(this)) {
				addIncludeHeader(attr.getElementType().getName().toLowerCase());
				addForwardDeclaredClass(attr.getClassType());
				}
				addMethod(new MethodLoadOneRelation(r));
				if(r.getDestTable().getPrimaryKey().isMultiColumn()) {
					addForwardDeclaredClass( ((Struct) Types.getRelationForeignPrimaryKeyTypeJsonEntities(r)));
				}
				
				addMethod(new MethodAttributeGetter(attr));	
				addMethod(new MethodOneRelationAttrSetter( this,r, true)); // internal setter
				addMethod(new MethodOneRelationAttrSetter( this,r, false)); // public setter
				
				if (!r.isPartOfPk()) {
					Attr attrModified = new Attr(Types.Bool, attr.getName()+"Modified");
					addAttr(attrModified);
					/*addMethod(new Method(Method.Public, Types.Bool, "is"+StringUtil.ucfirst(attrModified.getName())) {
						
						@Override
						public void addImplementation() {
							_return(attrModified);
						}
					}.setConstQualifier());*/
				}
				
//				for(Column col:r.getDestTable().getPrimaryKey().getColumns()) {
//					addMethod(new MethodManyDelegateGetter(attr,col,r.getDestTable().getUc1stCamelCaseName()));	
//				}
		}
		for(OneToManyRelation r:oneToManyRelations) {
			ManyAttr attr = new ManyAttr(r);
			addAttr(attr);
			Attr attrManyToManyAdded = new Attr(Types.qlist(Types.getRelationForeignPrimaryKeyTypeJsonEntities(r)) ,attr.getName()+"Added");
			addAttr(attrManyToManyAdded);
			addMethod(new MethodAttributeGetter(attrManyToManyAdded));
			
			Attr attrManyToManyRemoved = new Attr(Types.qlist(Types.getRelationForeignPrimaryKeyTypeJsonEntities(r)) ,attr.getName()+"Removed");
			addAttr(attrManyToManyRemoved);
			
			if(!this.equals((Cls) attr.getElementType())) {
				addIncludeDefaultHeaderFileName((Cls) attr.getElementType());
				addForwardDeclaredClass((Cls) attr.getElementType());
			}
			if(r.getDestTable().getPrimaryKey().isMultiColumn()) {
				addForwardDeclaredClass( ((Struct) Types.getRelationForeignPrimaryKeyTypeJsonEntities(r)));
			}
			
			addMethod(new MethodManyAttrGetter(attr));
			addMethod(new MethodAddRelatedBean(r, new Param(attr.getElementType().toConstRef(), ENTIITY_PARAM_NAME)));
			//addMethod(new MethodAddRelatedBean(r, new Param(Types.qvector(attr.getElementType()).toConstRef(), BEAN_PARAM_NAME)));
			addMethod(new MethodAddRelatedEntityInternal(r, new Param(attr.getElementType().toConstRef(), ENTIITY_PARAM_NAME)));
			addMethod(new MethodAddRelatedEntityInternal(r, new Param(Types.qlist(attr.getElementType()).toConstRef(), ENTIITY_PARAM_NAME)));
			addMethod(new MethodGetManyRelatedAtIndex(attr, r));
			addMethod(new MethodGetManyRelatedCount(attr, r));
			addMethod(new MethodRemoveAllManyRelatedEntities(r));
			addMethod(new MethodReplaceAllManyRelatedEntities(r));
			addMethod(new MethodGetLastItem(attr.getElementType(), r));
		}
		
		for(ManyRelation r:manyRelations) {
			ManyAttr attr = new ManyAttr(r);
			addAttr(attr);
			addAttr(new Attr(Types.qlist(JsonEntities.get(r.getDestTable()).getPkType()), attr.getName()+"Added"));
			addAttr(new Attr(Types.qlist(JsonEntities.get(r.getDestTable()).getPkType()), attr.getName()+"Removed"));
			addIncludeDefaultHeaderFileName(attr.getClassType());
			addForwardDeclaredClass((Cls) attr.getElementType());
			if(r.getDestTable().getPrimaryKey().isMultiColumn()) {
				addForwardDeclaredClass( ((JsonEntity) Types.getRelationForeignPrimaryKeyTypeJsonEntities(r)).getStructPk());
			}
			addMethod(new MethodManyAttrGetter(attr));
			addMethod(new MethodAddManyToManyRelatedEntity(r, new Param(attr.getElementType().toConstRef(), ENTIITY_PARAM_NAME)));
			//addMethod(new MethodAddRelatedBean(r, new Param(Types.qvector(attr.getElementType()).toConstRef(), BEAN_PARAM_NAME)));
			addMethod(new MethodAddManyToManyRelatedEntityInternal(r, new Param(attr.getElementType().toConstRef(), ENTIITY_PARAM_NAME)));
			addMethod(new MethodAddManyToManyRelatedEntityInternal(r, new Param(Types.qlist(attr.getElementType()).toConstRef(), ENTIITY_PARAM_NAME)));
			addMethod(new MethodGetManyRelatedAtIndex(attr, r));
			addMethod(new MethodGetManyRelatedCount(attr, r));
			addMethod(new MethodRemoveAllManyRelatedEntities(r));
			addMethod(new MethodReplaceAllManyRelatedEntities(r));
			addMethod(new MethodGetLastItem(attr.getElementType(), r));
			
		}
		
		Type nullstring = Types.nullable(Types.QString);
//		structPk.setScope(name);
		for(Column col:allColumns) {
						
			if (!col.hasOneRelation()
					
					) {
				Attr attr = new Attr(JsonEntity.getDatabaseMapper().getTypeFromDbDataType(col.getDbType(), col.isNullable()), col.getCamelCaseName());
				addAttr(attr);
				
				addMethod(new MethodAttributeGetter(attr));	
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
					/*addMethod(new Method(Method.Public, Types.Bool, "is"+StringUtil.ucfirst(attrModified.getName())) {
						
						@Override
						public void addImplementation() {
							_return(attrModified);
						}
					}.setConstQualifier());*/
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
				Attr attr = new Attr(JsonEntity.getDatabaseMapper().getTypeFromDbDataType(col.getDbType(), col.isNullable()), col.getCamelCaseName());
				addAttr(attr);
				addMethod(new MethodAttributeGetter(attr));	
			}
			
			if (col.isAutoIncrement()) {
				//addMethod(new MethodSetAutoIncrementId());
			}
//			if (col.isPartOfPk()) {
//				
//			}
			
		}
		
		for(OneToManyRelation r:oneToManyRelations) {
			addMethod(new MethodToggleAddRemoveRelatedEntity(r));
		}
		for(ManyRelation r:manyRelations) {
			addMethod(new MethodToggleAddRemoveRelatedEntity(r));
		}
		addMethod(new MethodToJson(this)); 
//		addAttr(new RepositoryAttr());
	}
	
	
	/*public Constructor getConstructor() {
		return super.getConstructors().get(0);
	}*/
	
	public void addDeclarations() {
		addSuperclass(JsonTypes.BaseJsonEntity);
		addConstructor(new Constructor() {
			
			@Override
			public void addImplementation() {
				JsonEntity entity = (JsonEntity) parent;
				for(OneRelation r:entity.getOneRelations()) {
					if (!r.isPartOfPk()) {
						_assign(parent.getAttrByName(entity.getOneRelationAttr(r).getName()+ "Modified"), BoolExpression.FALSE);
					}
				}
				for(Column col:tbl.getAllColumns()) {
					 
					if (!col.hasOneRelation() && !col.isFileImportEnabled()) {
						
						Expression defValExpr =  JsonEntity.getDatabaseMapper().getColumnDefaultValueExpression(col);
						if (defValExpr != null) {
							_assign(parent.getAttrByName(col.getCamelCaseName()),  defValExpr);
						} else {
							_assign(parent.getAttrByName(col.getCamelCaseName()), JsonEntity.getDatabaseMapper().getGenericDefaultValueExpression(col)); 
						}
						if(!col.isPartOfPk() ) {
							_assign(parent.getAttrByName(col.getCamelCaseName()+ "Modified"), BoolExpression.FALSE);
						}
					}
					if(col.isRawValueEnabled()) {
						Attr a = parent.getAttrByName("insertExpression"+col.getUc1stCamelCaseName());
						if(a.getInitValue() != null)
							_assign(a, a.getInitValue());
					}
				    
				}
				
			}
		});
	//	addPreprocessorInstruction("#define " + getName()+ " "+CodeUtil2.uc1stCamelCase(tbl.getName()));
		addIncludeLib(Types.QString);
		addIncludeLib(CoreTypes.QVariant);
		addIncludeLib(Types.QDate);
		if(!oneRelations.isEmpty() || !oneToManyRelations.isEmpty() || !manyRelations.isEmpty()) {
			addIncludeLib("memory");
			addIncludeHeaderInSource(JsonTypes.JsonEntityRepository.getHeaderInclude());
		}
		addIncludeLibInSource(JsonTypes.QJsonDocument);
		addIncludeLibInSource(JsonTypes.QJsonObject);
		if(tbl.hasNullableColumn()) {
			addIncludeDefaultHeaderFileName(Types.nullable(Types.Void));
		}
		addIncludeDefaultHeaderFileName(JsonTypes.BaseJsonEntity);

		addAttributes(tbl.getAllColumns());
		
		if (tbl.getPrimaryKey().isMultiColumn()) {
			addNonMemberMethod(new MethodQHashPkStruct(getStructPk(), tbl.getPrimaryKey()));
			
			addNonMemberOperator(new StructPkEqOperator(getStructPk()));

		}
	
	}
	
	
	@Override
	public void addMethodImplementations() {
		
		
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
		return obj instanceof JsonEntity && type.equals(((JsonEntity)obj).type);
	}

	@Override
	public int hashCode() {
		return type.hashCode();
	}

	
	
	@Override
	protected void addHeaderCodeBeforeClassDeclaration(StringBuilder sb) {
		super.addHeaderCodeBeforeClassDeclaration(sb);
		if(customPreprocessorCode != null) {
			sb.append('\n').append(BEGIN_CUSTOM_PREPROCESSOR).append('\n');
			for(String cc : customPreprocessorCode) {
				sb.append(cc.trim());
			}
			sb.append('\n').append(END_CUSTOM_PREPROCESSOR).append('\n');
		}
		if (tbl.getPrimaryKey().isMultiColumn()) {
			sb.append(getStructPk().toSourceString()).append('\n');
		}
//		if (fetchListHelper!=null) {
//			sb.append(fetchListHelper.toHeaderString()).append('\n').append('\n');
//		}
		
		
	}

	@Override
	protected void addAfterSourceCode(StringBuilder sb) {
		if(customSourceCode != null) {
			sb.append('\n').append(BEGIN_CUSTOM_CLASS_MEMBERS).append('\n');
			for(String cc : customSourceCode) {
				sb.append(cc.trim());
			}
			sb.append('\n').append(END_CUSTOM_CLASS_MEMBERS).append('\n');
		}
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
	
	public List<ManyRelation> getManyToManyRelations() {
		return manyRelations;
	}
	
	@Override
	protected void addClassHeaderCode(StringBuilder sb) {
		super.addClassHeaderCode(sb);
		if(customHeaderCode != null) {
			sb.append('\n').append(BEGIN_CUSTOM_CLASS_MEMBERS).append('\n');
			for(String cc : customHeaderCode) {
				sb.append(cc.trim());
			}
			sb.append('\n').append(END_CUSTOM_CLASS_MEMBERS).append('\n');
		}

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
				Attr attrPrev = new Attr(JsonEntity.getDatabaseMapper().getTypeFromDbDataType(col.getDbType(), col.isNullable()), col.getCamelCaseName()+"Previous");
				addAttr(attrPrev);
				getStructPk().addAttr(  new Attr(JsonEntity.getDatabaseMapper().columnToType(col), col.getCamelCaseName()));
			}
		} else {
			if (tbl.getPrimaryKey().getColumns().size()==0) {
				throw new RuntimeException("pk info missing for "+type);
			}
			Column col= tbl.getPrimaryKey().getFirstColumn();
			Attr attrPrev = new Attr(JsonEntity.getDatabaseMapper().getTypeFromDbDataType(col.getDbType(), col.isNullable()), col.getCamelCaseName()+"Previous");
			addAttr(attrPrev);
			pkType =JsonEntity.getDatabaseMapper().columnToType(col);
		}
		
	}
	
	@Override
	protected void addBeforeSourceCode(StringBuilder sb) {
		super.addBeforeSourceCode(sb);
		sb.append(BEGIN_CUSTOM_PREPROCESSOR).append('\n');
		if(customPreprocessorCodeInSource != null) {
			
			for(String cc : customPreprocessorCodeInSource) {
				sb.append(cc.trim());
			}
			
		}
		sb.append('\n').append(END_CUSTOM_PREPROCESSOR).append('\n').append('\n');
	}
	
	@Override
	protected void addBeforeHeader(StringBuilder sb) {
		CodeUtil.writeLine(sb, "/*Dies ist eine automatisch generierte Datei des C++ ORM Systems https://github.com/thecodemonkey86/orm2*/");
		CodeUtil.writeLine(sb, "/*Generator (Java-basiert): https://github.com/thecodemonkey86/orm2*/");
		CodeUtil.writeLine(sb, "/*Abhängigkeiten (C++ libraries): https://github.com/thecodemonkey86/libcpporm, https://github.com/thecodemonkey86/QtCommonLibs2, https://github.com/thecodemonkey86/SqlUtil2*/");
		CodeUtil.writeLine(sb, "/*API Level " + APILEVEL + "* (JSOM/REST API modus)*/\n");
		
	}

	public void addCustomPreprocessorCodeInSource(String code) {
		if(this.customPreprocessorCodeInSource == null) {
			this.customPreprocessorCodeInSource = new ArrayList<>();
		}
		this.customPreprocessorCodeInSource.add(code);
	}
	
}
