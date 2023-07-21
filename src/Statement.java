import java.util.ArrayList;

public class Statement {
	private TypeStatement typeStatement;
	private ArrayList<String> listEleInt;
	private Property property;
	private boolean notProp;
	private Context context;
	
	public Statement(TypeStatement typeStatement) {
		this.typeStatement = typeStatement;
		listEleInt = new ArrayList<>();
		property = Property.UNDEFINED;
		notProp = false;
		context = Context.UNDEFINED;
	}
	
	public void setTypeStatement(TypeStatement typeStatement) {
		this.typeStatement = typeStatement;
	}
	
	public TypeStatement getTypeStatement() {
		return typeStatement;
	}
	
	public void addEleInt(String eleInt) {
		listEleInt.add(eleInt);
	}
	
	public ArrayList<String> getListEleInt(){
		return listEleInt;
	}
	
	public String listEleIntToString() {
		String eleIntString = "";
		if (!listEleInt.isEmpty()) {
			for (String ele: listEleInt) {
				eleIntString += ele + ", ";
			}
		}
		eleIntString = "{" + eleIntString.substring(0, eleIntString.length()-2) + "}";
		
		return eleIntString;
	}
	
	public void setProperty(Property property) {
		this.property = property;
	}
	
	public Property getProperty() {
		return property;
	}
	
	public void setNotProp(Boolean bool) {
		notProp = bool;
	}
	
	public Boolean getNotProp() {
		return notProp;
	}	
	
	public void setContext(Context context) {
		this.context = context;
	}
	
	public Context getContext() {
		return context;
	}
	
	public void clear() {
		typeStatement = TypeStatement.UNDEFINED;
		listEleInt.clear();
		property = Property.UNDEFINED;
		notProp = false;
		context = Context.UNDEFINED;
	}
}
