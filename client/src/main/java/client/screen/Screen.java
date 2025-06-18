package client.screen;

import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.BoxLayout;
import java.awt.Component;

public class Screen extends JFrame {

	private Screen() {}

	public void updateScreen() {

		this.pack();
		this.setVisible(true);

	}

	public static Screen createMenuScreen() { 

		ArrayList<Component> components = new ArrayList<>();
		Screen menuScreen = new Screen();
		JPanel textPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		JPanel inputPanel = new JPanel();
		JLabel textLabel = new JLabel("Welcome to Swiss!");
		JButton connectButton = new JButton("CONNECT");
		JButton getButton = new JButton("GET");
		JButton addButton = new JButton("ADD");
		components.add(connectButton);
		components.add(inputPanel);
		components.add(menuScreen);
		connectButton.addActionListener(new CButtonListener(components));
		components.set(0, getButton);
		getButton.addActionListener(new GButtonListener(components));
		components.set(0, addButton);
		addButton.addActionListener(new AButtonListener(components));
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