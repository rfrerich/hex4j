package de.lambdamoo.hex4j.search.finder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

import de.lambdamoo.hex4j.search.ActionStoppedException;
import de.lambdamoo.hex4j.search.Finder;
import de.lambdamoo.hex4j.search.estimator.Estimator;
import de.lambdamoo.hex4j.search.producer.SuccessorProducer;

public class AStarFinder<T> implements Finder<T> {

	private final SuccessorProducer<T> producer;
	private final Estimator<T> estimator;

	public AStarFinder(SuccessorProducer<T> prod, Estimator<T> estimator) {
		this.producer = prod;
		this.estimator = estimator;
	}

	public List<T> find(T start, T goal, StopDemander stop) {
		Node<T> startNode = new Node<T>(start, null, 0, estimator.estimate(start, goal));
		if (start.equals(goal)) {
			return startNode.getPathElements();
		}

		// PriorityQueue with the total f-cost as the ordering criteria
		PriorityQueue<Node<T>> openList = new PriorityQueue<Node<T>>(new Comparator<Node<T>>() {
			@Override
			public int compare(Node<T> o1, Node<T> o2) {
				return Integer.compare(o1.getCostF(), o2.getCostF());
			}
		});
		List<T> closedList = new ArrayList<T>();

		openList.add(startNode);
		closedList.add(start);

		while (!openList.isEmpty()) {
			Iterator<Node<T>> it = openList.iterator();
			Node<T> currentNode = it.next();
			it.remove();

			if (currentNode.getElement().equals(goal)) {
				return currentNode.getPathElements();
			}
			if (stop != null && stop.isDemandingStop()) {
				throw new ActionStoppedException();
			}

			closedList.add(currentNode.getElement());

			// expand the node
			List<T> successors = producer.getSuccessors(currentNode.getElement());
			for (T successor : successors) {
				if (closedList.contains(successor)) {
					continue;
				}

				// calculate the new g-value. You could add here a weight for
				// the new edge, if your terrain has different movement costs
				int gCurrent = currentNode.getCostG();
				int tentative_g = gCurrent++;
				int hSuccessor = estimator.estimate(successor, goal);

				// check whether the successor is already on the open list
				// if the new path is more costly then the existing, do nothing
				Node<T> oldPathToNode = getNode(openList, successor);
				if (oldPathToNode != null && tentative_g >= oldPathToNode.getCostG()) {
					continue;
				}

				Node<T> successorNode = new Node<T>(successor, currentNode, tentative_g, hSuccessor);
				if (oldPathToNode != null) {
					// exchange the better path with the old one
					openList.remove(oldPathToNode);
				}
				openList.add(successorNode);

			}
		}
		// Not found
		return null;
	}

	private Node<T> getNode(PriorityQueue<Node<T>> queue, T element) {
		for (Node<T> node : queue) {
			if (node.getElement().equals(element)) {
				return node;
			}
		}
		return null;
	}
}
