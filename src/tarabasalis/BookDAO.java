package tarabasalis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    private static final String FILE_PATH = "books.csv";

    public static List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return books;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length >= 5) {
                    Book book = new Book(values[0], values[1], values[2], values[3], values[4]);
                    if (values.length > 5 && values[5] != null) {
                        book.setBorrowedBy(values[5]);
                    }
                    books.add(book);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return books;
    }

    public static void addBook(Book book) {
        List<Book> books = getAllBooks();
        books.add(book);
        saveBooks(books);
    }

    public static void borrowBook(String title, String borrower) {
        List<Book> books = getAllBooks();
        for (Book book : books) {
            if (book.getTitle().equals(title)) {
                book.setBorrowedBy(borrower);
                break;
            }
        }
        saveBooks(books);
    }

    private static void saveBooks(List<Book> books) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Book book : books) {
                String borrowedBy = book.getBorrowedBy() != null ? book.getBorrowedBy() : "";
                String line = String.join(",", book.getTitle(), book.getAuthor(), book.getGenre(), book.getIsbn(), book.getType(), borrowedBy);
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
