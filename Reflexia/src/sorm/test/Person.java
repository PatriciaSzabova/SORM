package sorm.test;

public class Person {
	private long ident;
	private String firstName;
	private String lastName;
	private int age;

	public Person() {
	}

	public Person(String firstName, String lastName, int age) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
	}
	
	public Person(long ident, String firstName, String lastName, int age) {
		super();
		this.ident = ident;
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
	}

	public long getIdent() {
		return ident;
	}
	
	public void setIdent(long ident) {
		this.ident = ident;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "Person [firstName=" + firstName + ", lastName=" + lastName + ", age=" + age + "]";
	}
}
