package client.screen;

import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import client.net.LinkHandler;
import shared.Associate;
import shared.AssociateHandler;

public class CButtonListener implements ActionListener {

	private final JPanel panel;
	private final Screen screen;
	private final AssociateHandler assocHandler;
	private final LinkHandler lkHandler;
	private JTextField answerText;

	//components: 0 is the parent panel, 1 is 2's parent screen

	public CButtonListener(ArrayList<Component> components, AssociateHandler assocHandler, LinkHandler lkHandler) {

		this.panel = (JPanel) components.get(0);
		this.screen = (Screen) components.get(1);
		this.assocHandler = assocHandler;
		this.lkHandler = lkHandler;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		ArrayList<Component> components;
		StringBuilder sb;
		JLabel l1, l2;

		if (panel.getComponentCount() == 0) {
			sb = new StringBuilder();
			components = new ArrayList<>();
			l1 = new JLabel("Please enter the name of the associate you wish to contact.");
			sb = new StringBuilder("<html>");
			for (Associate a : assocHandler.getAssociates()) 
				sb.append(a.getID() + " : " + a.getName() + " ---> " + a.getHost() + "<br>");
			sb.append("</html>");
			l2 = new JLabel(sb.toString());
			answerText = new JTextField(16);
			components.add(answerText);
			components.add(panel);
			components.add(screen);
			answerText.addActionListener(new TextListener(components, MenuOption.CONNECT, assocHandler, lkHandler));
			panel.add(l1);
			panel.add(l2);
			panel.add(answerText);
			screen.add(panel);
			screen.updateScreen();
		}

	}

}