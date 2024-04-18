package com.example.ms1.note.note;

import com.example.ms1.note.noteBook.NoteBook;
import com.example.ms1.note.noteBook.NoteBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class NoteService {
    private final NoteRepository noteRepository;
    private final NoteBookRepository noteBookRepository;

    public Note createNote(NoteBook noteBook) {
        NoteBook savedNoteBook = noteBookRepository.save(noteBook);
        // Note 객체를 생성하고 저장된 NoteBook 객체를 참조합니다.
        Note note = new Note();
        note.setTitle("new title..");
        note.setContent("");
        note.setCreateDate(LocalDateTime.now());
        note.setNoteBook(savedNoteBook);

        noteRepository.save(note);
        return note;
    }

    public Note findNote(long id){
        return noteRepository.findById(id).get();
    }
    public NoteBook findNoteBook(long id){
        return noteRepository.findById(id).get().getNoteBook();
    }

    public List<Note> getList(){
        return noteRepository.findAll();
    }
}
