package cpp.lib;

import cpp.Types;
import cpp.core.Attr;
import cpp.core.Cls;
import cpp.core.Type;
import cpp.core.method.MethodAttributeGetter;
import cpp.core.method.TplMethod;
import cpp.entityrepository.ClsEntityRepository;

public class ClsBaseRepository extends Cls{

	public static final String saveBean = "saveBean"; 
	public static final String insertOrIgnorePg = "insertOrIgnorePg"; 
	public static final String bulkSave = "bulkSave"; 
	public static final String bulkInsert = "bulkInsert"; 
	public static final String prepareInsertOrIgnorePg = "prepareInsertOrIgnorePg"; 
	
	public ClsBaseRepository() {
		super("BaseRepository");
		Attr sqlCon = new Attr(Types.QSqlDatabase.toConstRef(), ClsEntityRepository.sqlCon);
		addAttr(sqlCon);
		addMethod(new MethodAttributeGetter(sqlCon));
		addMethod( new LibMethod(Types.Void, saveBean));
		addMethod( new LibMethod(Types.Void, bulkSave));
		addMethod( new LibMethod(Types.Void, bulkInsert));
		addMethod( new LibMethod(Types.Void, insertOrIgnorePg));
		addMethodTemplate( new LibMethodTemplate(Types.Void, prepareInsertOrIgnorePg) {
			
			@Override
			public TplMethod getConcreteMethod(Type... types) {
				return new LibTplMethod(this, TplMethod.Public, returnType, useNamespace, types);
			}
		});
		headerInclude = type.toLowerCase();
	}

}
