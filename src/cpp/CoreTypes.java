package cpp;

import cpp.core.Cls;
import cpp.core.Optional;
import cpp.core.PrimitiveType;
import cpp.core.TplCls;
import cpp.core.Type;
import cpp.lib.ClsQByteArray;
import cpp.lib.ClsQDate;
import cpp.lib.ClsQDateTime;
import cpp.lib.ClsQLatin1String;
import cpp.lib.ClsQSet;
import cpp.lib.ClsQSqlDatabase;
import cpp.lib.ClsQSqlQuery;
import cpp.lib.ClsQSqlRecord;
import cpp.lib.ClsQString;
import cpp.lib.ClsQStringList;
import cpp.lib.ClsQTime;
import cpp.lib.ClsQVariant;
import cpp.lib.ClsQVariantList;
import cpp.lib.ClsQList;
import cpp.lib.ClsQRegularExpression;

public class CoreTypes {
	public static final PrimitiveType Int32=new PrimitiveType("int32_t");
	public static final PrimitiveType Int = new PrimitiveType("int");
	public static final PrimitiveType Int16=new PrimitiveType("int16_t");
	public static final PrimitiveType Int8=new PrimitiveType("int8_t");
	public static final PrimitiveType Int64=new PrimitiveType("int64_t");
	public static final PrimitiveType UInt32=new PrimitiveType("uint32_t", true);
	public static final PrimitiveType UInt = new PrimitiveType("uint", true);
	public static final PrimitiveType UInt16=new PrimitiveType("uint16_t", true);
	public static final PrimitiveType UInt8=new PrimitiveType("uint8_t", true);
	public static final PrimitiveType UInt64=new PrimitiveType("uint64_t", true);
//	public static final PrimitiveType LongLong=new PrimitiveType("long long");
	public static final PrimitiveType Short=new PrimitiveType("short");
	public static final ClsQString QString= new ClsQString();
	public static final ClsQLatin1String QLatin1String= new ClsQLatin1String();
	public static final PrimitiveType Char= new PrimitiveType("char");
	public static final Type ConstCharPtr= Type.constPtr(Char);
	public static final PrimitiveType Bool = new PrimitiveType("bool");
	public static final Type NullptrType = new Type("nullptr");
	public static final ClsQDate QDate = new ClsQDate();
	public static final ClsQDateTime QDateTime = new ClsQDateTime(); 
	public static final ClsQTime QTime = new ClsQTime(); 
	public static final PrimitiveType Double = new PrimitiveType("double");
	public static final Type QByteArray = new ClsQByteArray();
	public static final ClsQVariant QVariant = new ClsQVariant();
	public static final PrimitiveType Void = new PrimitiveType("void");
	public static final Cls QChar = new Cls("QChar");
	public static final ClsQVariantList QVariantList = new ClsQVariantList();
	public static final ClsQStringList QStringList = new ClsQStringList();
	public static final ClsQSqlRecord QSqlRecord = new ClsQSqlRecord();
	public static final ClsQSqlQuery QSqlQuery = new ClsQSqlQuery();
	public static final ClsQSqlDatabase QSqlDatabase = new ClsQSqlDatabase();
	public static final ClsQRegularExpression QRegularExpression = new ClsQRegularExpression();
	public static final PrimitiveType SizeT = new PrimitiveType("size_t");
	
	public static ClsQList qlist(Type element) {
		return new ClsQList(element);
	}
	public static TplCls qset(Type element) {
		return new ClsQSet(element);
	}
	
	public static Optional optional(Type t) {
		return new Optional(t);
	}
}
