package config.cpp;

import java.nio.file.Path;
import java.sql.Connection;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import config.ConfigReader;
import database.Database;


public class CppConfigReader extends ConfigReader{

	public CppConfigReader(Path xmlDirectory,Connection conn,Database database) {
		super(xmlDirectory,conn,database);
		// TODO Auto-generated constructor stub
	}
	
	@Override
		public void endDocument() throws SAXException {
			// TODO Auto-generated method stub
			super.endDocument();
			
			if(getCfg().getDbPoolClass() == null) {
				getCfg().setDbPoolClass("DbPool");
			}
			
			if(getCfg().getDbPoolHeader() == null) {
				getCfg().setDbPoolHeader("util/db/dbpool.h");
			}
		}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
		super.startElement(uri, localName, qName, atts);
		switch (this.tags.peek()) {
		case "cpp":
			getCfg().setExportMacro(atts.getValue("exportMacro"),atts.getValue("exportMacroIncludeHeader"));
			String dbPoolHdr = atts.getValue("dbPoolIncludeHeader");
			String dbPoolClass = atts.getValue("dbPoolClass");
			
			if(dbPoolHdr!=null && dbPoolClass!=null) {
				getCfg().setDbPoolClass(dbPoolClass);
				getCfg().setDbPoolHeader(dbPoolHdr);
			}
//			getCfg().setNamespace(atts.getValue("namespace"));
			break;
		default:
			break;
			
		}
	}
	
	@Override
	protected void createConfig() {
		cfg = new CppOrmConfig();
	}
	
	@Override
	public CppOrmConfig getCfg() {
		return (CppOrmConfig) cfg;
	}

}
