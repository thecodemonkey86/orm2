package config.php;

import java.nio.file.Path;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import config.ConfigReader;
import php.Php5;
import php.Php5NoTypeHints;
import php.Php7_0;
import php.Php7_2;


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

	public PhpConfigReader(Path xmlDirectory) {
		super(xmlDirectory);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
		super.startElement(uri, localName, qName, atts);
		switch (this.tags.peek()) {
		case "php":
			switch(atts.getValue("version")) {
			case "5.6":
				((PhpOrmConfig)cfg).setPhpversion(new Php5());
				break;	
			case "5.3":
				((PhpOrmConfig)cfg).setPhpversion(new Php5NoTypeHints());
				break;	
			case "7.0":
			case "7.1":
				((PhpOrmConfig)cfg).setPhpversion(new Php7_0());
				break;
			case "7.2":
				((PhpOrmConfig)cfg).setPhpversion(new Php7_2());
				break;
			default:
				((PhpOrmConfig)cfg).setPhpversion(new Php5());
				break;
			}
			break;
		default:
			break;
			
		}
	}
	
	protected void createConfig() {
		cfg = new PhpOrmConfig();
	}
	
	@Override
	public PhpOrmConfig getCfg() {
		return (PhpOrmConfig) cfg;
	}

}
