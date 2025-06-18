package client.screen;

import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GButtonListener implements ActionListener {

	private final JPanel panel;
	private final Screen screen;
	private JTextField answerText;

	//components: index 0 is action parent, 1 is 0's parent panel, 2 is 1's parent screen

	public GButtonListener(ArrayList<Component> components) {

		this.panel = (JPanel) components.get(1);
		this.screen = (Screen) components.get(2);
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
			answerText.addActionListener(new TextListener(components, MenuOption.GET));
			panel.add(answerText);
			screen.add(panel);
			screen.updateScreen();
		}

	}

}