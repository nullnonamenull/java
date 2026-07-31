package com.noname.consumerone;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TestInMemoryService {

    private final List<SomeDto> someDtoList = new ArrayList<>();

    public void create(SomeDto dto) {
        someDtoList.add(dto);
    }

    public List<SomeDto> getAll() {
        return someDtoList;
    }

}
