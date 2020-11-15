package cpp.lib;

import cpp.Namespaces;
import cpp.Types;
import cpp.core.TplCls;
import cpp.core.Type;
import cpp.core.method.TplMethod;

public class ClsBaseRepository extends TplCls{

	public static final String save = "save"; 
	public static final String insertOrIgnorePg = "insertOrIgnorePg"; 
	public static final String bulkSave = "bulkSave"; 
	public static final String bulkInsert = "bulkInsert"; 
	public static final String prepareInsertOrIgnorePg = "prepareInsertOrIgnorePg"; 
	
	public ClsBaseRepository(Type dbConnectionPool) {
		super("BaseRepository",dbConnectionPool);
		addMethod( new LibMethod(Types.Void, save,true));
		addMethod( new LibMethod(Types.Void, bulkSave,true));
		addMethod( new LibMethod(Types.Void, bulkInsert,true));
		addMethod( new LibMethod(Types.Void, insertOrIgnorePg,true));
		addMethodTemplate( new LibMethodTemplate(Types.Void, prepareInsertOrIgnorePg,true) {
			
			@Override
			public TplMethod getConcreteMethodImpl(Type... types) {
				return new LibTplMethod(this, TplMethod.Public, returnType, useNamespace, types);
			}
		});
		headerInclude = type.toLowerCase();
		setUseNamespace(Namespaces.ORM2);
	}

}
