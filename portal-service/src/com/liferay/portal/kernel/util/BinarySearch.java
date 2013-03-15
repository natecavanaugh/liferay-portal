/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.kernel.util;

import java.util.Comparator;
import java.util.List;

/**
 * @author Igor Spasic
 */
public abstract class BinarySearch<E> {

	public static <T extends Comparable<T>> BinarySearch<T> forList(
		final List<T> list) {

		return new BinarySearch<T>() {

			@Override
			protected int compare(int index, T e) {
				return list.get(index).compareTo(e);
			}

			@Override
			protected int getLastIndex() {
				return list.size() - 1;
			}

		};
	}

	public static <T> BinarySearch<T> forList(
		final List<T> list, final Comparator<T> comparator) {

		return new BinarySearch<T>() {

			@Override
			protected int compare(int index, T e) {
				return comparator.compare(list.get(index), e);
			}

			@Override
			protected int getLastIndex() {
				return list.size() - 1;
			}

		};
	}

	public int find(E e) {
		return find(e, 0, getLastIndex());
	}

	public int find(E e, int low) {
		return find(e, low, getLastIndex());
	}

	public int find(E e, int low, int high) {
		while (low <= high) {
			int mid = (low + high) >>> 1;

			int delta = compare(mid, e);

			if (delta < 0) {
				low = mid + 1;
			}
			else if (delta > 0) {
				high = mid - 1;
			}
			else {
				return mid;
			}
		}

		return -(low + 1);
	}

	public int findFirst(E e) {
		return findFirst(e, 0, getLastIndex());
	}

	public int findFirst(E e, int low) {
		return findFirst(e, low, getLastIndex());
	}

	public int findFirst(E e, int low, int high) {
		int index = -1;

		while (low <= high) {
			int mid = (low + high) >>> 1;

			int delta = compare(mid, e);

			if (delta < 0) {
				low = mid + 1;
			}
			else {
				if (delta == 0) {
					index = mid;
				}

				high = mid - 1;
			}
		}

		if (index == -1) {
			return -(low + 1);
		}

		return index;
	}

	public int findLast(E e) {
		return findLast(e, 0, getLastIndex());
	}

	public int findLast(E e, int low) {
		return findLast(e, low, getLastIndex());
	}

	public int findLast(E e, int low, int high) {
		int index = -1;

		while (low <= high) {
			int mid = (low + high) >>> 1;

			int delta = compare(mid, e);

			if (delta > 0) {
				high = mid - 1;
			}
			else {
				if (delta == 0) {
					index = mid;
				}

				low = mid + 1;
			}
		}

		if (index == -1) {
			return -(low + 1);
		}

		return index;
	}

	protected abstract int compare(int index, E element);

	protected abstract int getLastIndex();

}