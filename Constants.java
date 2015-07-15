public class Constants{

	//These feilds are used to store constants used in the XML String
	//Mandatory feilds
	public static final String OPERATION_TYPE = "CreateOrder";
	public static final String TRANSACTION_LANGUAGE = "EN";
	public static final String APPROVE_URL = "https://www.mysite.com/approveuri";
	public static final String CANCEL_URL = "https://www.mysite.com/canceluri";
	public static final String DECLINE_URL = "https://www.mysite.com/declineuri";
	public static final String MERCHANT_ID = "UNIGATEWAYTEST1";


 
	//Non Mandatory Feilds


	public static final String URL_TO_CONNECT = "https://196.46.20.36:5444/Exec";
	public static final String PINPAD_URL = "https://www.mysite.com/runtran.jsp";

	public static final String ENCRYPTION_KEY = "0123456789abcdef";


    //These feilds are used to store the path to the certificate generated for
//you by UPSL
     public static final String CERTIFICATE_PATH_WINDOWS =
"C:\\path\\to\\certificate\\certificatename.pfx";     public static final
String CERTIFICATE_PATH_UNIX =
"/Users/tolulopeogunremi/upsl_wepay/upsl_payarena/UNIGATEWAYTEST1.pfx";
public static final String CERTIFICATE_PASSWORD = "123456";


}
