package main.java;

import java.io.IOException;
import java.io.Reader;

import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Key;

public class LoginInfo extends GenericJson {
	@Key
	String netid = "";
	@Key
	String password = "";
	public static LoginInfo load(JsonFactory jf, Reader reader)  {
		
		try {
			return jf.fromReader(reader, LoginInfo.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	
	
}
