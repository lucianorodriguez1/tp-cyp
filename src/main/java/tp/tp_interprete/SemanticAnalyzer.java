package tp.tp_interprete;

import tp.tp_interprete.MiniLangParser.*;

public class SemanticAnalyzer extends MiniLangBaseVisitor<String> {

	//agrego constructor para pasar una unica tabla creada en el app.java
	private SymbolTable symbolTable;
	
	public SemanticAnalyzer (SymbolTable symbolTable) {
		this.symbolTable = symbolTable;
	}
	
	// 0. RECORRIDO DEL PROGRAMA
    @Override
    public String visitProgram(ProgramContext ctx) {
        for (SentenceContext sentenceCtx : ctx.sentence()) {
            visit(sentenceCtx); 
        }
        return null; 
    }
	

	// 1. REGISTRO DE VARIABLES
	@Override
	public String visitDeclaration(DeclarationContext ctx) {
		String type = ctx.TYPE().getText();
		String id = ctx.ID().getText();

		symbolTable.declaration(id, type);

		return null;
	}

	// 2. CONTROL DE ASIGNACIÓN
	@Override
	public String visitAssignment(AssignmentContext ctx) {
		String id = ctx.ID().getText();

		String variableType = symbolTable.getType(id);
		if (variableType == null) {
			throw new RuntimeException("Error semántico: La variable '" + id + "' no ha sido declarada.");
		}

		String expressionType = visit(ctx.expression());

		if (!variableType.equals(expressionType)) {
			throw new RuntimeException("Error semántico: No se puede asignar '" + expressionType + "' a la variable '"
					+ id + "' de tipo '" + variableType + "'.");
		}

		return null;
	}
	
	// 3 CONTROL DE ESTRUCTURAS (IF y SWITCH)
    @Override
    public String visitCondition(ConditionContext ctx) {
        String conditionType = visit(ctx.expression());
        if (!conditionType.equals("boolean")) {
            throw new RuntimeException("Error semántico: La condición del 'if' debe ser boolean.");
        }
        for (SentenceContext sentenceCtx : ctx.sentence()) {
            visit(sentenceCtx); 
        }
        return null;
    }

    @Override
    public String visitSwitch(SwitchContext ctx) {
        String switchType = visit(ctx.expression());
        
        for (Cases_swContext caseCtx : ctx.cases_sw()) {
            String caseExprType = visit(caseCtx.expression());
            if (!switchType.equals(caseExprType)) {
                throw new RuntimeException("Error semántico: El 'case' (" + caseExprType + 
                                           ") no coincide con el tipo del 'switch' (" + switchType + ").");
            }
            for (SentenceContext sentenceCtx : caseCtx.sentence()) {
                visit(sentenceCtx);
            }
        }
        if (ctx.default_sw() != null) {
            visit(ctx.default_sw());
        }
        return null;
    }

    @Override
    public String visitDefault_sw(Default_swContext ctx) {
        for (SentenceContext sentenceCtx : ctx.sentence()) {
            visit(sentenceCtx);
        }
        return null;
    }
	
	

	// 4. PROPAGACIÓN DE TIPOS DESDE LAS HOJAS
	@Override
	public String visitInteger(IntegerContext ctx) {
		return "int";
	}

	@Override
	public String visitDecimal(DecimalContext ctx) {
		return "float";
	}

	@Override
	public String visitString(StringContext ctx) {
		return "string";
	}

	@Override
	public String visitBoolean(BooleanContext ctx) {
		return "boolean";
	}

	@Override
	public String visitVariable(VariableContext ctx) {
		String id = ctx.ID().getText();
		String type = symbolTable.getType(id);
		if (type == null) {
			throw new RuntimeException("Error semántico: La variable '" + id + "' no existe.");
		}
		return type;
	}

	// 5. VALIDACIÓN EN EXPRESIONES
	@Override
	public String visitParenthesis(ParenthesisContext ctx) {
		return visit(ctx.expression());
	}

	@Override
	public String visitPlusMinus(PlusMinusContext ctx) {
		String izq = visit(ctx.expression(0));
		String der = visit(ctx.expression(1));

		// Regla semántica:
		// int + int = int
		// float + float = float
		// NO SE PERMITE SUMAR int + float y viceversa.
		if (izq.equals("int") && der.equals("int"))
			return "int";
		if (izq.equals("float") && der.equals("float"))
			return "float";

		throw new RuntimeException("Error semántico: Operación aritmética no permitida entre " + izq + " y " + der);
	}

	@Override
	public String visitMultiplicationDivision(MultiplicationDivisionContext ctx) {
		String izq = visit (ctx.expression(0));
		String der = visit (ctx.expression(1));
		
		if (izq.equals("int")&& der.equals("int")) {
			return "int";
		}
		if (izq.equals("float")&& der.equals("float")) {
			return "float";
		}
		throw new RuntimeException ("Error semántico: Operación aritmética no permitida entre " + izq + " y " + der);
		// Usa exactamente las mismas reglas de tipos que la suma y resta
		//return visitPlusMinus((PlusMinusContext) (Object) ctx);
	}

	@Override
	public String visitComparison(ComparisonContext ctx) {
		String izq = visit(ctx.expression(0));
		String der = visit(ctx.expression(1));

		// Solo permitimos comparar números entre sí (int o float)
		boolean esIzqNum = izq.equals("int") || izq.equals("float");
		boolean esDerNum = der.equals("int") || der.equals("float");

		if (esIzqNum && esDerNum) {
			return "boolean";
		}
		throw new RuntimeException("Error semántico: No se pueden comparar los tipos " + izq + " y " + der);
	}

	@Override
	public String visitLogic(LogicContext ctx) {
		String izq = visit(ctx.expression(0));
		String der = visit(ctx.expression(1));

		// AND y OR solo operan entre expresiones booleanas
		if (izq.equals("boolean") && der.equals("boolean")) {
			return "boolean";
		}
		throw new RuntimeException("Error semántico: Operadores lógicos requieren operandos de tipo boolean.");
	}

	@Override
	public String visitNegation(NegationContext ctx) {
		String tipo = visit(ctx.expression());
		if (!tipo.equals("boolean")) {
			throw new RuntimeException("Error semántico: El operador '!' solo se aplica a booleanos.");
		}
		return "boolean";
	}

	// 6. SENTENCIAS COMPLEMENTARIAS
	@Override
	public String visitPrint(PrintContext ctx) {
		//evaluamos que todas las instrucciones del print sean validas por las dudas
		for (ExpressionContext expCtx: ctx.expression()) {
			String tipo = visit(expCtx);
			if (tipo == null) {
				throw new RuntimeException("Error, elemento invalido en el print");
			}
		}
		return null;
	}

}