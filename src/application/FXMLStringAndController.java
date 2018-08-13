package application;

public class FXMLStringAndController {
	private String fxmlString;
	private Object controller;
	
	public FXMLStringAndController(String fxmlString, Object controller) {
		this.fxmlString = fxmlString;
		this.controller = controller;
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean isEqual = false;
		
		if (obj == null || !(obj instanceof FXMLStringAndController)) {
			return isEqual;
		}
		
		FXMLStringAndController fxmlStringAndControllerObj = (FXMLStringAndController) obj;
		
		if (this.fxmlString.equals(fxmlStringAndControllerObj.fxmlString)) {
			isEqual = true;
		} 
		
		return isEqual;
	}
	
	@Override
	public int hashCode() {
		return fxmlString.hashCode();
	}
	
	public String getFxmlString() {
		return fxmlString;
	}

	public Object getController() {
		return controller;
	}
}
