package client.screen;

import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import client.net.LinkHandler;
import client.net.OneWayLink;
import client.net.TwoWayLink;
import shared.Associate;
import shared.AssociateHandler;
import shared.Logger;

public class Screen extends JFrame {

	private final Screen previousScreen;

	private Screen(Screen previousScreen) { this.previousScreen = previousScreen; }

	public void updateScreen() {

		if (this.previousScreen != null)
			this.previousScreen.setVisible(false);
		this.pack();
		this.setVisible(true);

	}

	public static Screen createMenuScreen(Screen previousScreen) {

		ArrayList<Component> components = new ArrayList<>();
		AssociateHandler assocHandler = new AssociateHandler();
		Screen menuScreen = new Screen(previousScreen);
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
						String s1 = answerText.getText();
						answerText.setText(null);
						components.add(answerText);
						components.add(inputPanel);
						components.add(menuScreen);
						OneWayLink link = MenuOptionsHandler.initOneWayConnection(s1, assocHandler, lkHandler);
						Screen chatScreen = createChatScreen(s1, link, menuScreen);
						chatScreen.updateScreen();
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
						String s1 = answerText.getText();
						answerText.setText(null);
						components.add(answerText);
						components.add(inputPanel);
						components.add(menuScreen);
						TwoWayLink link = MenuOptionsHandler.initTwoWayConnection(s1, assocHandler, lkHandler);
						Screen chatScreen = Screen.createChatScreen(s1, link, menuScreen);
						chatScreen.updateScreen();
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

	public static Screen createChatScreen(String title, OneWayLink link, Screen previousScreen) {

		Screen chatScreen = new Screen(previousScreen);
		JPanel infoPanel = new JPanel();
		JPanel chatPanel = new JPanel();
		JLabel screenTitle = new JLabel("Messaging " + title, SwingConstants.LEFT);
		JLabel[] msgs = new JLabel[15];
		for (int i = 0; i < msgs.length; i++)
			msgs[i] = new JLabel();
		JTextField msgField = new JTextField(16);
		msgField.addActionListener(e -> {
			String s1 = msgField.getText();
			msgField.setText(null);
			link.sendMessage(s1);
			msgs[0].setText(s1);
			rollArray(msgs);
			for (int i = 0; i < msgs.length; i++)
				msgs[i].repaint();
		});
		infoPanel.setLayout(new BorderLayout());
		chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        for (JLabel l : msgs)
			chatPanel.add(l);
		chatPanel.add(msgField);
		infoPanel.add(screenTitle, BorderLayout.LINE_START);
		chatScreen.setLayout(new BoxLayout(chatScreen.getContentPane(), BoxLayout.Y_AXIS));
		chatScreen.add(infoPanel);
		chatScreen.add(chatPanel);
		chatScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		return chatScreen;

	}

	public static Screen createChatScreen(String title, TwoWayLink link, Screen previousScreen) {

		Screen chatScreen = new Screen(previousScreen);
		JPanel infoPanel = new JPanel();
		JPanel chatPanel = new JPanel();
		JLabel screenTitle = new JLabel("Chatting with " + title);
		JLabel[] yourMessages = new JLabel[5];
		JLabel[] theirMessages = new JLabel[5];
		chatPanel.setLayout(new GridLayout(yourMessages.length, 2));
		infoPanel.add(screenTitle);
		for (int i = 0; i < yourMessages.length; i++)
			chatPanel.add((yourMessages[i]) = new JLabel());
		for (int i = 0; i < theirMessages.length; i++)
			chatPanel.add((theirMessages[i]) = new JLabel());
		chatScreen.setLayout(new FlowLayout());
		chatScreen.add(infoPanel);
		chatScreen.add(chatPanel);
		chatScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		chatScreen.previousScreen.setVisible(false);
		return chatScreen;

	}

	private static void rollArray(Object[] arr) {

		Object[] temp = arr.clone();

		for (int i = 1; i < arr.length; i++)
			arr[i] = temp[i - 1];
		arr[0] = temp[temp.length - 1];

	}

}