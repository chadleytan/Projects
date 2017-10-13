package photo_renamer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * The catalog of all tags that can be added to an image. 
 * This class allows the addition and removal of tags to the catalog.
 * 
 * Implements TagsList
 * 
 * Implements Serializable to be able to store an instance of
 * TagsCatalog so that the list of all available tags 
 * can be retrieved even after the program exits.
 * 
 * @author Pravpro
 * @author ChadleyTan
 */
public class TagsCatalog implements TagsList, Serializable{
	
	/** The list of tags that can be attached to an image. */
	private ArrayList<String> tagsList;
	
	/**	The TagsCatlog instance for this class */
	private static TagsCatalog catalog;
	
	/**
	 * Creates a new TagsCatalog which initializes an empty 
	 * ArrayList<String> tagsList that stores all available tags that
	 * could be attached to an image.
	 */
	private TagsCatalog() {
		tagsList = new ArrayList<String>();
	}
	

	public static TagsCatalog getInstance(String fileName) {
		if (catalog == null) {
			catalog = TagsCatalog.createTagsCatalog(fileName);
			return catalog;
		}
		return catalog;
	}
	
	/**
	 * Creates a TagsCatalog from an existing serialized TagsCatalog in a file
	 */
	private static TagsCatalog createTagsCatalog(String fileName) {
		TagsCatalog tlist = null;
		try {
			ObjectInputStream tlInput = new ObjectInputStream(new FileInputStream(fileName));
			tlist = (TagsCatalog)tlInput.readObject();
			tlInput.close();
		} catch (FileNotFoundException e1) {
			tlist = new TagsCatalog();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("You have created an instance of TagsCatalog once already");
		}
		return tlist;
	}
	
	/**
	 * Saves an instance of TagsCatalog into a file
	 */
	public void saveTagsCatalog() {
		try {
			ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("tags.txt"));
			os.writeObject(this);
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds tag to tagList.
	 * 
	 * @param tag the tag to add
	 */
	@Override
	public void addTag(String tag) {
		if (!checkTagExists(tag)) {
			tagsList.add(tag);
		}
	}

	/**
	 * Removes tag from tagList.
	 * 
	 * @param tag
	 *            the tag to remove
	 */
	@Override
	public void removeTag(String tag) {
		if (checkTagExists(tag)) {
			tagsList.remove(tag);
		}
	}
	
	/**
	 * Gets the list of available tags.
	 * 
	 * @return
	 *            the list of tags
	 */
	public ArrayList<String> getTagsList() {
		return tagsList;
	}
	
	/**
	 * Returns a string representation of TagsCatalog that 
	 * contains a message and is followed by a new tag on each line.
	 * 
	 * @return s
	 *            String representation of TagsCatalog.
	 */
	public String toString() {
		String s = "This list of tags:";
		for(String tag: tagsList) {
			s += "\n" + tag;
		}
		return s;
	}
	
	/**
	 * Returns a True iff the tag exists in the list of currently existing tags.
	 * 
	 * @param tag
	 * 			  the tag to check
	 * @return 
	 *            True if tag exists .
	 */
	@Override
	public boolean checkTagExists(String tag) {
		if(tagsList.contains(tag)) {
			return true;
		}
		return false;
	}
	
	
	public static void main(String[] args) {
//		//All this in the beginning will create the TagsList tl1, add 2 items to the array 
//		// and store the Object tl1 in a file (using ObjectOutputStream)
//		TagsCatalog tl1 = new TagsCatalog();
//		tl1.addTag("gulu");
//		tl1.addTag("gauru");
//		System.out.println("This is the info we are writing to the file");
//		System.out.println(tl1.toString());
//		tl1.saveTagsCatalog();
		
		
		//Now I'm gonna create a new TagsList using the data from the file tags and print it
		TagsCatalog tc = createTagsCatalog("tags.txt");
		System.out.println(tc.toString());
	}
}
