package com.example.ms1.note.note;

import com.example.ms1.note.noteBook.NoteBook;
import com.example.ms1.note.noteBook.NoteBookRepository;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class NoteController {

    private final NoteRepository noteRepository;
    private final NoteBookRepository noteBookRepository;

    @RequestMapping("/test")
    @ResponseBody
    public String test() {
        return "test";
    }

    @RequestMapping("/")
    public String main(Model model) {
        //1. DB에서 데이터 꺼내오기
        List<NoteBook> noteBookList = noteBookRepository.findAll();
        if (noteBookList.isEmpty()){
            Default();
            return "redirect:/";
        }
        NoteBook noteBook = noteBookList.get(0);
        List<Note> noteList = noteBook.getNoteList();
        Note note = noteList.get(0);


        //2. 꺼내온 데이터를 템플릿으로 보내기
        model.addAttribute("noteBookList", noteBookList);
        model.addAttribute("targetNoteBook", noteBook);
        model.addAttribute("noteList", noteList);
        model.addAttribute("targetNote", note);

        return "main";
    }

    @PostMapping("/write")
    public String write(@RequestParam("targetNoteBookId") long targetNoteBookId) {
        NoteBook noteBook = noteBookRepository.findById(targetNoteBookId).orElse(null);
        if (noteBook == null){
            return "redirect:/";
        }
        CreateNote(noteBook);
        return "redirect:/detailBook/" + targetNoteBookId;
    }

    @PostMapping("/writeBook")
    public String writeBook() {
        Default();
        List<NoteBook> noteBookList = noteBookRepository.findAll();
        long index = noteBookList.size();
        return "redirect:/detailBook/" + index;
    }

    @PostMapping("/groups/{id}/writeBook")
    public String ChildBook(@PathVariable("id") long id){
        NoteBook parent = noteBookRepository.findById(id).orElseThrow();

        NoteBook child = new NoteBook();
        child.setName("새 노트북");
        Note note = CreateNote(child);
        child.getNoteList().add(note);
        noteBookRepository.save(child);

        parent.addChild(child);
        noteBookRepository.save(parent);

        return "redirect:/detailBook/" + parent.getId();
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable Long id) {
        Note note = noteRepository.findById(id).get();
        NoteBook noteBook = note.getNoteBook();

        model.addAttribute("noteBookList", noteBookRepository.findAll());
        model.addAttribute("targetNoteBook", noteBook);
        model.addAttribute("targetNote", note);
        model.addAttribute("noteList", noteBook.getNoteList());

        return "main";
    }

    @GetMapping("/detailBook/{id}")
    public String detailBook(Model model, @PathVariable Long id) {
        NoteBook noteBook = noteBookRepository.findById(id).get();
        List<Note> noteList = noteBook.getNoteList();

        model.addAttribute("noteBookList", noteBookRepository.findAll());
        model.addAttribute("targetNoteBook", noteBook);
        model.addAttribute("noteList", noteList);
        model.addAttribute("targetNote", noteList.get(0));

        return "main";
    }
    @PostMapping("/update")
    public String update(Long id, String title, String content) {
        Note note = noteRepository.findById(id).get();
        if (title.trim().length()==0){
            title = "제목없음";
        }
        note.setTitle(title);
        note.setContent(content);

        noteRepository.save(note);
        return "redirect:/detail/" + id;
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") long id) {
        Note note = noteRepository.findById(id).get();

        noteRepository.delete(note);
        return "redirect:/";
    }

    public void Default(){
        NoteBook noteBook = new NoteBook();
        noteBook.setName("새노트");

        Note note = CreateNote(noteBook);
        noteBook.getNoteList().add(note);
        noteBookRepository.save(noteBook);
    }

    public Note CreateNote(NoteBook noteBook){
        NoteBook savedNoteBook = noteBookRepository.save(noteBook);
        Note note = new Note();
        note.setTitle("new title..");
        note.setContent("");
        note.setCreateDate(LocalDateTime.now());
        note.setNoteBook(savedNoteBook);

        noteRepository.save(note);
        return note;
    }
}
