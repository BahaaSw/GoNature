package utils.enums;

import java.util.HashMap;
import java.util.Map;

public enum OrderStatusEnum {
	Wait_Notification ("Wait Notify"), // Order created and waiting notification to be sent 24 hours before arrive
	Notification_Was_Sent("Notified"), // 24 hours before arrive/leaves waiting list notification was sent, client should confirm
	Order_Confirmed_Paid ("Paid"), // client confirmed the order
	Order_Confirmed_Not_Paid("Confirm Not Paid"),
	Occasional_Order ("Occasional Order"), // for occasional visit, park employee creates new order
	Cancelled ("Cancelled"), //
	In_Waiting_List ("Wait in list"), // order is in waiting list
	In_Park ("In park"), // visitors entered park, and order has been done
	Completed ("Completed"), // visitors left park
	Time_Passed ("Passed"); // visitors did not come to the order and did not cancelled it
	
	private static final Map<String, OrderStatusEnum> enumMap = new HashMap<>();

    static {
        for (OrderStatusEnum orderStatusEnum : OrderStatusEnum.values()) {
            enumMap.put(orderStatusEnum.name, orderStatusEnum);
        }
    }
	
	private String name;
	private OrderStatusEnum(String name) {
		this.name=name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
    public static OrderStatusEnum fromString(String name) {
        return enumMap.get(name);
    }
}
