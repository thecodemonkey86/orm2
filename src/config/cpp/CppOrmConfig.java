package config.cpp;

import config.OrmConfig;

public class CppOrmConfig extends OrmConfig{
	protected String exportMacro,exportMacroIncludeHeader;
	
	public void setExportMacro(String exportMacro, String exportMacroIncludeHeader) {
		this.exportMacro = exportMacro;
		this.exportMacroIncludeHeader = exportMacroIncludeHeader;
	}
	public String getExportMacro() {
		return exportMacro;
	}
	
	public String getExportMacroIncludeHeader() {
		return exportMacroIncludeHeader;
	}
}
