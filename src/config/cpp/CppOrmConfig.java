package config.cpp;

import config.OrmConfig;

public class CppOrmConfig extends OrmConfig{
	protected String exportMacro,exportMacroIncludeHeader,namespace;
	
	public void setExportMacro(String exportMacro, String exportMacroIncludeHeader) {
		this.exportMacro = exportMacro;
		this.exportMacroIncludeHeader = exportMacroIncludeHeader;
		namespace=null;
	}
	public String getExportMacro() {
		return exportMacro;
	}
	
//	public void setNamespace(String namespace) {
//		this.namespace = namespace;
//	}
//	
//	public String getNamespace() {
//		return namespace;
//	}
	
	public String getExportMacroIncludeHeader() {
		return exportMacroIncludeHeader;
	}
//	public boolean hasNamespace() {
//		return namespace!=null;
//	}
	
	
	
}
