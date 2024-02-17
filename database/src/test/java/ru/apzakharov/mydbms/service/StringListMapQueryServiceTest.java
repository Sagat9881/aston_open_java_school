package ru.apzakharov.mydbms.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.apzakharov.mydbms.parsers.QueryParser;
import ru.apzakharov.mydbms.query.*;
import ru.apzakharov.mydbms.queryprocessors.QueryProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class StringListMapQueryServiceTest {
    public static final String UPDATED_NAME = "другой";
    public static final String BASE_NAME = "одно";
    @Mock
    private QueryParser<String> parser;

    private List<Map<String, Object>> storage = new ArrayList<>();
    @Mock
    private QueryProcessor<List<Map<String, Object>>> processor;
    private StringListMapQueryService service;
    private Map<String, Object> row = new HashMap<>() {{
        put("lastname", BASE_NAME);
        put("id", 1L);
        put("age", 1L);
        put("active", "true");
    }};

    @BeforeEach
    void initMocks() {
        service = new StringListMapQueryService(parser, processor, storage);
        storage.add(row);

    }

    @Test
    public void shouldInsert() {
        when(parser.parseQuery(argThat(arg -> arg.toLowerCase().contains("insert"))))
                .thenReturn(InsertQuery.builder().build());
        when(processor.processQuery(argThat(arg -> arg.getType() == QueryType.INSERT), eq(storage)))
                .then(answer -> {
                    storage.add(new HashMap<>());
                    return storage;
                });


        service.processCommand("INSERT VALUES lastName=" + BASE_NAME + ", id=3, age=40, active=true");
        service.processCommand("INSERT VALUES lastName=" + BASE_NAME + ", id=4, age=40, active=true");
        service.processCommand("INSERT VALUES lastName=" + BASE_NAME + ", id=5, age=40, active=true");
        service.processCommand("INSERT VALUES lastName=" + BASE_NAME + ", id=6, age=40, active=true");
        service.processCommand("INSERT VALUES lastName=" + BASE_NAME + ", id=7, age=40, active=true");

        verify(parser, times(5)).parseQuery(anyString());
        verify(processor, times(5)).processQuery(any(InsertQuery.class), eq(storage));

        assert storage.size() == 6;
    }

    @Test
    public void shouldDelete() {
        when(parser.parseQuery(argThat(arg -> arg.toLowerCase().contains("delete"))))
                .thenReturn(DeleteQuery.builder().build());
        when(processor.processQuery(argThat(arg -> arg.getType() == QueryType.DELETE), eq(storage)))
                .then(answer -> {
                    storage.remove(0);
                    return storage;
                });
        service.processCommand("DELETE WHERE lastName=" + BASE_NAME);

        verify(parser, times(1)).parseQuery(anyString());
        verify(processor, times(1)).processQuery(any(DeleteQuery.class), eq(storage));

        assert storage.size() == 0;
    }

    @Test
    public void shouldUpdate() {
        when(parser.parseQuery(argThat(arg -> arg.toLowerCase().contains("update"))))
                .thenReturn(UpdateQuery.builder().build());
        when(processor.processQuery(argThat(arg -> arg.getType() == QueryType.UPDATE), eq(storage)))
                .then(answer -> {
                    Map<String, Object> row = storage.stream()
                            .filter(rows->rows.get("lastname")==BASE_NAME)
                            .findFirst()
                            .orElse(null);
                    row.replace("lastname",UPDATED_NAME);
                    return storage;
                });

        service.processCommand("UPDATE VALUES lastName=" + BASE_NAME + " WHERE lastName=" + UPDATED_NAME);

        verify(parser, times(1)).parseQuery(anyString());
        verify(processor, times(1)).processQuery(any(UpdateQuery.class), eq(storage));

        assert storage.size() == 1;
        assert storage.stream().anyMatch(row -> row.get("lastname") == UPDATED_NAME);
    }


}
























