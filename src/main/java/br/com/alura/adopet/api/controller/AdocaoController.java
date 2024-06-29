package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.exception.ValidacaoExpeption;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.StatusAdocao;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.service.AdocaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/adocoes")
public class AdocaoController {

    @Autowired
    private AdocaoService adocaoService;

    @PostMapping
    @Transactional
    public ResponseEntity<String> solicitar(Adocao adocao) {
        try {
            this.adocaoService.solicitar(adocao);
            return  ResponseEntity.ok("Adocao feita com sucesso!");
        } catch (ValidacaoExpeption e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PutMapping("/aprovar")
    @Transactional
    public ResponseEntity<String> aprovar(@RequestBody @Valid Adocao adocao) {
        try {
            this.adocaoService.aprovar(adocao);
            return ResponseEntity.ok("Aprovacao realizada");
        } catch (ValidacaoExpeption e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/reprovar")
    @Transactional
    public ResponseEntity<String> reprovar(@RequestBody @Valid Adocao adocao) {
        try {
            this.adocaoService.reprovar(adocao);
            return ResponseEntity.ok("Aprovacao realizada");
        } catch (ValidacaoExpeption e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
