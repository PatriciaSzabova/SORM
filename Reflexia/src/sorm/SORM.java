package sorm;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SORM {
	public static final boolean DEBUG = true;

	private final Connection connection;

	public SORM(Connection connection) {
		this.connection = connection;
	}

	public String getCreateTableString(Class<?> clazz) {
		StringBuilder sb = new StringBuilder();

		sb.append("CREATE TABLE " + clazz.getSimpleName() + " (\n");

		boolean notFirst = false;
		for (Field field : clazz.getDeclaredFields()) {
			if (notFirst)
				sb.append(",\n");
			notFirst = true;
			sb.append("  " + field.getName() + " " + getSQLType(field.getType()));
		}

		sb.append("\n)");

		return sb.toString();
	}

	public String getDropTableString(Class<?> clazz) {
		return "DROP TABLE " + clazz.getSimpleName();
	}

	public String getInsertString(Class<?> clazz) {
		return String.format("INSERT INTO %s (%s) VALUES (%s)", 
					clazz.getSimpleName(),
					Arrays.stream(clazz.getDeclaredFields()).map(f -> f.getName()).collect(Collectors.joining(", ")),
					Arrays.stream(clazz.getDeclaredFields()).map(f -> "?").collect(Collectors.joining(", ")) );
		
		/*StringBuilder sb = new StringBuilder();

		sb.append("INSERT INTO " + clazz.getSimpleName() + " (");

		boolean notFirst = false;
		for (Field field : clazz.getDeclaredFields()) {
			if (notFirst)
				sb.append(", ");
			notFirst = true;
			sb.append(field.getName());
		}

		sb.append(") VALUES (");

		notFirst = false;
		for (Field field : clazz.getDeclaredFields()) {
			if (notFirst)
				sb.append(", ");
			notFirst = true;
			sb.append("?");
		}

		sb.append(")");

		return sb.toString();*/
	}

	public String getSelectString(Class<?> clazz) {
		return String.format("SELECT %s FROM %s",
				Arrays.stream(clazz.getDeclaredFields()).map(f -> f.getName()).collect(Collectors.joining(", ")) 
					,clazz.getSimpleName());
	/*	StringBuilder sb = new StringBuilder();

		sb.append("SELECT ");

		boolean notFirst = false;
		for (Field field : clazz.getDeclaredFields()) {
			if (notFirst)
				sb.append(", ");
			notFirst = true;
			sb.append(field.getName());
		}

		sb.append(" FROM " + clazz.getSimpleName());

		return sb.toString();*/
	}

	public String getUpdateString(Class<?> clazz) {
		StringBuilder sb = new StringBuilder();

		return sb.toString();
	}

	public String getDeleteString(Class<?> clazz) {
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE" + clazz.getSimpleName());

		return sb.toString();

	}

	public void insert(Object object) throws SORMException {
		Class<?> clazz = object.getClass();
		String command = getInsertString(clazz);

		if (DEBUG)
			System.out.println(command);

		try (PreparedStatement ps = connection.prepareStatement(command)) {
			int index = 1;
			for (Field field : clazz.getDeclaredFields()) {
				field.setAccessible(true);
				Object value = field.get(object);
				ps.setObject(index, value);
				index++;
			}
			ps.executeUpdate();
		} catch (Exception e) {
			throw new SORMException("Cannot insert object into database", e);
		}
	}

	public <T> List<T> select(Class<T> clazz) throws SORMException {
		return select(clazz, null);
	}

	public <T> List<T> select(Class<T> clazz, String condition) throws SORMException {
		String command = getSelectString(clazz);
		if (condition != null)
			command += " " + condition;

		if (DEBUG)
			System.out.println(command);

		List<T> objects = new ArrayList<>();
		try (Statement s = connection.createStatement(); ResultSet rs = s.executeQuery(command)) {
			while (rs.next()) {
				T object = clazz.newInstance();

				int index = 1;
				for (Field field : clazz.getDeclaredFields()) {
					field.setAccessible(true);
					Object value = rs.getObject(index);
					field.set(object, value);
					index++;
				}

				objects.add(object);
			}
		} catch (Exception e) {
			throw new SORMException("Cannot select objects from database", e);
		}
		return objects;
	}

	private String getSQLType(Class<?> clazz) {
		switch (clazz.getSimpleName()) {
		case "String":
			return "VARCHAR(64)";
		case "int":
			return "INTEGER";
		case "long":
			return "INTEGER";
		default:
			throw new IllegalArgumentException("Un. java type " + clazz.getCanonicalName());
		}
	}
}
