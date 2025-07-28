package client.screen;

import java.awt.Dimension;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.BoxLayout;
import java.awt.Component;
import java.awt.BorderLayout;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import client.net.HolePuncher;
import client.net.TwoWayLink;
import shared.Associate;
import shared.AssociateHandler;
import shared.UserNetworkIdentifier;

public class Screen extends JFrame {

	private static final String VERS = "SWISS V1.1E";
	private final Screen previousScreen;
	private final ExecutorService threadCommand;

	private Screen(Screen previousScreen) {

		this.previousScreen = previousScreen;
		threadCommand = Executors.newCachedThreadPool();

	}

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
		JPanel textPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		JPanel inputPanel = new JPanel();
		JLabel textLabel = new JLabel("Welcome to Swiss!");
		JButton connectButton = new JButton("CONNECT");
		JButton getButton = new JButton("GET");
		JButton addButton = new JButton("ADD");
		DatagramSocket socket;

		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			throw new RuntimeException(e);
		}
		components.add(inputPanel);
		components.add(menuScreen);
		inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
		connectButton.addActionListener(e -> {
			JButton twoWay = new JButton("Two-Way");
			twoWay.addActionListener(eb -> {
				JTextField answerText = new JTextField(16);
				StringBuilder sb = new StringBuilder("<html>");
				for (Associate a : assocHandler.getAssociates())
					sb.append(a.getID() + " ----> " + a.getName() + "<br>");
				sb.append("</html>");
				answerText.addActionListener(ec -> {
					String s1 = answerText.getText();
					assert (s1 != null);
					if (s1.isEmpty()) {
						answerText.setText("Please provide a valid associate to contact!");
						return;
					}
					answerText.setText("Waiting for " + s1 + " to connect...");
					components.add(answerText);
					components.add(inputPanel);
					components.add(menuScreen);
					menuScreen.threadCommand.execute(() -> {
						TwoWayLink link = MyStaticMethods.initTwoWayConnection(s1, assocHandler);
						Screen chatScreen = Screen.createChatScreen(s1, link, menuScreen);
						answerText.setText(null);
						chatScreen.updateScreen();
					});
				});
				inputPanel.add(new JLabel("Please enter the name of the associate you wish to contact."));
				inputPanel.add(new JLabel(sb.toString()));
				inputPanel.add(answerText);
				menuScreen.add(inputPanel);
				menuScreen.updateScreen();
			});
			inputPanel.add(twoWay);
			menuScreen.add(inputPanel);
			menuScreen.updateScreen();
		});
		getButton.addActionListener(ea -> {
            String[] sus = HolePuncher.getSTUN(socket);
			for (String s : sus)
				System.out.println(s);
            JLabel getResult = new JLabel("My UNetID: " + UserNetworkIdentifier.encrypt(MyStaticMethods.getMyLANIP()));
			inputPanel.add(getResult);
			menuScreen.add(inputPanel);
			menuScreen.updateScreen();
		});
		addButton.addActionListener(ec -> {});
		textPanel.add(textLabel);
		buttonPanel.add(connectButton);
		buttonPanel.add(getButton);
		buttonPanel.add(addButton);
		menuScreen.setLayout(new BoxLayout(menuScreen.getContentPane(), BoxLayout.Y_AXIS));
		menuScreen.add(textPanel);
		menuScreen.add(buttonPanel);
		menuScreen.setTitle(VERS);
		menuScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		return menuScreen;

	}

	public static Screen createChatScreen(String title, TwoWayLink link, Screen previousScreen) {

		Screen chatScreen = new Screen(previousScreen);
		JPanel infoPanel = new JPanel();
		JPanel chatPanel = new JPanel();
		JPanel msgPanel = new JPanel();
		JTextArea chat = new JTextArea(5, 15);
		JScrollPane scroller = new JScrollPane(chat);
		JTextField msgField = new JTextField(16);
		JButton closeButton = new JButton("DISCONNECT");
		msgField.addActionListener(e -> {
			String s1 = msgField.getText();
			msgField.setText(null);
			chat.append("Me: " + s1 + '\n');
			link.sendMessage(s1);
		});
		closeButton.addActionListener(e -> {
			link.close();
			chatScreen.setVisible(false);
			chatScreen.previousScreen.setVisible(true);
		});
		infoPanel.setLayout(new BorderLayout());
		infoPanel.add(closeButton, BorderLayout.EAST);
		chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.X_AXIS));
		msgPanel.add(msgField);
		chatPanel.add(scroller);
		chatScreen.setLayout(new BorderLayout(5, 5));
		chatScreen.add(infoPanel, BorderLayout.NORTH);
		chatScreen.add(chatPanel, BorderLayout.CENTER);
		chatScreen.add(msgPanel, BorderLayout.SOUTH);
		chatScreen.setTitle("Messaging " + title);
		chatScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		chatScreen.setPreferredSize(new Dimension(450, 250));
		Thread t = new Thread(link::checkHello);
		//negotiate connection
		t.start();
		try {
			while (t.isAlive())
				link.sayHello();
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		}
		//
        chatScreen.threadCommand.execute(() -> {
			String line = null;
			while (true) {
				if ((line = link.recvMessage()) != null) {
					chat.append(title + ": " + line + '\n');
				} else {
					link.close();
					chatScreen.setVisible(false);
					chatScreen.previousScreen.setVisible(true);
				}
			}
		});
		return chatScreen;

	}

}