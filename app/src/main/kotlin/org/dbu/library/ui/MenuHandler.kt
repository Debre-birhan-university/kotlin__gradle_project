package org.dbu.library.ui

import org.dbu.library.model.Book
import org.dbu.library.model.Patron
import org.dbu.library.repository.LibraryRepository
import org.dbu.library.service.BorrowResult
import org.dbu.library.service.LibraryService

// ----------------- Menu Display -----------------
fun showMenu(): String {
    println(
        """
        |================ LIBRARY MENU =================
        | 1 | Add Book
        | 2 | Register Patron
        | 3 | Borrow Book
        | 4 | Return Book
        | 5 | Search
        | 6 | List All Books
        | 0 | Exit
        |==============================================
        |Enter option:
        """.trimMargin()
    )
    return readLine()?.trim() ?: ""
}

// ----------------- Menu Handling -----------------
fun handleMenuAction(
    choice: String,
    service: LibraryService,
    repository: LibraryRepository
): Boolean {
    return when (choice) {
        "1" -> { addBook(service); true }
        "2" -> { registerPatron(repository); true }
        "3" -> { borrowBook(service); true }
        "4" -> { returnBook(service); true }
        "5" -> { search(service); true }
        "6" -> { listAllBooks(repository); true }
        "0" -> false
        else -> { println("Invalid option"); true }
    }
}

// ----------------- Menu Functions -----------------
fun addBook(service: LibraryService) {
    print("ISBN: "); val isbn = readLine()!!
    print("Title: "); val title = readLine()!!
    print("Author: "); val author = readLine()!!
    print("Year: "); val year = readLine()!!.toInt()

    val success = service.addBook(Book(isbn, title, author, year))
    if (success) println("Book added successfully!")
    else println("Book already exists.")
}

fun registerPatron(repository: LibraryRepository) {
    print("Patron ID: "); val id = readLine()!!
    print("Name: "); val name = readLine()!!

    val success = repository.addPatron(Patron(id, name))
    if (success) println("Patron registered successfully!")
    else println("Patron already exists.")
}

fun borrowBook(service: LibraryService) {
    print("Patron ID: "); val pid = readLine()!!
    print("Book ISBN: "); val isbn = readLine()!!

    when (service.borrowBook(pid, isbn)) {
        BorrowResult.SUCCESS -> println("Book borrowed successfully!")
        BorrowResult.BOOK_NOT_FOUND -> println("Book not found!")
        BorrowResult.PATRON_NOT_FOUND -> println("Patron not found!")
        BorrowResult.NOT_AVAILABLE -> println("Book not available!")
        BorrowResult.LIMIT_REACHED -> println("Patron reached borrow limit!")
    }
}

fun returnBook(service: LibraryService) {
    print("Patron ID: "); val pid = readLine()!!
    print("Book ISBN: "); val isbn = readLine()!!

    if (service.returnBook(pid, isbn)) println("Book returned successfully!")
    else println("Failed to return book.")
}

fun search(service: LibraryService) {
    print("Search query: "); val query = readLine()!!
    val results = service.search(query)
    if (results.isEmpty()) println("No books found.")
    else results.forEach { println("${it.isbn} | ${it.title} | ${it.author} | ${it.year}") }
}

fun listAllBooks(repository: LibraryRepository) {
    val books = repository.getAllBooks()
    if (books.isEmpty()) println("No books in the library.")
    else books.forEach {
        println("${it.isbn} | ${it.title} | ${it.author} | ${it.year} | Available: ${it.isAvailable}")
    }
}