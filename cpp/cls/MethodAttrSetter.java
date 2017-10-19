package cpp.cls;

//import cpp.Types;
//import util.StringUtil;
//
//public class MethodAttrSetter extends Method{
//	protected Attr attr;
//	
//	public MethodAttrSetter(Attr attr ) {
//		super(Public, Types.Void, "set"+StringUtil.ucfirst(attr.getName()));
//		addParam(new Param(attr.getType().isPrimitiveType()
//				||attr.getType() instanceof RawPtr ?  attr.getType() : attr.getType().toConstRef(), attr.getName()));
//		this.attr=attr;
//	}
//
//	@Override
//	public void addImplementation() {
//	 addInstr(_this().accessAttr(attr).assign(getParam(attr.getName())));
//		
//	}
//
//}
