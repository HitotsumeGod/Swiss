package client.screen;

import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.BoxLayout;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Screen extends JFrame {

	private class ComponentListener implements ActionListener {

		//index 0 is action parent, 1 is 0's parent panel, 2 is 1's parent screen
		private ArrayList<Component> components;

		public ComponentListener(ArrayList<Component> components) {

			this.components = components;

		}

		public void actionPerformed(ActionEvent e) {

			JTextField answerText = new JTextField(16);

			((JPanel) components.get(1)).add(answerText);
			((Screen) components.get(2)).updateScreen();

		}

		public void load(ArrayList<Component> components) {

			this.components.addAll(components);

		}

	}

	private Screen() {}

	public void updateScreen() {

		this.pack();
		this.setVisible(true);

	}

	public static Screen createMenuScreen() { 

		Screen menuScreen = new Screen();
		JPanel textPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		JLabel textLabel = new JLabel("Welcome to Swiss!\n-----OPTIONS-----\nCONNECT   GET   ADD\n");
		JButton connectButton = new JButton("CONNECT");
		JButton getButton = new JButton("GET");
		JButton addButton = new JButton("ADD");
		textPanel.add(textLabel);
		buttonPanel.add(connectButton);
		buttonPanel.add(getButton);
		buttonPanel.add(addButton);
		menuScreen.setLayout(new BoxLayout(menuScreen.getContentPane(), BoxLayout.Y_AXIS));
		menuScreen.add(textPanel);
		menuScreen.add(buttonPanel);
		menuScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		return menuScreen;

	}

	public static Screen createChatScreen() { return null; }

}