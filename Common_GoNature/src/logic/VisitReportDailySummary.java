package logic;

import java.io.Serializable;

public class VisitReportDailySummary implements Serializable{


	public int FamilyVisitAmount;
	public int GroupVisitAmount;
	public int SoloVisitAmount;
	public int FamilyVisitIdle;
	public int GroupVisitIdle;
	public int SoloVisitIdle;
	
	public VisitReportDailySummary() {}
	
	public int getFamiltVisitAmount() {
		return FamilyVisitAmount;
	}
	public void setFamiltVisitAmount(int familtVisitAmount) {
		FamilyVisitAmount = familtVisitAmount;
	}
	public int getGroupVisitAmount() {
		return GroupVisitAmount;
	}
	public void setGroupVisitAmount(int groupVisitAmount) {
		GroupVisitAmount = groupVisitAmount;
	}
	public int getSoloVisitAmount() {
		return SoloVisitAmount;
	}
	public void setSoloVisitAmount(int soloVisitAmount) {
		SoloVisitAmount = soloVisitAmount;
	}
	

	public int getFamiltVisitIdle() {
		return FamilyVisitIdle;
	}

	public void setFamiltVisitIdle(int familtVisitIdle) {
		FamilyVisitIdle = familtVisitIdle;
	}

	public int getGroupVisitIdle() {
		return GroupVisitIdle;
	}

	public void setGroupVisitIdle(int groupVisitIdle) {
		GroupVisitIdle = groupVisitIdle;
	}

	public int getSoloVisitIdle() {
		return SoloVisitIdle;
	}

	public void setSoloVisitIdle(int soloVisitIdle) {
		SoloVisitIdle = soloVisitIdle;
	}

	
}
