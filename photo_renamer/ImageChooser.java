package photo_renamer;

import java.awt.BorderLayout;
import java.awt.Container;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

/**
 * The GUI that allows a user to select an Image to edit.
 * 
 * Implements ListSelectionListener that allows the index of the selected image of the list to
 * be chosen
 * 
 * @author Pravpro
 * @author ChadleyTan
 */
public class ImageChooser implements ListSelectionListener{

	/** Instance of Jframe */
	private final JFrame jframe;
	
	/** Instance of JPanel */
	private final JPanel buttonContainer;
	
	/** Instance of JButton to go back to previous window */
	private final JButton backButton; 
	
	/** Instance of JButton to select the Image wished to be edit */
	private final JButton selectButton;
	
	/** Instance of JButton to edit the master list of tags */
	private final JButton editTagsButton;
	
	/** Instance of JList */
	private JList<String> list;
	
	/** Instance of JDefaultListModel */
	private DefaultListModel<String> listModel;
	
	/** Create an empty instance of ImageHandler */
	private ImageHandler im = new ImageHandler();
	
	/** Instance of an ArrayList<File> that are all images */
	private ArrayList<File> images; 
	
	/** Instance of an Image */
	private Image image;
	
	/** A file directory */
	private static File dir;

	/**
	 * Create the frame.
	 * 
	 * @param dir
	 * 			directory that is chosen
	 */
	public ImageChooser(File dir) {
		//Sets dir to the dir passed in the constructor
		ImageChooser.dir = dir; 
		//Gets all the images within a directory
		images = im.getAllImages(dir);
		
		//Creates a Frame
		this.jframe = new JFrame(ImageChooser.dir.getName());
		//Creates a DefaultListModel
		listModel = new DefaultListModel<String>();
		
		//Adds all images in the listModel to be selected by the user
		for (String image: im.changeToName(images)){
			listModel.addElement(image);
		}
		
		//Formats the JList
		list = new JList<String>(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		list.addListSelectionListener(this);
		list.setVisibleRowCount(30);
		//Provides a scrollable view
		JScrollPane listScrollPane = new JScrollPane(list);
		
		//Edit tags button that allows the user to edit the tags catalogs 
		editTagsButton = new JButton("Edit Tags Catalog");
		editTagsButton.addActionListener(new ActionListener() {
			//Create and shows the TagsListEditor window
			@Override
			public void actionPerformed(ActionEvent e){
				TagsListEditor tl = new TagsListEditor();
				tl.createAndShowGUI();
			}
		});
		
		//Select Button that selects the image to be edited
		selectButton = new JButton("Select");
		selectButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = list.getSelectedIndex();
				File selectedImage = images.get(index);
				//Initializes a image from the file image
				image = new Image(selectedImage);
				//Creates and shows the ImageEditor window
				ImageEditor ie =  new ImageEditor(image);
				jframe.dispose();
				ie.createAndShowGui();
				list.setSelectedIndex(index);
				list.ensureIndexIsVisible(index);
			}
		});
		
		//Back Button that creates and shows the PhotoRenamer window
		backButton = new JButton("back");
		backButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				PhotoRenamer pr = new PhotoRenamer();
				jframe.dispose();
				pr.createAndShowGui();
			}
		});
		
		Container c = this.jframe.getContentPane();
		
		//Adds Buttons
		buttonContainer = new JPanel();
		buttonContainer.add(backButton, BorderLayout.LINE_START);
		buttonContainer.add(selectButton, BorderLayout.CENTER);
		buttonContainer.add(editTagsButton, BorderLayout.LINE_END);
		
		c.add(listScrollPane);
		c.add(buttonContainer, BorderLayout.PAGE_END);
		
	}
	
	/** 
	 * Enables the selectButton only if an element of a list is selected.
	 * 
	 * @param e
	 * 			A ListElectionEvent
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting() == false) {
			 
    		if (list.getSelectedIndex() == -1) {
    			selectButton.setEnabled(false);

    		} 
    		else {
    			selectButton.setEnabled(true);
    		}
    	}
	}
	/** 
	 * Returns the directory that the user selected
	 * 
	 * @return 
	 * 			the directory that the user selected
	 */
	public static File getDirectiory(){
		return dir;
	}
	
	/** 
	 * Creates and shows the GUI for ImageChooser
	 */
	protected void createAndShowGui() {
		jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		jframe.pack();
		jframe.setTitle("Image Chooser");
		jframe.setSize(400, 300);
		jframe.setVisible(true);
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		File directory = null;
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("."));
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = chooser.showOpenDialog(new JFrame());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			directory = chooser.getSelectedFile();
		}
		
		ImageChooser ic = new ImageChooser(directory);
		ic.createAndShowGui();
	}

}
