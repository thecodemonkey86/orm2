package cpp.entity;

public class SetterValidator {

	public enum OnFailValidateMode{Ignore,ThrowException,ReturnFalse;

	public static OnFailValidateMode fromString(String onFail) {
		if(onFail != null) {
			if(onFail.equals("throwException")) {
				return ThrowException;
			} else if(onFail.equals("returnFalse")) {
				return ReturnFalse;
			}  else if(onFail.equals("ignore")) {
				return Ignore;
			}
		}
		return Ignore;
	}}
	String condition;
	OnFailValidateMode onFail;
	String exceptionMessage;
	
	public SetterValidator(String condition,OnFailValidateMode onFail,String exceptionMessage) {
		this.condition = condition;
		this.onFail = onFail;
		this.exceptionMessage = exceptionMessage;
	}

	public String getCondition() {
		return condition;
	}
	
	public String getExceptionMessage() {
		return exceptionMessage;
	}
	public OnFailValidateMode onFailMode() {
		return onFail;
	}
	
}
