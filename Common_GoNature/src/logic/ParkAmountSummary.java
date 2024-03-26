
package logic;

import java.io.Serializable;

import utils.enums.ParkNameEnum;

public class ParkAmountSummary implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8702175099372264200L;
	private int month;
	private int year;
	private int amountSolo;
	private int amountFamily;
	private int amountGroup;
	private ParkNameEnum park;
	
	public ParkAmountSummary() {}; 
	
	public ParkAmountSummary(int month,int year, int amountSolo, int amountFamily, int amountGroup, ParkNameEnum park) {
		super();
		this.month = month;
		this.amountSolo = amountSolo;
		this.amountFamily = amountFamily;
		this.amountGroup = amountGroup;
		this.year=year;
		this.park = park;
	}
	
	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getAmountSolo() {
		return amountSolo;
	}

	public void setAmountSolo(int amountSolo) {
		this.amountSolo = amountSolo;
	}

	public int getAmountFamily() {
		return amountFamily;
	}

	public void setAmountFamily(int amountFamily) {
		this.amountFamily = amountFamily;
	}

	public int getAmountGroup() {
		return amountGroup;
	}

	public void setAmountGroup(int amountGroup) {
		this.amountGroup = amountGroup;
	}

	public ParkNameEnum getPark() {
		return park;
	}

	public void setPark(ParkNameEnum park) {
		this.park = park;
	}
	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

}
