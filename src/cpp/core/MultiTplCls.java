package cpp.core;

import java.util.ArrayList;

import codegen.CodeUtil;

public class MultiTplCls extends TplCls {

	ArrayList<Type> moreTplTypes;
	
	public MultiTplCls(String name, Type... elements) {
		super(name, elements[0]);
		moreTplTypes=new ArrayList<>();
		for(int i=1;i<elements.length;i++) {
			moreTplTypes.add(elements[i]);
		}
	}
	
	private ArrayList<String> tplTypesUsageStrings() {
		ArrayList<String> tplTypes=new ArrayList<>();
		tplTypes.add(element.toUsageString());
		for(Type tplType:this.moreTplTypes) {
			tplTypes.add(tplType.toUsageString());
		}
		return tplTypes;
	}
	@Override
	public String toUsageString() {
		ArrayList<String> tplTypes=tplTypesUsageStrings();
		String str= constness 
				? CodeUtil.sp("const",type,isPtr()?"*":"") 
				: 
					isPtr()
						? type+ CodeUtil.abr(CodeUtil.commaSep(tplTypes))+"*"
						:type+ CodeUtil.abr(CodeUtil.commaSep(tplTypes));
		return (useNamespace != null) ? useNamespace+"::"+ str : str;
		
	}
	
	@Override
	public String getConstructorName() {
		ArrayList<String> tplTypes=tplTypesUsageStrings();
		return type+ CodeUtil.abr(CodeUtil.commaSep(tplTypes));
	}
	
	@Override
	public String toString() {
		ArrayList<String> tplTypes=tplTypesUsageStrings();
		return type + CodeUtil.abr(CodeUtil.commaSep(tplTypes));
	}

}
