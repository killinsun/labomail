package transceiver;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;

class TextCheckerRenderer extends JPanel implements ListCellRenderer {
	private Border focusBorder; // = new DotBorder(new
								// Color(~list1.getSelectionBackground().getRGB()),2);
	private static final Border NOMAL_BORDER = 
			BorderFactory.createEmptyBorder(2, 2, 2, 2);
	private static final Color EVEN_COLOR = new Color(230, 255, 230);

	private JCheckBox checkBox = new JCheckBox("");
	private JTextArea textArea = new JTextArea();
	private JLabel icon = new JLabel();
	
	public TextCheckerRenderer() {

		super(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		setOpaque(false);
		add(textArea);
		
//		JLabel label = icon;
		add(checkBox, BorderLayout.WEST);
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object object,
			int index, boolean isSelected, boolean cellHasFocus) {

		textArea.setText((object == null) ? "" : object.toString());
//		JLabel label = icon;
		
		MailObject mail = (MailObject)object;
		
		switch (mail.status) {
		case RECEIVE:
//			label.setIcon(new ImageIcon("data/receive2.png"));
			break;
		case SENT:
//			label.setIcon(new ImageIcon("data/sent2.png"));
			break;
		case DRAFT:
//			label.setIcon(new ImageIcon("data/not_send2.png"));
			break;

		default:
//			label.setIcon(new ImageIcon("data/other2.png"));
			break;
		}

		if (focusBorder == null) {
			focusBorder = new DotBorder(new Color(list
					.getSelectionBackground().getRGB()), 2);
		}
		if (isSelected) {
			textArea.setBackground(list.getSelectionBackground());
			textArea.setForeground(list.getSelectionForeground());
		} else {
			textArea.setBackground(index % 2 == 0 ? EVEN_COLOR : list.getBackground());
			textArea.setForeground(list.getForeground());
		}
		textArea.setBorder(cellHasFocus ? focusBorder : NOMAL_BORDER);
		return this;
	}
}