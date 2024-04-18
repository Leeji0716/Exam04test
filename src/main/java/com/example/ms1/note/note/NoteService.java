package com.example.ms1.note.note;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class NoteService {
    private final NoteRepository noteRepository;

//    public Note Default(){
//        Note note = new Note();
//        note.setTitle("new title..");
//        note.setContent("");
//        note.setCreateDate(LocalDateTime.now());
//
//        noteRepository.save(note);
//        return note;
//    }

    public Note findNote(long id){
        return noteRepository.findById(id).get();
    }

    public List<Note> getList(){
        return noteRepository.findAll();
    }
}
