package client.screen;

import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.BoxLayout;
import java.awt.Component;
import shared.AssociateHandler;

public class Screen extends JFrame {

	private Screen() {}

	public void updateScreen() {

		this.pack();
		this.setVisible(true);

	}

	public static Screen createMenuScreen(AssociateHandler handler) { 

		ArrayList<Component> components = new ArrayList<>();
		Screen menuScreen = new Screen();
		JPanel textPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		JPanel inputPanel = new JPanel();
		JLabel textLabel = new JLabel("Welcome to Swiss!");
		JButton connectButton = new JButton("CONNECT");
		JButton getButton = new JButton("GET");
		JButton addButton = new JButton("ADD");
		components.add(inputPanel);
		components.add(menuScreen);
		inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
		connectButton.addActionListener(new CButtonListener(components, handler));
		getButton.addActionListener(new GButtonListener(components, handler));
		addButton.addActionListener(new AButtonListener(components, handler));
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

	public static Screen createChatScreen(AssociateHandler handler) { return null; }

}