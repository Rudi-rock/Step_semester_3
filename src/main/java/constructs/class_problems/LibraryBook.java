package constructs.class_problems;

public class BookCataloguing {
    String title;
    String isbn;

    public BookCataloguing(String title, String isbn) {
        this.title = title;
        this.isbn = (isbn == null || isbn.trim().isEmpty()) ? "PENDING" : isbn;
    }

    public BookCataloguing(String title) {
        this(title, "PENDING");
    }

    void display() {
        System.out.println(title + " | " + isbn + " | Catalogued: true");
    }

    public static void main(String[] args) {
        String[] titles = {"Clean Code", "Untitled Draft", "1984", "Notes"};
        String[] isbns = {"978-0132350884", "", "9780451524935", ""};

        for (int i = 0; i < titles.length; i++) {
            BookCataloguing book = (isbns[i] == null || isbns[i].isEmpty())
                    ? new BookCataloguing(titles[i])
                    : new BookCataloguing(titles[i], isbns[i]);
            book.display();
        }
    }
}
