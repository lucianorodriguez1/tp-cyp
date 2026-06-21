package tp.tp_interprete;

import tp.tp_interprete.MiniLangParser.*;

public class SemanticAnalyzer extends MiniLangBaseVisitor<String> {
    
    private SymbolTable symbolTable = new SymbolTable();

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
            throw new RuntimeException("Error semántico: No se puede asignar '" + expressionType + 
                                       "' a la variable '" + id + "' de tipo '" + variableType + "'.");
        }
        
        return null;
    }
    
    // 3. PROPAGACIÓN DE TIPOS DESDE LAS HOJAS
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
    
    
    
}