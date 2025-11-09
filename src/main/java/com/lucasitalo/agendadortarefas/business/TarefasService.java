package com.lucasitalo.agendadortarefas.business;

import com.lucasitalo.agendadortarefas.business.dto.TarefasDTO;
import com.lucasitalo.agendadortarefas.business.mapper.TarefasConverter;
import com.lucasitalo.agendadortarefas.infrasctructure.entity.TarefasEntity;
import com.lucasitalo.agendadortarefas.infrasctructure.enums.StatusNotificacaoEnum;
import com.lucasitalo.agendadortarefas.infrasctructure.repository.TarefasRepository;
import com.lucasitalo.agendadortarefas.infrasctructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor

public class TarefasService {

    private final TarefasRepository tarefasRepository;
    private final TarefasConverter tarefaConverter;
    private final JwtUtil jwtUtil;

    public TarefasDTO gravarTarefa(String token, TarefasDTO dto){
        String email = jwtUtil.extrairEmailToken(token.substring(7));
        dto.setDataAlteracao(LocalDateTime.now());
        dto.setStatusNotificacaoEnum(StatusNotificacaoEnum.PENDENTE);
        dto.setEmailUsuario(email);
        TarefasEntity entity = tarefaConverter.paraTarefaEntity(dto);


        return tarefaConverter.paraTarefaDTO(
                tarefasRepository.save(entity));
    }
}
