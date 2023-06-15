package pl.wieczorekp.macchiato;

import pl.wieczorekp.macchiato.builders.BlockBuilder;
import pl.wieczorekp.macchiato.instructions.expressions.*;
import pl.wieczorekp.macchiato.instructions.statements.*;
import pl.wieczorekp.macchiato.builders.IfBuilder;
import pl.wieczorekp.po1.instructions.expressions.*;
import pl.wieczorekp.po1.instructions.statements.*;

import java.util.List;

import static pl.wieczorekp.macchiato.instructions.expressions.LiteralExpression.ONE_LITERAL;
import static pl.wieczorekp.macchiato.instructions.expressions.LiteralExpression.ZERO_LITERAL;

/*
 * Class members order:
 *  - static final vars
 *  - static vars
 *  - final vars
 *  - other vars
 *  - constructors
 *  - abstract methods
 *  - other methods
 *  - static methods
 *  - overloaded methods (overloaded from classes other than Object; overloaded from Object class)
 *  - nested classes
 *  - nested enums
 * All the blocks should be ordered by visibility (namely: public, protected, package-private, private)
 *
 */

public class Main {

    public static CodeBlock buildSampleProgramPrimesWithoutBuilders(int ub) {
        // Without using builders:
        // begin block
        CodeBlock root = new CodeBlock(null);

        // var n 30
        VariableExpression nVar = new VariableExpression("n");
        AssignmentStatement n30 = new VariableDeclarationStatement(root,
                "n",
                new LiteralExpression(ub));
        root.appendStatement(n30);

        // for k n-1
        CodeBlock forBody = new CodeBlock(root);
        VariableExpression kVar = new VariableExpression("k");
        ForStatement forKN = new ForStatement(root,
                "k",
                new SubExpression(nVar, new LiteralExpression(1)),
                forBody);
        root.appendStatement(forKN);

        // begin block
        // var p 1
        VariableExpression pVar = new VariableExpression("p");
        AssignmentStatement p1 = new VariableDeclarationStatement(forBody, "p", ONE_LITERAL);
        forBody.appendStatement(p1);

        // k := k+2
        AssignmentStatement kPlus2 = new AssignmentStatement(forBody, "k", new SumExpression(kVar,
                new LiteralExpression(2)));
        forBody.appendStatement(kPlus2);

        // for i k-2
        CodeBlock forIKSub2Body = new CodeBlock(forBody);
        VariableExpression iVar = new VariableExpression("i");
        ForStatement forIKSub2 = new ForStatement(forBody,
                "i",
                new SubExpression(kVar, new LiteralExpression(2)),
                forIKSub2Body);
        forBody.appendStatement(forIKSub2);

        // i := i+2
        AssignmentStatement iEqIPlus2 = new AssignmentStatement(forIKSub2Body,
                "i",
                new SumExpression(iVar, new LiteralExpression(2)));
        forIKSub2Body.appendStatement(iEqIPlus2);

        // if k % i == 0
        ModExpression kModI = new ModExpression(kVar, iVar);
        CodeBlock ifKModIBranchBlock = new CodeBlock(forIKSub2Body);
        IfStatement ifKModI = new IfStatement(forIKSub2Body,
                kModI,
                ZERO_LITERAL,
                IfStatement.Condition.EQ,
                ifKModIBranchBlock,
                null);
        forIKSub2Body.appendStatement(ifKModI);

        // p := 0
        ifKModIBranchBlock.appendStatement(new AssignmentStatement(ifKModIBranchBlock, "p", ZERO_LITERAL));

        // if p == 0
        CodeBlock ifPEq0Branch = new CodeBlock(forBody);
        IfStatement ifPEq0 = new IfStatement(forBody, pVar, ONE_LITERAL, IfStatement.Condition.EQ, ifPEq0Branch, null);
        forBody.appendStatement(ifPEq0);

        // print k
        ifPEq0Branch.appendStatement(new PrintStatement(ifPEq0Branch, kVar));
        return root;
    }

    public static CodeBlock buildSampleProgramPrimesWithBuilders(int ub) {
        return new BlockBuilder()
                .declareVariable("n", LiteralExpression.of(ub))
                .loop("k",
                        SubExpression.of(VariableExpression.named("n"), LiteralExpression.of(1)),
                        new BlockBuilder()
                                .declareVariable("p", LiteralExpression.of(1))
                                .assign("k", SumExpression.of(VariableExpression.named("k"), LiteralExpression.of(2)))
                                .loop("i", SubExpression.of(VariableExpression.named("k"), LiteralExpression.of(2)), new BlockBuilder()
                                        .assign("i", SumExpression.of(VariableExpression.named("i"), LiteralExpression.of(2)))
                                        .ifElse(new IfBuilder()
                                                .leftOperand(ModExpression.of(VariableExpression.named("k"), VariableExpression.named("i")))
                                                .condition(IfStatement.Condition.EQ)
                                                .rightOperand(ZERO_LITERAL)
                                                .ifTrue(new BlockBuilder()
                                                        .assign("p", ZERO_LITERAL)
                                                        .build())
                                                .build())
                                        .build())
                                .ifElse(new IfBuilder()
                                        .leftOperand(VariableExpression.named("p"))
                                        .condition(IfStatement.Condition.EQ)
                                        .rightOperand(ONE_LITERAL)
                                        .ifTrue(new BlockBuilder()
                                                .print(VariableExpression.named("k"))
                                                .build())
                                        .build())
                                .build()
                )
                .build();
    }

    public static CodeBlock buildSampleProgramContainingMultipleBlocks() {
        return new BlockBuilder()
                .declareVariable("a", LiteralExpression.of(1))
                .print(VariableExpression.named("a"))
                .declareVariable("b", LiteralExpression.of(100))
                .block(new BlockBuilder()
                        .declareVariable("a", LiteralExpression.of(2))
                        .block(new BlockBuilder()
                                .declareVariable("a", LiteralExpression.of(3))
                                .print(VariableExpression.named("a"))
                                .print(VariableExpression.named("b"))
                                .build())
                        .print(VariableExpression.named("a"))
                        .build())
                .print(VariableExpression.named("a"))
                .build();
    }

    public static void executeSampleLoop() {
        CodeBlock root = new CodeBlock(null);

        CodeBlock forBody = new CodeBlock(root);
        ForStatement forLoop = new ForStatement(root, "i", new LiteralExpression(5), forBody);
        root.appendStatement(forLoop);

        forBody.appendStatement(new PrintStatement(forBody, new VariableExpression("i")));
        forBody.appendStatement(new PrintStatement(forBody, new VariableExpression("i")));

        Execution e = new Execution(root);
        e.run();
    }

    public static void executeSampleNestedLoop() {
        CodeBlock root = new CodeBlock(null);

        CodeBlock forBody = new CodeBlock(root);
        ForStatement forLoop = new ForStatement(root, "i", new LiteralExpression(5), forBody);
        root.appendStatement(forLoop);

        forBody.appendStatement(new PrintStatement(forBody, new VariableExpression("i")));

        CodeBlock nestedForBody = new CodeBlock(forBody);
        ForStatement nestedForLoop = new ForStatement(forBody, "j", new SumExpression(new LiteralExpression(1), new VariableExpression("i")), nestedForBody);
        forBody.appendStatement(nestedForLoop);

        nestedForBody.appendStatement(new PrintStatement(nestedForBody, new SumExpression(new LiteralExpression(10), new VariableExpression("j"))));


        Execution e = new Execution(root);
        e.run();
    }

    public static CodeBlock buildSampleProgramContainingProcedures() {
        return new BlockBuilder()
                .declareVariable("x", LiteralExpression.of(101))
                .declareVariable("y", LiteralExpression.of(1))
                .declareProcedure("out", List.of("a"), new BlockBuilder()
                        .print(SumExpression.of(VariableExpression.named("a"), VariableExpression.named("x")))
                        .build()
                )
                .assign("x", SubExpression.of(VariableExpression.named("x"), VariableExpression.named("y")))
                .invoke("out", List.of(VariableExpression.named("x")))
                .invoke("out", List.of(LiteralExpression.of(100)))
                .block(new BlockBuilder()
                        .declareVariable("x", LiteralExpression.of(10))
                        .invoke("out", List.of(LiteralExpression.of(100)))
                        .build()
                )
                .build();
    }

    public static void main(String[] args) {
//        DebuggerExecution dbg = new DebuggerExecution(buildSampleProgramPrimesWithBuilders());
        DebuggerExecution dbg = new DebuggerExecution(buildSampleProgramContainingProcedures());
        dbg.run();
    }
}
