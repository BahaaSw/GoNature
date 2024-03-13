package logic;

import java.io.Serializable;
import java.util.ArrayList;

public class ServerResponseBackToClient<T> implements Serializable{
	private boolean result;
	private ArrayList<T> resultSet = new ArrayList<T>();
	private int arrayListSize;

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}


	public ArrayList<T> getResultSet() {
		return resultSet;
	}

	public void setResultSet(ArrayList<T> resultSet) {
		this.resultSet = resultSet;
	}

	public int getArrayListSize() {
		return arrayListSize;
	}

	public void setArrayListSize(int arrayListSize) {
		this.arrayListSize = arrayListSize;
	}
}
