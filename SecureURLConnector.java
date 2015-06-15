import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import java.net.URL;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.SecureRandom;


import javax.net.ssl.KeyManagerFactory; 
import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import java.net.MalformedURLException;

public class SecureURLConnector{
	
	private static final int READ_TIMEOUT = 30000;
	private static final int CONNECT_TIMEOUT = 30000;

	/**
	*This file specifies the location os the ceonnection certificate ---- 
	*I think theCertificate should be *.pfx extension
	*/
	private File certificateFile;
	/**This is the URL to call securly with https protocol using the certificate */
	private URL connectionURL;

	private String certificatePassword;


	/**
	*This constructor decalres a new secureURLConnector
	*@throws MalformedURLException, FileNotFoundException
	*@param certificatePath The Path of the Certificate
	*@param urlString The URL to go
	*/
	public SecureURLConnector(String certificatePath,String certificatePassword, String urlString) throws MalformedURLException, NullPointerException{
		File certificateFile = new File(certificatePath);
		this.certificateFile = certificateFile;
		this.certificatePassword = certificatePassword;
		this.connectionURL = new URL(urlString);
	}




	/**This method is used to get a SSL socket factory by using the certificate File */
	private SSLSocketFactory getSocketFactory() throws FileNotFoundException, Exception{
		
		try{
			KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
			//Get the type of KeyStore Instance //PKCS12
	        KeyStore keyStore = KeyStore.getInstance("PKCS12");
	        //Instantiate inputstream of certificate file
	        InputStream keyInput = new FileInputStream(certificateFile);
	        //load the key into the keystore
	        keyStore.load(keyInput, certificatePassword.toCharArray());
	        //Close stream
	        keyInput.close();
	        //Initialize the factory with the keystore
	        keyManagerFactory.init(keyStore, certificatePassword.toCharArray());
	        //Instantiate the secure socket
	        SSLContext context = SSLContext.getInstance("TLS");
	        //Initialise
	        context.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());
	        //The real Sesure Socket Factory to use
	        SSLSocketFactory socketFactory = context.getSocketFactory();
	        //Return Socket Factory
	        return socketFactory;
         
        }catch(FileNotFoundException ex){
        	throw ex;

    }
	} 


	/**
	*Calls the URL using a secure https connection and returns
	*an String form
	*@return String containing order information
	*/
	public String sendData(String dataToSend) throws FileNotFoundException,IOException, Exception
	{
		//Instantiate a new Https connection
		HttpsURLConnection secureURLConnection = (HttpsURLConnection)connectionURL.openConnection();
		 secureURLConnection.setSSLSocketFactory(this.getSocketFactory());
				secureURLConnection.setHostnameVerifier(new HostnameVerifier() { public boolean verify(String hostname, SSLSession session) {
                // ip address of the service URL(like.23.28.244.244)
                if (hostname.equals("196.46.20.36"))
                    return true;
                 	return false;
                   }
                  });

		//Set Other Properties of the connection
		secureURLConnection.setDoInput(true);
        secureURLConnection.setDoOutput(true);
        secureURLConnection.setUseCaches(false);
        secureURLConnection.setConnectTimeout(CONNECT_TIMEOUT);
        secureURLConnection.setReadTimeout(READ_TIMEOUT);
        secureURLConnection.setRequestMethod("POST");
		//Certificate File is used in this function
		
		

		 //Get the Output Stream of the connection to write the Data To
         System.out.println("\n\n\n\n\nConnecting...........now");

		 OutputStream secureOutputStream = secureURLConnection.getOutputStream();
 System.out.println("Data to send to URL ===="+ dataToSend + "\n\n\n\n\nSending...........now");
		
		  PrintWriter printWriter = new PrintWriter(secureOutputStream);
		  //Write the data into the stream
		  printWriter.print(dataToSend);
		  //make sure you flush for safety
		  printWriter.flush();
		  //Close, for system resource freedom
          printWriter.close();  

          String dataGotBack;
         
          //Read the response
            //You can use ReadLine BUT is not save -Not guaranteed to read all data from stream
        /* BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));  
         dataGotBack = br.readLine();*/
           

             System.out.println("\n\n\n\nReceiving...........now");
		
          //Alternative way to read all data from the stream

           dataGotBack = ConversionUtils.readStringFromStream(secureURLConnection.getInputStream(), 1028);

             System.out.println("Data gotten back from URL ===="+ dataGotBack + "\n\n\n\n\nSending...........now");
		
           //Return Data Gotten back
           return dataGotBack;


	}


	



	 
}