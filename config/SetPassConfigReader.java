package config;

import java.io.IOException;
import java.nio.file.Paths;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import database.Database;
import database.DbCredentials;
import database.FirebirdCredentials;
import database.FirebirdDatabase;
import database.MySqlCredentials;
import database.MySqlDatabase;
import database.PgCredentials;
import database.PgDatabase;
import database.SqliteCredentials;
import database.SqliteDatabase;

public class SetPassConfigReader implements ContentHandler{

	DbCredentials credentials;
	
	public DbCredentials getCredentials() {
		return credentials;
	}
	
	@Override
	public void setDocumentLocator(Locator locator) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startPrefixMapping(String prefix, String uri) throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endPrefixMapping(String prefix) throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
		try {
			switch (localName) {
			case "database":
				OrmConfig cfg = new OrmConfig();
				cfg.setDbEngine(atts.getValue("engine"));
				Database database=null; 
				
				if(atts.getValue("password") != null) {
					throw new IOException("Remove password from XML");
				}
				
				if (cfg.isEnginePostgres()) {
					Class.forName("org.postgresql.Driver");
					database = new PgDatabase(atts.getValue("name"), atts.getValue("schema"));
					credentials = new PgCredentials(atts.getValue("user"), atts.getValue("host"),atts.getValue("port") != null ? Integer.parseInt(atts.getValue("port")) : 5432, database);
					
				} else if (cfg.isEngineMysql()) {
					database = new MySqlDatabase(atts.getValue("name"));
					credentials = new MySqlCredentials(atts.getValue("user"), atts.getValue("host"), atts.getValue("port") != null ? Integer.parseInt(atts.getValue("port")) : 3306, database);
					
				} else if (cfg.isEngineFirebird()) {
					Class.forName("org.firebirdsql.jdbc.FBDriver");
					database = new FirebirdDatabase(atts.getValue("name"));
					credentials = new FirebirdCredentials(atts.getValue("user"), atts.getValue("host"), atts.getValue("file"),atts.getValue("port") != null ? Integer.parseInt(atts.getValue("port")) : 23053, atts.getValue("charSet")  != null ?  atts.getValue("charSet")  : "UTF-8", database);
				} else if (cfg.isEngineSqlite()) {
					Class.forName("org.sqlite.JDBC");
					database = new SqliteDatabase();
					credentials = new SqliteCredentials(Paths.get(atts.getValue("file")) , database);
						
				} else {
					throw new IOException(
							"Database engine \"" + atts.getValue("engine") + "\" is currently not supported");
				}
				
			}
		} catch (Exception e) {
			throw new SAXException(e);
		}
	
		
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processingInstruction(String target, String data) throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void skippedEntity(String name) throws SAXException {
		// TODO Auto-generated method stub
		
	}

}
