import java.util.ArrayList;
import java.util.List;

public class StudentLinkedlist {

    /* ======================  NODE  ====================== */
    static class Student {
        String nim;
        String name;
        double gpa;
        Student next;

        public Student(String nim, String name, double gpa) {
            this.nim  = nim;
            this.name = name;
            this.gpa  = gpa;
            this.next = null;
        }
    }

    /* ===============  SINGLE LINKED LIST  =============== */
    static class StudentLinkedList {
        private Student head;
        private Student tail;
        private int size;

        /* ---------- METODE DASAR (TIDAK DIUBAH) ---------- */
        public void insertFirst(String nim, String name, double gpa) {
            Student newNode = new Student(nim, name, gpa);
            newNode.next = head;
            head = newNode;
            if (tail == null) tail = newNode;
            size++;
        }

        public void insertLast(String nim, String name, double gpa) {
            Student newNode = new Student(nim, name, gpa);
            if (isEmpty()) {
                head = tail = newNode;
            } else {
                tail.next = newNode;
                tail = newNode;
            }
            size++;
        }

        public void insertAt(int position, String nim, String name, double gpa) {
            if (position < 0 || position > size) {
                System.out.println("Posisi tidak valid!");
                return;
            }
            if (position == 0) { insertFirst(nim, name, gpa); return; }
            if (position == size) { insertLast(nim, name, gpa); return; }

            Student newNode = new Student(nim, name, gpa);
            Student prev = head;
            for (int i = 0; i < position - 1; i++) prev = prev.next;
            newNode.next = prev.next;
            prev.next = newNode;
            size++;
        }

        public boolean deleteByNim(String nim) {
            if (isEmpty()) return false;

            if (head.nim.equals(nim)) {
                head = head.next;
                if (head == null) tail = null;
                size--; return true;
            }
            Student prev = head, curr = head.next;
            while (curr != null && !curr.nim.equals(nim)) {
                prev = curr; curr = curr.next;
            }
            if (curr == null) return false;

            prev.next = curr.next;
            if (curr == tail) tail = prev;
            size--; return true;
        }

        public Student search(String nim) {
            for (Student c = head; c != null; c = c.next)
                if (c.nim.equals(nim)) return c;
            return null;
        }

        public void display() {
            System.out.println("=== Data Mahasiswa ===");
            for (Student c = head; c != null; c = c.next)
                System.out.printf("NIM: %s, Nama: %-15s IPK: %.2f%n",
                                  c.nim, c.name, c.gpa);
            System.out.println("Total mahasiswa: " + size);
        }

        public int size() { return size; }
        public boolean isEmpty() { return head == null; }

        /* ---------- METODE LANJUTAN (DITAMBAHKAN) ---------- */

        // 1. Urutkan descending berdasarkan GPA (bubble‑sort, swap data)
        public void sortByGPA() {
            if (size < 2) return;
            boolean swapped;
            do {
                swapped = false;
                for (Student cur = head; cur.next != null; cur = cur.next) {
                    if (cur.gpa < cur.next.gpa) {
                        // tukar data dalam node
                        String tNim = cur.nim;  String tName = cur.name; double tGpa = cur.gpa;
                        cur.nim          = cur.next.nim;
                        cur.name         = cur.next.name;
                        cur.gpa          = cur.next.gpa;
                        cur.next.nim     = tNim;
                        cur.next.name    = tName;
                        cur.next.gpa     = tGpa;
                        swapped = true;
                    }
                }
            } while (swapped);
        }

        // 2. Balik urutan list
        public void reverse() {
            Student prev = null, cur = head, next;
            tail = head;                         // head lama menjadi tail baru
            while (cur != null) {
                next = cur.next;
                cur.next = prev;
                prev = cur;
                cur = next;
            }
            head = prev;
        }

        // 3. Mahasiswa dengan IPK tertinggi
        public Student findHighestGPA() {
            if (isEmpty()) return null;
            Student max = head;
            for (Student c = head.next; c != null; c = c.next)
                if (c.gpa > max.gpa) max = c;
            return max;
        }

        // 4. Daftar mahasiswa dengan IPK di atas threshold
        public List<Student> getStudentsAboveGPA(double threshold) {
            List<Student> list = new ArrayList<>();
            for (Student c = head; c != null; c = c.next)
                if (c.gpa > threshold) list.add(c);
            return list;
        }

        // 5. Merge dua list yang sudah di‑sort descending
        public static StudentLinkedList mergeSortedList(StudentLinkedList a, StudentLinkedList b) {
            StudentLinkedList merged = new StudentLinkedList();
            Student p1 = a.head, p2 = b.head;

            while (p1 != null || p2 != null) {
                if (p2 == null || (p1 != null && p1.gpa >= p2.gpa)) {
                    merged.insertLast(p1.nim, p1.name, p1.gpa);
                    p1 = p1.next;
                } else {
                    merged.insertLast(p2.nim, p2.name, p2.gpa);
                    p2 = p2.next;
                }
            }
            return merged;
        }
    }

    /* =========================  DEMO  ========================= */
    public static void main(String[] args) {

        /* --------- Contoh operasi dasar --------- */
        StudentLinkedList list = new StudentLinkedList();
        list.insertLast("12345", "Andrean Reza",      3.95);
        list.insertLast("12346", "Afif Athallah",     3.82);
        list.insertLast("12347", "Arief Hidayatullah",3.65);

        list.display();

        list.insertFirst("12344", "M. Zein", 3.90);
        list.insertAt(2, "12348", "Gilang Afrilian", 3.70);
        System.out.println("\nSetelah insertFirst & insertAt:");
        list.display();

        list.deleteByNim("12347");
        System.out.println("\nSetelah deleteByNim:");
        list.display();

        /* --------- Operasi lanjutan --------- */
        System.out.println("\nSEBELUM sortByGPA():");
        list.display();

        list.sortByGPA();
        System.out.println("\nSETELAH sortByGPA():");
        list.display();

        list.reverse();
        System.out.println("\nSETELAH reverse():");
        list.display();

        Student top = list.findHighestGPA();
        System.out.printf("%nMahasiswa IPK tertinggi: %s (%.2f)%n",
                          top.name, top.gpa);

        System.out.println("\nMahasiswa dengan IPK > 3.7:");
        for (Student s : list.getStudentsAboveGPA(3.7))
            System.out.printf("- %s (%.2f)%n", s.name, s.gpa);

        /* --------- Merge dua list terurut --------- */
        StudentLinkedList another = new StudentLinkedList();
        another.insertLast("20001", "Rina", 3.90);
        another.insertLast("20002", "Agus", 3.70);
        another.sortByGPA();                     // pastikan terurut

        StudentLinkedList merged = StudentLinkedList.mergeSortedList(list, another);
        System.out.println("\nSETELAH merge dua list terurut:");
        merged.display();
    }
}
