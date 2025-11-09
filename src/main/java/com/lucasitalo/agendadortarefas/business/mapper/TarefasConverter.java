package com.lucasitalo.agendadortarefas.business.mapper;

import com.lucasitalo.agendadortarefas.business.dto.TarefasDTO;
import com.lucasitalo.agendadortarefas.infrasctructure.entity.TarefasEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface TarefasConverter {

    TarefasEntity paraTarefaEntity(TarefasDTO dto);

    TarefasDTO paraTarefaDTO(TarefasEntity entity);
}
