package io;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import cryptography.aes.AES;
import cryptography.md5.MD5;
import database.DbCredentials;

public class PasswordManager {
	private static byte[] superPassword=null;
	
	public static void setSuperPassword(byte[] superPassword) {
		PasswordManager.superPassword = superPassword;
	}
	
	public static void saveToFile(DbCredentials database, String password ) throws IOException {
		byte[] bytes = AES.encrypt(password.getBytes(StandardCharsets.UTF_8), superPassword)[1];
		Path file = getFilePath(database);
		Files.createDirectories(file.getParent());
		Files.write(file, bytes, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
		
	}
	
	private static Path getFilePath(DbCredentials database) {
		String workingDirectory;
		String OS = (System.getProperty("os.name")).toUpperCase();
		if (OS.contains("WIN"))
		{
		    workingDirectory = System.getenv("AppData");
		}
		else
		{
		    workingDirectory = System.getProperty("user.home");
		    if (OS.contains("MAC OS"))
		    	workingDirectory += "/Library/Application Support";
		}
		
		return Paths.get(workingDirectory).resolve("ORM2").resolve(MD5.toMd5Hex(database.getConnectionUrl()));
	}
	
	public static String loadFromFile(DbCredentials database ) throws IOException {
		Path file = getFilePath(database);
		if(!Files.exists(file)) {
			return null;
		}
		return new String(AES.decrypt( Files.readAllBytes( file),superPassword), StandardCharsets.UTF_8);
	}
}
