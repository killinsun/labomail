package TopView;

import javax.swing.Icon;

public class IconSet {

	Icon defaultImage;
	Icon onMouseImage;
	
	public IconSet(Icon defIcon, Icon onMouseIcon) {

		defaultImage = defIcon;
		onMouseImage = onMouseIcon;
	}
	
	public Icon getDefault() {
		return defaultImage;
	}
	
	public Icon getOnMouse() {
		return onMouseImage;
	}
}
