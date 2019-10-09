public class Report {
	private double english;
	private double math;
	private double science;
	private double history;
	public Report(double engRep, double mathRep, double sciRep, double histRep){
		english = engRep;
		math = mathRep;
		science = sciRep;
		history = histRep;
	}
	
	public String toString(){
		return "\nEnglish: " + english + "\nMath: " + math + "\nScience: " + science + "\nHistory: " + history;
	}
}