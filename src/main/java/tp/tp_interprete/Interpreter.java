package tp.tp_interprete;

import tp.tp_interprete.MiniLangParser.*;


public class Interpreter extends MiniLangBaseVisitor<Object>{
	private SymbolTable symbolTable = new SymbolTable();
	
	//programa completo 
	@Override
	public Object visitProgram (ProgramContext progCtx) {
		//print para ver inicializacion de ejecucion
		System.out.println("Intérprete, ejecución inicio con exito");
		
		for (SentenceContext sentenceCtx: progCtx.sentence()) {
			visit(sentenceCtx);//ejecuta linea por linea
		}
		return null;
	}
	
	//declaracion, llama metodo y registra la variable
	@Override
	public Object visitDeclaration (DeclarationContext decCtx) {
		String id = decCtx.ID().getText();
		String type = decCtx.TYPE().getText();
		symbolTable.declaration(id, type);
		
		return null;
	}
	
	//asignacion de valores
	@Override
	public Object visitAssignment (AssignmentContext assigCtx) {
		String id = assigCtx.ID().getText();
		Object realVal = visit(assigCtx.expression());
		symbolTable.assign(id, realVal);
		return null;
	}
	
	//print
	@Override
	public Object visitPrint(PrintContext printCtx) {
		Object val = visit(printCtx.expression());
		System.out.println("> " + val);
		return null;
	}
	
	//las etiquetas con # del minilang
	//#parentesis
	@Override
	public Object visitParenthesis (ParenthesisContext parCtx) {
		return visit(parCtx.expression());
	}
	
	//aritmeticas
	// suma o resta
	@Override
	public Object visitPlusMinus(PlusMinusContext pluMinCtx) {
		Object izq = visit(pluMinCtx.expression(0));
		Object der = visit(pluMinCtx.expression(1));
		String opcion = pluMinCtx.getChild(1).getText();
		
		if (izq instanceof Integer && der instanceof Integer) {
			return opcion.equals("+") ? (Integer) izq + (Integer) der : (Integer) izq - (Integer) der;
		} else {
			double i = (izq instanceof Integer) ? (Integer) izq : (Double) izq;
			double d = (der instanceof Integer) ? (Integer) der : (Double) der;
			return opcion.equals("+") ? i + d : i - d;
		}
	}
	
	// mult o div
	@Override
	public Object visitMultiplicationDivision(MultiplicationDivisionContext multDivCtx) {
		Object izq = visit(multDivCtx.expression(0));
		Object der = visit(multDivCtx.expression(1));
		String opcion = multDivCtx.getChild(1).getText();
		
		if (izq instanceof Integer && der instanceof Integer) {
			return opcion.equals("*") ? (Integer) izq * (Integer) der : (Integer) izq / (Integer) der;
		} else {
			double i = (izq instanceof Integer) ? (Integer) izq : (Double) izq;
			double d = (der instanceof Integer) ? (Integer) der : (Double) der;
			return opcion.equals("*") ? i * d : i / d;
		}
	}
	
	//logicas
	//and or
	@Override
	public Object visitLogic(LogicContext logCtx) {
		boolean izq = (Boolean) visit(logCtx.expression(0));
		boolean der = (Boolean) visit(logCtx.expression(1));
		String opcion = logCtx.getChild(1).getText();
		
		return opcion.equals("&&") ? izq && der : izq || der;
	}
	
	//negaciuon
	@Override 
	public Object visitNegation (NegationContext negCtx) {
		boolean valor = (Boolean) visit(negCtx.expression());
		return !valor;
	}
	
	//comparaciones
	@ Override 
	public Object visitComparison (ComparisonContext compCtx) {
		double izq = ((Number) visit(compCtx.expression(0))).doubleValue();
		double der = ((Number) visit(compCtx.expression(1))).doubleValue();
		String opcion = compCtx.getChild(1).getText();
	
		
		switch (opcion) {
			case ">": return izq > der;
			case "<": return izq < der;
			case ">=": return izq >= der;
			case "<=": return izq <= der;
			case "==": return izq == der;
			case "!=": return izq != der;
			default: return false;
		}
	}
	
	//variable
	@Override
	public Object visitVariable (VariableContext varCtx) {
		return symbolTable.get(varCtx.ID().getText());
	}
	
	//tipos de datos
	//int
	@Override
	public Object visitInteger (IntegerContext intCtx) {
		return Integer.parseInt(intCtx.INT().getText());
	}
	
	//float - decimal
	@Override
	public Object visitDecimal (DecimalContext decCtx) {
		return Double.parseDouble(decCtx.FLOAT().getText());
	}
	
	//string
	@Override
	public Object visitString (StringContext stringCtx) {
		String texto = stringCtx.STRING().getText();
		return texto.substring(1, texto.length() - 1); //sirve para sacarle las comillas
	}
	
	//bool
	@Override
	public Object visitBoolean (BooleanContext boolCtx) {
		return Boolean.parseBoolean(boolCtx.BOOLEAN().getText());
	}
}
