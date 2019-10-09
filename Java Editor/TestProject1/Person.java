public class Person {
	private String fName;
	private String lName;
	private Report grades;
	public Person(String firstName, String lastName) {
		fName = firstName;
		lName = lastName;
	}
	public Person(String firstName, String lastName, Report grade) {
		this(firstName, lastName);
		grades = grade;
	}
	
	public void updateGrades(Report grade){
		grades = grade;
	}
	public String toString(){
		if(grades == null){
			return fName + " " + lName + " does not have a grade report yet."	;
		}
		else{
			return fName + " " + lName + " has a report of: " + grades;
		}
	}
}