class GameNode {
    Game game;
    GameNode next;

    GameNode(Game game) {
        this.game = game;
        this.next = null;
    }
}

class GameLinkedList {
    private GameNode head;
    private int size;

    GameLinkedList() {
        this.head = null;
        this.size = 0;
    }

    public void add(Game game) {
        GameNode newNode = new GameNode(game);
        if (head == null) {
            head = newNode;
        } else {
            GameNode current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
    }

    public void display() {
        GameNode current = head;
        while (current != null) {
            System.out.println(
                    current.game.title + ", Type: " + current.game.type + ", Available: " + current.game.availability);
            current = current.next;
        }
    }

    public int getSize() {
        return size;
    }

    // Method to search a game by title
    public Game searchGameByTitle(String title) {
        GameNode current = head;
        while (current != null) {
            if (current.game.title.equalsIgnoreCase(title)) {
                return current.game; // Return the game if title matches
            }
            current = current.next;
        }
        return null; // Return null if the game is not found
    }

    void getRecent() {
        if (head == null) {
            System.out.println("No Game Added Recently");
        } else {
            GameNode temp = head;
            while (temp.next != null) {
                temp = temp.next;
            }
            System.out.println(
                    temp.game.title + ", Type: " + temp.game.type + ", Available: " + temp.game.availability);
        }
    }
}