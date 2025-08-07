package client.screen;

import java.awt.Dimension;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
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
import client.net.TwoWayLink;
import client.util.Logger;
import client.util.UserNetworkIdentifier;

public class Screen extends JFrame {

	private static final String VERS = "SWISS V1.2E";
	private final Screen previousScreen;
	private final ExecutorService threadCommand;
	private final Logger lincoln;

	private Screen(Screen previousScreen) {

		this.previousScreen = previousScreen;
		threadCommand = Executors.newCachedThreadPool();
		String logpath = null;
		if (previousScreen == null)
			logpath = new String("logs/screen.log");
		else {
			StringBuilder sb = new StringBuilder("logs/");
			sb.append(previousScreen.getTitle());
			sb.append("_screen.log");
			logpath = sb.toString();
		}
		lincoln = new Logger(logpath, false);

	}

	public void updateScreen() {

		if (this.previousScreen != null)
			this.previousScreen.setVisible(false);
		this.pack();
		this.setVisible(true);

	}

	public static Screen createMenuScreen(Screen previousScreen) {

		ArrayList<Component> components = new ArrayList<>();
		Screen menuScreen = new Screen(previousScreen);
		JPanel textPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		JPanel inputPanel = new JPanel();
		JLabel textLabel = new JLabel("Welcome to Swiss!");
		JButton connectButton = new JButton("CONNECT");
		JButton getButton = new JButton("GET");
		DatagramSocket socket;

		try {
			socket = new DatagramSocket(BroadNetworking.BINDPORT);
		} catch (SocketException e) {
			throw new RuntimeException(e);
		}
		components.add(inputPanel);
		components.add(menuScreen);
		inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
		connectButton.addActionListener(e -> {
			inputPanel.removeAll();
			menuScreen.updateScreen();
			JTextArea idField = new JTextArea(1, 16);
			idField.setText("Your UNetID is : ");
			String s = BroadNetworking.getSTUN(socket).toString();
			idField.append(s);
			System.out.println(s);
			idField.setEditable(false);
			JTextField answerText = new JTextField(16);
			answerText.addActionListener(eb -> {
				String s1 = answerText.getText();
				assert (s1 != null);
				if (s1.isEmpty()) {
					answerText.setText("Please provide a valid associate to contact!");
					return;
				}
				answerText.setText("Waiting for associate to connect...");
				components.add(answerText);
				components.add(inputPanel);
				components.add(menuScreen);
				menuScreen.threadCommand.execute(() -> {
					TwoWayLink link = new TwoWayLink(new UserNetworkIdentifier(s1), socket);
					Screen chatScreen = Screen.createChatScreen(s1, link, menuScreen);
					answerText.setText(null);
					chatScreen.updateScreen();
				});
			});
			inputPanel.add(new JLabel("Please enter an associate's UNetID."));
			inputPanel.add(idField);
			inputPanel.add(answerText);
			menuScreen.add(inputPanel);
			menuScreen.updateScreen();
			menuScreen.lincoln.write("connectButton logic completed.");
		});
		getButton.addActionListener(ea -> {
			inputPanel.removeAll();
			JLabel label = new JLabel("Please enter an IPV4 ip:port combination such as '10.10.10.10:8080");
			JTextField answerText = new JTextField(16);
			JTextArea translatedText = new JTextArea(1, 16);
			translatedText.setEditable(false);
			answerText.addActionListener(eb -> {
				String s1 = answerText.getText();
				if (!s1.contains(":"))
					answerText.setText("ip:port string malformed!");
				else {
					translatedText.setText("UNetID : ");
					translatedText.append(UserNetworkIdentifier.encrypt(s1));
				}
			});
			inputPanel.add(label);
			inputPanel.add(answerText);
			inputPanel.add(translatedText);
			menuScreen.add(inputPanel);
			menuScreen.updateScreen();
			menuScreen.lincoln.write("getButton logic completed.");
		});
		textPanel.add(textLabel);
		buttonPanel.add(connectButton);
		buttonPanel.add(getButton);
		menuScreen.setLayout(new BoxLayout(menuScreen.getContentPane(), BoxLayout.Y_AXIS));
		menuScreen.add(textPanel);
		menuScreen.add(buttonPanel);
		menuScreen.setTitle(VERS);
		menuScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		menuScreen.setPreferredSize(new Dimension(750, 500));
		menuScreen.lincoln.write("New menuScreen established.");
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
		Thread t = new Thread(() -> {
			while (!link.checkHello());
		});
		//negotiate connection
		t.start();
		try {
			while (t.isAlive()) {
				Thread.sleep(500);
				link.sayHello();
			}
			t.join();
			chatScreen.lincoln.write("Associate connection negotiated.");
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		//
        chatScreen.threadCommand.execute(() -> {
			String line = null;
			while (true) {
				if ((line = link.recvMessage()) != null) {
					chat.append(title + ": " + line + '\n');
					chatScreen.lincoln.write("Received and displayed associate message.");
				} else {
					link.close();
					chatScreen.setVisible(false);
					chatScreen.previousScreen.setVisible(true);
					chatScreen.lincoln.write("chatScreen disabled; returned to previous Screen.");
				}
			}
		});
		chatScreen.lincoln.write("New chatScreen established.");
		return chatScreen;

	}

}
