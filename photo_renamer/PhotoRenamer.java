package photo_renamer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * The GUI that allows a user to choose a directory that shows all the images within that directory
 * and allows them to add or remove tags to a image of their choice which then renames an image name to 
 * its currently assigned tags. Also allows the user to view the log of all name changes from 
 * all images.
 * 
 * @author Pravpro
 * @author ChadleyTan
 */
public class PhotoRenamer {
	/** Instance of JFrame */
	private final JFrame jframe;
	/** Instances of JPanel */
	private final JPanel buttonContainer;
	private final JPanel imageContainer;
	/** Instance of JButton used to open a file directory */
	private final JButton openFileButton;
	/** Instance of JButton used to view the log */
	private final JButton viewLogButton; 
	/** Instance of Logger for  */
	private Logger log = new Logger();
	/** The directory chosen by the user  */
	private File chosenDirectory;
	/** The scaled version of the logo to be diplayed */
	private final ImageIcon logo;
	/** The label to display the image on*/
	private final JLabel imageLabel;
	
	
	/** 
	 * Creates the JFrame and buttons for the user to view and click.
	 */
	public PhotoRenamer() {
		this.jframe = new JFrame("Photo Renamer");
		//Selects directory button
		openFileButton = new JButton("Select directory");
		openFileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//User selects a directory
				showFileChooser();
				//Creates a new window from ImageChooser and show its GUI 
				ImageChooser ic = new ImageChooser(chosenDirectory);
				jframe.dispose();
				ic.createAndShowGui();
				
			}
		});
		
		//Image scaled and added to its respective JLabel with a Text component
		logo = new ImageIcon(
				new ImageIcon("main logo.png").getImage()
				.getScaledInstance(300, 100, java.awt.Image.SCALE_SMOOTH));
		imageLabel = new JLabel("This Program will allow you to edit your Images using Tags", logo, JLabel.CENTER);
		imageLabel.setVerticalTextPosition(JLabel.BOTTOM);
		imageLabel.setHorizontalTextPosition(JLabel.CENTER);
		
		//Views the log history of photo renames
		viewLogButton = new JButton("View Log");
		viewLogButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				//Reads the log text file and prints it
				log.readEvents();
			}
		});
		
		Container startUp = this.jframe.getContentPane();
		
		//Sets JPanel layout
		buttonContainer = new JPanel();
		buttonContainer.add(openFileButton, BorderLayout.LINE_START);
		buttonContainer.add(viewLogButton, BorderLayout.CENTER);
		imageContainer = new JPanel();
		imageContainer.add(imageLabel, BorderLayout.NORTH);
		startUp.add(imageContainer, BorderLayout.PAGE_START);
		startUp.add(buttonContainer, BorderLayout.PAGE_END);
	}

	/** 
	 * Allows user to select a directory by opening the system's directories.
	 */
	private void showFileChooser() {
		//Select a directory
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("."));
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = chooser.showOpenDialog(jframe);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			//Stores the chosen directory
			chosenDirectory = chooser.getSelectedFile();
		}
	}
	
	/** 
	 * Creates and shows the GUI for PhotoRenamer
	 */
	protected void createAndShowGui() {
		jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		jframe.pack();
		jframe.setTitle("Photo Renamer");
		jframe.setSize(400, 275);
		jframe.setVisible(true);
		
	}
	/** 
	 * Main method to run PhotoRenamer
	 */
	public static void main(String[] args){
		PhotoRenamer pr = new PhotoRenamer();
		pr.createAndShowGui();
	}
}
