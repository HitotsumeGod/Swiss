package client.screen;

import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import shared.AssociateHandler;

public class CButtonListener implements ActionListener {

	private final JPanel panel;
	private final Screen screen;
	private final AssociateHandler handler;
	private JLabel label;
	private JTextField answerText;

	//components: 0 is the parent panel, 1 is 2's parent screen

	public CButtonListener(ArrayList<Component> components, AssociateHandler handler) {

		this.panel = (JPanel) components.get(0);
		this.screen = (Screen) components.get(1);
		this.handler = handler;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		ArrayList<Component> components;

		if (panel.getComponentCount() == 0) {
			components = new ArrayList<>();
			label = new JLabel("Please enter the name of the associate you wish to contact.");
			answerText = new JTextField(16);
			components.add(answerText);
			components.add(panel);
			components.add(screen);
			answerText.addActionListener(new TextListener(components, MenuOption.CONNECT, handler));
			panel.add(label);
			panel.add(answerText);
			screen.add(panel);
			screen.updateScreen();
		}

	}

}