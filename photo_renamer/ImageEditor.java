package photo_renamer;

import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;

/**
 * The GUI that allows a user to add and remove tags from an Image and changes the name.
 * 
 * Extends JFrame
 * 
 * @author Pravpro
 * @author ChadleyTan
 */
public class ImageEditor extends JFrame {
	
	/** A JPanel layout. */
	private JPanel contentPane;
	
	/** A JList layout model. */
    private DefaultListModel<String> imageTagsListModel;
    
    /** JList for showing the image Tags. */
    private JList<String> imageTags;
    
	/**
	 * Create the frame and allows the image to be edited.
	 * 
	 * @param imageForEditing
	 * 			image to be edited
	 */
	public ImageEditor(Image imageForEditing) {	
		setTitle(imageForEditing.getCurrName());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 364);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblCurrentListOfTags = new JLabel("Current List of Tags");
		lblCurrentListOfTags.setBounds(26, 0, 253, 33);
		contentPane.add(lblCurrentListOfTags);
		
		//Adds all the current tags of the given image to a list model that can be selected
		//by a user
		imageTagsListModel = new DefaultListModel<String>();
		for(String s : imageForEditing.getCurrTags()) {
			imageTagsListModel.addElement(s);
		}
		//Provides a scrollable a view
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(26, 28, 366, 131);
		contentPane.add(scrollPane);
		
		//Initialize a JList and set it to the scrollPane
		imageTags = new JList<String>(imageTagsListModel);
		scrollPane.setViewportView(imageTags);
		imageTags.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		imageTags.setSelectedIndex(0);
		
		//Remove button to remove tags from the selected image
		JButton btnRemove = new JButton("Remove");
		//Disables the removeTagButton when there are no elements in the JList model
		if (imageTagsListModel.getSize() == 0) {
			btnRemove.setEnabled(false);
		}
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Removes the name at the corresponding index from the tags list of the Image and
				//from the JList
				int index = imageTags.getSelectedIndex();
				imageForEditing.removeTag(imageTagsListModel.remove(index));
				
				//Disables the removeTagButton when there are no elements in the JList model
				int size = imageTagsListModel.getSize();
				if (size == 0) {
					btnRemove.setEnabled(false);
				}
				//Gets the previous index to prevent out of bounds index
				else {
					if (index==imageTagsListModel.getSize()){
						index--;
					}
					//Sets cursor/selected tag to at the previous index of the removed tag
					imageTags.setSelectedIndex(index);
					imageTags.ensureIndexIsVisible(index);
				}
			}
		});
		btnRemove.setBounds(282, 172, 110, 23);
		contentPane.add(btnRemove);
		
		//ComboBox for selecting a tag from the tags catalog to add to the image
		JComboBox<String> cbForAdd = new JComboBox<String>();
		cbForAdd.setBounds(26, 206, 253, 23);
		for (String s : Image.getTagsCatalog().getTagsList()) {
			cbForAdd.addItem(s);
		}
		contentPane.add(cbForAdd);
		
		//Add Button to add a selected tag to the image's existing t
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selectedTag = (String) cbForAdd.getSelectedItem();
				
				//Update the currTags of the Image and the JList if selected tag is not 
				//already a tag. Enables the remove button if there is a tag in the JList
				if (imageForEditing.checkTagExists(selectedTag)) {
					Toolkit.getDefaultToolkit().beep();
				}
				else {
					imageForEditing.addTag(selectedTag);
					imageTagsListModel.addElement(selectedTag);
					if(imageTags.isSelectionEmpty()){
						imageTags.setSelectedIndex(0);
					}
					btnRemove.setEnabled(true);
					
				}
				
				if (imageTagsListModel.getSize() > 0) {
					
				}
			}
		});
		btnAdd.setBounds(282, 206, 110, 23);
		contentPane.add(btnAdd);
		
		//Update button that changes the name of image to any changes to its tags
		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Gives a beep if user trying to update an image with the same tags
				if (imageForEditing.getCurrName().equals(imageForEditing.constructName(imageForEditing.getCurrTags()))){
					Toolkit.getDefaultToolkit().beep();
					return;
				}
				else{
					imageForEditing.changeName();
					ImageChooser ic = new ImageChooser(ImageChooser.getDirectiory());
					dispose();
					ic.createAndShowGui();
				}
			}
		});
		btnUpdate.setBounds(26, 235, 110, 23);
		contentPane.add(btnUpdate);
		
		//Back buttons that creates and shows ImageChooser GUI and disposes this window
		JButton btnBack = new JButton("back");
		btnBack.setBounds(282, 235, 110, 23);
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ImageChooser ic = new ImageChooser(ImageChooser.getDirectiory());
				dispose();
				ic.createAndShowGui();
			}
		});
		contentPane.add(btnBack);
		
		//Previous names button that allows the user to view all previous names of image and
		//the select one to become the current name of image
		JButton btnPrevNames = new JButton("Previous Names");
		btnPrevNames.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Creates and show NameHistoryEditor GUI and diposes this window
				NameHistoryEditor nh = new NameHistoryEditor(imageForEditing);
				dispose();
				nh.createAndShowGUI();
			}
		});
		btnPrevNames.setBounds(26, 172, 253, 23);
		contentPane.add(btnPrevNames);
		
		setTitle(imageForEditing.getCurrName());
		
	}
	
	/** 
	 * Creates and shows the GUI for ImageEditor.
	 */
	protected void createAndShowGui() {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		pack();
		setSize(450, 360);
		setVisible(true);
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		/*EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					File f1 = new File("C:\\Users\\pravir\\Pictures\\Saved Pictures\\Intro to Evol Antro.jpg");
					Image im = new Image(f1);
					ImageEditor frame = new ImageEditor(im);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});*/
	}
}
