package client;

import java.io.File;
import client.screen.Screen;

public class Pilot {

	public static void main(String[] args) {

		File data = new File("data");
		File logs = new File("logs");
		if (!data.exists())
			if (!data.mkdir())
				System.out.println("Fatal error creating requisite directories. Ensure you are running with proper permissions.");
		if (!logs.exists())
			if (!logs.mkdir())
				System.out.println("Fatal error creating requisite directories. Ensure you are running with proper permissions.");
		Screen s = Screen.createMenuScreen(null);
		s.updateScreen();

	}

}
