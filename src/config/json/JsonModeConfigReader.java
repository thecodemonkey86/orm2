package config.json;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import config.ConfigReader;
import config.OrmConfig;
import config.OrmConfig.JsonMode;
import config.cpp.CppConfigReader;
import config.php.PhpConfigReader;
import database.Database;
import generate.CppOrm;
import generate.OrmGenerator;
import generate.PhpOrm;
import util.Pair;

public class JsonModeConfigReader {
	public static Pair<OrmGenerator, OrmGenerator> read(Path xmlFile,Connection conn,String engine, Database db) throws IOException {
		

		try(InputStream inputStream = new ByteArrayInputStream(Files.readAllBytes(xmlFile))) {
			SAXParserFactory parserFactory = SAXParserFactory.newInstance();
			SAXParser parser = parserFactory.newSAXParser();
			XMLReader xr = parser.getXMLReader();
			JsonConfigContentHandler handler = new JsonConfigContentHandler();
			xr.setContentHandler(handler);
			xr.parse(new InputSource(inputStream));
			String serverType= handler.getServerType();
			String clientType= handler.getClientType();
			inputStream.reset();
			if(serverType==null) throw new IOException("Server type missing");
			boolean serverIsCpp = serverType.equals("cpp");
			ConfigReader serverCfgReader = serverIsCpp ? new CppConfigReader(xmlFile,conn,db) : new PhpConfigReader(xmlFile,conn,db);
			
			parser = parserFactory.newSAXParser();
			xr = parser.getXMLReader();
			xr.setContentHandler(serverCfgReader);
			xr.parse(new InputSource(inputStream));
			
			OrmConfig serverConfig = serverCfgReader.getCfg();
			serverConfig.setBasePath(handler.getServerBasePath());
			serverConfig.setModelPath(handler.getServerModelPath());
			serverConfig.setRepositoryPath(handler.getServerRepositoryPath());
			serverConfig.setJsonMode(JsonMode.Server);
			
			inputStream.reset();
			boolean clientIsCpp = clientType.equals("cpp");
			ConfigReader clientCfgReader = clientIsCpp ? new CppConfigReader(xmlFile,conn,db) : new PhpConfigReader(xmlFile,conn,db);
			
		
			parser = parserFactory.newSAXParser();
			xr = parser.getXMLReader();
			xr.setContentHandler(clientCfgReader);
			xr.parse(new InputSource(inputStream));
			OrmConfig clientConfig = clientCfgReader.getCfg();
			clientConfig.setBasePath(handler.getClientBasePath());
			clientConfig.setModelPath(handler.getClientModelPath());
			clientConfig.setRepositoryPath(handler.getClientRepositoryPath());
			clientConfig.setJsonMode(JsonMode.Client);
			JsonOrmConfig cfg = handler.getConfig();
			
			for(String excludeTableServer : cfg.getExcludeServer()) {
				serverConfig.removeTable(excludeTableServer);
			}
			for(String excludeTableClient : cfg.getExcludeClient()) {
				clientConfig.removeTable(excludeTableClient);
			}
			
			cfg.setClientConfig(clientConfig);
			cfg.setServerConfig(serverConfig);
			
			serverConfig.setDatabase(db);
			serverConfig.setDbEngine(engine);
			clientConfig.setDatabase(db);
			clientConfig.setDbEngine(engine);
			OrmGenerator orm1 = serverIsCpp ? new CppOrm(serverConfig) : new PhpOrm(serverConfig);
			OrmGenerator orm2 = clientIsCpp ? new CppOrm(clientConfig) : new PhpOrm(clientConfig);
			return new Pair<OrmGenerator, OrmGenerator>(orm1, orm2);
		} catch (SAXException | ParserConfigurationException e) {
			throw new IOException(e);
		}
		
		
	}
}
