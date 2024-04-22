package com.example.ms1.note.noteBook;

import com.example.ms1.note.note.Note;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class NoteBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "noteBook")
    private List<Note> noteList;

    public NoteBook(){
        this.noteList = new ArrayList<>();
    }

    @ManyToOne
    private NoteBook parent;

    @OneToMany(mappedBy = "parent")
    private List<NoteBook> children = new ArrayList<>();

    public void addChild(NoteBook child){
        children.add(child);
        child.setParent(this);
    }
}
