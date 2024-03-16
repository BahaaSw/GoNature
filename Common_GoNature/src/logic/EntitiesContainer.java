package logic;

public class EntitiesContainer {
	private Object entity1;
	private Object entity2;
	private Object entity3;
	private ICustomer customerInterface;
	
	public EntitiesContainer(Object entity1,Object entity2,Object entity3) {
		this.entity1=entity1;
		this.entity2=entity2;
		this.entity3=entity3;
	}
	
	public EntitiesContainer(Object entity1,Object entity2) {
		this.entity1=entity1;
		this.entity2=entity2;
	}
	
	public EntitiesContainer(Object entity1) {
		this.entity1=entity1;
	}
	
	public EntitiesContainer(Object entity1,ICustomer customerInterface) {
		this.entity1=entity1;
		this.setCustomerInterface(customerInterface);
	}
	
	public Object getEntity1() {
		return entity1;
	}

	public void setEntity1(Object entity1) {
		this.entity1 = entity1;
	}

	public Object getEntity2() {
		return entity2;
	}

	public void setEntity2(Object entity2) {
		this.entity2 = entity2;
	}

	public Object getEntity3() {
		return entity3;
	}

	public void setEntity3(Object entity3) {
		this.entity3 = entity3;
	}

	public ICustomer getCustomerInterface() {
		return customerInterface;
	}

	public void setCustomerInterface(ICustomer customerInterface) {
		this.customerInterface = customerInterface;
	}
	
}
