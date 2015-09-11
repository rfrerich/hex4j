package de.lambdamoo.hex4j.search.producer;

import java.util.List;

public interface SuccessorProducer<T> {
	List<T> getSuccessors(T object);
}
