package zillow;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
	
	// Address info
	static String number = null;
	static String street = "n congress";
	static String city = "ypsilanti";
	static String state = "mi";	
	static String zip = "48197";	
	
	static ArrayList<Property> properties = new ArrayList<Property>();	

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		
		// Set the path to store the XML data
		String xmlPath = null;
		xmlPath = "c:\\Users\\Michael\\Desktop\\Zillow\\zillow.xml";
		if(xmlPath == null){
			System.out.println("Epic fail. Set your xmlPath.");
			return;
		}			
		
		// Get the range of addresses
		int startAddr = 0;
		int endAddr = 0;
		try {
			street = Input.getString("Street");
			city = Input.getString("City");;
			state = Input.getString("State");;	
			zip = Input.getString("Zip");;	
			startAddr = Input.getInt("Start");
			endAddr = Input.getInt("End");
		} catch (IOException e) {
			System.out.println("Bad integer input!!!");
			e.printStackTrace();
		}		
		
		// Get pricing info for range of addresses
		for(int addr = startAddr; addr <= endAddr; addr += 2){			
			Property thisP = new Property(Integer.toString(addr), street, city, state, zip);
			properties.add(thisP);			
			XML.getXMLFromAPICall(API.generateCall(thisP), xmlPath);	
			XML.read(xmlPath, thisP);
		}
		
		// Find the property with the best price to rent ratio
		float minRatio = 0;
		int minRatioIndex = 0;
		for(int i = 0; i < properties.size(); i++){
			Property thisP = properties.get(i);			
			// Skip properties where value information is missing
			if(thisP.getPEst() == 0)
				continue;
			if(thisP.getPriceRentRatio() < minRatio || minRatio == 0){
				minRatio = thisP.getPriceRentRatio();
				minRatioIndex = i;
			}
		}
		
		// Print information for best property
		Property thisP = properties.get(minRatioIndex);
		System.out.println("");
		System.out.println("----------------------------------");
		System.out.println("Best property: " + thisP.getFullAddress());
		System.out.println("Price: low=$" + thisP.getPLow() + " estimate=$" + thisP.getPEst() + " high=$" + thisP.getPHigh() + " range=$1" + thisP.getPRange());
		System.out.println("Rent: low=$" + thisP.getRLow() + " estimate=$" + thisP.getREst() + " high=$" + thisP.getRHigh() + " range=$" + thisP.getRRange());
		float down = 0.2f;
		int invest = (int)(thisP.getPEst() * down);
		System.out.println("Estimated investment: $" + invest);
		System.out.println("P/R ratio: " + thisP.getPriceRentRatio() + " I/R ratio: " +  ((float)invest / ((float)thisP.getREst() * 12)));
		System.out.println("");		
	}
}