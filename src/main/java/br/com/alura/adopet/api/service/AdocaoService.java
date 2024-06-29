package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.exception.ValidacaoExpeption;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.StatusAdocao;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AdocaoService {

    @Autowired
    private AdocaoRepository repository;

    @Autowired
    private EnviarEmailService sendEmail;

    public void solicitar(Adocao adocao){
        if (adocao.getPet().getAdotado() == true) {
            throw new ValidacaoExpeption("Pet já foi adotado!");
        } else {
            List<Adocao> adocoes = repository.findAll();
            for (Adocao a : adocoes) {
                if (a.getTutor() == adocao.getTutor() && a.getStatus() == StatusAdocao.AGUARDANDO_AVALIACAO) {
                    throw new ValidacaoExpeption("Tutor já possui outra adoção aguardando avaliação!");
                }
            }
            for (Adocao a : adocoes) {
                if (a.getPet() == adocao.getPet() && a.getStatus() == StatusAdocao.AGUARDANDO_AVALIACAO) {
                    throw new ValidacaoExpeption("Pet já está aguardando avaliação para ser adotado!");
                }
            }
            for (Adocao a : adocoes) {
                int contador = 0;
                if (a.getTutor() == adocao.getTutor() && a.getStatus() == StatusAdocao.APROVADO) {
                    contador = contador + 1;
                }
                if (contador == 5) {
                    throw new ValidacaoExpeption("Tutor chegou ao limite máximo de 5 adoções!");
                }
            }
        }
        adocao.setData(LocalDateTime.now());
        adocao.setStatus(StatusAdocao.AGUARDANDO_AVALIACAO);
        repository.save(adocao);

        sendEmail.enviarEmail(adocao.getPet().getAbrigo().getEmail(),"Solicitação de adoção","Olá " +adocao.getPet().getAbrigo().getNome() +"!\n\nUma solicitação de adoção foi registrada hoje para o pet: " +adocao.getPet().getNome() +". \nFavor avaliar para aprovação ou reprovação.");

    }

    public void aprovar(Adocao adocao) {
        try {
            adocao.setStatus(StatusAdocao.APROVADO);
            repository.save(adocao);
            sendEmail.enviarEmail(adocao.getPet().getAbrigo().getEmail(),"Adoção Aprovada","Olá " +adocao.getPet().getAbrigo().getNome() +"!\n\nUma solicitação de adoção foi registrada hoje para o pet: " +adocao.getPet().getNome() +". \nFavor avaliar para aprovação ou reprovação.");
        } catch (ValidacaoExpeption e ){
            throw new ValidacaoExpeption("Erro no processo de aprovar");
        }

    }

    public void reprovar(Adocao adocao) {
        try {
            adocao.setStatus(StatusAdocao.REPROVADO);
            repository.save(adocao);
            sendEmail.enviarEmail(adocao.getPet().getAbrigo().getEmail(),"Adoção Reprovada","Olá " +adocao.getPet().getAbrigo().getNome() +"!\n\nUma solicitação de adoção foi registrada hoje para o pet: " +adocao.getPet().getNome() +". \nFavor avaliar para aprovação ou reprovação.");

        }catch (ValidacaoExpeption e ){
            throw new ValidacaoExpeption("Erro no processo de aprovar");
        }

    }

}
