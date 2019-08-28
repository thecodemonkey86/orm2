package php.core.instruction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import codegen.CodeUtil;
import php.core.AbstractPhpCls;
import php.core.expression.Expression;

public class SwitchBlock extends Instruction{
	private List<CaseBlock> cases;
	private DefaultCaseBlock defaultCase;
	protected Expression switchExpression;
	private AbstractPhpCls parent;
	
	public SwitchBlock(Expression switchExpression) {
		cases = new ArrayList<>();
		this.switchExpression = switchExpression;
	}
	public SwitchBlock(Expression switchExpression,CaseBlock...caseBlocks) {
		cases = new ArrayList<>(Arrays.asList(caseBlocks));
		this.switchExpression = switchExpression;
	}
	public SwitchBlock(Expression switchExpression,List<CaseBlock>caseBlocks) {
		cases = caseBlocks;
		this.switchExpression = switchExpression;
	}
	
	public void addCase(CaseBlock c) {
		this.cases.add(c);
	}
	
	public void setDefaultCase(DefaultCaseBlock defaultCase) {
		this.defaultCase = defaultCase;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("switch");
		sb.append(CodeUtil.parentheses(switchExpression))
		.append(" {\n");
		for (CaseBlock caseBlock : cases) {
			CodeUtil.writeLine(sb, caseBlock);
		}
		if(defaultCase != null)
			CodeUtil.writeLine(sb, defaultCase);
		CodeUtil.writeLine(sb, "}");
		return sb.toString();
	}
	
	public CaseBlock _case(Expression condition) {
		
		CaseBlock caseBlock = new CaseBlock();
		caseBlock.setParent(parent);
		caseBlock.addCondition(condition);
		addCase(caseBlock);
		return caseBlock;
	}
	
	public CaseBlock _case(Expression...conditions) {
		
		CaseBlock caseBlock = new CaseBlock(Arrays.asList(conditions));
		addCase(caseBlock);
		return caseBlock;
	}
	
	public DefaultCaseBlock _default() {
		DefaultCaseBlock defaultCaseBlock = new DefaultCaseBlock();
		setDefaultCase(defaultCaseBlock);
		return defaultCaseBlock;
	}
	
	public void setStandardDefaultCase() {
		DefaultCaseBlock defaultCaseBlock = new DefaultCaseBlock();
		defaultCaseBlock._break();
		setDefaultCase(defaultCaseBlock);
		
	}
	public void setParent(AbstractPhpCls parent) {
		this.parent = parent;
		
	}
	
	public DefaultCaseBlock getDefaultCase() {
		return defaultCase;
	}
	
	
	
}
