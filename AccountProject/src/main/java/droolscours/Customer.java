package droolscours;

public class Customer {

	private String name;
	private String surname;
	private String country;
	
	public Customer(String name, String surname, String country) {
		this.name = name;
		this.surname = surname;
		this.country = country;
	}
	
	public Customer() { }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	@Override
	public String toString() {
		StringBuffer buff = new StringBuffer();
		buff.append("-----Customer-----\n");
		buff.append("Name = " + name + "\n");
		buff.append("Surname = " + surname + "\n");
		buff.append("Country = " + country + "\n");
		buff.append("-----Customer end-----");
		return buff.toString();
	}	
}
