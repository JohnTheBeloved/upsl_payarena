/**
 * TrippleDESTool.java
 * Purpose: This class is used to show an encryption decryption example
 * @author JohnTheBeloved
*/

import java.util.HashMap;
public class TestEncryption{
	
	public static void main(String[] args) throws Exception
	{
		try{
			TestEncryption testEncryption = new TestEncryption();
			Transaction exampleTransaction = new Transaction("1000","566","Bag and Shoe");
			Card exampleCard = new Card("4012001038000002","05","11","028"); 

			String transactionXML = testEncryption.getTransactionXMLFormat(exampleTransaction);
			String cardXML = testEncryption.getCardXMLFormat(exampleCard);
			System.out.println("Transaction XML ==="+transactionXML);
			System.out.println("Card XML ==="+cardXML);
			
			String encryptedCard = testEncryption.encryptCard(cardXML);
 			

			System.out.println("Encrypted Card String ==="+encryptedCard);

			String decryptedCard = testEncryption.decryptCard(encryptedCard);
 			

			System.out.println("Decrypted Card XML ==="+decryptedCard);


}catch(Exception ex)
{
	throw ex;
}

}


public String getOrderURL(Transaction transaction,Card card) throws Exception
{
	SecureURLConnector connector;
	
	try{
		connector = new SecureURLConnector(Constants.CERTIFICATE_PATH_UNIX,Constants.CERTIFICATE_PASSWORD,Constants.URL_TO_CONNECT);
		
		HashMap<String,String> orderInfoMap = null;
		

		String orderXML = connector.sendData(getTransactionXMLFormat(transaction));
		orderInfoMap = CWorker.validateXML(orderXML);
		if(orderInfoMap!= null){
			if(orderInfoMap.get("Status") != null)
			{
				System.out.println("Status from Map===") ;
				if(orderInfoMap.get("Status") == "00")
				{
					String sessionID, orderID,encryptedCardData;
					sessionID  = orderInfoMap.get("sessionID");
					orderID = orderInfoMap.get("sessionID");
					String orderURL = Constants.PINPAD_URL + "?ORDERID=" + orderID + "&SESSIONID=" + sessionID ;

				 	//Encrypted Card Data
					String cardXMLString = getCardXMLFormat(card);
					encryptedCardData = 	encryptCard(cardXMLString);
					orderURL+= "&reqData=" + encryptedCardData;

					return orderURL; 
				}else{ return "Transaction order denied --- Status of transaction is ==="+orderInfoMap.get("Status");}
				
			}else{return "No Transaction Status Received ---";}
		}else{return "Null Map Returned.....Please check receved XML";}
	}catch(Exception ex)
	{
		System.out.println(ex.getMessage());throw ex;
	}
	

}

private String encryptCard(String cardXMLString)
{
	String encryptedCardString="";
	try{
		TrippleDESTool trippleDESTool = new TrippleDESTool(Constants.ENCRYPTION_KEY);
		encryptedCardString = trippleDESTool.encryptStringData(cardXMLString);
		return encryptedCardString;
	}catch(Exception ex){System.out.println(ex);}
	return encryptedCardString;
	
}

private String decryptCard(String encryptedCardString)
{
	String decryptedCardString="";
	try{
		TrippleDESTool trippleDESTool = new TrippleDESTool(Constants.ENCRYPTION_KEY);
		decryptedCardString = trippleDESTool.decryptData(encryptedCardString);
		return decryptedCardString;
	}catch(Exception ex){System.out.println(ex);}
	return decryptedCardString;
	
}

private void displayPinPadFrame(String pinPadUrl)
{

}


private String getTransactionXMLFormat(Transaction transaction)
{
	String transactionOrderString =	"<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	transactionOrderString += "<TKKPG>";
	transactionOrderString += "<Request>";
	transactionOrderString += "<Operation>"+Constants.OPERATION_TYPE +"</Operation>";
	transactionOrderString += "<Language>"+Constants.TRANSACTION_LANGUAGE+"</Language>";
	transactionOrderString += "<Order>";
	transactionOrderString += "<Merchant>"+Constants.MERCHANT_ID+"</Merchant>";
	transactionOrderString += "<Amount>"+transaction.amount+"</Amount>";
	transactionOrderString += "<Currency>"+transaction.currency+"</Currency>";
	transactionOrderString += "<Description>"+transaction.description+"</Description>";
	transactionOrderString += "<ApproveURL>"+Constants.APPROVE_URL+"</ApproveURL>";
	transactionOrderString += "<CancelURL>"+Constants.CANCEL_URL+"</CancelURL>";
	transactionOrderString += "<DeclineURL>"+Constants.DECLINE_URL+"</DeclineURL>";
		//Uncoment bellow and Terminate here if AddParams node isn't neccesarry
		//transactionOrderString+ "</AddParams></Order></Request></TKKPG>";
	transactionOrderString += "<AddParams>";
	transactionOrderString += "<SenderName>"+"SenderNameVal"+"</SenderName>";
	transactionOrderString += "<ResidentCityInLatin>"+"ResidentCityInLatinVal"+"</ResidentCityInLatin>";
	transactionOrderString += "<ResidentCountry>"+"ResidentCountryVal"+"</ResidentCountry>";
	transactionOrderString += "<Address>"+"AddressVal"+"</Address>";
	transactionOrderString += "<SenderPostalCode>"+"SenderPostalCodeVal"+"</SenderPostalCode>";
	transactionOrderString += "</AddParams></Order></Request></TKKPG>";
	return transactionOrderString;
}

private String getCardXMLFormat(Card card){
	String reqdata = "<ReqData>";
	reqdata += "<PAN>"+card.cardNumber+"</PAN>";
	reqdata += "<EXPDATE_M>"+card.EXP_MONTH+"</EXPDATE_M>";
	reqdata += "<EXPDATE_Y>"+card.EXP_YEAR+"</EXPDATE_Y>";
	reqdata += "<CVV2>"+card.CVV2+"</CVV2>";
		//Please omit Card holder Name
	reqdata += "<CHName></CHName>";
	reqdata += "</ReqData>";
	return reqdata;

}

}