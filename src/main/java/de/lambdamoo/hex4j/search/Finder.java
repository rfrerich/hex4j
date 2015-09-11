package de.lambdamoo.hex4j.search;

import java.util.List;

import de.lambdamoo.hex4j.search.finder.StopDemander;

public interface Finder<T> {

	List<T> find(T start, T goal, StopDemander stop) throws ActionStoppedException;

}