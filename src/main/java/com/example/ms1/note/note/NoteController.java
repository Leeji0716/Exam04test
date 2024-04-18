package com.example.ms1.note.note;

import com.example.ms1.note.noteBook.NoteBook;
import com.example.ms1.note.noteBook.NoteBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class NoteController {
    private final NoteBookRepository noteBookRepository;
    private final NoteRepository noteRepository;
    private final NoteService noteService;

    @RequestMapping("/test")
    @ResponseBody
    public String test() {
        return "test";
    }

    @RequestMapping("/")
    public String main(Model model) {
        List<NoteBook> noteBookList = noteBookRepository.findAll();

        if (noteBookList.isEmpty()) {
            Default();
            return "redirect:/";
        }
        NoteBook targetNoteBook = noteBookList.get(0);
        List<Note> noteList = targetNoteBook.getNoteList();
        Note targetNote = noteList.get(0);

        model.addAttribute("noteBookList", noteBookList);
        model.addAttribute("targetNoteBook", targetNoteBook);
        model.addAttribute("noteList", noteList);
        model.addAttribute("targetNote", targetNote);

        return "main";
    }

    @PostMapping("/write")
    public String write(@RequestParam("targetNoteBookId") Long targetNoteBookId) {
        NoteBook noteBook = noteBookRepository.findById(targetNoteBookId).orElse(null);
        if (noteBook == null) {
            return "redirect:/";
        }

        this.noteService.createNote(noteBook);

        return "redirect:/detailBook/" + targetNoteBookId;
    }

    @PostMapping("/writeBook")
    public String writeBook() {
        Default();
        List<NoteBook> noteBookList = noteBookRepository.findAll();
        long index = noteBookList.size();
        return "redirect:/detailBook/" + index;
    }
    @GetMapping("/detailNote/{id}")
    public String detailNote(Model model, @PathVariable Long id) {
        Note note = noteService.findNote(id);
        NoteBook noteBook = note.getNoteBook();
        List<Note> noteList = noteBook.getNoteList();
        List<NoteBook> noteBookList = noteBookRepository.findAll();

        model.addAttribute("targetNoteBook", noteBook);
        model.addAttribute("targetNote", note);
        model.addAttribute("noteList", noteList);
        model.addAttribute("noteBookList", noteBookList);

        return "main";
    }
    @GetMapping("/detailBook/{id}")
    public String detailBook(Model model, @PathVariable Long id) {
        NoteBook noteBook = noteBookRepository.findById(id).get();
        Note note = noteBook.getNoteList().get(0);
        List<Note> noteList = noteBook.getNoteList();
        List<NoteBook> noteBookList = noteBookRepository.findAll();

        model.addAttribute("targetNoteBook", noteBook);
        model.addAttribute("targetNote", note);
        model.addAttribute("noteList", noteList);
        model.addAttribute("noteBookList", noteBookList);

        return "main";
    }
    @PostMapping("/update")
    public String update(Long id, String title, String content) {
        Note note = noteService.findNote(id);
        if (title.trim().length() == 0) {
            title = "제목없음";
        }
        note.setTitle(title);
        note.setContent(content);

        noteRepository.save(note);
        return "redirect:/detailNote/" + id;
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") long id) {
        noteRepository.deleteById(id);
        return "redirect:/";
    }

    public void Default() {
        NoteBook noteBook = new NoteBook();
        noteBook.setName("새노트");

        noteBook.getNoteList().add(noteService.createNote(noteBook));
        noteBookRepository.save(noteBook);
    }
}

