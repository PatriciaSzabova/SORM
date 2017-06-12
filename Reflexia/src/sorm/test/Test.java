package sorm.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import sorm.SORM;

public class Test {

	public static void main(String[] args) throws Exception {
		Connection connection = DriverManager.getConnection("jdbc:derby://localhost:1527/TestRegister", "meno", "heslo");
		SORM sorm = new SORM(connection);
//		System.out.println(sorm.getCreateTableString(Person.class));
//		System.out.println(sorm.getDropTableString(Person.class));
		System.out.println(sorm.getInsertString(Person.class));
		System.out.println(sorm.getSelectString(Person.class));
		
//		Person person = new Person(1, "Janko", "Hrasko", 14);
//		sorm.insert(person);
//		List<Person> persons = sorm.select(Person.class);
//		System.out.println(persons);

//		persons = sorm.select(Person.class, "WHERE firstName LIKE 'J%'");
//		System.out.println(persons);
	}
}
