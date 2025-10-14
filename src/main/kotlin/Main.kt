//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.io.File
import java.io.IOException
import java.lang.System.exit
import kotlin.system.exitProcess


/********************************
 * A value for folder is defined.
 * A check to see if folder exists is done.
 * A while loop runs displaying the app options
 * The user is prompted to make a choice
 * When a choice is made the corresponding function is called
 * Choice "4" exits the app
 ********************************/
fun noteMenu() {

    val folder = File("notes")
    if (!folder.exists()) folder.mkdir()

    while(true) {
        println("*********Note Taking App Menu*********")
        println("1. Create a new note.")
        println("2. Update a note.")
        println("3. Delete a note.")
        println("4. Exit Note Taking App")
        print("Please enter a choice: ")
        val choice = readln().trim()

        when (choice) {
            "1" -> createNote(folder)
            "2" -> updateNote()
            "3" -> deleteNote()
            "4" -> {
                println("Thanks for going through the app!")
                return
            }
            else -> println("Invalid input. Please try again.")
        }
    }
}

/********************************
 * Checks to see if folder exists.
 * If folder does nto exist it creates the folder.
 * Calls CheckNewFild
 * Prompts user to add a note to named file
 * Reads note content
 * Makes sure folder exists before creating the file
 * Writes the content to the file
 * Confirms note is created
 * Catches any errors that may occur while creating a note
 *********************************/
fun createNote(folder: File): File? {
    if (!folder.exists()) folder.mkdirs()
    val newNote  = checkNewFile(folder)
    print("Add a note to ${newNote.nameWithoutExtension}:  ")
    val noteContent = readln()

    try{
        newNote.parentFile?.mkdirs()
        newNote.writeText(noteContent + "\n")
        println("Note is created: ${newNote.absolutePath}")
        return newNote
    } catch (e: IOException){
        println("An error occurred while creating note: ${e.message}")
        return null
    }
}


/********************************
 * Takes input of fileName from user
 * Calls standardizeFileName
 ********************************/
fun fileNameInput(): String{

    print("Enter a file name: ")
    val input: String = readlnOrNull() ?: ""
    val cleanFileName = standardizeFileName(input)

    return cleanFileName
}

/********************************
 * Function to check new file name
 * Checks to see if folder exists
 * If folder does not exist the folder is created
 * fileNameInput is called and gives a standardized fileName input from user
 * Creates a file path with the cleanFileName
 * Checks to see if cleanFileName is blank. If true prints a warning
 * Checks to see if cleanFileName exists already. If true prints a warning and prompt to try again.
 * If cleanFileName passes checks, prints confirmation of creation.
 * returns created file
 ********************************/
fun checkNewFile(folder: File): File {
    if (!folder.exists()) folder.mkdirs()

    while (true) {
       val cleanFileName = fileNameInput()
        val file = File(folder, "${cleanFileName}.txt")

        if (cleanFileName.isBlank())  {
            println("File name cannot be blank")
        } else if (file.exists()) {
        println("File name already exists. Please try again.")
        } else {
            println("File name is created.")
            return file
        }
    }
}


/********************************
 * Removes white space at beginning and end
 * Removes ".txt" if user inputs it as part of fileName.
 * Ignores case
 * Replaces non-alphanumeric chars with "_"
 * Removes leading or trialing "_"
 ********************************/
fun standardizeFileName(fileName: String): String {
    var fileName = fileName.trim()
    if (fileName.endsWith(".txt", ignoreCase = true)) {
        fileName = fileName.dropLast(4)
    }
        fileName = fileName.replace("[^a-zA-Z0-9]".toRegex(), "_")
        fileName = fileName.lowercase()

    return fileName.trim('_')
}

/********************************
 * Defining the File path directory
 * Displaying the directory in use
 * Listing and showing all files found in the "notes" directory
 * Asking user to input the file to update
 * Finds the file using .find()
 * The try/catch section is wrapped in an if statement to check
 * for a null (if a file isn't found). I learned from Google AI that
 * Kotlin requires that a null
 * possibility is explicitly handled.
 * Inside the try/catch the original content is read and printed
 * The user is prompted to enter the text that will be replaced (updated)
 * The user is then prompted to enter the text that will be the replacement.
 * The replacement text then overwrites the text to be replaced.
 * A confirmation message is displayed
 * The new note is displayed
 * The catch will display an error message if the file name input results in no
 * file found.
 ********************************/
fun updateNote() {
    val fileToUpdate = listNoteFiles()

    if (fileToUpdate != null) {
        try {
            val content = fileToUpdate.readText()
            println("\nOriginal note: $content")

            print("Enter text you would like to replace: ")
            val oldText = readln()

            print("Enter note update: ")
            val newText = readln()

            val updateContent = content.replace(oldText, newText)
            fileToUpdate.writeText(updateContent)
            println("\nNote is updated: ${fileToUpdate.absolutePath}")

            println("\nUpdated note: $updateContent")
        } catch (e: IOException) {
            println("An error occurred while updating note: ${e.message}")
        }
   } else {
        println("File not found.")

   }
}

/********************************
 * From Google AI I learned that a function in Kotlin
 * needs to be file matched. A ('unit' or no type) function cannot
 * return a 'File' type.
 * I tested this function without using 'File'
 * type, it didn't work.
 * This is a helper function that will search for the 'notes' directory.
 * Print a list of files using the note directory.
 * Uses checks to handle the possibility of null returns for file
 * Requests that the user enters a file name to be managed.
 * Finds the users requested file and returns it.
 * This function can be used in the updateNote(), readNote() and DeleteNote() functions.
 ********************************/
fun listNoteFiles():File? {
    val directory = File("notes")
    if (!directory.exists()) {
        println("The 'notes' folder does not exist.")
    }

    println("Using directory: ${directory.absolutePath}")
    val files = directory.listFiles { _, name -> name.endsWith(".txt") }

    if (files.isNullOrEmpty()) {
        println("There are no files in the 'notes' folder.")
        noteMenu()
    } else {
        files.forEach { println(it.name) }

        print("Enter file to be manage: ")
        val input = readln()
        return files.find { it.name == input }
    }
    return null
}

/********************************
 * Calls listNoteFiles and sets the resulting
 * file equal to 'fileToDelete'
 * Checks to see if 'fileToDelete' is not null
 * Deletes the file
 * Confirms file deletion
 * Prints a warning if the 'fileToDelete' does not exist.
 ********************************/
fun deleteNote() {
    val fileToDelete = listNoteFiles()
    if (fileToDelete != null) {
        fileToDelete.delete()

        println("$fileToDelete was successfully deleted.")
    } else {
        println("$fileToDelete does not exist.")
    }
}

fun main(args: Array<String>) {
noteMenu()
}
