package de.lambdamoo.hex4j.search.finder;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import de.lambdamoo.hex4j.search.Finder;
import de.lambdamoo.hex4j.search.producer.SuccessorProducer;

public class DepthFirstFinder<T> implements Finder<T> {

	private final SuccessorProducer<T> producer;

	public DepthFirstFinder(SuccessorProducer<T> prod) {
		this.producer = prod;
	}

	public List<T> find(T start, T goal, StopDemander stop) {
		Node<T> startNode = new Node<T>(start, null, 0, 0);
		if (start.equals(goal)) {
			return startNode.getPathElements();
		}

		List<Node<T>> queue = new LinkedList<Node<T>>();
		Set<T> visited = new HashSet<T>();

		queue.add(startNode);
		visited.add(start);

		while (!queue.isEmpty()) {
			Node<T> node = queue.remove(queue.size() - 1);

			List<T> successors = producer.getSuccessors(node.getElement());
			for (T successor : successors) {
				if (!visited.contains(successor)) {
					Node<T> subNode = new Node(successor, node, 0, 0);
					if (successor.equals(goal)) {
						// The goal is found
						return subNode.getPathElements();
					}
					queue.add(subNode);
					visited.add(successor);
				}
			}
		}
		// Not found
		return null;
	}
}