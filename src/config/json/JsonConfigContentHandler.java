package config.json;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public class JsonConfigContentHandler implements ContentHandler {

	private String serverType= null;
	private String clientType= null;
	private String serverBasePath ,serverModelPath,	serverRepositoryPath;
	private String clientBasePath ,clientModelPath,	clientRepositoryPath;
	private JsonOrmConfig cfg;
	
	public JsonConfigContentHandler() {
		cfg =  new JsonOrmConfig();
	}
	
	public String getClientType() {
		return clientType;
	}
	
	public String getServerType() {
		return serverType;
	}
	
	@Override
	public void startPrefixMapping(String prefix, String uri) throws SAXException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
		String lowercase = localName.toLowerCase();
		
		switch (lowercase) {
		case "server":
			this.serverType = atts.getValue("type");
			this.serverBasePath = atts.getValue("basePath");
			this.serverModelPath = atts.getValue("modelPath");
			this.serverRepositoryPath = atts.getValue("repositoryPath");
			break;
		case "client":
			this.clientType = atts.getValue("type");
			this.clientBasePath = atts.getValue("basePath");
			this.clientModelPath = atts.getValue("modelPath");
			this.clientRepositoryPath = atts.getValue("repositoryPath");
			break;
		case "entity":
			String excludeJsonClient = atts.getValue("excludeJsonClient") ;
			if(excludeJsonClient != null
					&& (excludeJsonClient.equals("true") || excludeJsonClient.equals("1") )) {
			 cfg.addExcludeClientTable(atts.getValue("table"));
			}
			String excludeJsonServer = atts.getValue("excludeJsonServer") ;
			if(excludeJsonServer != null
					&& (excludeJsonServer.equals("true") || excludeJsonServer.equals("1") )) {
				cfg.addExcludeServerTable(atts.getValue("table"));
			}
			break;
		default:
			break;
		}
		
	}
	
	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void skippedEntity(String name) throws SAXException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void setDocumentLocator(Locator locator) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void processingInstruction(String target, String data) throws SAXException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void endPrefixMapping(String prefix) throws SAXException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		// TODO Auto-generated method stub
		
	}
	
	public String getClientBasePath() {
		return clientBasePath;
	}
	
	public String getClientModelPath() {
		return clientModelPath;
	}
	
	public String getClientRepositoryPath() {
		return clientRepositoryPath;
	}
	
	public String getServerBasePath() {
		return serverBasePath;
	}
	
	public String getServerModelPath() {
		return serverModelPath;
	}
	
	public String getServerRepositoryPath() {
		return serverRepositoryPath;
	}

	public JsonOrmConfig getConfig() {
		return cfg;
	}
}
