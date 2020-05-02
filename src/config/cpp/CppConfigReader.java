package config.cpp;

import java.nio.file.Path;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import config.ConfigReader;


public class CppConfigReader extends ConfigReader{
//	@Override
//	public void characters(char[] ch, int start, int length)
//			throws SAXException {
//		super.characters(ch, start, length);
//		PhpOrmOutputConfig cfg = (PhpOrmOutputConfig) this.cfg;
//		if (this.tag!=null) {
//			String v=new String(ch, start, length);
//			switch(this.tag) {
//			case "entityPhpPackage":
//				cfg.setBeanPackageName(v);
//				return;
//			case "repositoryPhpPackage":
//				cfg.setRepositoryPackageName(v);
//				return;
//			}
//		}
//	}

	public CppConfigReader(Path xmlDirectory) {
		super(xmlDirectory);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
		super.startElement(uri, localName, qName, atts);
		switch (this.tags.peek()) {
		case "cpp":
			getCfg().setExportMacro(atts.getValue("exportMacro"),atts.getValue("exportMacroIncludeHeader"));
//			getCfg().setNamespace(atts.getValue("namespace"));
			break;
		default:
			break;
			
		}
	}
	
	protected void createConfig() {
		cfg = new CppOrmConfig();
	}
	
	@Override
	public CppOrmConfig getCfg() {
		return (CppOrmConfig) cfg;
	}

}
