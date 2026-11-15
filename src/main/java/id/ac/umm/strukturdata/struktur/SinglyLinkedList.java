package id.ac.umm.strukturdata.struktur;

public class SinglyLinkedList<T> implements StrukturData<T> {

    private static class Node<T> {
        T data;
        Node<T> next;
        Node(T data) { this.data = data; }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;
    private int lastSearchSteps;

    public void addFirst(T value) {
        Node<T> node = new Node<>(value);
        node.next = head;
        head = node;
        if (tail == null) tail = node;
        size++;
    }

    public void addLast(T value) {
        Node<T> node = new Node<>(value);
        if (tail == null) {
            head = node;
            tail = node;
        } else {
            tail.next = node;
            tail = node;
        }
        size++;
    }

    @Override
    public void add(T value) { addLast(value); }

    public void insert(int position, T value) {
        if (position < 0 || position > size)
            throw new IndexOutOfBoundsException("Posisi " + position + ", size " + size);
        if (position == 0) { addFirst(value); return; }
        if (position == size) { addLast(value); return; }
        Node<T> prev = nodeAt(position - 1);
        Node<T> node = new Node<>(value);
        node.next = prev.next;
        prev.next = node;
        size++;
    }

    @Override
    public T get(int position) {
        checkIndex(position);
        return nodeAt(position).data;
    }

    @Override
    public T remove(int position) {
        checkIndex(position);
        Node<T> removed;
        if (position == 0) {
            removed = head;
            head = head.next;
            if (head == null) tail = null;
        } else {
            Node<T> prev = nodeAt(position - 1);
            removed = prev.next;
            prev.next = removed.next;
            if (removed == tail) tail = prev;
        }
        removed.next = null;
        size--;
        return removed.data;
    }

    /** Mencari posisi elemen pertama yang cocok; mengisi jumlah langkah. -1 jika tidak ada. */
    public int search(Kriteria<T> kriteria) {
        lastSearchSteps = 0;
        Node<T> current = head;
        int index = 0;
        while (current != null) {
            lastSearchSteps++;
            if (kriteria.cocok(current.data)) return index;
            current = current.next;
            index++;
        }
        return -1;
    }

    @Override
    public boolean contains(T value) {
        return search(item -> item.equals(value)) >= 0;
    }

    public int getLastSearchSteps() { return lastSearchSteps; }

    @Override public int size() { return size; }
    @Override public boolean isEmpty() { return size == 0; }

    @Override
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    public void validateInvariant() {
        if (size == 0) {
            if (head != null || tail != null)
                throw new IllegalStateException("List kosong tapi head/tail tidak null");
            return;
        }
        if (head == null || tail == null)
            throw new IllegalStateException("List tidak kosong tapi head/tail null");
        if (tail.next != null)
            throw new IllegalStateException("tail.next harus null");
        int count = 0;
        Node<T> current = head;
        Node<T> last = null;
        while (current != null) {
            count++;
            if (count > size) throw new IllegalStateException("Jumlah node melebihi size (siklus?)");
            last = current;
            current = current.next;
        }
        if (count != size) throw new IllegalStateException("size tidak sama dengan jumlah node");
        if (last != tail) throw new IllegalStateException("tail bukan node terakhir");
    }

    private void checkIndex(int position) {
        if (position < 0 || position >= size)
            throw new IndexOutOfBoundsException("Posisi " + position + ", size " + size);
    }

    private Node<T> nodeAt(int position) {
        Node<T> current = head;
        for (int i = 0; i < position; i++) current = current.next;
        return current;
    }
}