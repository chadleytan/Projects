package photo_renamer;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TagsCatalogTest {

	private static TagsCatalog catalog = getCatalog();
	
	/** 
	 * The TagsCatalog object to do testing with (an empty object unless file created previously)
	 */
	private static TagsCatalog getCatalog() {
		return catalog = TagsCatalog.getInstance("tagsTest.txt");
	}
	
	
	/**
	 * Test to check if a tag is successfully added to the TagsCatalog
	 */
	@Test
	public void addTagNotInCatalogTest() {
		catalog.addTag("tag1");
		
		assertTrue(catalog.getTagsList().contains("tag1"));
	}
	
	/**
	 * Test to make sure an already existing tag does not get added again
	 */
	@Test
	public void addTagAlreadyInCatalogTest() {
		catalog.addTag("tag1");
		catalog.addTag("tag1");
		
		assertEquals(catalog.getTagsList().size(), 1);
	}
	
	/**
	 * Test to check if a tag is successfully removed from a TagsCatalog object
	 */
	@Test
	public void removeTagAlreadyInCatalogTest() {
		System.out.println(catalog == null);
		catalog.addTag("tag1");
		catalog.removeTag("tag1");
		
		assertEquals(catalog.getTagsList().size(), 0);
	}
	
	/**
	 * Test to make sure nothing happens when a tag not in a TagsCatalog tries to be removed
	 */
	@Test
	public void removeTagInEmptyCatalogTest() {
		catalog.removeTag("tag1");
		
		assertEquals(catalog.getTagsList().size(), 0);
	}
	
	

}
