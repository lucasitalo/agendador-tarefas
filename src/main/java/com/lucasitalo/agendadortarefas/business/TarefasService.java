package com.lucasitalo.agendadortarefas.business;

import com.lucasitalo.agendadortarefas.business.dto.TarefasDTO;
import com.lucasitalo.agendadortarefas.business.mapper.TarefaUpdateConverter;
import com.lucasitalo.agendadortarefas.business.mapper.TarefasConverter;
import com.lucasitalo.agendadortarefas.infrasctructure.entity.TarefasEntity;
import com.lucasitalo.agendadortarefas.infrasctructure.enums.StatusNotificacaoEnum;
import com.lucasitalo.agendadortarefas.infrasctructure.exceptions.ResourceNotFoundException;
import com.lucasitalo.agendadortarefas.infrasctructure.repository.TarefasRepository;
import com.lucasitalo.agendadortarefas.infrasctructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor

public class TarefasService {

    private final TarefasRepository tarefasRepository;
    private final TarefasConverter tarefaConverter;
    private final JwtUtil jwtUtil;
    private final TarefaUpdateConverter tarefaUpdateConverter;

    public TarefasDTO gravarTarefa(String token, TarefasDTO dto) {
        String email = jwtUtil.extrairEmailToken(token.substring(7));
        dto.setDataCriacao(LocalDateTime.now());
        dto.setDataAlteracao(LocalDateTime.now());
        dto.setStatusNotificacaoEnum(StatusNotificacaoEnum.PENDENTE);
        dto.setEmailUsuario(email);
        TarefasEntity entity = tarefaConverter.paraTarefaEntity(dto);

        return tarefaConverter.paraTarefaDTO(
                tarefasRepository.save(entity));
    }

    public List<TarefasDTO> buscaTarefasAgendadasPorPeriodo(LocalDateTime dataInicial, LocalDateTime dataFinal) {
        return tarefaConverter.ParaListaTarefasDTO(
                tarefasRepository.findByDataEventoBetween(dataInicial, dataFinal));
    }

    public List<TarefasDTO> buscaTarefasPorEmail(String token) {

        String email = jwtUtil.extrairEmailToken(token.substring(7));
        List<TarefasEntity> listaTarefas = tarefasRepository.findByEmailUsuario(email);

        return tarefaConverter.ParaListaTarefasDTO(listaTarefas);
    }

    public void detelaTarefaPorId(String id) {
        try {
            tarefasRepository.deleteById(id);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Erro ao deletar tarefa por id, id inexistente " + id, e.getCause());
        }
    }

    public TarefasDTO alteraStatus(StatusNotificacaoEnum status, String id) {
        try {

            TarefasEntity entity = tarefasRepository.findById(id).
                    orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada " + id));
            entity.setStatusNotificacaoEnum(status);
            return tarefaConverter.paraTarefaDTO(tarefasRepository.save(entity));
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Erro ao alterar status da tarefa " + e.getCause());
        }

    }

    public TarefasDTO updateTarefas(TarefasDTO dto, String id) {

        try {
            TarefasEntity entity = tarefasRepository.findById(id).
                    orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada " + id));
            tarefaUpdateConverter.updateTarefas(dto, entity);
            return tarefaConverter.paraTarefaDTO(tarefasRepository.save(entity));

        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Erro ao alterar status da tarefa " + e.getCause());
        }
    }

}
