package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.exception.ValidacaoExpeption;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.repository.AbrigoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class AbrigoService {

    @Autowired
    private AbrigoRepository repository;

    public void cadastrar(Abrigo abrigo){
        boolean nomeJaCadastrado = repository.existsByNome(abrigo.getNome());
        boolean telefoneJaCadastrado = repository.existsByTelefone(abrigo.getTelefone());
        boolean emailJaCadastrado = repository.existsByEmail(abrigo.getEmail());

        if (nomeJaCadastrado || telefoneJaCadastrado || emailJaCadastrado) {
            throw new ValidacaoExpeption("Dados já cadastrados para outro abrigo!");
        } else {
            repository.save(abrigo);
        }
    }

    public void cadastrarPet(String idOuNome, Pet pet){

        try {
            Long id = Long.parseLong(idOuNome);
            Abrigo abrigo = repository.getReferenceById(id);
            pet.setAbrigo(abrigo);
            pet.setAdotado(false);
            abrigo.getPets().add(pet);
            repository.save(abrigo);
            throw new ValidacaoExpeption("Abrigo não encontrado");
        } catch (EntityNotFoundException enfe) {
            throw new ValidacaoExpeption(enfe.getMessage());
        } catch (NumberFormatException nfe) {
            try {
                Abrigo abrigo = repository.findByNome(idOuNome);
                pet.setAbrigo(abrigo);
                pet.setAdotado(false);
                abrigo.getPets().add(pet);
                repository.save(abrigo);
                throw new ValidacaoExpeption("Abrigo não encontrado");
            } catch (EntityNotFoundException enfe) {
                throw new ValidacaoExpeption(enfe.getMessage());
            }
        }
    }
    
    public void listarPets(String idOuNome, Pet pet) {
        try {
            Long id = Long.parseLong(idOuNome);
            List<Pet> pets = repository.getReferenceById(id).getPets();
            throw new ValidacaoExpeption("Pet não encontrado");
        } catch (EntityNotFoundException enfe) {
        	throw new ValidacaoExpeption(enfe.getMessage());
        } catch (NumberFormatException e) {
            try {
                List<Pet> pets = repository.findByNome(idOuNome).getPets();
                throw new ValidacaoExpeption("Pet não encontrado");
            } catch (EntityNotFoundException enfe) {
            	throw new ValidacaoExpeption(enfe.getMessage());
            }
        }
    }
    
    public void listarAbrigos() {}
}
