package php.config;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import config.ConfigReader;


public class PhpConfigReader extends ConfigReader{
//	@Override
//	public void characters(char[] ch, int start, int length)
//			throws SAXException {
//		super.characters(ch, start, length);
//		PhpOrmOutputConfig cfg = (PhpOrmOutputConfig) this.cfg;
//		if (this.tag!=null) {
//			String v=new String(ch, start, length);
//			switch(this.tag) {
//			case "beanPhpPackage":
//				cfg.setBeanPackageName(v);
//				return;
//			case "repositoryPhpPackage":
//				cfg.setRepositoryPackageName(v);
//				return;
//			}
//		}
//	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
		super.startElement(uri, localName, qName, atts);
		switch (this.tag) {
		case "java":
			((PhpOrmOutputConfig)cfg).setBeanPackageName(atts.getValue("beanPackageName"));
			((PhpOrmOutputConfig)cfg).setRepositoryPackageName(atts.getValue("repositoryPhpPackage"));
			break;
		default:
			break;
			
		}
	}
	
	protected void createConfig() {
		cfg = new PhpOrmOutputConfig();
	}
	
	@Override
	public PhpOrmOutputConfig getCfg() {
		return (PhpOrmOutputConfig) cfg;
	}
}
