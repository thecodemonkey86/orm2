package config.json;

import java.util.ArrayList;
import java.util.List;

import config.OrmConfig;

public class JsonOrmConfig {
	OrmConfig serverConfig, clientConfig;
	List<String> excludeServer, excludeClient;
	
	public JsonOrmConfig() {
		excludeServer = new ArrayList<>();
		excludeClient = new ArrayList<>();
	}
	
	public void setClientConfig(OrmConfig clientConfig) {
		this.clientConfig = clientConfig;
	}
	
	public void setServerConfig(OrmConfig serverConfig) {
		this.serverConfig = serverConfig;
	}
	
	public OrmConfig getClientConfig() {
		return clientConfig;
	}
	
	public OrmConfig getServerConfig() {
		return serverConfig;
	}
	
	public void addExcludeServerTable(String tbl) {
		this.excludeServer.add(tbl);
	}
	
	public void addExcludeClientTable(String tbl) {
		this.excludeClient.add(tbl);
	}
	
	public List<String> getExcludeClient() {
		return excludeClient;
	}
	
	public List<String> getExcludeServer() {
		return excludeServer;
	}
}
