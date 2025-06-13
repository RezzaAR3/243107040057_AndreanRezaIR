public class CircularPlayList {
    static class Song {
        String title, artist;
        int duration;
        Song next;

        Song(String title, String artist, int duration) {
            this.title = title;
            this.artist = artist;
            this.duration = duration;
        }
    }

    private Song head, tail, current;
    private int size;

    public void addSong(String title, String artist, int duration) {
        Song newSong = new Song(title, artist, duration);
        if (head == null) {
            head = tail = current = newSong;
            newSong.next = newSong;
        } else {
            tail.next = newSong;
            tail = newSong;
            tail.next = head;
        }
        size++;
    }

    public void nextSong() {
        if (current != null) {
            current = current.next;
        }
    }

    public void visualizeList() {
        if (head == null) {
            System.out.println("[EMPTY PLAYLIST]");
            return;
        }

        StringBuilder nodes = new StringBuilder();
        StringBuilder info = new StringBuilder("    ");

        Song temp = head;
        for (int i = 0; i < size; i++) {
            String box = String.format("[%s]", temp.title.length() > 12 ? temp.title.substring(0, 12) : temp.title);
            if (temp == current) box = "*" + box + "*";
            nodes.append(box);
            nodes.append(i == size - 1 ? " -> (HEAD)" : " -> ");
            info.append(String.format("(%d:%02d)  ", temp.duration / 60, temp.duration % 60));
            temp = temp.next;
        }

        System.out.println(nodes);
        System.out.println(info);
    }
}
