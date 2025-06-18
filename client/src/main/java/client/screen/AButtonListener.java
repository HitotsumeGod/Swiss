package client.screen;

import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import shared.AssociateHandler;

public class AButtonListener implements ActionListener {

	private final JPanel panel;
	private final Screen screen;
	private final AssociateHandler handler;
	private JTextField answerText;

	//components: index 0 is action parent, 1 is 0's parent panel, 2 is 1's parent screen

	public AButtonListener(ArrayList<Component> components, AssociateHandler handler) {

		this.panel = (JPanel) components.get(0);
		this.screen = (Screen) components.get(1);
		this.handler = handler;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		ArrayList<Component> components;

		if (panel.getComponentCount() == 0) {
			components = new ArrayList<>();
			answerText = new JTextField(16);
			components.add(answerText);
			components.add(panel);
			components.add(screen);
			answerText.addActionListener(new TextListener(components, MenuOption.ADD, handler));
			panel.add(answerText);
			screen.add(panel);
			screen.updateScreen();
		}

	}

}