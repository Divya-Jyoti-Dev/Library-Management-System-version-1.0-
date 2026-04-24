import java.io.*;
import java.util.*;

// ----------- Book Class -----------
class Book implements Serializable {
    private String title;
    private String author;
    private String isbn;
    private int totalCopies;
    private int availableCopies;

    public Book(String title, String author, String isbn, int copies) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.totalCopies = copies;
        this.availableCopies = copies;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public boolean issueBook() {
        if (availableCopies > 0) {
            availableCopies--;
            return true;
        }
        return false;
    }

    public void returnBook() {
        if (availableCopies < totalCopies)
            availableCopies++;
    }

    @Override
    public String toString() {
        return "Title: " + title + ", Author: " + author +
                ", ISBN: " + isbn + ", Available: " + availableCopies + "/" + totalCopies;
    }
}

// ----------- Library Class -----------
class Library {
    private HashMap<String, Book> books; // ISBN -> Book

    public Library() {
        books = new HashMap<>();
    }

    public void addBook(Book book) {
        books.put(book.getIsbn(), book);
        System.out.println("Book added successfully!");
    }

    public void issueBook(String isbn) {
        Book book = books.get(isbn);
        if (book != null) {
            if (book.issueBook()) {
                System.out.println("Book issued successfully!");
            } else {
                System.out.println("No copies available to issue.");
            }
        } else {
            System.out.println("Book not found.");
        }
    }

    public void returnBook(String isbn) {
        Book book = books.get(isbn);
        if (book != null) {
            book.returnBook();
            System.out.println("Book returned successfully!");
        } else {
            System.out.println("Book not found.");
        }
    }

    public void searchByTitle(String title) {
        boolean found = false;
        for (Book b : books.values()) {
            if (b.getTitle().toLowerCase().contains(title.toLowerCase())) {
                System.out.println(b);
                found = true;
            }
        }
        if (!found)
            System.out.println("No books found with that title.");
    }

    public void searchByAuthor(String author) {
        boolean found = false;
        for (Book b : books.values()) {
            if (b.getAuthor().toLowerCase().contains(author.toLowerCase())) {
                System.out.println(b);
                found = true;
            }
        }
        if (!found)
            System.out.println("No books found for that author.");
    }

    public void displayAllBooks() {
        if (books.isEmpty()) {
            System.out.println("Library is empty.");
        } else {
            for (Book b : books.values()) {
                System.out.println(b);
            }
        }
    }

    // Save library data to file
    public void saveToFile(String filename) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(books);
            System.out.println("Library saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving library: " + e.getMessage());
        }
    }

    // Load library data from file
    public void loadFromFile(String filename) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            books = (HashMap<String, Book>) in.readObject();
            System.out.println("Library loaded successfully!");
        } catch (FileNotFoundException e) {
            System.out.println("No saved library found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading library: " + e.getMessage());
        }
    }
}

// ----------- Main Class -----------
public class LibraryManagementSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Library library = new Library();
        String filename = "library.dat";

        // Load existing library if available
        library.loadFromFile(filename);

        boolean running = true;
        while (running) {
            System.out.println("\n--- Library Management System ---");
            System.out.println("1) Add Book");
            System.out.println("2) Issue Book");
            System.out.println("3) Return Book");
            System.out.println("4) Search by Title");
            System.out.println("5) Search by Author");
            System.out.println("6) Display All Books");
            System.out.println("7) Save and Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter Title: ");
                    String title = sc.nextLine();
                    System.out.print("Enter Author: ");
                    String author = sc.nextLine();
                    System.out.print("Enter ISBN: ");
                    String isbn = sc.nextLine();
                    System.out.print("Enter Number of Copies: ");
                    int copies = sc.nextInt();
                    sc.nextLine();
                    Book book = new Book(title, author, isbn, copies);
                    library.addBook(book);
                    break;
                case 2:
                    System.out.print("Enter ISBN to issue: ");
                    String issueIsbn = sc.nextLine();
                    library.issueBook(issueIsbn);
                    break;
                case 3:
                    System.out.print("Enter ISBN to return: ");
                    String returnIsbn = sc.nextLine();
                    library.returnBook(returnIsbn);
                    break;
                case 4:
                    System.out.print("Enter title to search: ");
                    String searchTitle = sc.nextLine();
                    library.searchByTitle(searchTitle);
                    break;
                case 5:
                    System.out.print("Enter author to search: ");
                    String searchAuthor = sc.nextLine();
                    library.searchByAuthor(searchAuthor);
                    break;
                case 6:
                    library.displayAllBooks();
                    break;
                case 7:
                    library.saveToFile(filename);
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }

        sc.close();
        System.out.println("Goodbye!");
    }
}
