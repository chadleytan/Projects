package photo_renamer;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.*;

/**
 * The GUI that allows a user to select a previous name of image and set it to its current name.
 * 
 * Extends JPanel
 * 
 * Implements ListSelectionListener that allows the index of the selected element of the list to
 * be used to set a previous name to an image's current name
 * 
 * @author Pravpro
 * @author ChadleyTan
 */
public class NameHistoryEditor extends JPanel implements ListSelectionListener {
	
	/** Instance of Jframe */
	private final JFrame jframe;
	
	/** Instance of JList */
	private JList<String> list;
	
	/** Instance of DefaultListModel */
	private DefaultListModel<String> listModel;
	
	/** Instance of JButton used to set the name of an image to a previous name */
	private final JButton setNameButton; 
	
	/** Instance of JButton used to go back to previous Frame */
	private final JButton backButton;
 
	/**
	 * Creates the panel.
	 */
	public NameHistoryEditor(Image image) {
		super(new BorderLayout());
		
		this.jframe = new JFrame("Name History");
		listModel = new DefaultListModel<String>();
	
		//Adds all previous names of image to the listModel
		for (ArrayList<String> tagsList: image){
			listModel.addElement(image.constructName(tagsList));
		}
		
		//Initialize a JList and set its format 
		list = new JList<String>(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		list.addListSelectionListener(this);
		list.setVisibleRowCount(10);
		//Provides a scrollable view
		JScrollPane listScrollPane = new JScrollPane(list);
		
		//Set Name Button that changes the current Tags of the image according to a previous name
		setNameButton = new JButton("Set Name");
		if (listModel.isEmpty()) {
			setNameButton.setEnabled(false);
		}
		setNameButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//Changes image name to a previous name of image
				int index = list.getSelectedIndex();
				image.changeToPreviousName(index);
				//Returns back to the ImageEditor Window (JFrame)
				ImageEditor ie = new ImageEditor(image);
				jframe.dispose();
				ie.createAndShowGui();
			}	
		});
		
		//Back button that creates a new NameHistoryEditor window and shows its GUI
		backButton = new JButton("back");
		backButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				ImageEditor ie = new ImageEditor(image);
				jframe.dispose();
				ie.createAndShowGui();
			}	
		});
		
		//Create and set up the content pane.
        JComponent newContentPane = this;
        newContentPane.setOpaque(true); //content panes must be opaque
        jframe.setContentPane(newContentPane);
        
        //Formatting of the JPanel
      	JPanel buttonContainer = new JPanel();
      	buttonContainer.setLayout(new BoxLayout(buttonContainer,
      				BoxLayout.LINE_AXIS));
      	//Add Buttons
      	buttonContainer.add(backButton);
      	buttonContainer.add(Box.createHorizontalStrut(100));
      	buttonContainer.add(setNameButton);
      	
      	buttonContainer.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
      	
      	add(listScrollPane, BorderLayout.CENTER);
        add(buttonContainer, BorderLayout.PAGE_END);
	}
	
	/**
	 * Sets the setNameButton True only if an element in a list is selected
	 * 
	 * @param e
	 * 			A ListSelectionEvent
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting() == false) {
			 
    		if (list.getSelectedIndex() == -1) {
    			setNameButton.setEnabled(false);
    		} 
    		else {
    			setNameButton.setEnabled(true);
    		}
    	}
	}
	
	/** 
	 * Creates and shows the GUI for NameHistoryEditor
	 */
	protected void createAndShowGUI() {
        //Create and set up the window.
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);      
        jframe.pack();
        jframe.setVisible(true);
    	}
}
