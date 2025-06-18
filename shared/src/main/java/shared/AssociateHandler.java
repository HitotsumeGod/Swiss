package shared;

import java.util.ArrayList;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class AssociateHandler {

	private static final String ASSOCIATEDBPATH = "src/main/resources/assocs.db";
	private ArrayList<Associate> associateList;

	public AssociateHandler() {

		associateList = new ArrayList<>();
		loadAssociates();

	}

	private void loadAssociates() {

		BufferedReader reader;
		String lne;
		int n;

		n = 0;
		try {
			reader = new BufferedReader(new FileReader(new File(ASSOCIATEDBPATH)));
			while ((lne = reader.readLine()) != null)
				associateList.add(new Associate(lne.substring(0, lne.indexOf('%')), new UserNetworkIdentifier(lne.substring(lne.indexOf('%') + 1))));
			reader.close();
		} catch (IOException io) {
			io.printStackTrace();
		}

	}

	public void addAssociate(String name, UserNetworkIdentifier unetid) {

		associateList.add(new Associate(name, unetid));

	}

	public ArrayList<Associate> getAssociates() {

		return associateList;

	}

}