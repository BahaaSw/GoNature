package utils;

import utils.enums.OrderTypeEnum;
import utils.enums.ParkNameEnum;

public class NotificationMessageTemplate {
	
	public static String orderConfirmMessage(int orderId,String park,String date,
			String type, int amountOfVisitors,double totalPrice,boolean isPaid) {
		StringBuilder sb= new StringBuilder();
		sb.append("Thank you for your order!\n");
		sb.append("We will send you a reminder one day before your visit.\n");
		sb.append("You need to confirm your order when you receive the reminder.\n");
		if(!isPaid)
			sb.append("NOTICE: You'll have to pay at the entrance!\n");
		sb.append("Your order information:\n");
		sb.append(String.format("Order id: %d.\n", orderId));
		sb.append(String.format("Park: %s.\n", park));
		sb.append(String.format("Date And Time: %s.\n", date));
		sb.append(String.format("Type: %s.\n", type));
		sb.append(String.format("Visitors: %d.\n", amountOfVisitors));
		sb.append(String.format("Total price: %.2f.\n\n", totalPrice));
		sb.append("We will see you at the park,\n GoNature Group 9 !");

		return sb.toString();
	}
	
	public static String orderSummaryMessage(String park,String date,
			String type, int amountOfVisitors,double priceAfterDiscount, double priceBeforeDiscount) {
		StringBuilder sb= new StringBuilder();
		sb.append("Thank you for your order!\n");
		sb.append("Last step before we book your order.\n");
		sb.append("You have to choose pay now or pay later.\n");
		sb.append("NOTICE: PrePayment gives you discount\n");
		sb.append("Your order information:\n");
		sb.append(String.format("Park: %s.\n", park));
		sb.append(String.format("Date And Time: %s.\n", date));
		sb.append(String.format("Type: %s.\n", type));
		sb.append(String.format("Visitors: %d.\n", amountOfVisitors));
		sb.append(String.format("Total Price (PrePayment): %.2f\n",priceAfterDiscount));
		sb.append(String.format("Total Price (Pay Later): %.2f\n\n", priceBeforeDiscount));
		sb.append("We will see you at the park,\n GoNature Group 9 !");

		return sb.toString();
	}
	
	public static String entrancePaymentReceiptMessage(ParkNameEnum parkName,OrderTypeEnum type,String firstName,String lastName,int amountOfVisitors,double totalPrice,long estimatedTimeVisit) {
		StringBuilder sb= new StringBuilder();
		sb.append(String.format("Welcome To %s\n",parkName.toString()));
		sb.append(String.format("Dear, %s %s \n",firstName,lastName));
		if(type.toString().contains("Group")) {
			sb.append(String.format("Your order total price (after discount) is: %.2f\n\n",totalPrice));
		}
		else
			sb.append(String.format("Your order total price (no discount) is: %.2f\n\n",totalPrice));
		sb.append(String.format("Total amount : %d\n", amountOfVisitors));
		sb.append("Pay at entrance.\n");
		sb.append(String.format("NOTE: Visit time duration is %d hours long.\n\n",estimatedTimeVisit));
		sb.append("Best Regards,\n");
		sb.append("GoNature Group 9 !");
		return sb.toString();
	}
	
	public static String prePaymentReceiptMessage(ParkNameEnum parkName,String orderDate, int amountOfVisitors,
			String firstName,String lastName,double prepaidPrice,double entrancePrice,long estimatedTimeVisit) {
		StringBuilder sb= new StringBuilder();
		sb.append(String.format("Dear, %s %s\n\n",firstName,lastName));
		sb.append(String.format("Thank you for choosing %s for your upcoming visit!\n", parkName.name()));
		sb.append("We're excited to have you with us and are here to ensure your experience\n");
		sb.append("is seamless and enjoyable. Below, you'll find a summary of your order.\n\n");
		sb.append("Order Summary:\n");
		sb.append(String.format("Order Date : %s\n",orderDate));
		sb.append(String.format("Visitors: %d.\n", amountOfVisitors));
		sb.append(String.format("Total Price (Pre Payment): %.2f\n",prepaidPrice));
		sb.append(String.format("Total Price (Pay at Entrance): %.2f\n\n", entrancePrice));
		sb.append(String.format("NOTE: Visit time duration is max %d hours long.\n\n",estimatedTimeVisit));
		sb.append("Best Regards,\n");
		sb.append("GoNature Group 9 !");
		return sb.toString();
	}

	public static String enterWaitingListMessage(String park, String dateAndTime ,String amoutOfVisitors) {
		StringBuilder sb= new StringBuilder();
		sb.append("Enter waiting list\n");
		sb.append("You are now in the waiting list\n");
		sb.append("We will send you an email if someone will cancel their visit\n");
		sb.append("Your order information:\n");
		sb.append(String.format("Park: %s.\n", park));
		sb.append(String.format("Visit Date and Time: %s.\n", dateAndTime));
		sb.append(String.format("Visitors: %s.\n", amoutOfVisitors));
		sb.append("Thank you!\n");
		sb.append("GoNature Group 9 !");

		return sb.toString();
	}
	
	public static String errorWaitingListMessage() {
		StringBuilder sb= new StringBuilder();
		sb.append("Error Waiting List!\n");
		sb.append("There was error trying to put you in the waiting list.\n");
		sb.append("Please try again later.\n");
		sb.append(String.format("Thank you!\n"));
		sb.append("GoNature Group 9 !");

		return sb.toString();
	}
	
	public static String passwordRecoveryMessage(String userId,String userPassword) {
		StringBuilder sb= new StringBuilder();
		sb.append("GoNature Password Recovery\n");
		sb.append("Hello,\nHere are your login information:\\n");
		sb.append(String.format("ID: %s\n", userId));
		sb.append(String.format("Password: %s\n\n", userPassword));
		sb.append(String.format("You Welcome!\n"));
		sb.append("GoNature Group 9 !");
		return sb.toString();
	}
	
	public static String confirmOrder1DayBeforeVisitMessage() {
		StringBuilder sb= new StringBuilder();
		sb.append("Please Confirm your order\n");
		sb.append("Hello,");
		sb.append(String.format("You have made an order for a visit in %s in %s at %s.\\n"));
		sb.append("Please confirm your order within two hours.\n");
		sb.append("NOTE: If you will not confirm your visit beforehand, your order will be automatically cancelled.\n\n");
		sb.append("Best Regards,\n");
		sb.append("GoNature Group 9 !");
		return sb.toString();
	}
	
	public static String orderCanceledMessage() {
		StringBuilder sb= new StringBuilder();
		sb.append("Your Order has been cancel\n");
		sb.append("Hello,\n");
		sb.append(String.format("We would like to inform you that your visit order to %s in %s at %s was cancelled.\n\n"));
		sb.append("Best Regards,\n");
		sb.append("GoNature Group 9 !");
		return sb.toString();
	}
	
	public static String spotFromWaitingListMessage() {
		StringBuilder sb= new StringBuilder();
		sb.append("We have a spot for you in the park!\n");
		sb.append("Hello,\n");
		sb.append("We are happy to inform you that there was an opening for a visit while you were waiting!\n");
		sb.append(String.format("At %s park on %s at %s.\n"));
		sb.append("If you would like to come at this time, please confirm.\n");
		sb.append("You have 1 hours to confirm the order before it automatically cancelled\n\n");
		sb.append("Best Regards,\n");
		sb.append("GoNature Group 9 !");
		return sb.toString();
	}
}
