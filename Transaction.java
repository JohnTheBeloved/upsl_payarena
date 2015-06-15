/**
 * Transaction.java
 * Purpose: Example Calss to store Credit card Attributes
 * @author JohnTheBeloved
*/
public class Transaction{
	
	 public String amount;
	 public String currency;
	 public String description;
	 public String merchantID;

	 public Transaction(String amount, String currency,String description){
	 	this.amount = amount;
	 	this.currency = currency;
	 	this.description = description;
	 }
}