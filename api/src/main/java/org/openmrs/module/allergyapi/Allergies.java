/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.allergyapi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.openmrs.api.APIException;

/**
 * Represents patient allergies
 */
public class Allergies implements List<Allergy> {
	
	public static final String UNKNOWN = "unknown";
	
	public static final String NO_KNOWN_ALLERGIES = "no known allergies";
	
	public static final String SEE_LIST = "see list";
	
	private String allergyStatus = UNKNOWN;
	
	private List<Allergy> allergies = new ArrayList<Allergy>();
	
	
    /**
     * @return the allergyStatus
     */
    public String getAllergyStatus() {
    	return allergyStatus;
    }

	public boolean add(Allergy allergy) {
		allergyStatus = SEE_LIST;
		return allergies.add(allergy);
	}
	
	public boolean remove(Allergy allergy) {
		boolean result = allergies.remove(allergy);
		if (allergies.isEmpty()) {
			allergyStatus = UNKNOWN;
		}
		return result;
	}
	
	public void clear() {
		allergyStatus = UNKNOWN;
		allergies.clear();
	}
	
	public void confirmNoKnownAllergies() throws APIException {
		if (!allergies.isEmpty()) {
			throw new APIException("Cannot confirm no known allergies if allergy list is not empty");
		}
		allergyStatus = NO_KNOWN_ALLERGIES;
	}
	
	/**
	 * @see java.util.List#iterator()
	 */
	@Override
	public Iterator<Allergy> iterator() {
		return allergies.iterator();
	}
	
	/**
	 * @see java.util.List#add(int, java.lang.Object)
	 */
	@Override
	public void add(int index, Allergy element) {
		allergies.add(index, element);
		allergyStatus = SEE_LIST;
	}
	
	/**
	 * @see java.util.List#addAll(java.util.Collection)
	 */
	@Override
	public boolean addAll(Collection<? extends Allergy> c) {
		allergyStatus = SEE_LIST;
		return allergies.addAll(c);
	}
	
	/**
	 * @see java.util.List#addAll(int, java.util.Collection)
	 */
	@Override
	public boolean addAll(int index, Collection<? extends Allergy> c) {
		allergyStatus = SEE_LIST;
		return allergies.addAll(index, c);
	}
	
	/**
	 * @see java.util.List#contains(java.lang.Object)
	 */
	@Override
	public boolean contains(Object o) {
		return allergies.contains(o);
	}
	
	/**
	 * @see java.util.List#containsAll(java.util.Collection)
	 */
	@Override
	public boolean containsAll(Collection<?> c) {
		return allergies.containsAll(c);
	}
	
	/**
	 * @see java.util.List#get(int)
	 */
	@Override
	public Allergy get(int index) {
		return allergies.get(index);
	}
	
	/**
	 * @see java.util.List#indexOf(java.lang.Object)
	 */
	@Override
	public int indexOf(Object o) {
		return allergies.indexOf(o);
	}
	
	/**
	 * @see java.util.List#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return allergies.isEmpty();
	}
	
	/**
	 * @see java.util.List#lastIndexOf(java.lang.Object)
	 */
	@Override
	public int lastIndexOf(Object o) {
		return allergies.lastIndexOf(o);
	}
	
	/**
	 * @see java.util.List#listIterator()
	 */
	@Override
	public ListIterator<Allergy> listIterator() {
		return allergies.listIterator();
	}
	
	/**
	 * @see java.util.List#listIterator(int)
	 */
	@Override
	public ListIterator<Allergy> listIterator(int index) {
		return allergies.listIterator(index);
	}
	
	/**
	 * @see java.util.List#remove(int)
	 */
	@Override
	public Allergy remove(int index) {
		Allergy allergy = allergies.remove(index);
		if (allergies.isEmpty()) {
			allergyStatus = UNKNOWN;
		}
		return allergy;
	}
	
	/**
	 * @see java.util.List#remove(java.lang.Object)
	 */
	@Override
	public boolean remove(Object o) {
		Boolean removed = allergies.remove(o);
		if (allergies.isEmpty()) {
			allergyStatus = UNKNOWN;
		}
		return removed;
	}
	
	/**
	 * @see java.util.List#removeAll(java.util.Collection)
	 */
	@Override
	public boolean removeAll(Collection<?> c) {
		boolean changed = allergies.removeAll(c);
		if (allergies.isEmpty()) {
			allergyStatus = UNKNOWN;
		}
		return changed;
	}
	
	/**
	 * @see java.util.List#retainAll(java.util.Collection)
	 */
	@Override
	public boolean retainAll(Collection<?> c) {
		boolean changed = allergies.retainAll(c);
		if (allergies.isEmpty()) {
			allergyStatus = UNKNOWN;
		}
		return changed;
	}
	
	/**
	 * @see java.util.List#set(int, java.lang.Object)
	 */
	@Override
	public Allergy set(int index, Allergy element) {
		allergyStatus = SEE_LIST;
		return allergies.set(index, element);
	}
	
	/**
	 * @see java.util.List#size()
	 */
	@Override
	public int size() {
		return allergies.size();
	}
	
	/**
	 * @see java.util.List#subList(int, int)
	 */
	@Override
	public List<Allergy> subList(int fromIndex, int toIndex) {
		return allergies.subList(fromIndex, toIndex);
	}
	
	/**
	 * @see java.util.List#toArray()
	 */
	@Override
	public Object[] toArray() {
		return allergies.toArray();
	}
	
	/**
	 * @see java.util.List#toArray(T[])
	 */
	@Override
	public <T> T[] toArray(T[] a) {
		return allergies.toArray(a);
	}
}
