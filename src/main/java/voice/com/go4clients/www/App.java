package voice.com.go4clients.www;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
import voice.com.go4clients.www.RespuestaCliente;

/**
 * Ejemplo Envio de llamadas Go4clients
 *
 */
public class App 
{
    public static void main( String[] args )
    {
		HttpURLConnection conn = null;
		
		try {
		 // Service POST INVOCATION
		URL url = new URL("https://go4clients.com:8443/TelintelSms/api/voice/outcall");
		String request ="{\"from\": \"12345\",\"toList\": [\"573196296769\"],\"callSteps\": [{\"type\": \"SAY\",\"text\": \"hola bienvenido a gouforclaients ñoño \",\"voice\": \"CARLOS\",\"sourceType\":\"STANDARD\"}]}";

		conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Accept-Charset", "UTF-8");
		conn.setRequestProperty("Apikey", "ApiKey");
		conn.setRequestProperty("Apisecret", "Apisecret");
		DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));
		writer.write(request);
		writer.close();
		wr.flush();
		wr.close();

		// The response code must be HTTP_OK(200) in other case it means
		// that an error has been returned
		if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
			InputStream errorStream = conn.getErrorStream();
			System.out.println(conn.getResponseCode());
		}else {
			// Get file name
			String contentDisposition = conn.getHeaderField("Content-Disposition");
			// Get the response
						BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
						String line;
						String result = "";
						while ((line = rd.readLine()) != null) {
							result += line;
						}
						wr.close();
						rd.close();

						String respuesta = result.replaceAll("\\s+", " ");
						if (respuesta.startsWith(" ")) {
							respuesta = respuesta.substring(1, respuesta.length());
						}
						if (respuesta.endsWith(" ")) {
							respuesta = respuesta.substring(0, respuesta.length() - 1);
						}
						
						Gson gson = new Gson();
						RespuestaCliente rc = gson.fromJson(respuesta, RespuestaCliente.class);
						
						
						System.out.println(rc.getDescription());//respuesta 
						System.out.println("---Esto es una respuesta completa en JSON----" + respuesta);
		}


		
	} catch (IOException e) {
		e.printStackTrace();
	} finally {
		if (conn != null) {
			conn.disconnect();
		}
	}
		

    }
}
