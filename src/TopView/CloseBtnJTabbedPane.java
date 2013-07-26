package TopView;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/** 閉じるボタン付きJTabbedPane */
public class CloseBtnJTabbedPane extends JTabbedPane {

	private Icon icon = new ImageIcon("data/batten_.gif");
	private Dimension buttonSize = new Dimension(16, 16);

	@Override
	public void addTab(String title, final Component component) {
		JPanel tab = new JPanel(new BorderLayout());
		tab.setOpaque(false);
		JLabel label = new JLabel(title);
		label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 4));
		JButton button = new JButton(icon);
		button.setPreferredSize(buttonSize);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeTabAt(indexOfComponent(component));
			}
		});
		tab.add(label, BorderLayout.WEST);
		if( !(component.getName().equals("TOP")) ) {
			tab.add(button, BorderLayout.EAST);
		}
		tab.setBorder(BorderFactory.createEmptyBorder(2, 1, 1, 1));
		super.addTab(null, component);
		setTabComponentAt(getTabCount() - 1, tab);
	}
}
