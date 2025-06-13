public class TextEditor {
    static class Action {
        String type, text, prevText;
        int position;
        Action prev, next;

        Action(String type, String text, int position, String prevText) {
            this.type = type;
            this.text = text;
            this.position = position;
            this.prevText = prevText;
        }
    }

    private StringBuilder buffer = new StringBuilder();
    private Action head, tail, current;

    public void insertText(String text, int position) {
        buffer.insert(position, text);
        record(new Action("INSERT", text, position, null));
    }

    private void record(Action action) {
        if (current != tail) {
            Action nextNode = (current == null) ? head : current.next;
            while (nextNode != null) {
                Action temp = nextNode.next;
                nextNode.prev = nextNode.next = null;
                nextNode = temp;
            }
            if (current == null) {
                head = tail = null;
            } else {
                current.next = null;
                tail = current;
            }
        }

        if (head == null) {
            head = tail = action;
        } else {
            tail.next = action;
            action.prev = tail;
            tail = action;
        }
        current = tail;
    }

    public void visualizeList() {
        StringBuilder output = new StringBuilder("NULL <-> ");
        for (Action a = head; a != null; a = a.next) {
            output.append("[");
            output.append(a.type);
            if (a == current) output.append("*");
            output.append("] <-> ");
        }
        output.append("NULL");
        System.out.println(output);
    }
}
