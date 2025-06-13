import java.util.*;

/* =========================================================
 *  Main.java  —  tiga struktur data dalam satu file:
 *    1)  StudentLinkedList   (single linked list + fitur lanjutan)
 *    2)  CircularPlaylist    (circular linked list untuk musik)
 *    3)  TextEditor          (doubly linked list undo/redo)
 * ========================================================= */
public class Main {

    /* =====================================================
     *                SINGLE LINKED LIST MAHASISWA
     * ===================================================== */
    static class Student {
        String nim;
        String name;
        double gpa;
        Student next;
        Student(String nim, String name, double gpa) {
            this.nim = nim; this.name = name; this.gpa = gpa;
        }
    }

    static class StudentLinkedList {
        private Student head, tail;
        private int size;

        /* ── Operasi dasar ─────────────────────────────── */
        void insertFirst(String nim, String name, double gpa) {
            Student n = new Student(nim, name, gpa);
            n.next = head; head = n;
            if (tail == null) tail = n;
            size++;
        }
        void insertLast(String nim, String name, double gpa) {
            Student n = new Student(nim, name, gpa);
            if (isEmpty()) head = tail = n;
            else { tail.next = n; tail = n; }
            size++;
        }
        void insertAt(int pos, String nim, String name, double gpa) {
            if (pos < 0 || pos > size) { System.out.println("Posisi tidak valid"); return; }
            if (pos == 0) { insertFirst(nim, name, gpa); return; }
            if (pos == size) { insertLast(nim, name, gpa); return; }
            Student prev = head;
            for (int i = 0; i < pos - 1; i++) prev = prev.next;
            Student n = new Student(nim, name, gpa);
            n.next = prev.next; prev.next = n; size++;
        }
        boolean deleteByNim(String nim) {
            if (isEmpty()) return false;
            if (head.nim.equals(nim)) {
                head = head.next; if (head == null) tail = null; size--; return true;
            }
            Student prev = head, cur = head.next;
            while (cur != null && !cur.nim.equals(nim)) { prev = cur; cur = cur.next; }
            if (cur == null) return false;
            prev.next = cur.next; if (cur == tail) tail = prev; size--; return true;
        }
        Student search(String nim) {
            for (Student c = head; c != null; c = c.next) if (c.nim.equals(nim)) return c;
            return null;
        }
        boolean isEmpty() { return head == null; }
        int size() { return size; }

        /* ── Tampilan ──────────────────────────────────── */
        void display() {
            System.out.println("=== Data Mahasiswa ===");
            for (Student c = head; c != null; c = c.next)
                System.out.printf("NIM: %s, Nama: %-18s IPK: %.2f%n", c.nim, c.name, c.gpa);
            System.out.println("Total mahasiswa: " + size);
        }

        /* ── Fitur lanjutan ────────────────────────────── */
        void sortByGPA() {           // bubble sort (descending)
            if (size < 2) return;
            boolean sw;
            do {
                sw = false;
                for (Student c = head; c.next != null; c = c.next) {
                    if (c.gpa < c.next.gpa) {
                        String tN = c.nim; String tNa = c.name; double tG = c.gpa;
                        c.nim = c.next.nim; c.name = c.next.name; c.gpa = c.next.gpa;
                        c.next.nim = tN;    c.next.name = tNa;    c.next.gpa = tG;
                        sw = true;
                    }
                }
            } while (sw);
        }
        void reverse() {
            Student prev = null, cur = head, nxt;
            tail = head;
            while (cur != null) {
                nxt = cur.next; cur.next = prev; prev = cur; cur = nxt;
            }
            head = prev;
        }
        Student findHighestGPA() {
            if (isEmpty()) return null;
            Student max = head;
            for (Student c = head.next; c != null; c = c.next) if (c.gpa > max.gpa) max = c;
            return max;
        }
        List<Student> getStudentsAboveGPA(double thr) {
            List<Student> res = new ArrayList<>();
            for (Student c = head; c != null; c = c.next) if (c.gpa > thr) res.add(c);
            return res;
        }
        static StudentLinkedList mergeSortedList(StudentLinkedList a, StudentLinkedList b) {
            StudentLinkedList m = new StudentLinkedList();
            Student p1 = a.head, p2 = b.head;
            while (p1 != null || p2 != null) {
                if (p2 == null || (p1 != null && p1.gpa >= p2.gpa)) { m.insertLast(p1.nim,p1.name,p1.gpa); p1=p1.next; }
                else { m.insertLast(p2.nim,p2.name,p2.gpa); p2=p2.next; }
            }
            return m;
        }
    }

    /* =====================================================
     *               CIRCULAR LINKED LIST — PLAYLIST
     * ===================================================== */
    static class Song {
        String title, artist; int duration; Song next;
        Song(String t, String a, int d) { title=t; artist=a; duration=d; }
    }
    static class CircularPlaylist {
        private Song head, tail, current; int size;
        void addSong(String t, String a, int d) {
            Song n = new Song(t,a,d);
            if (head == null) { head = tail = current = n; n.next = n; }
            else { tail.next = n; tail = n; tail.next = head; }
            size++;
        }
        boolean removeSong(String title) {
            if (head == null) return false;
            Song prev = tail, cur = head;
            for (int i=0;i<size;i++) {
                if (cur.title.equalsIgnoreCase(title)) {
                    if (size==1) { head=tail=current=null; }
                    else {
                        prev.next = cur.next;
                        if (cur==head) head = cur.next;
                        if (cur==tail) tail = prev;
                        if (cur==current) current = cur.next;
                    }
                    size--; return true;
                }
                prev = cur; cur = cur.next;
            }
            return false;
        }
        void playNext() { if (current!=null) current = current.next; }
        void playPrevious() {
            if (current == null) return;
            Song p = current;
            do { p = p.next; } while (p.next != current);
            current = p;
        }
        Song getCurrentSong() { return current; }
        void shuffle() {
            if (size < 2) return;
            List<Song> list = new ArrayList<>();
            Song c = head; for (int i=0;i<size;i++){ list.add(c); c=c.next; }
            Collections.shuffle(list);
            head = list.get(0);
            for (int i=0;i<size-1;i++) list.get(i).next = list.get(i+1);
            tail = list.get(size-1); tail.next = head; current = head;
        }
        String getTotalDuration() {
            int s=0; Song c=head; for(int i=0;i<size;i++){ s+=c.duration; c=c.next; }
            return String.format("%d:%02d", s/60, s%60);
        }
        void displayPlaylist() {
            if (head==null){ System.out.println("(playlist kosong)"); return; }
            System.out.println("=== Current Playlist ===");
            Song c=head; for(int i=0;i<size;i++){
                String mark = (c==current)? "-> Currently Playing: ":"   "+(i+1)+". ";
                System.out.printf("%s%s - %s (%d:%02d)%n", mark,c.title,c.artist,c.duration/60,c.duration%60);
                c=c.next;
            }
            System.out.println("Total Duration: "+getTotalDuration());
        }
    }

    /* =====================================================
     *          DOUBLY LINKED LIST — TEXT EDITOR
     * ===================================================== */
    static class Action {
        String type, text, prevText; int pos;
        Action next, prev;
        Action(String t,String txt,int p,String prevTxt){ type=t;text=txt;pos=p;prevText=prevTxt; }
    }
    static class TextEditor {
        private StringBuilder buf = new StringBuilder();
        private Action head, tail, cur;

        /* helper: catat aksi & putuskan branch redo */
        private void record(Action a){
            if (cur!=tail){ // buang masa depan
                Action n=(cur==null)?head:cur.next;
                while(n!=null){ Action nx=n.next; n.prev=n.next=null; n=nx; }
                if(cur==null){ head=tail=null; }
                else{ cur.next=null; tail=cur; }
            }
            if(head==null) head=tail=a; else { tail.next=a; a.prev=tail; tail=a; }
            cur=tail;
        }

        /* ── operasi dasar ───────────────────────── */
        void insertText(String txt,int pos){
            if(pos<0||pos>buf.length()) throw new IndexOutOfBoundsException();
            buf.insert(pos,txt);
            record(new Action("INSERT",txt,pos,null));
        }
        void deleteText(int start,int len){
            if(start<0||start+len>buf.length()) throw new IndexOutOfBoundsException();
            String rem = buf.substring(start,start+len);
            buf.delete(start,start+len);
            record(new Action("DELETE",rem,start,null));
        }
        void replaceText(int start,int len,String newTxt){
            if(start<0||start+len>buf.length()) throw new IndexOutOfBoundsException();
            String old = buf.substring(start,start+len);
            buf.replace(start,start+len,newTxt);
            record(new Action("REPLACE",newTxt,start,old));
        }

        /* ── undo / redo ─────────────────────────── */
        void undo(){
            if(cur==null) return;
            switch(cur.type){
                case "INSERT": buf.delete(cur.pos,cur.pos+cur.text.length()); break;
                case "DELETE": buf.insert(cur.pos,cur.text); break;
                case "REPLACE": buf.replace(cur.pos,cur.pos+cur.text.length(),cur.prevText); break;
            }
            cur = cur.prev;
        }
        void redo(){
            if(cur==tail) return;
            Action r = (cur==null)?head:cur.next;
            if(r==null) return;
            switch(r.type){
                case "INSERT": buf.insert(r.pos,r.text); break;
                case "DELETE": buf.delete(r.pos,r.pos+r.text.length()); break;
                case "REPLACE": buf.replace(r.pos,r.pos+r.prevText.length(),r.text); break;
            }
            cur = r;
        }

        /* ── util ────────────────────────────────── */
        String getCurrentText(){ return buf.toString(); }
        String getActionHistory(){
            StringJoiner sj=new StringJoiner(" → ");
            for(Action a=head;a!=null;a=a.next){
                String m=(a==cur)?"*":""; sj.add(m+a.type+m);
            }
            return sj.length()==0?"(kosong)":sj.toString();
        }
    }


    /* =====================================================
     *                       DEMO
     * ===================================================== */
    public static void main(String[] args) {

        /* ---------- 1. Demostrasi StudentLinkedList ---------- */
        System.out.println("===== DEMO StudentLinkedList =====");
        StudentLinkedList sl = new StudentLinkedList();
        sl.insertLast("12345","Andrean Reza",3.95);
        sl.insertLast("12346","Afif Athallah",3.82);
        sl.insertLast("12347","Arief Hidayatullah",3.65);
        sl.display();

        sl.sortByGPA(); System.out.println("\nSetelah sortByGPA():"); sl.display();

        sl.reverse(); System.out.println("\nSetelah reverse():"); sl.display();

        Student best = sl.findHighestGPA();
        System.out.printf("Mahasiswa IPK tertinggi: %s (%.2f)%n%n",best.name,best.gpa);

        /* ---------- 2. Demo CircularPlaylist ---------- */
        System.out.println("===== DEMO CircularPlaylist =====");
        CircularPlaylist pl = new CircularPlaylist();
        pl.addSong("Bohemian Rhapsody","Queen",363);
        pl.addSong("Hotel California","Eagles",391);
        pl.addSong("Imagine","John Lennon",183);
        pl.displayPlaylist();

        pl.playNext(); System.out.println("\nSetelah playNext():"); pl.displayPlaylist();
        pl.shuffle(); System.out.println("\nSetelah shuffle():"); pl.displayPlaylist();
        pl.removeSong("Imagine"); System.out.println("\nSetelah removeSong(\"Imagine\"):");
        pl.displayPlaylist();
        System.out.println();

        /* ---------- 3. Demo TextEditor ---------- */
        System.out.println("===== DEMO TextEditor =====");
        TextEditor ed = new TextEditor();
        ed.insertText("Hello World",0);
        ed.insertText(" Beautiful",5);
        System.out.println("Teks sekarang : "+ed.getCurrentText());
        ed.undo(); System.out.println("Setelah undo : "+ed.getCurrentText());
        ed.redo(); System.out.println("Setelah redo : "+ed.getCurrentText());

        ed.replaceText(6,9,"Amazing");
        ed.deleteText(0,6);
        System.out.println("\nTeks setelah edit   : "+ed.getCurrentText());
        System.out.println("Riwayat aksi        : "+ed.getActionHistory());

        ed.undo(); ed.undo();
        System.out.println("\nSetelah 2× undo     : "+ed.getCurrentText());
        ed.redo();
        System.out.println("Setelah 1× redo     : "+ed.getCurrentText());
    }
}
