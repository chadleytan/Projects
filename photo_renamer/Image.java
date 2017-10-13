package photo_renamer;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An image that contains the name of the image, all the tags that are tagged
 * to this image and records the changes made to the name. 
 * 
 * Implements TagsList
 * 
 * Implements Serializable to be able to retrieve an instance of
 * TagsCatalog from a txt file so that the list of all available tags 
 * can be retrieved and stored in a variable.
 * 
 * Implements Iterator pattern for iterating over the elements of the history of tag
 * changes and is used in the NameHistoryEditor class
 * 
 * @author Pravpro
 * @author ChadleyTan
 */
class Image implements TagsList, Serializable, Iterable<ArrayList<String>> {

	/** The Image's history of tag changes. */
	private ArrayList<ArrayList<String>> tags;
	
	/** The Image's current tags. */
	private ArrayList<String> currTags = new ArrayList<String>();
	
	/** The Image's original name. */
	private String originalName;
	
	/** The Image's current name. */
	private String currName;
	
	/** The image file. */
	private File file;
	
	/** The Image's log of changes. */
	private Logger log = new Logger();
	
	/** The list of all available tags. */
	private static TagsCatalog tc = TagsCatalog.getInstance("tags.txt");
	
	/** The handler which stores the list of editedImages */
	private static ImageHandler ih = ImageHandler.open();
	
	/**
	 * Creates a new Image containing the image name, setting its current name to
	 * original name and creating an empty ArrayList<ArrayList<String>> tags.
	 * 
	 * @param file	
	 * 				the file that the user has selected to edit
	 */
	public Image(File file) {
		this.file = file;
		currName = file.getName().substring(0, file.getName().lastIndexOf("."));
		try{
			Image im = ih.getImage(currName);
			originalName = im.getOriginalName();
			tags = im.getTags();
			currTags = (ArrayList<String>) tags.get(tags.size()-1).clone();
		} catch (NullPointerException e) {
			originalName = currName;
			tags = new ArrayList<ArrayList<String>>();
		}
	}
	
	/**
	 * Adds tag to currTags.
	 * 
	 * @param tag
	 * 			  the tag to add
	 */
	@Override
	public void addTag(String tag) {
		if(!checkTagExists(tag) && tc.checkTagExists(tag)) {
			currTags.add(tag);
		}
	}
	
	/**
	 * Removes tag from currTags.
	 * 
	 * @param tag
	 * 			  the tag to remove
	 */
	@Override
	public void removeTag(String tag) {
		if (checkTagExists(tag)) {
			currTags.remove(tag);	
		}
	}
	
	/**
	 * Returns the history of all tag changes.
	 * 
	 * @return tags
	 * 				the tag changes
	 */
	public ArrayList<ArrayList<String>> getTags(){
		return tags;
	}
	
	/**
	 * Returns the original name of Image.
	 * 
	 * @return originalName
	 *            the original name of Image
	 */
	public String getOriginalName(){
		return originalName;
	}
	
	/**
	 * Returns the file of Image.
	 * 
	 * @return file
	 *            file of Image
	 */
	public File getFile(){
		return this.file;
	}
	
	/**
	 * Returns the current name of Image.
	 * 
	 * @return currName
	 *            the current name of Image
	 */
	public String getCurrName(){
		return currName;
	}
	
	/**
	 * Returns an ArrayList<String> which are the current tags of Image.
	 * 
	 * @return currTags
	 *            the current tags of Image
	 */
	public ArrayList<String> getCurrTags(){
		return currTags;
	}
	
	/**
	 * Returns the instance of TagsCatalog
	 */
	public static TagsCatalog getTagsCatalog() {
		return tc;
	}
	
	/**
	 * Constructs the current existing name of image with its tags.
	 * 
	 *  @param tagged
	 *  		   the image tags
	 *  @return 
	 *  		   the original name and its tags
	 */
	public String constructName(ArrayList<String> tagged){
		String name = "";
		for (String t: tagged){
			name += " @" + t;
		}
		return this.originalName + name;
	}
	
	/**
	 * Changes the current existing name of image and records the tags of a name 
	 * change in tags. Also changes the name of the file. 
	 */
	public void changeName() {
		String oldName = currName;
		currName = constructName(currTags);
		// Update the log with a new rename 
		log.writeEvent(oldName, currName);
		
		// Adds the tags of the new name change to tags (using copy since arrayList is mutable)
		tags.add((ArrayList<String>) currTags.clone());
		
		// Update the Image Handler
		ih.updateImageList(this);
		
		// Changes the name of the file to the current name of Image
		File newName = new File(file.getParent() + "/" + currName + ".jpg");
		this.file.renameTo(newName);
		
	}
	
	/**
	 * Changes the current existing name of image and records the tags of a name 
	 * change in tags.
	 * 
	 * @param index
	 * 			the index of previous name change
	 * 
	 */
	public void changeToPreviousName(int index) {
		currTags = (ArrayList<String>) tags.get(index).clone(); 
	}
	
	/**
	 * Returns all the names that an image file has had.
	 */
	public String imageHistory() {
		String s = "";
		for (ArrayList<String> a: tags){
			s += constructName(a) + "\n";
		}
		return s;
	}

	/**
	 * Returns a True iff the tag exists in the list of current tags of Image.
	 * 
	 * @param tag
	 * 			  the tag to check
	 * @return 
	 *            True if tag exists .
	 */
	@Override
	public boolean checkTagExists(String tag) {
		if(currTags.contains(tag)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Returns a string representation of Image.
	 * 
	 * @return 
	 *         current name of Image
	 */
	@Override
	public String toString() {
		return this.currName;
	}
	
	/**
	 * Returns a string representation of Image.
	 * 
	 * @return 
	 *         current name of Image
	 */
	@Override
	public Iterator<ArrayList<String>> iterator() {
		return new TagsIterator();
	}
	
	/**
	 * Returns an Iterator for this Image.
	 * 
	 * @return 
	 *         an Iterator for this Image.
	 */
	private class TagsIterator implements Iterator<ArrayList<String>> {

        /** The index of the next ArrayList<String> to return. */
        private int current = 0;

        /**
         * Returns whether there is another ArrayList<String> to return.
         * @return whether there is another ArrayList<String> to return.
         */
        @Override
        public boolean hasNext() {
            return current < tags.size();
        }

        /**
         * Returns the next ArrayList<String>.
         * @return the next ArrayList<String>.
         */
        @Override
        public ArrayList<String> next() {
            ArrayList<String> res;

            // List.get(i) throws an IndexOutBoundsException if
            // we call it with i >= contacts.size().
            // But Iterator's next() needs to throw a 
            // NoSuchElementException if there are no more elements.
            try {
                res = tags.get(current);
            } catch (IndexOutOfBoundsException e) {
                throw new NoSuchElementException();
            }
            current += 1;
            return res;
        }
	}

	
//	//Main method to test code.
//	public static void main(String[] args) {
//		TagsCatalog tl1 = new TagsCatalog();
//		tl1.addTag("Chadley");
//		tl1.addTag("Pravir");
//		tl1.saveTagsCatalog();
//		
//		File file = new File("/Users/Chadley/Downloads/photo.jpg");
//		Image im = new Image(file);
//		im.addTag("Chadley");
//		im.addTag("hey");
//		im.changeName();
//		System.out.println(im.getCurrTags());
//		
//		File f1 = new File("C:\\Users\\pravir\\Pictures\\Saved Pictures\\Intro to Evol Antro.jpg");
//		System.out.println(f1.getName());
//		Image im1 = new Image(f1);
//		File f2 = new File("/Users/pravir/Pictures/Saved Pictures/MAT224 - Linear Algebra @Chadley.jpg");
//		Image im2 = new Image(f2);
//		
//		im1.addTag("Chadley");
//		im1.changeName();
//		im1.addTag("Pravir");
//		im1.changeName();
//		for (ArrayList<String> im: im1) {
//			System.out.println(im);
//		}
//	}
}
