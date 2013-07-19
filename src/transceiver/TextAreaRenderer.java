package transceiver;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;

class TextAreaRenderer extends JTextArea implements ListCellRenderer {
	private Border focusBorder; // = new DotBorder(new
								// Color(~list1.getSelectionBackground().getRGB()),2);
	private static final Border NOMAL_BORDER = 
			BorderFactory.createEmptyBorder(2, 2, 2, 2);
	private static final Color EVEN_COLOR = new Color(230, 255, 230);

	@Override
	public Component getListCellRendererComponent(JList list, Object object,
			int index, boolean isSelected, boolean cellHasFocus) {
		
		add(new JButton(""));
		// setLineWrap(true);
		if (focusBorder == null) {
			focusBorder = new DotBorder(new Color(~list
					.getSelectionBackground().getRGB()), 2);
		}
		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(index % 2 == 0 ? EVEN_COLOR : list.getBackground());
			setForeground(list.getForeground());
		}
		setBorder(cellHasFocus ? focusBorder : NOMAL_BORDER);
		setText((object == null) ? "" : object.toString());
		return this;
	}
}