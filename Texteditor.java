import java.util.StringJoiner;

public class Texteditor {

    static class Action {
        String type;        // "INSERT", "DELETE", "REPLACE"
        String text;        // teks yang ditambahkan / pengganti
        int    position;    // posisi aksi
        String previousText;// teks sebelum aksi (untuk UNDO)
        Action next, prev;  // pointer 2‑arah

        Action(String type, String text, int position, String previousText) {
            this.type = type;
            this.text = text;
            this.position = position;
            this.previousText = previousText;
        }
    }

    static class TextEditor {
        private StringBuilder buffer = new StringBuilder();
        private Action head, tail;   // riwayat aksi (doubly list)
        private Action current;      // posisi pointer UNDO/REDO

        /* ------------ 1. INSERT ------------ */
        public void insertText(String text, int position) {
            if (position < 0 || position > buffer.length())
                throw new IndexOutOfBoundsException("Posisi di luar range");
            buffer.insert(position, text);
            recordAction(new Action("INSERT", text, position, null));
        }

        /* ------------ 2. DELETE ------------ */
        public void deleteText(int start, int length) {
            if (start < 0 || start + length > buffer.length())
                throw new IndexOutOfBoundsException("Range hapus salah");
            String removed = buffer.substring(start, start + length);
            buffer.delete(start, start + length);
            recordAction(new Action("DELETE", removed, start, null));
        }

        /* ------------ 3. REPLACE ----------- */
        public void replaceText(int start, int length, String newText) {
            if (start < 0 || start + length > buffer.length())
                throw new IndexOutOfBoundsException("Range replace salah");
            String old = buffer.substring(start, start + length);
            buffer.replace(start, start + length, newText);
            recordAction(new Action("REPLACE", newText, start, old));
        }

        /* ------------ 4. UNDO -------------- */
        public void undo() {
            if (current == null) return; // tak ada yang di‑undo

            switch (current.type) {
                case "INSERT":
                    buffer.delete(current.position,
                                  current.position + current.text.length());
                    break;
                case "DELETE":
                    buffer.insert(current.position, current.text);
                    break;
                case "REPLACE":
                    buffer.replace(current.position,
                                   current.position + current.text.length(),
                                   current.previousText);
                    break;
            }
            current = current.prev; // mundur satu langkah
        }

        /* ------------ 5. REDO -------------- */
        public void redo() {
            if (current == tail) return; // tak ada yang perlu di‑redo
            Action redoAct = (current == null) ? head : current.next;
            if (redoAct == null) return;

            switch (redoAct.type) {
                case "INSERT":
                    buffer.insert(redoAct.position, redoAct.text);
                    break;
                case "DELETE":
                    buffer.delete(redoAct.position,
                                  redoAct.position + redoAct.text.length());
                    break;
                case "REPLACE":
                    buffer.replace(redoAct.position,
                                   redoAct.position + redoAct.previousText.length(),
                                   redoAct.text);
                    break;
            }
            current = redoAct; // maju satu langkah
        }

        /* ------------ 6. CURRENT TEXT ------ */
        public String getCurrentText() {
            return buffer.toString();
        }

        /* ------------ 7. HISTORY ----------- */
        public String getActionHistory() {
            StringJoiner sj = new StringJoiner(" → ");
            Action temp = head;
            while (temp != null) {
                String marker = (temp == current) ? "*" : "";
                sj.add(marker + temp.type + marker);
                temp = temp.next;
            }
            return sj.length() == 0 ? "(kosong)" : sj.toString();
        }

        /* ---- helper: catat aksi & potong redo branch ---- */
        private void recordAction(Action act) {
            // jika sedang di tengah (setelah UNDO), buang "masa depan"
            if (current != tail) {
                Action node = (current == null) ? head : current.next;
                while (node != null) {
                    Action next = node.next;
                    node.prev = node.next = null; // help GC
                    node = next;
                }
                if (current == null) head = tail = null;
                else {
                    current.next = null;
                    tail = current;
                }
            }
            // tambahkan di akhir
            if (head == null) head = tail = act;
            else {
                tail.next = act;
                act.prev  = tail;
                tail      = act;
            }
            current = tail; // pointer di akhir setelah aksi baru
        }
    }

    public static void main(String[] args) {
        TextEditor editor = new TextEditor();

        editor.insertText("Hello World", 0);
        editor.insertText("Beautiful ", 6);
        System.out.println(editor.getCurrentText()); // Hello Beautiful World

        editor.undo();
        System.out.println(editor.getCurrentText()); // Hello World

        editor.redo();
        System.out.println(editor.getCurrentText()); // Hello Beautiful World

        // Contoh ekstra
        editor.replaceText(6, 9, "Amazing ");
        editor.deleteText(0, 6);
        System.out.println("\nTeks sekarang : " + editor.getCurrentText());
        System.out.println("Riwayat aksi  : " + editor.getActionHistory());

        // Undo 2x
        editor.undo();
        editor.undo();
        System.out.println("\nSetelah 2× undo: " + editor.getCurrentText());
        System.out.println("Riwayat aksi    : " + editor.getActionHistory());

        // Redo 1x
        editor.redo();
        System.out.println("\nSetelah 1× redo: " + editor.getCurrentText());
        System.out.println("Riwayat aksi    : " + editor.getActionHistory());
    }
}
