package droolscours;

import java.text.DateFormat;
import java.util.Date;

public class CashFlow {
	
	public static int CREDIT = 1;
	public static int DEBIT = 2;
	
	private Date mvtDate;
	private double amount;
	private int type;
	private long accountNo;
	
	public CashFlow(Date mvtDate, double amount, int type, long accountNo) {
		this.mvtDate = mvtDate;
		this.amount = amount;
		this.type = type;
		this.accountNo = accountNo;
	}
	
	public CashFlow() { }
	
	public Date getMvtDate() {
		return mvtDate;
	}
	public void setMvtDate(Date mvtDate) {
		this.mvtDate = mvtDate;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public long getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(long accountNo) {
		this.accountNo = accountNo;
	}	
	
	@Override
	public String toString() {
		
		StringBuffer buff = new StringBuffer();
		
		buff.append("-----CashFlow-----\n");
		buff.append("Account no=" + accountNo + "\n");
		if (null != mvtDate) {
			buff.append("Mouvement Date= " + DateFormat.getDateInstance().format(mvtDate) + "\n");
		} else {
			buff.append("No mouvement date was set\n");
		}
		
		buff.append("Mouvement Amount=" + amount + "\n");
		buff.append("-----CashFlow end-----");		
		
		return buff.toString();
	}	
}
