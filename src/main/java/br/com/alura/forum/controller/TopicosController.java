package br.com.alura.forum.controller;

import br.com.alura.forum.controller.dto.DetalharTopicoDTO;
import br.com.alura.forum.controller.dto.TopicoDTO;
import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/topicos")
public class TopicosController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping
    public List<TopicoDTO> Lista(String nomeCurso) {

        if(nomeCurso == null) {
            List<Topico> topico = topicoRepository.findAll();
            return TopicoDTO.converter(topico);
        }else{
            List<Topico> topico = topicoRepository.findByCurso_Nome(nomeCurso);
            return TopicoDTO.converter(topico);
        }

    }

    @PostMapping
    public ResponseEntity<TopicoDTO> cadastrar (@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriComponentsBuilder) {

        Topico topico = form.converter(cursoRepository);
        topicoRepository.save(topico);

        URI uri = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicoDTO(topico));

    }

    @GetMapping("/{id}")
    public DetalharTopicoDTO detalhar(@PathVariable Long id){
        Topico topico = topicoRepository.getOne(id);
        return new DetalharTopicoDTO(topico);
    }
}
