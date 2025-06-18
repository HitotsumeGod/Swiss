package client.screen;

import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import client.net.LinkHandler;
import shared.AssociateHandler;

public class GButtonListener implements ActionListener {

	private final JPanel panel;
	private final Screen screen;
	private final LinkHandler lkHandler;
	private final AssociateHandler assocHandler;
	private JTextField answerText;

	//components: index 0 is action parent, 1 is 0's parent panel, 2 is 1's parent screen

	public GButtonListener(ArrayList<Component> components, AssociateHandler assocHandler, LinkHandler lkHandler) {

		this.panel = (JPanel) components.get(0);
		this.screen = (Screen) components.get(1);
		this.assocHandler = assocHandler;
		this.lkHandler = lkHandler;

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
			answerText.addActionListener(new TextListener(components, MenuOption.ADD, assocHandler, lkHandler));
			panel.add(answerText);
			screen.add(panel);
			screen.updateScreen();
		}

	}

}