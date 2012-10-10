package uk.co.optimisticpanda.storybuilder.section;

import static com.google.common.collect.Iterables.getOnlyElement;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.collect.Iterables;

public class NodeTest {

	@Test
	public void isRoot(){
		Node root = new Node("a");
		assertTrue(root.isRoot());
		root.addChild("b");
		assertFalse(getOnlyElement(root.getChildren()).isRoot());
	}

	@Test
	public void isLeaf(){
		Node root = new Node("a");
		assertTrue(root.isLeaf());
		
		root.addChild("b");
		assertFalse(root.isLeaf());
		assertTrue(getOnlyElement(root.getChildren()).isLeaf());
	}
	
    @Test
	public void getDescendent(){
    	Node root = new Node("a");
    	root.addChild("b1"); //
    	root.addChild("b2"); //

    	Node b1 = root.getDescendent("b1");
		assertEquals("b1", b1.getName());

		b1.addChild("c1");
		Node c1 = root.getDescendent("b1", "c1");
		assertEquals("c1", c1.getName());
		
		c1.addChild("d1");
		Node d1 = root.getDescendent("b1", "c1", "d1");
		assertEquals("d1", d1.getName());
    }
    
    @Test
	public void hasDescendent(){
    	Node root = new Node("a");
		root.addChild("b1"); //
    	assertTrue(root.hasDescendent("b1"));
    	
    	Node b1 = root.getDescendent("b1");
    	b1.addChild("c1");

    	assertTrue(root.hasDescendent("b1", "c1"));
    }

    @Test
	public void addDescendent(){
    	Node root = new Node("a");
    	root.addDescendent("b1"); //
    	assertTrue(root.hasDescendent("b1"));
    	
    	root.addDescendent("b1", "c1"); //
    	assertTrue(root.hasDescendent("b1", "c1"));
    	
    	root.addDescendent("b1", "c2"); //
    	assertThat(root.getDescendent("b1").getChildren().size(), is(2));
    	assertThat(root.getDescendent("b1").getChildren().get(0).getName(), is("c1"));
    	assertThat(root.getDescendent("b1").getChildren().get(1).getName(), is("c2"));
    	
    	Node descendent = root.addDescendent("b1", "c2", "d1"); //
    	assertThat(descendent.getName(), is("d1"));
    }
    
    @Test
    public void getLeaves(){
    	Node root = new Node("a");
    	root.addDescendent("b1"); //
    	root.addDescendent("b2"); //
    	root.addDescendent("b1", "c1"); //
    	root.addDescendent("b1", "c2"); //
    	root.addDescendent("b1", "c2" , "d3"); //
    	root.addDescendent("b1", "c2" , "d4"); //
    	
    	Iterable<Node> leaves = root.getLeaves();
    	assertThat(Iterables.size(leaves), is(4));
    	assertThat(leaves, hasItems( //
    			root.getDescendent("b2"),     //
    			root.getDescendent("b1", "c1"),   //
    			root.getDescendent("b1", "c2", "d3"),  // 
    			root.getDescendent("b1", "c2", "d4")  //
    			));
    }

    @Test
    public void getDotSeperatePath(){
    	Node root = new Node("a");
    	Node descendent = root.addDescendent("b1", "c2" , "d4"); //
    	assertThat(descendent.getDotSeperatedPath(), is("a.b1.c2.d4"));
    	
    	descendent = root.addDescendent("b1", "C2" , "d 4"); //
    	assertThat(descendent.getDotSeperatedPath(), is("a.b1.c2.d4"));
    }

    @Test 
    public void getGreaterThanSeperatedPath(){
    	Node root = new Node("a");
    	assertThat(root.getGTSeperatedPath(), is("a"));
    	Node child = root.addChild("b");
    	assertThat(child.getGTSeperatedPath(), is("a > b"));
    	
    }
}
