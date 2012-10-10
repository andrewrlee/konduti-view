package uk.co.optimisticpanda.storybuilder.section;

import static com.google.common.collect.Iterables.find;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.reverse;

import java.util.ArrayList;
import java.util.List;

import uk.co.optimisticpanda.utils.Functions.LowerAndReplaceWhitespaceWithUnderscores;
import uk.co.optimisticpanda.utils.Functions.Named;
import uk.co.optimisticpanda.utils.Functions.With;

import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
/**
 * A tree node.
 */
public class Node implements Named{

	private final String name;
	private final String path;
	private List<Node> children = newArrayList();
	private transient final Node parent;
	@SuppressWarnings("unused")
	private String dotPath;

	public Node(String name) {
		this(null, name);
	}

	public Node(Node parent, String name) {
		this.parent = parent;
		this.name = name;
		this.path = getGTSeperatedPath();
		this.dotPath = getDotSeperatedPath();
	}

	public boolean isRoot() {
		return parent == null;
	}

	public boolean isLeaf() {
		return children.isEmpty();
	}

	Node addChild(String name) {
		Node node = new Node(this, name);
		this.children.add(node);
		return node;
	}

	public List<Node> getChildren() {
		return children;
	}

	public String getName() {
		return name;
	}

	public Node getDescendent(String... path) {
		Node currentNode = this;
		for (String pathElement : path) {
			Node found = currentNode.findChild(pathElement);
			if (found == null) {
				return null;
			}
			currentNode = found;
		}
		return currentNode;
	}

	boolean hasDescendent(String... path) {
		return getDescendent(path) != null;
	}

	public Node addDescendent(String... path) {
		Node currentNode = this;
		for (String element : path) {
			Node child = currentNode.findChild(element);
			if (child == null) {
				child = currentNode.addChild(element);
			}
			currentNode = child;
		}
		return currentNode;
	}

	Node findChild(final String name) {
		return find(children, With.name(name), null);
	}

	public Iterable<Node> getLeaves() {
		ArrayList<Node> leaves = Lists.newArrayList();
		findLeaves(leaves, this);
		return leaves;
	}

	private void findLeaves(List<Node> leaves, Node currentNode) {
		for (Node child : currentNode.children) {
			if (child.isLeaf()) {
				leaves.add(child);
			} else {
				findLeaves(leaves, child);
			}
		}
	}

	private List<String> getPathToRoot() {
		List<String> paths = newArrayList();
		Node current = this;
		while (current != null) {
			paths.add(current.name);
			current = current.parent;
		}
		return paths;
	}

	public String getGTSeperatedPath() {
		return Joiner.on(" > ").join(reverse(getPathToRoot()));
	}

	public String getDotSeperatedPath() {
		return Joiner.on(".").join(Iterables.transform(reverse(getPathToRoot()), new LowerAndReplaceWhitespaceWithUnderscores()));
	}

	@Override
	public String toString() {
		return path;
	}
}
