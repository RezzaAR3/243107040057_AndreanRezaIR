/* =========================================================
 *  Main.java —  StudentLinkedList, CircularPlaylist,
 *               dan TextEditor + visualisasi ASCII
 * ========================================================= */
public class Main {

    /* ───────────────────────────────────────────────────────
     *  1)  SINGLE LINKED LIST — MAHASISWA
     * ─────────────────────────────────────────────────────── */
    static class Student {
        String nim, name; double gpa; Student next;
        Student(String n,String nm,double g){ nim=n; name=nm; gpa=g; }
    }
    static class StudentLinkedList {
        private Student head, tail; private int size;

        /* operasi dasar */
        void insertLast(String nim,String name,double gpa){
            Student n=new Student(nim,name,gpa);
            if(head==null){ head=tail=n; } else { tail.next=n; tail=n; }
            size++;
        }
        boolean isEmpty(){ return head==null; }

        /* tampilkan baris data ringkas */
        void display(){
            System.out.println("=== Data Mahasiswa ===");
            for(Student c=head;c!=null;c=c.next)
                System.out.printf("NIM: %s  %-18s IPK: %.2f%n",c.nim,c.name,c.gpa);
            System.out.println("Total: "+size);
        }

        /* ── visualisasi ASCII ── */
        void visualizeList(){
            if(isEmpty()){ System.out.println("[EMPTY LIST]"); return; }

            StringBuilder nodes=new StringBuilder();
            StringBuilder nimLine=new StringBuilder("     ");
            StringBuilder gpaLine=new StringBuilder("     ");

            Student cur=head;
            while(cur!=null){
                String box=String.format("[%-10s]",cur.name.length()>10?cur.name.substring(0,10):cur.name);
                nodes.append(box).append(" -> ");
                nimLine.append(String.format("NIM: %-6s     ",cur.nim));
                gpaLine.append(String.format("IPK: %-5.2f    ",cur.gpa));
                cur=cur.next;
            }
            nodes.append("NULL");
            System.out.println(nodes);
            System.out.println(nimLine);
            System.out.println(gpaLine);
        }
    }

    /* ───────────────────────────────────────────────────────
     *  2)  CIRCULAR LINKED LIST — PLAYLIST
     * ─────────────────────────────────────────────────────── */
    static class Song {
        String title,artist; int dur; Song next;
        Song(String t,String a,int d){ title=t; artist=a; dur=d; }
    }
    static class CircularPlaylist {
        private Song head,tail,current; int size;

        void addSong(String t,String a,int d){
            Song n=new Song(t,a,d);
            if(head==null){ head=tail=current=n; n.next=n; }
            else { tail.next=n; tail=n; tail.next=head; }
            size++;
        }

        /* visualisasi: node -> … -> (HEAD) */
        void visualizeList(){
            if(head==null){ System.out.println("[EMPTY PLAYLIST]"); return; }

            StringBuilder nodes=new StringBuilder();
            StringBuilder info=new StringBuilder("    ");

            Song c=head;
            for(int i=0;i<size;i++){
                String nameBox=String.format("[%s]", c.title.length()>12?c.title.substring(0,12):c.title);
                if(c==current) nameBox="*"+nameBox+"*";
                nodes.append(nameBox);
                nodes.append(i==size-1? " -> (HEAD)":" -> ");
                info.append(String.format("(%d:%02d)  ",c.dur/60,c.dur%60));
                c=c.next;
            }
            System.out.println(nodes);
            System.out.println(info);
        }
    }

    /* ───────────────────────────────────────────────────────
     *  3)  DOUBLY LINKED LIST — TEXT EDITOR
     * ─────────────────────────────────────────────────────── */
    static class Action {
        String type,text,prevT; int pos; Action prev,next;
        Action(String t,String txt,int p,String pr){ type=t;text=txt;pos=p;prevT=pr; }
    }
    static class TextEditor {
        private StringBuilder buf=new StringBuilder();
        private Action head,tail,cur;

        /* helper: catat aksi & putus cabang redo */
        private void record(Action a){
            if(cur!=tail){
                Action n=(cur==null)?head:cur.next;
                while(n!=null){ Action nx=n.next; n.prev=n.next=null; n=nx; }
                if(cur==null){ head=tail=null; } else { cur.next=null; tail=cur; }
            }
            if(head==null) head=tail=a; else { tail.next=a; a.prev=tail; tail=a; }
            cur=tail;
        }

        /* operasi insert sederhana utk demo */
        void insertText(String txt,int p){ buf.insert(p,txt); record(new Action("INSERT",txt,p,null)); }

        /* visualisasi riwayat */
        void visualizeList(){
            StringBuilder line=new StringBuilder("NULL <-> ");
            for(Action a=head;a!=null;a=a.next){
                line.append("[");
                line.append(a.type);
                if(a==cur) line.append("*");
                line.append("] <-> ");
            }
            line.append("NULL");
            System.out.println(line);
        }
    }

    /* ───────────────────────────────────────────────────────
     *                        DEMO
     * ─────────────────────────────────────────────────────── */
    public static void main(String[] args) {

        System.out.println("=== Visual StudentLinkedList ===");
        StudentLinkedList sl=new StudentLinkedList();
        sl.insertLast("001","Andrean",3.95);
        sl.insertLast("002","Afif",3.82);
        sl.insertLast("003","Arief",3.65);
        sl.visualizeList();

        System.out.println("\n=== Visual CircularPlaylist ===");
        CircularPlaylist pl=new CircularPlaylist();
        pl.addSong("Bohemian Rhapsody","Queen",363);
        pl.addSong("Hotel California","Eagles",391);
        pl.addSong("Imagine","John Lennon",183);
        pl.visualizeList();          // current = head (default)

        System.out.println("\nPutar lagu berikutnya, lalu visualisasi:");
        pl.current=pl.current.next;  // playNext()
        pl.visualizeList();

        System.out.println("\n=== Visual TextEditor History ===");
        TextEditor ed=new TextEditor();
        ed.insertText("Hello",0);
        ed.insertText(" World",5);
        ed.visualizeList();
    }
}
