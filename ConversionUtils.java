import java.io.*;
import java.math.BigInteger;
public class ConversionUtils{
	

	 public static byte[] stringToHexToBytes(String string) throws UnsupportedEncodingException {
        return hexToBytes(stringToHex(string));
    }

    public static String byteToHexToString(byte[] bytes) throws UnsupportedEncodingException {
        return hexToString(bytesToHex(bytes));
    }

    public static String stringToHex(String arg) throws UnsupportedEncodingException {
        return String.format("%040x", new BigInteger(arg.getBytes("utf-8"))) + "000000";


    }

    public static byte[] hexToBytes(String str) {

        //it gives a byte length of the Hex String length divided by 2
        //So declare a byte array length of str length /2
        int hexLength = str.length();
        int byteArrayLength = hexLength / 2;
        byte[] bytes = new byte[byteArrayLength];
        //This loop converts each two Characters of the Hex to 1 byte
        //So at the end you get 8 bytes
        int step = 0;
        String twoChars = "";
        for (int i = 0; i < bytes.length; i++) {
            // bytes[i] = (byte) Integer.parseInt(str.substring(2 * i, 2 * i + 2), 16);
            step = 2 * i;
            twoChars = str.substring(step, step + 2);
            bytes[i] = (byte) Integer.parseInt(twoChars, 16);
            // System.out.println("\nstep ==" + step + "\nTwo chars ==" + twoChars + "\nResult ====" + bytes[i]);

        }
        //  System.out.println("\nAfter HEX2Byte Conversion here after for loop");
        return bytes;
    }

    private static final char[] hexArray = "0123456789abcdef".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    //I prefer this
    public static String hexToString(String hex) throws UnsupportedEncodingException {
        // String hex = "75546f7272656e745c436f6d706c657465645c6e667375635f6f73745f62795f6d757374616e675c50656e64756c756d2d392c303030204d696c65732e6d7033006d7033006d7033004472756d202620426173730050656e64756c756d00496e2053696c69636f00496e2053696c69636f2a3b2a0050656e64756c756d0050656e64756c756d496e2053696c69636f303038004472756d2026204261737350656e64756c756d496e2053696c69636f30303800392c303030204d696c6573203c4d757374616e673e50656e64756c756d496e2053696c69636f3030380050656e64756c756d50656e64756c756d496e2053696c69636f303038004d50330000";
       
		 StringBuilder output = new StringBuilder();
        for (int i = 0; i < hex.length(); i += 2) {
            String str = hex.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }
        return output.toString();
    }

    public static String hexToString2(String txtInHex) {
        byte[] txtInByte = new byte[txtInHex.length() / 2];
        int j = 0;
        for (int i = 0; i < txtInHex.length(); i += 2) {
            txtInByte[j++] = Byte.parseByte(txtInHex.substring(i, i + 2), 16);
        }
        return new String(txtInByte);
    }



public static String readStringFromStream(final InputStream is, final int bufferSize)
{
  final char[] buffer = new char[bufferSize];
  final StringBuilder out = new StringBuilder();
  try {
    final Reader in = new InputStreamReader(is, "UTF-8");
    try {
      for (;;) {
        int rsz = in.read(buffer, 0, buffer.length);
        if (rsz < 0)
          break;
        out.append(buffer, 0, rsz);
         System.out.println(out.toString());
      }
    }
    finally {
      in.close();
    }
  }
  catch (UnsupportedEncodingException ex) {
	System.out.println("Exception in ConversionUtils --- readStringFromStream --- UnsupportedEncodingException "+ex.getMessage());  }
  catch (IOException ex) {
     System.out.println("Exception in ConversionUtils --- readStringFromStream --- IOException "+ex.getMessage());
  }catch(Exception ex){
  	System.out.println("Exception in ConversionUtils --- readStringFromStream --- Exception "+ex.getMessage());
  }

  return out.toString();
}
}