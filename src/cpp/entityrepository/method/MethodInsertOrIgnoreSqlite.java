package cpp.entityrepository.method;

import cpp.core.MethodTemplate;
import cpp.core.Param;
import cpp.core.Type;
import cpp.core.instruction.Instruction;
import cpp.core.method.TplMethod;
import cpp.util.ClsDbPool;

public class MethodInsertOrIgnoreSqlite extends TplMethod {
	Param pEntity;
	boolean byRef;
	public MethodInsertOrIgnoreSqlite(MethodTemplate tpl,boolean byRef,Type...types) {
		super(tpl,types);
		pEntity = addParam(byRef? tpl.getTplTypes().get(0).toRef() : tpl.getTplTypes().get(0).toSharedPtr(),"entity");
		setStatic(true);
		this.byRef = byRef;
	}

	@Override
	public void addImplementation() {
		addInstr(new Instruction() {
			@Override
			public String toString() {
				String a=byRef?".":"->";
				return String.format("QString query = QStringLiteral(\"INSERT OR IGNORE INTO %%1 (%%2) VALUES (%%3)\").arg(T::getTableName(), T::getInsertFields(), T::getInsertValuePlaceholders());\r\n" + 
						"QList<QVariant> params=entity%sgetInsertParams();\r\n" + 
						"SqlUtil4::Sql::execute(%s::getDatabase(), query,params);\r\n" + 
						"entity%ssetInsertNew(false);",a,ClsDbPool.instance.getName(),a );
			}
		}); 
//EntityCls.getDatabaseMapper().getRepositoryInsertOrIgnoreMethod() ) ;
	}

}
