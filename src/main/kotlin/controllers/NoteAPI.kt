package controllers
import models.Note
import persistence.Serializer


    class NoteAPI(serializerType: Serializer) {

        private var serializer: Serializer = serializerType

        private var notes = ArrayList<Note>()
        private fun formatListString(notesToFormat: List<Note>): String =
            notesToFormat
                .joinToString(separator = "\n") { note ->
                    notes.indexOf(note).toString() + ": " + note.toString()
                }


        fun add(note: Note): Boolean {

            return notes.add(note)
        }

        fun listAllNotes(): String =
            if (notes.isEmpty()) "No notes stored"
            else formatListString(notes)

        fun listNotesBySelectedPriority(priority: Int): String =
            if (notes.isEmpty()) "No notes stored"
            else {
                val listOfNotes = formatListString(notes.filter { note -> note.notePriority == priority })
                if (listOfNotes.equals("")) "No notes with priority: $priority"
                else "${numberOfNotesByPriority(priority)} notes with priority $priority: $listOfNotes"
            }


        fun listActiveNotes(): String =
            if (numberOfActiveNotes() == 0) "No active notes stored"
            else formatListString(notes.filter { note -> !note.isNoteArchived })


        fun listArchivedNotes(): String =
            if  (numberOfArchivedNotes() == 0) "No archived notes stored"
            else formatListString(notes.filter { note -> note.isNoteArchived})

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
            return isValidListIndex(index, notes)
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

        fun sortByPriority(){
            for (i in 0 until notes.size){
                for(j in i + 1 until notes.size){
                if (notes[i].notePriority > notes[j].notePriority){
                    swap(notes[i], notes[j])
                }
                }
            }
        }
        private fun swap (firstNote: Note, secondNote: Note){
            val tempNote = notes[notes.indexOf(secondNote)]
            val indexOffirstNote = notes.indexOf(firstNote)
            notes[notes.indexOf(secondNote)] = firstNote
            notes[indexOffirstNote] = tempNote

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

fun countNotesOfaSpecificCategory(category: String) =
    notes.count{ note: Note -> note.noteCategory==category  }

        fun numberOfActiveNotes(): Int = notes.count { note: Note -> note.isNoteArchived }

        fun numberOfArchivedNotes(): Int = notes.count { note: Note -> note.isNoteArchived }
        fun numberOfNotesByPriority(priority: Int): Int = notes.count { p: Note -> p.notePriority == priority }
        fun searchByTitle (searchString : String) =
            formatListString(
                notes.filter { note -> note.noteTitle.contains(searchString, ignoreCase = true) })

    }

