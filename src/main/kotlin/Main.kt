import controllers.NoteAPI
import models.Note
import mu.KotlinLogging
import persistence.XMLSerializer
import utils.ScannerInput
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import java.io.File
import java.lang.System.exit


private val logger = KotlinLogging.logger {}
private val noteAPI = NoteAPI(XMLSerializer(File("notes.xml")))


fun main(args: Array<String>) {

    runMenu()

}

fun mainMenu() : Int {
    return ScannerInput.readNextInt(""" 
         > ----------------------------------
         > |        NOTE KEEPER APP         |
         > ----------------------------------
         > | NOTE MENU                      |
         > |   1) Add a note                |
         > |   2) List all notes            |
         > |   3) Update a note             |
         > |   4) Delete a note             |
         > |   20) Save notes               |
         > |   21) Load notes               |
         > ----------------------------------
         > |   0) Exit                      |
         > ----------------------------------
         > ==>> """.trimMargin(">"))

}


fun runMenu() {
    do {
        val option = mainMenu()
        when (option) {
            1 -> addNote()
            2 -> listNotes()
            3 -> updateNote()
            4 -> deleteNote()
            20 -> save()
            21 -> load()
            0 -> exitApp()
            else -> println("Invalid option entered: $option")
        }
    } while (true)
}


    //logger.info { "addNote() function invoked" }
    fun addNote(){
        //logger.info { "addNote() function invoked" }
        val noteTitle = readNextLine("Enter a title for the note: ")
        val notePriority = readNextInt("Enter a priority (1-low, 2, 3, 4, 5-high): ")
        val noteCategory = readNextLine("Enter a category for the note: ")
        val isAdded = noteAPI.add(Note(noteTitle, notePriority, noteCategory, false))

        if (isAdded) {
            println("Added Successfully")
        } else {
            println("Add Failed")
        }
    }



fun listNotes() {
    //logger.info { "listNotes() function invoked " }
    println(noteAPI.listAllNotes())
}

fun updateNote() {
    //logger.info {  " UpdateNote() function invoked"}
    listNotes()
    if (noteAPI.numberOfNotes() > 0) {
        //only ask the user to choose the note if notes exist
        val indexToUpdate = readNextInt("Enter the index of the note to update: ")
        if (noteAPI.isValidIndex(indexToUpdate)) {
            val noteTitle = readNextLine("Enter a title for the note: ")
            val notePriority = readNextInt("Enter a priority (1-low, 2, 3, 4, 5-high): ")
            val noteCategory = readNextLine("Enter a category for the note: ")

            //pass the index of the note and the new note details to NoteAPI for updating and check for success.
            if (noteAPI.updateNote(indexToUpdate, Note(noteTitle, notePriority, noteCategory, false))){
                println("Update Successful")
            } else {
                println("Update Failed")
            }
        } else {
            println("There are no notes for this index number")
        }
    }
}



fun deleteNote() {

   // logger.info {  " DeleteNote function invoked"}
    listNotes()
    if (noteAPI.numberOfNotes() >0){
        val indexToDelete = readNextInt("Enter the index of the note to delete: ")
        val noteToDelete = noteAPI.deleteNote(indexToDelete)
        if (noteToDelete != null) {
            println("Delete Successful! Delete note: ${noteToDelete.noteTitle}")
        }else{
            println("Delete Not Successful")
        }
    }
}

fun save() {
    try {
        noteAPI.store()
    } catch (e: Exception) {
        System.err.println("Error writing to file: $e")
    }
}

fun load() {
    try {
        noteAPI.load()
    } catch (e: Exception) {
        System.err.println("Error reading from file: $e")
    }
}

fun exitApp() {
    logger.info {  "ExitApp() f     §unction invoked"}
    exit(0)
}
