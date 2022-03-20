package config.json;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

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
	public static Pair<OrmGenerator, OrmGenerator> read(Path xmlFile,Connection connServer,Connection connClient,Database serverDb,Database clientDb) throws IOException {
		

		try(InputStream inputStream = new ByteArrayInputStream(Files.readAllBytes(xmlFile))) {
			XMLReader xr = XMLReaderFactory.createXMLReader();
			
			JsonConfigContentHandler handler = new JsonConfigContentHandler();
			xr.setContentHandler(handler);
			xr.parse(new InputSource(inputStream));
			String serverType= handler.getServerType();
			String clientType= handler.getClientType();
			inputStream.reset();
			
			boolean serverIsCpp = serverType.equals("cpp");
			ConfigReader serverCfgReader = serverIsCpp ? new CppConfigReader(xmlFile,connServer,serverDb) : new PhpConfigReader(xmlFile,connServer,serverDb);
			
			xr = XMLReaderFactory.createXMLReader();
			xr.setContentHandler(serverCfgReader);
			xr.parse(new InputSource(inputStream));
			
			OrmConfig serverConfig = serverCfgReader.getCfg();
			serverConfig.setBasePath(handler.getServerBasePath());
			serverConfig.setModelPath(handler.getServerModelPath());
			serverConfig.setRepositoryPath(handler.getServerRepositoryPath());
			serverConfig.setJsonMode(JsonMode.Server);
			
			inputStream.reset();
			boolean clientIsCpp = clientType.equals("cpp");
			ConfigReader clientCfgReader = clientIsCpp ? new CppConfigReader(xmlFile,connClient,clientDb) : new PhpConfigReader(xmlFile,connClient,clientDb);
			
		
			xr = XMLReaderFactory.createXMLReader();
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
			OrmGenerator orm1 = serverIsCpp ? new CppOrm(serverConfig) : new PhpOrm(serverConfig);
			OrmGenerator orm2 = clientIsCpp ? new CppOrm(clientConfig) : new PhpOrm(clientConfig);
			return new Pair<OrmGenerator, OrmGenerator>(orm1, orm2);
		} catch (SAXException e) {
			throw new IOException(e);
		}
		
		
	}
}
