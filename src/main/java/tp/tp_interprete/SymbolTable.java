package tp.tp_interprete;
import java.util.HashMap;

public class SymbolTable {
	private HashMap<String, Object> values = new HashMap<>(); //guarda los valores reales asignados
	private HashMap<String, String> types = new HashMap<>(); //guarda el tipo con el nombre dfe la variable
	
	
	//asignacion
	public void assign (String name, Object newValue) {
		//NO SE PUEDE USAR SI NO ESTA DECLARADA
		if (!values.containsKey(name)) {
			throw new RuntimeException ("Error la variable " + name + " no fue declarada");
		}
		
		String type = types.get(name);
		//validacion de tipos
		if (newValue != null) {
			if (type.equals("int") && !(newValue instanceof Integer)) {
				throw new RuntimeException("Error de tipo, no se puede asignar un valor que no sea entero a " + name);
			}
			if (type.equals("float") && !(newValue instanceof  Double || newValue instanceof Float)) {
				throw new RuntimeException ("Error de tipo, no se puede asignar un valor que no sea decimal a " + name);
			}
			if (type.equals("string") && !(newValue instanceof String)) {
				throw new RuntimeException ("Error de tipo, no se puede asignar un valor que no sea texto a "+ name);
			}
			if (type.equals("boolean") && !(newValue instanceof Boolean)) {
				throw new RuntimeException ("Error de tipo, no se puede asignar un booleano a " + name);
			}
			
			//si paso todos los controles se guarda el valor
			values.put(name, newValue);
		}
	}
	
	//declaracion 
	public void declaration(String name, String type) {
		//no redeclarar si ya existe
		if (values.containsKey(name) && values.get(name)!= null) {
			throw new RuntimeException ("Error la variable " + name + " ya existe" );
		}
			
		//guardo en los hashmap
		types.put(name, type); //guarda tipo
		values.put(name, null); //empieza en nulo
	}
	
	//no se puede usar si no fue declarada
	//obtener el valor para por ej un print
	public Object get(String name) {
		if (!values.containsKey(name)) {
			throw new RuntimeException ("Error la variable " + name + " no esta declarada");
		}
		return values.get(name);
	}
	
	public String getType(String name) { 
		return types.get(name); 
	}

}
