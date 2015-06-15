/**
 * TrippleDESTool.java
 * Purpose: Encrypts and Decrypts String of data.
 *using Tripple DES encryption 
 * @author JohnTheBeloved
 *
 *Note that you cannot Encypt a non Hexadecimal String 
 *So This Class concerts the data you pass in to hexadecimal
 *before encrypting it
 *
 *Likewise, it converts the the decrypted data back to a normal String
 *i.e Hex String is returned after a normal java cryptogram decryption
*/

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import java.security.MessageDigest;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
public class TrippleDESTool{
	
	/**The Key being used for encryption*/
	private Key cipherKey = null;

	/**
	*Constructor receives a String specifying the key to use for encryption
	*@param The Key to use for the Encryption
	*/
	public TrippleDESTool(String keyHexString) throws InvalidKeyException, InvalidKeySpecException,NoSuchAlgorithmException{
		String threeDESKeyString = getRefinedKey(keyHexString);
		cipherKey = readKey(ConversionUtils.hexToBytes(threeDESKeyString));

	}

	/**
	*Encrypts a String of data and returns an Encrypted String
	*using the key specified during instantiation of this class
	*
	*Note that the String is first converted to Hexadecimal
	*
	*@throws UnsupportedEncodingException This will be due to the String Format supplied
	*/
	public  String encryptStringData(String stringToEncrypt) throws UnsupportedEncodingException{

		//Convert the String gotten to hexadecimal String
		String StringInHex = ConversionUtils.stringToHex(stringToEncrypt) + "000000";
		//Declare Encrypted String here
		String encryptedString= "";
		//Encrypt call
		try{
		 encryptedString = Encrypt( cipherKey , StringInHex);
		 System.out.println("Encrypted Data From" +stringToEncrypt+  "to"+encryptedString);
		}catch(Exception ex)
		{
			System.out.println(ex);
		}

		//Test Decryption here
	//	String decryptedString = decryptData(encryptedString);
		//Try to test decryption here
		

		return encryptedString;
	}

	/**
	*Decrypts a String of data and returns a decrypted String
	*using the key specified during instantiation of this class
	*	
	*@param dataStringToEncrypt --shold be in hexadecimal format
	*Note the decrypted data is converted to string before returning - Hex String is returned during decyption
	*/
	public String decryptData(String dataStringToEncrypt){
			 String realDataString= "";
		try{
		String decryptedDataHex; decryptedDataHex = Decrypt(cipherKey, dataStringToEncrypt);
		realDataString = ConversionUtils.hexToString(decryptedDataHex);

		}catch(Exception ex)
		{
			System.out.println(ex);
		}
		
		// System.out.println("Decrypted Data From" +dataStringToEncrypt+  "to"+realDataString);

		return  realDataString;
	}

	/**
     * Adds padding characters to the data to be encrypted. Also adds random
     * Initial Value to the beginning of the encrypted data when using Triple
     * DES in CBC mode (DES-EDE3/CBC).
     * 
     * @param inData
     *            Array of bytes to be padded
     * @param offset
     *            Offset to starting point within array
     * @param len
     *            Number of bytes to be encrypted
     * @return Padded array of bytes
     */
    public static byte[] addPadding(byte[] inData, int offset, int len) {
          final int PADDING_BLOCK = 8;
        System.out.println("addPadding offset >> "+offset+", len >> "+len);
        byte[] bp = null;
        int padChars = PADDING_BLOCK; // start with max padding value
        int partial = (len + 1) % padChars; // calculate how many extra bytes
                                            // exist
        if (partial == 0) {
            padChars = 1; // if none, set to only pad with length byte
        } else {
            padChars = padChars - partial + 1; // calculate padding size to
                                                // include length
        }
        System.out.println("addPadding >> Add padding of "+padChars);
        /*
         * Create a byte array large enough to hold data plus padding bytes The
         * count of padding bytes is placed in the first byte of the data to be
         * encrypted. That byte is included in the count.
         */
        bp = new byte[len + padChars];
        bp[0] = Byte.parseByte(Integer.toString(padChars));
        System.arraycopy(inData, offset, bp, 0, len);
        return bp;
    }

	

	/**
	*This function Encrypts the an hexadecimal String using the java Secret Key Object Provided
	*@return Encrypted Hexadecimal String
	*@param key The Java SecretKey to use for the encryption
	*@param dataToEncryptInHex THe data to encrypt***In Hexadecimal Form****
	*/
	private String Encrypt(Key key, String dataToEncryptInHex) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException 
	{

		
		Cipher cipher;
		cipher = Cipher.getInstance("DESede/ECB/NoPadding");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		byte[] clearText = ConversionUtils.hexToBytes(dataToEncryptInHex);

		CipherOutputStream out = new CipherOutputStream(bytes, cipher);
		out.write(addPadding(clearText,0,clearText.length));
		out.flush();
		out.close();
		byte[] ciphertext = bytes.toByteArray();
		bytes.flush();
		bytes.close();

		String encrypted = ConversionUtils.bytesToHex(ciphertext);
		java.util.Arrays.fill(clearText, (byte) 0);
		java.util.Arrays.fill(ciphertext, (byte) 0);
		return encrypted;

	}

	/**
	*This function Decrypts the an hexadecimal String using the java Secret Key Object Provided
	*@return Decrypted Hexadecimal String
	*@param key The Java SecretKey to use for the encryption
	*@param dataToEncryptInHex THe data to decrypt***In Hexadecimal Form****
	*/
	private static String Decrypt(Key key, String dataToDecryptInHex) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException 
	{

		//Complicated issues here

		// Instantitae The Java Cryptogram Cipher Object specifying the Decryption Type to use
		Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");
		//Initialise cipher using the secret key
		cipher.init(Cipher.DECRYPT_MODE, key);
		//Stream is used in the decryption
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		//Convert data to decrypt to a byte array
		byte[] ciphertext = ConversionUtils.hexToBytes(dataToDecryptInHex);
		//This objects vomits the decrypted data
		CipherOutputStream out;
		//
		out = new CipherOutputStream(bytes, cipher);
		//Decrypt the data in byte Array form using the initilised cipher object
		out.write(ciphertext);
		out.flush();
		out.close();
		//
		byte[] deciphertext = bytes.toByteArray();
		bytes.flush();
		bytes.close();

		//Convert Decrypted data into Hexadecimal String
		String decryptedData = ConversionUtils.bytesToHex(deciphertext);



		//
		java.util.Arrays.fill(ciphertext, (byte) 0);
		java.util.Arrays.fill(deciphertext, (byte) 0);
		// Take your data
		return decryptedData;

	}

	public SecretKey readKey(byte[] rawkey) throws InvalidKeyException, InvalidKeySpecException,NoSuchAlgorithmException {

		// Read the raw bytes from the keyfile
	

			DESedeKeySpec keyspec = new DESedeKeySpec(rawkey);
			SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DESede");
			SecretKey key;
			key = keyfactory.generateSecret(keyspec);

			key = keyfactory.translateKey(key);
			return key;
		
	}


	/**
	*This method makes sures the key string length is 48
	*Cos Encryption uses se bytes key
	*Key should a
	*/
	private String getRefinedKey(String keyString)
	{
		//Pad the key to make it 48 length
		switch(keyString.length())
		{
			case 16:
				return keyString+keyString+keyString;
			
			case 32:
				return keyString+keyString;
		
			case 48:
				return keyString;
		
			default:
				return keyString;
			
		}
		
	}

}