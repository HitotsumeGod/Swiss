package shared;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;

public class Logger {

	private File logfile;
	private FileWriter writer;
	private StringBuilder logStr;

	public Logger(String logpath, boolean append) {
		
		try {
			logfile = new File(logpath);
			logfile.createNewFile();
			writer = new FileWriter(logfile, append);
		} catch (IOException io) {
			io.printStackTrace();
		}
       
	}

	public void write(String toWrite) {

		try {
			logStr = new StringBuilder(LocalTime.now().toString());
			logStr.append(" : ");
			logStr.append(toWrite);
			writer.write(logStr.toString(), 0, logStr.length());
			writer.write((int) '\n');
			writer.flush();
		} catch (IOException io) {
			io.printStackTrace();
		}

	}

	public void append(String toWrite) {

		try {
			logStr = new StringBuilder(LocalTime.now().toString());
			logStr.append(" : ");
			logStr.append(toWrite);
			writer.write(logStr.toString(), 0, logStr.length());
			writer.write((int) '\n');
			writer.flush();
		} catch (IOException io) {
			io.printStackTrace();
		}

	}

	public void close() {

		try {
			writer.close();
		} catch (IOException io) {
			io.printStackTrace();
		}

	}

}
