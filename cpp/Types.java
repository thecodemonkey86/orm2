package cpp;

import model.AbstractRelation;
import cpp.cls.Cls;
import cpp.cls.PrimitiveType;
import cpp.cls.TplCls;
import cpp.cls.Type;
import cpp.cls.bean.BeanCls;
import cpp.cls.bean.Beans;
import cpp.cls.bean.Nullable;
import cpp.cls.bean.repo.ClsBeanRepository;
import cpp.lib.ClsBaseBean;
import cpp.lib.ClsOrderedSet;
import cpp.lib.ClsQDate;
import cpp.lib.ClsQDateTime;
import cpp.lib.ClsQSet;
import cpp.lib.ClsQSqlQuery;
import cpp.lib.ClsQSqlRecord;
import cpp.lib.ClsQString;
import cpp.lib.ClsQStringList;
import cpp.lib.ClsQTime;
import cpp.lib.ClsQVariant;
import cpp.lib.ClsQVariantList;
import cpp.lib.ClsQVector;
import cpp.lib.ClsSql;
import cpp.lib.ClsSqlQuery;
import cpp.lib.ClsTemplateAbstractBeanQuery;


public class Types {
	
	public static final PrimitiveType Int=new PrimitiveType("int");
	public static final PrimitiveType Int64=new PrimitiveType("int64_t");
	public static final PrimitiveType LongLong=new PrimitiveType("long long");
	public static final PrimitiveType Short=new PrimitiveType("short");
	public static final ClsQString QString= new ClsQString();
	public static final PrimitiveType Char= new PrimitiveType("char");
	public static final Type ConstCharPtr= Type.constPtr(Char);
	public static final PrimitiveType Bool = new PrimitiveType("bool");
	public static final Type NullptrType = new Type("nullptr");
	public static final ClsQDate QDate = new ClsQDate();
	public static final ClsQDateTime QDateTime = new ClsQDateTime(); 
	public static final ClsQTime QTime = new ClsQTime(); 
	public static final PrimitiveType Double = new PrimitiveType("double");
	public static final Type QByteArray = Type.nonPtr("QByteArray");
	public static final ClsQVariant QVariant = new ClsQVariant();
	public static final PrimitiveType Void = new PrimitiveType("void");
	public static final Cls QChar = new Cls("QChar");
	public static final ClsQVariantList QVariantList = new ClsQVariantList();
	public static final ClsQStringList QStringList = new ClsQStringList();
	public static final ClsQSqlRecord QSqlRecord = new ClsQSqlRecord();
	public static final ClsQSqlQuery QSqlQuery = new ClsQSqlQuery();
	public static final PrimitiveType Uint = new PrimitiveType("uint");
	public static final ClsSqlQuery SqlQuery = 	new ClsSqlQuery();
	public static final ClsSql Sql = 	new ClsSql();
	public static final ClsBeanRepository BeanRepository = new ClsBeanRepository();
	public static final ClsBaseBean BaseBean = new ClsBaseBean();

//	
	public static TplCls qlist(Type element) {
		return new TplCls("QList", element);
	}
	
	public static TplCls beanQuery(Cls cls) {
		return new ClsTemplateAbstractBeanQuery().getConcreteClass(cls);
	}
	public static ClsQVector qvector(Type element) {
		return new ClsQVector(element);
	}
	public static TplCls qset(Type element) {
		return new ClsQSet(element);
	}
	public static Type nullable(Type element) {
		return new Nullable( element);
	}
	
	public static Type getRelationForeignPrimaryKeyType(AbstractRelation r) {
		Type beanPk = null;
		if(r.getDestTable().getPrimaryKey().isMultiColumn()) {
			beanPk = Beans.get(r.getDestTable().getUc1stCamelCaseName()).getStructPk();
			
		} else {
			beanPk =BeanCls.getDatabaseMapper().columnToType(r.getDestTable().getPrimaryKey().getColumns().get(0));
		}
		return beanPk;
		
	}
	
	
	public static ClsOrderedSet orderedSet(Type elementType) {
		return new ClsOrderedSet(elementType);
	}
	
	
}
