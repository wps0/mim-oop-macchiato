package pl.wieczorekp.po1;

import pl.wieczorekp.po1.instructions.expressions.*;
import pl.wieczorekp.po1.instructions.statements.*;
import pl.wieczorekp.po1.instructions.statements.IfStatement.Condition;

import static pl.wieczorekp.po1.instructions.expressions.LiteralExpression.ONE_LITERAL;
import static pl.wieczorekp.po1.instructions.expressions.LiteralExpression.ZERO_LITERAL;

public class Main {

    public static void executeSampleProgram() {
        // begin block
        CodeBlock root = new CodeBlock(null);

        // var n 30
        VariableExpression nVar = new VariableExpression("n");
        AssignmentStatement n30 = new AssignmentStatement(root,
                "n",
                new LiteralExpression(30));
        root.addStatement(n30);

        // for k n-1
        CodeBlock forBody = new CodeBlock(root);
        VariableExpression kVar = new VariableExpression("k");
        ForStatement forKN = new ForStatement(root,
                "k",
                new SubExpression(nVar, new LiteralExpression(1)),
                forBody);
        root.addStatement(forKN);

        // begin block
        // var p 1
        VariableExpression pVar = new VariableExpression("p");
        AssignmentStatement p1 = new AssignmentStatement(forBody, "p", ONE_LITERAL);
        forBody.addStatement(p1);

        // k := k+2
        AssignmentStatement kPlus2 = new AssignmentStatement(forBody, "k", new SumExpression(kVar,
                new LiteralExpression(2)));
        forBody.addStatement(kPlus2);

        // for i k-2
        CodeBlock forIKSub2Body = new CodeBlock(forBody);
        VariableExpression iVar = new VariableExpression("i");
        ForStatement forIKSub2 = new ForStatement(forBody,
                "i",
                new SubExpression(kVar, new LiteralExpression(2)),
                forIKSub2Body);
        forBody.addStatement(forIKSub2);

        // i := i+2
        AssignmentStatement iEqIPlus2 = new AssignmentStatement(forIKSub2Body,
                "i",
                new SumExpression(iVar, new LiteralExpression(2)));
        forIKSub2Body.addStatement(iEqIPlus2);

        // if k % i == 0
        ModExpression kModI = new ModExpression(kVar, iVar);
        CodeBlock ifKModIBranchBlock = new CodeBlock(forIKSub2Body);
        IfStatement ifKModI = new IfStatement(forIKSub2Body,
                kModI,
                ZERO_LITERAL,
                Condition.EQ,
                ifKModIBranchBlock,
                null);
        forIKSub2Body.addStatement(ifKModI);

        // p := 0
        ifKModIBranchBlock.addStatement(new AssignmentStatement(ifKModIBranchBlock, "p", ZERO_LITERAL));

        // if p == 0
        CodeBlock ifPEq0Branch = new CodeBlock(forBody);
        IfStatement ifPEq0 = new IfStatement(forBody, pVar, ONE_LITERAL, Condition.EQ, ifPEq0Branch, null);
        forBody.addStatement(ifPEq0);

        // print k
        ifPEq0Branch.addStatement(new PrintStatement(ifPEq0Branch, kVar));

        DebuggerExecution program = new DebuggerExecution(root);
        program.execute();
    }

    public static void main(String[] args) {
        executeSampleProgram();
    }
}
