package com.obinna.example.fileuploaddemo.fileuploaddemowebapp.controller;

import com.obinna.example.fileuploaddemo.fileuploaddemowebapp.exception.StorageFileNotFoundException;
import com.obinna.example.fileuploaddemo.fileuploaddemowebapp.service.StorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = {"/fileuploaddemo"})
public class FileMgmtController {

    private final StorageService storageService;

    public FileMgmtController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping(value = {"/managefiles"})
    public String listUploadedFiles(Model model) throws IOException {
        var listOfFiles = storageService.loadAll()
                .map(path -> MvcUriComponentsBuilder.fromMethodName(FileMgmtController.class,
                        "serveFile", path.getFileName().toString())
                        .build()
                        .toUri()
                        .toString())
                        .collect(Collectors.toList());
        model.addAttribute("files", listOfFiles);
        return "files/managefiles";
    }

    @GetMapping(value = {"/managefiles/{filename:.+}"})
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" +
                        file.getFilename() + "\"")
                .body(file);
    }

    @PostMapping(value = {"/managefiles"})
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {
        storageService.store(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded "
                        + file.getOriginalFilename() + "!");
        return "redirect:/fileuploaddemo/managefiles";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException ex) {
        return ResponseEntity.notFound().build();
    }
}
