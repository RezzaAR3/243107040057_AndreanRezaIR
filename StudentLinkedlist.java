public class StudentLinkedList {
    static class Student {
        String nim, name;
        double gpa;
        Student next;

        Student(String nim, String name, double gpa) {
            this.nim = nim;
            this.name = name;
            this.gpa = gpa;
        }
    }

    private Student head, tail;
    private int size;

    public void insertLast(String nim, String name, double gpa) {
        Student newStudent = new Student(nim, name, gpa);
        if (head == null) {
            head = tail = newStudent;
        } else {
            tail.next = newStudent;
            tail = newStudent;
        }
        size++;
    }

    public void display() {
        System.out.println("=== Data Mahasiswa ===");
        Student current = head;
        while (current != null) {
            System.out.printf("NIM: %s  %-18s IPK: %.2f%n", current.nim, current.name, current.gpa);
            current = current.next;
        }
        System.out.println("Total: " + size);
    }

    public void visualizeList() {
        if (head == null) {
            System.out.println("[EMPTY LIST]");
            return;
        }

        StringBuilder nodes = new StringBuilder();
        StringBuilder nimLine = new StringBuilder("     ");
        StringBuilder gpaLine = new StringBuilder("     ");

        Student current = head;
        while (current != null) {
            String box = String.format("[%-10s]", current.name.length() > 10 ? current.name.substring(0, 10) : current.name);
            nodes.append(box).append(" -> ");
            nimLine.append(String.format("NIM: %-6s     ", current.nim));
            gpaLine.append(String.format("IPK: %-5.2f    ", current.gpa));
            current = current.next;
        }

        nodes.append("NULL");
        System.out.println(nodes);
        System.out.println(nimLine);
        System.out.println(gpaLine);
    }
}
