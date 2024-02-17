package ru.apzakharov.mydbms.service;

import lombok.experimental.SuperBuilder;
import ru.apzakharov.mydbms.parsers.QueryParser;
import ru.apzakharov.mydbms.parsers.StringParser;
import ru.apzakharov.mydbms.queryprocessors.ListMapQueryProcessor;
import ru.apzakharov.mydbms.queryprocessors.QueryProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@SuperBuilder
public class StringListMapQueryService extends AbstractQueryService<String, List<Map<String,Object>>>{
    public StringListMapQueryService() {
        super(new StringParser(), new ListMapQueryProcessor(), new ArrayList<>());
    }

    public StringListMapQueryService(QueryParser<String> parser, QueryProcessor<List<Map<String,Object>>> processor, List<Map<String,Object>> storage){
        super(parser, processor, storage);
    }

    public StringListMapQueryService(List storage){
        super(new StringParser(), new ListMapQueryProcessor(), storage);
    }


}
