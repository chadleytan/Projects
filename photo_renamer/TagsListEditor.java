package photo_renamer;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

/**
 * The GUI that allows a user to select a previous name of image and set it to its current name.
 * 
 * Extends JPanel
 * 
 * Implements ListSelectionListener that allows the index of the selected element of the list to
 * be removed
 * 
 * @author Pravpro
 * @author ChadleyTan
 */
public class TagsListEditor extends JPanel implements ListSelectionListener{
	
	/** Instance of Jframe */
	private final JFrame jframe;
	
	/** The display a of list of tags and allows the user to select them. */
	private JList<String> tagsList;
	
	/** A JList layout model. */
    private DefaultListModel<String> listModel;
    
    /** The string of the add tag button. */
    private String addString = "Add Tag";
    
    /** The string of the remove string button. */
    private String removeString = "Remove Tag";
    
    /** The button to remove an existing tag in the catalog. */
    private JButton removeTagButton;
    
    /** The button to save any changes to the catalog. */
    private JButton saveButton;
    
    /** The button to open the previous JFrame. */
    private JButton backButton;
    
    /** The textspace for a user to enter a new tag. */
    private JTextField newTag;
    
    /**
	 * Creates a new TagsListEditor which creates a panel for a user to add tags and remove 
	 * existing tags.
	 */
	public TagsListEditor() {
		super(new BorderLayout());
		
		//Initializes JFrame
		this.jframe = new JFrame();
		//Adds any existing tags from the tags catalog to the list of tags that can be selected
		//by a user
		listModel = new DefaultListModel<String>();
		
		//Add all tags in the TagsCatalog to the listModel
		for (String tag: Image.getTagsCatalog().getTagsList()){
			listModel.addElement(tag);
		}
		
		//Initialize a JList and set its format 
		tagsList = new JList<String>(listModel);
		tagsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tagsList.setSelectedIndex(0);
		tagsList.addListSelectionListener(this);
		tagsList.setVisibleRowCount(20);
		//Provides a scrollable view
		JScrollPane listScrollPane = new JScrollPane(tagsList);
		
		//Create an add tag button that takes the text entered in the TextField and adds it to 
		//the the list of tags
		JButton addTagButton = new JButton(addString);
		AddTagListener addTagListener = new AddTagListener(addTagButton);
		addTagButton.setActionCommand(addString);
		addTagButton.addActionListener(addTagListener);
		addTagButton.setEnabled(false);
		
		//Initializes a remove tag button that allows the user to select a tag and remove it using
		//the remove tag button
		removeTagButton = new JButton(removeString);
		removeTagButton.setActionCommand(removeString);
		removeTagButton.addActionListener(new RemoveTagListener());
		
		//Initializes a save tag button that allows the user to save the changes to the TagsCatalog
		saveButton = new JButton("Save Changes");
		saveButton.setActionCommand("Save Changes");
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Saves the changes by serializing the TagsCatalog instance
				Image.getTagsCatalog().saveTagsCatalog();	
				jframe.dispose();
			}
		});
		
		//Closes the window using the backButton
		backButton = new JButton("back");
		backButton.setActionCommand("back");
		backButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				jframe.dispose();		
			}	
		});
				
		//Allows user to type a new tag they wish to add in a TextField
		newTag = new JTextField(10);
		newTag.addActionListener(addTagListener);
		newTag.getDocument().addDocumentListener(addTagListener);
		
		//Create and set up the content pane.
        JComponent newContentPane = this;
        newContentPane.setOpaque(true); //content panes must be opaque
        jframe.setContentPane(newContentPane);
        
		//Formatting of the JPanel
		JPanel buttonContainer = new JPanel();
		buttonContainer.setLayout(new BoxLayout(buttonContainer,
				BoxLayout.LINE_AXIS));
		//Adds Buttons
		buttonContainer.add(backButton);
		buttonContainer.add(Box.createHorizontalStrut(5));
		buttonContainer.add(removeTagButton);
		buttonContainer.add(Box.createHorizontalStrut(5));
		buttonContainer.add(new JSeparator(SwingConstants.VERTICAL));
	    buttonContainer.add(Box.createHorizontalStrut(5));
	    buttonContainer.add(newTag);
	    buttonContainer.add(addTagButton);
	    buttonContainer.add(Box.createHorizontalStrut(5));
	    buttonContainer.add(saveButton);
	    buttonContainer.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
	    
	    add(listScrollPane, BorderLayout.CENTER);
        add(buttonContainer, BorderLayout.PAGE_END);
		
	}
	
	/** 
	 * An ActionListener that allows a user to remove a tag from the listModel 
	 * and the TagsCatalog.
	 * 
	 * Implements ActionListener
	 */
	class RemoveTagListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//Stores the selected index in a variable
			int index = tagsList.getSelectedIndex();
			//Removes the index from the TagsCatalog list and the JList
			Image.getTagsCatalog().removeTag(listModel.remove(index));
			int size = listModel.getSize();
			
			//Disables the removeTagButton when there are no elements in the JList
			if (size == 0) {
				removeTagButton.setEnabled(false);
			}
			//Gets the previous index of the removed tag
			else {
				if (index==listModel.getSize()){
					index--;
				}
			//Sets cursor/selected tag to at the previous index of the removed tag
			tagsList.setSelectedIndex(index);
			tagsList.ensureIndexIsVisible(index);
			}
				
		}
	}
	
	/** 
	 * An ActionListener that allows a user to add a tag to the listModel 
	 * and the TagsCatalog.
	 * 
	 * Implements ActionListener
	 * 
	 * Implements DocumentListener that enables a button depending on the
	 * status of a TextField.
	 */
	class AddTagListener implements ActionListener, DocumentListener {
		//Initialized as false unless some text is entered
		private boolean status = false;
		private JButton button;
		
		//Initializes a new AddTagListener
		public AddTagListener(JButton button) {
			this.button = button;
		}
	
		public void actionPerformed(ActionEvent e) {
			//Gets text from JTextArea
			String name = newTag.getText();
			
			//Does not allowed an empty string or a currrently existing tag to be added
			if (name.equals("") || Image.getTagsCatalog().checkTagExists(newTag.getText())) {
				Toolkit.getDefaultToolkit().beep();
				newTag.requestFocusInWindow();
				newTag.selectAll();
				return;
			}
			
			//Controls where selected index will be
			int index = tagsList.getSelectedIndex();
			if (index == -1){
				index = 0;
			}
			else {
				index = listModel.getSize();
			}
			//Adds tag to the TagsCatalog and updates the JList
			Image.getTagsCatalog().addTag(newTag.getText());
			listModel.addElement(newTag.getText());
			
			//Clears the JText area by setting newTag to an empty string
			newTag.requestFocusInWindow();
			newTag.setText("");
			//Sets cursor/selected index to most recent tag added
			tagsList.setSelectedIndex(index);
			tagsList.ensureIndexIsVisible(index);
				
		}
		
		//Methods to be implemented from the DocumentListener interface
		@Override
		public void insertUpdate(DocumentEvent e) {
			enableButton();	
		}
		@Override
		public void removeUpdate(DocumentEvent e) {
			handleEmptyTextField(e);			
		}
		@Override
		public void changedUpdate(DocumentEvent e) {
			if (!handleEmptyTextField(e)) {
                enableButton();
            }
        }
		//Enables a button
		private void enableButton() {
            if (!status) {
                button.setEnabled(true);
            }
		}
		
        private boolean handleEmptyTextField(DocumentEvent e) {
            if (e.getDocument().getLength() <= 0) {
                    button.setEnabled(false);
                    status = false;
                    return true;
             }
             return false;
         }
	}
	
	/** 
	 * Enables the removeTagButton only if an element of a list is selected.
	 * 
	 * @param e
	 * 			A ListElectionEvent
	 */
	public void valueChanged(ListSelectionEvent e) {
    	if (e.getValueIsAdjusting() == false) {
		 
    		if (tagsList.getSelectedIndex() == -1) {
    			removeTagButton.setEnabled(false);

    		} 
    		else {
    			removeTagButton.setEnabled(true);
    		}
    	}
    }
	
	/** 
	 * Creates and shows the GUI for TagsListEditor
	 */
	 protected void createAndShowGUI() {
	        //Create and set up the window.
		 	jframe.setTitle("Tags Catalog");
	        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);      
	        jframe.pack();
	        jframe.setVisible(true);
	 }
	 
	 //Main Method
	 public static void main(String[] args) {
		 TagsListEditor tl = new TagsListEditor();
	     tl.createAndShowGUI();
	     }
	 }