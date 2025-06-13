import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CircularPlayList {

    static class Song {
        String title;
        String artist;
        int duration;   // detik
        Song next;

        Song(String title, String artist, int duration) {
            this.title    = title;
            this.artist   = artist;
            this.duration = duration;
            this.next     = null;
        }
    }

    static class CircularPlaylist {
        private Song head;      // lagu pertama
        private Song tail;      // lagu terakhir (next‑nya menunjuk head)
        private Song current;   // lagu yang sedang diputar
        private int  size;

        /* 1. Tambah lagu di akhir playlist */
        public void addSong(String title, String artist, int duration) {
            Song n = new Song(title, artist, duration);
            if (head == null) {                 // playlist kosong
                head = tail = current = n;
                n.next = n;                     // menunjuk dirinya sendiri
            } else {
                tail.next = n;
                tail      = n;
                tail.next = head;               // circular
            }
            size++;
        }

        /* 2. Hapus lagu berdasarkan judul */
        public boolean removeSong(String title) {
            if (head == null) return false;
            Song prev = tail;
            Song cur  = head;
            for (int i = 0; i < size; i++) {
                if (cur.title.equalsIgnoreCase(title)) {
                    if (size == 1) {            // hanya satu lagu
                        head = tail = current = null;
                    } else {
                        prev.next = cur.next;
                        if (cur == head)  head  = cur.next;
                        if (cur == tail)  tail  = prev;
                        if (cur == current) current = cur.next;
                    }
                    size--;
                    return true;
                }
                prev = cur;
                cur  = cur.next;
            }
            return false;                       // tidak ditemukan
        }

        /* 3. Putar lagu berikutnya (circular) */
        public void playNext() {
            if (current != null) current = current.next;
        }

        /* 4. Putar lagu sebelumnya (circular) */
        public void playPrevious() {
            if (current == null) return;
            Song prev = current;
            // cari node sebelum current
            do {
                prev = prev.next;
            } while (prev.next != current);     // berhenti saat prev adalah node sebelum current
            current = prev;
        }

        /* 5. Lagu yang sedang diputar */
        public Song getCurrentSong() {
            return current;
        }

        /* 6. Acak urutan playlist */
        public void shuffle() {
            if (size < 2) return;

            // Simpan semua node ke list
            List<Song> list = new ArrayList<>();
            Song cur = head;
            for (int i = 0; i < size; i++) {
                list.add(cur);
                cur = cur.next;
            }

            Collections.shuffle(list);

            // Bangun ulang circular links
            head = list.get(0);
            for (int i = 0; i < size - 1; i++) {
                list.get(i).next = list.get(i + 1);
            }
            tail = list.get(size - 1);
            tail.next = head;

            current = head;   // mulai lagi dari head (opsional)
        }

        /* 7. Total durasi dalam mm:ss */
        public String getTotalDuration() {
            int totalSec = 0;
            Song cur = head;
            for (int i = 0; i < size; i++) {
                totalSec += cur.duration;
                cur = cur.next;
            }
            int mm = totalSec / 60;
            int ss = totalSec % 60;
            return String.format("%d:%02d", mm, ss);
        }

        /* 8. Tampilkan playlist */
        public void displayPlaylist() {
            if (head == null) {
                System.out.println("(playlist kosong)");
                return;
            }
            System.out.println("=== Current Playlist ===");
            Song cur = head;
            for (int i = 0; i < size; i++) {
                String marker = (cur == current) ? "-> Currently Playing: " : "   " + (i + 1) + ". ";
                System.out.printf("%s%s - %s (%s)%n",
                        marker, cur.title, cur.artist, toMMSS(cur.duration));
                cur = cur.next;
            }
            System.out.println("Total Duration: " + getTotalDuration());
        }

        /* -------- util: ubah detik → mm:ss -------- */
        private String toMMSS(int sec) {
            return String.format("%d:%02d", sec / 60, sec % 60);
        }
    }

    public static void main(String[] args) {
        CircularPlaylist pl = new CircularPlaylist();

        pl.addSong("Bohemian Rhapsody", "Queen",        6 * 60 + 3);
        pl.addSong("Hotel California",  "Eagles",       6 * 60 + 31);
        pl.addSong("Stairway to Heaven","Led Zeppelin", 8 * 60 + 2);
        pl.addSong("Imagine",           "John Lennon",  3 * 60 + 3);

        // tampilkan playlist awal
        pl.displayPlaylist();

        // pindah ke next dan previous
        pl.playNext();
        System.out.println("\nSetelah playNext():");
        pl.displayPlaylist();

        pl.playPrevious();
        System.out.println("\nSetelah playPrevious():");
        pl.displayPlaylist();

        // shuffle
        pl.shuffle();
        System.out.println("\nSetelah shuffle():");
        pl.displayPlaylist();

        // hapus lagu
        pl.removeSong("Hotel California");
        System.out.println("\nSetelah removeSong(\"Hotel California\"):");
        pl.displayPlaylist();
    }
}
