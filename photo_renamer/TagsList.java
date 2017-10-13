package photo_renamer;

/**
 * Tags that are available, which could be added and removed.
 * 
 * TagsList is an interface to be implemented in its subclass with
 * methods addTag and removeTag that adds and removes a tag respectively
 * from a list of tags.
 * 
 * @author Pravpro
 * @author ChadleyTan
 */
public interface TagsList {

	/**
	 * Adds tag.
	 * 
	 * @param tag
	 *            the tag to add
	 */
	public void addTag(String tag);

	/**
	 * Removes tag.
	 * 
	 * @param tag
	 *            the tag to remove
	 */
	public void removeTag(String tag);
	
	/**
	 * Returns true if tag is in tags else false
	 * 
	 * @param tag 
	 * 			  the tag to check 
	 * @return	
	 * 			  True if tag in the list
	 */
	public boolean checkTagExists(String tag);
}
