public class MinHeap {

	private SuperNode[] heap;
	private int[] distance;
	private int size;
	private int maxsize;

	public MinHeap(int maxSize) {
		this.heap = new SuperNode[maxSize];
		this.distance = new int[maxSize];
		this.maxsize = maxSize;
		this.size = 0;
	}

	private int getParent(int position) {
		return (position - 1) / 2;
	}

	private int getLeftChild(int position) {
		return (2 * position) + 1;
	}

	private int getRightChild(int position) {
		return 2 * position + 2;
	}

	private void swap(int position1, int position2) {
		SuperNode temp = heap[position1];
		int tempdist = distance[position1];
		heap[position1] = heap[position2];
		distance[position1] = distance[position2];
		heap[position2] = temp;
		distance[position2] = tempdist;
	}

	private boolean isLeaf(int position) {
		return position >= size / 2;

	}

	public void insert(SuperNode n, int distance) {
		if (size < maxsize) {
			heap[size] = n;
			this.distance[size] = distance;
			int currentItem = size;
			while (this.distance[getParent(currentItem)] > this.distance[currentItem]) {
				swap(getParent(currentItem), currentItem);
				currentItem = getParent(currentItem);
			}
			size++;
		} else {
			System.out.println("Plein");
		}
	}

	public SuperNode delete() {
		SuperNode itemPopped = null;
		if (size > 1) {
			itemPopped = heap[0];
			swap(0, size - 1);
			heap[size - 1] = null;
			distance[size - 1] = Integer.MAX_VALUE;
			size--;
			heapify(0);
		} else if (size == 1) {
			itemPopped = heap[0];
			size--;
		} else
			System.out.println("Vide");
		return itemPopped;
	}

	private void heapify(int position) {
		if (isLeaf(position)) {
			return;
		}

		if (distance[position] > distance[getLeftChild(position)]
				|| distance[position] > distance[getRightChild(position)]) {

			if (distance[getLeftChild(position)] < distance[getRightChild(position)]) {
				swap(position, getLeftChild(position));
				heapify(getLeftChild(position));
			} else {
				swap(position, getRightChild(position));
				heapify(getRightChild(position));
			}
		}
	}
	public Boolean isEmpty(){
		return size==0;
	}
	@Override
	public String toString() {
		StringBuilder output = new StringBuilder();
		for (int i = 0; i < size; i++) {
			output.append("Parent :" + heap[i].getId() + " \n");

		}
		return output.toString();
	}

}
