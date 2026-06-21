package tp.tp_interprete;

import tp.tp_interprete.MiniLangParser.*;

public class SemanticAnalyzer extends MiniLangBaseVisitor<Object> {
    
    private SymbolTable symbolTable = new SymbolTable();

    @Override
    public Object visitDeclaration(DeclarationContext ctx) {
        String type = ctx.TYPE().getText(); // "int", "float", etc.
        String id = ctx.ID().getText();     // nombre de la variable
        
        symbolTable.declaration(id, type);
        
        return null; 
    }


    @Override
    public Object visitAssignment(AssignmentContext ctx) {
        String id = ctx.ID().getText();
        
        Object value = visit(ctx.expression());
        
        // llamas al metodo de asignación
        symbolTable.assign(id, value);
        
        return null;
    }
}