package sunjava.config;

import java.nio.file.Path;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import config.ConfigReader;


public class JavaConfigReader extends ConfigReader{
//	@Override
//	public void characters(char[] ch, int start, int length)
//			throws SAXException {
//		super.characters(ch, start, length);
//		JavaOrmOutputConfig cfg = (JavaOrmOutputConfig) this.cfg;
//		if (this.tag!=null) {
//			String v=new String(ch, start, length);
//			switch(this.tag) {
//			case "entityJavaPackage":
//				cfg.setBeanPackageName(v);
//				return;
//			case "repositoryJavaPackage":
//				cfg.setRepositoryPackageName(v);
//				return;
//			}
//		}
//	}

	public JavaConfigReader(Path xmlDirectory) {
		super(xmlDirectory);
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
		super.startElement(uri, localName, qName, atts);
		switch (this.tags.peek()) {
		case "java":
			((JavaOrmOutputConfig)cfg).setBeanPackageName(atts.getValue("entityPackageName"));
			((JavaOrmOutputConfig)cfg).setRepositoryPackageName(atts.getValue("repositoryJavaPackage"));
			break;
		default:
			break;
			
		}
	}
	
	protected void createConfig() {
		cfg = new JavaOrmOutputConfig();
	}
	
	@Override
	public JavaOrmOutputConfig getCfg() {
		return (JavaOrmOutputConfig) cfg;
	}
}
