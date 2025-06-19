package client.screen;

import java.util.ArrayList;
import javax.swing.*;
import java.awt.Component;
import client.net.LinkHandler;
import shared.Associate;
import shared.AssociateHandler;
import shared.Logger;

public class Screen extends JFrame {

	private Screen() {}

	public void updateScreen() {

		this.pack();
		this.setVisible(true);

	}

	public static Screen createMenuScreen(AssociateHandler assocHandler) {

		ArrayList<Component> components = new ArrayList<>();
		Screen menuScreen = new Screen();
		LinkHandler lkHandler = new LinkHandler(menuScreen);
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
		connectButton.addActionListener(e -> {
			JButton oneWay = new JButton("One-Way");
			JButton twoWay = new JButton("Two-Way");
			oneWay.addActionListener(ea -> {
				if (inputPanel.getComponentCount() == 2) {
					JTextField answerText = new JTextField(16);
					StringBuilder sb = new StringBuilder("<html>");
					for (Associate a : assocHandler.getAssociates())
						sb.append(a.getID() + " : " + a.getName() + " ---> " + a.getHost() + "<br>");
					sb.append("</html>");
					answerText.addActionListener(eb -> {
						answerText.setText(null);
						components.add(answerText);
						components.add(inputPanel);
						components.add(menuScreen);
						MenuOptionsHandler.performOneWayConnection(answerText.getText(), components, assocHandler, lkHandler);
					});
					inputPanel.add(new JLabel("Please enter the name of the associate you wish to contact."));
					inputPanel.add(new JLabel(sb.toString()));
					inputPanel.add(answerText);
					menuScreen.add(inputPanel);
					menuScreen.updateScreen();
				}
			});
			twoWay.addActionListener(eb -> {
				if (inputPanel.getComponentCount() == 2) {
					JTextField answerText = new JTextField(16);
					StringBuilder sb = new StringBuilder("<html>");
					for (Associate a : assocHandler.getAssociates())
						sb.append(a.getID() + " : " + a.getName() + " ---> " + a.getHost() + "<br>");
					sb.append("</html>");
					answerText.addActionListener(ec -> {
						answerText.setText(null);
						components.add(answerText);
						components.add(inputPanel);
						components.add(menuScreen);
						MenuOptionsHandler.performOneWayConnection(answerText.getText(), components, assocHandler, lkHandler);
					});
					inputPanel.add(new JLabel("Please enter the name of the associate you wish to contact."));
					inputPanel.add(new JLabel(sb.toString()));
					inputPanel.add(answerText);
					menuScreen.add(inputPanel);
					menuScreen.updateScreen();
				}
			});
			inputPanel.add(oneWay);
			inputPanel.add(twoWay);
			menuScreen.add(inputPanel);
			menuScreen.updateScreen();
		});
		getButton.addActionListener(ea -> {
			if (inputPanel.getComponentCount() == 0) {
				JTextField answerText = new JTextField(16);
				answerText.addActionListener(eb -> { MenuOptionsHandler.performGetUNetID(answerText.getText()); });
				inputPanel.add(answerText);
				menuScreen.add(inputPanel);
				menuScreen.updateScreen();
			}
		});
		addButton.addActionListener(ec -> {
			if (inputPanel.getComponentCount() == 0) {
				JTextField answerText = new JTextField(16);
				answerText.addActionListener(eb -> { MenuOptionsHandler.performAddAssociate(answerText.getText()); });
				inputPanel.add(answerText);
				menuScreen.add(inputPanel);
				menuScreen.updateScreen();
			}
		});
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

	public static Screen createChatScreen(AssociateHandler assocHandler) { return null; }

}