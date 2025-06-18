package client.screen;

import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import shared.AssociateHandler;

public class TextListener implements ActionListener {

	private final JTextField text;
	private final JPanel panel;
	private final Screen screen;
	private final MenuOption mo;
	private final AssociateHandler handler;

	public TextListener(ArrayList<Component> components, MenuOption mo, AssociateHandler handler) {

		this.text = (JTextField) components.get(0);
		this.panel = (JPanel) components.get(1);
		this.screen = (Screen) components.get(2);
		this.mo = mo;
		this.handler = handler;

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		switch (mo) {
		case CONNECT:
			MenuOptionsHandler.processCONNECT(text.getText(), handler);
			break;
		case GET:
			MenuOptionsHandler.processGET(text.getText());
			break;
		case ADD:
			MenuOptionsHandler.processADD(text.getText());
			break;
		}

	}

}