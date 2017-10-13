package photo_renamer;

import java.util.Date;
import java.io.*;
import java.text.SimpleDateFormat;

/**
 * The system log that keeps all renaming ever done(old name, new name, and timestamp)
 * and is also readable
 * 
 * @author Pravpro
 * @author ChadleyTan
 */
public class Logger implements Serializable{
	
	/** The file to which the logger writes to*/
	private File file;
	
	/**
	 * Creates an empty Logger (only if one does not already exist) and allows
	 * the logger to be updated or read. 
	 */
	public Logger() {
		file = new File("logger.txt");
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	/** 
	 * Writes the event that happened at date/time, ts to the Logger, file.
	 * 
	 * @param oldName
	 * 		     name before a rename
	 * @param newName
	 * 	         name after a rename
	 */		     
	public void writeEvent(String oldName, String newName) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file.getName(), true));
			bw.write("Old name: " + oldName + ", New Name: " + newName + ", Time: " + new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
			bw.newLine();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** 
	 * Reads all logs and prints out the log for the user to view
	 */	
	public void readEvents() {
		System.out.println("Log:");
		BufferedReader br;
		String line;
		try {
			br = new BufferedReader(new FileReader("logger.txt"));
			line = br.readLine();
			while (line!=null){
				System.out.println(line);
				line = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
//	public static void main(String[] args) {
//		Logger log = new Logger();
//		log.writeEvent("dance", "dance @chad @pravs");
//		log.readEvents();
//	}
}

