package senderView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;

public class Inserter extends JFrame implements ActionListener {

	private GetResult calledForm;
	private JList<String> firstSyntax;
	private JList<String> secondSyntax;
	private JList<String> thirdSyntax;
	private String[][] stab = {
			{ "aaaだから、", "bbbなので、", "cccだけど、" },
			{ "aaaを、", "bbbが、", "cccで、" },
			{ "aaaする。", "bbbだった。", "cccかもしんない。" }
	};

	public Inserter(GetResult calledForm) {
		//結果の格納先を設定
		this.calledForm = calledForm;

		getContentPane().setLayout(new MigLayout("", "[200,grow][200,grow][400,grow]", "[grow][25]"));

		/* 1列目 */
		JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, "cell 0 0,grow");

		DefaultListModel<String> model = new DefaultListModel<String>();
		for(int i=0; i<stab[0].length; i++){
			model.addElement(stab[0][i]);
		}
		firstSyntax = new JList<String>(model);
		firstSyntax.setSelectedIndex(0);
		scrollPane.setViewportView(firstSyntax);


		/* 2列目 */
		JScrollPane scrollPane_1 = new JScrollPane();
		getContentPane().add(scrollPane_1, "cell 1 0,grow");

		model = new DefaultListModel<String>();
		for(int i=0; i<stab[0].length; i++){
			model.addElement(stab[1][i]);
		}
		secondSyntax = new JList<String>(model);
		secondSyntax.setSelectedIndex(0);
		scrollPane_1.setViewportView(secondSyntax);


		/* 3列目 */
		JScrollPane scrollPane_2 = new JScrollPane();
		getContentPane().add(scrollPane_2, "cell 2 0, grow, wrap");

		model = new DefaultListModel<String>();
		for(int i=0; i<stab[0].length; i++){
			model.addElement(stab[2][i]);
		}
		thirdSyntax = new JList<String>(model);
		thirdSyntax.setSelectedIndex(0);
		scrollPane_2.setViewportView(thirdSyntax);


		/* 挿入ボタン */
		JButton button = new JButton("挿入");
		button.addActionListener(this);
		getContentPane().add(button, "span 3, grow");

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		StringBuilder template = new StringBuilder();
		template.append(firstSyntax.getSelectedValue());
		template.append(secondSyntax.getSelectedValue());
		template.append(thirdSyntax.getSelectedValue());

		calledForm.setResult(template.toString(), 0);
		this.dispose();
	}

}