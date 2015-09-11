package de.lambdamoo.hex4j.search.finder;

import java.util.LinkedList;
import java.util.List;

class Node<T> {

	private final T element;
	private final Node<T> parent;
	/**
	 * Current cost from the start to this node
	 */
	private final int costG;
	/**
	 * Estimated, remaining cost from the node to the goal
	 */
	private final int costH;

	public Node(T element, Node<T> parent, int costG, int costH) {
		this.element = element;
		this.parent = parent;
		this.costG = costG;
		this.costH = costH;
	}

	public T getElement() {
		return element;
	}

	public int getCostG() {
		return costG;
	}

	public int getCostH() {
		return costG;
	}

	/**
	 * This is the total value of the costs for this node
	 * 
	 * @return
	 */
	public int getCostF() {
		return costG + costH;
	}

	public List<T> getPathElements() {
		List<T> result = new LinkedList<T>();
		Node<T> node = this;
		result.add(node.element);
		while (node.parent != null) {
			node = node.parent;
			result.add(0, node.element);
		}
		return result;
	}

	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof Node)) {
			return false;
		}
		return element.equals(((Node) o).element);
	}

	public int hashCode() {
		return element.hashCode();
	}

	public String toString() {
		return element.toString();
	}
}
