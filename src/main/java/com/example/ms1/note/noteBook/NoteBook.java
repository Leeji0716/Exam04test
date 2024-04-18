package com.example.ms1.note.noteBook;

import com.example.ms1.note.note.Note;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
public class NoteBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "noteBook")
    private List<Note> noteList;

    public NoteBook() {
        this.noteList = new ArrayList<>();
    }
}
