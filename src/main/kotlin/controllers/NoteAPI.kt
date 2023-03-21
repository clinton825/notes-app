package controllers
import models.Note
import persistence.Serializer
import persistence.XMLSerializer
import kotlin.jvm.Throws


    class NoteAPI(serializerType: Serializer) {

        private var serializer: Serializer = serializerType

        private var notes = ArrayList<Note>()
        private fun formatListString(notesToFormat : List<Note>) : String =
            notesToFormat
                .joinToString (separator = "\n") { note ->
                    notes.indexOf(note).toString() + ": " + note.toString() }


        fun add(note: Note): Boolean {

            return notes.add(note)
        }

        fun listAllNotes(): String =
            if  (notes.isEmpty()) "No notes stored"
            else notes.joinToString (separator = "\n") { note ->
                notes.indexOf(note).toString() + ": " + note.toString() }


        fun listNotesBySelectedPriority(priority: Int): String {
            return if (notes.isEmpty()) {
                "No notes stored"
            } else {
                var listOfNotes = ""
                for (i in notes.indices) {
                    if (notes[i].notePriority == priority) {
                        listOfNotes +=
                            """$i: ${notes[i]}
                        """.trimIndent()
                    }
                }
                if (listOfNotes.equals("")) {
                    "No notes with priority: $priority"
                } else {
                    "${numberOfNotesByPriority(priority)} notes with priority $priority: $listOfNotes"
                }
            }
        }

        fun numberOfNotesByPriority(priority: Int): Int {
            var counter = 0
            for (note in notes) {
                if (note.notePriority == priority) {
                    counter++
                }
            }
            return counter
        }

        fun listActiveNotes(): String {
            return if (numberOfActiveNotes() == 0) {
                "No active notes stored"
            } else {
                var listOfActiveNottes = ""
                for (note in notes) {
                    if (!note.isNoteArchived) {
                        listOfActiveNottes += "${notes.indexOf(note)}: $note \n"
                    }

                }
                listOfActiveNottes
            }
        }

        fun listArchiveNotes(): String {
            return if (numberOfArchiveNote()== 0){
                "No archived notes stored"
            }else {
                var listOfArchiveNotes = ""
                for (note in notes){
                    if (note.isNoteArchived) {
                        listOfArchiveNotes += "${notes.indexOf(note)}: $note \n"
                    }
                }
                listOfArchiveNotes
            }
        }
        fun numberOfNotes(): Int {
            return notes.size
        }

        fun findNote(index: Int): Note? {
            return if (isValidListIndex(index, notes)) {
                notes[index]
            } else null
        }

        //utility method to determine if an index is valid in a list.
        fun isValidListIndex(index: Int, list: List<Any>): Boolean {
            return (index >= 0 && index < list.size)
        }

        fun isValidIndex(index: Int): Boolean {
            return isValidListIndex(index, notes);
        }

        fun deleteNote(indexToDelete: Int): Note? {
            return if (isValidListIndex(indexToDelete, notes)) {
                notes.removeAt(indexToDelete)
            } else null

        }

        fun updateNote(indexToUpdate: Int, note: Note?): Boolean {
            //find the note object by the index number
            val foundNote = findNote(indexToUpdate)

            //if the note exists, use the note details passed as parameters to update the found note in the ArrayList.
            if ((foundNote != null) && (note != null)) {
                foundNote.noteTitle = note.noteTitle
                foundNote.notePriority = note.notePriority
                foundNote.noteCategory = note.noteCategory
                return true
            }

            //if the note was not found, return false, indicating that the update was not successful
            return false
        }

        @Throws(Exception::class)
        fun load() {
            notes = serializer.read() as ArrayList<Note>
        }

        @Throws(Exception::class)
        fun store() {
            serializer.write(notes)
        }
        fun archiveNote(indexToArchive: Int): Boolean {
            if (isValidIndex(indexToArchive)) {
                val noteToArchive = notes[indexToArchive]
                if (!noteToArchive.isNoteArchived) {
                    noteToArchive.isNoteArchived = true
                    return true
                }
            }
            return false
        }



        fun numberOfActiveNotes(): Int{
            var counter = 0
            for (note in notes){
                if (!note.isNoteArchived){
                    counter++
                }
            }
            return counter
        }

        fun numberOfArchiveNote(): Int  {
        var counter = 0
            for (note in notes ){
                if (note.isNoteArchived) {
                    counter++
                }
            }
            return counter
        }
        fun searchByTitle (searchString : String) =
            formatListString(
                notes.filter { note -> note.noteTitle.contains(searchString, ignoreCase = true) })

    }

