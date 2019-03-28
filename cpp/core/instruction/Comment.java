package cpp.core.instruction;

public class Comment extends Instruction{
	protected String comment;
	public Comment(String comment) {
		this.comment = comment;
	}
	
	@Override
	public String toString() {
		return "//"+ comment;
	}
}
