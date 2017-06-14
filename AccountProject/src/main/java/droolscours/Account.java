package droolscours;

public class Account {
	
	private long accountNo;
	private double balance;
		
	public long getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(long accountNo) {
		this.accountNo = accountNo;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	@Override
	public String toString() {
		return "Account [accountno=" + accountNo + ", balance=" + balance + "]";
	}
}
