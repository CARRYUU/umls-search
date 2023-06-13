package com.sscs.definition;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sscs.cui.Cui;
import com.sscs.cui.CuiRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Slf4j
class DefinitionRepositoryTests {
    @Autowired DefinitionRepository repository;
    @Autowired CuiRepository cuiRepository;

    @Test
    void findByCui() {
        var cui = cuiRepository.save(new Cui("C0948008", "Ischemic Stroke", "Disease or Syndrome"));

        var def1 = new Definition();
        def1.setCui(cui);
        def1.setSourceName("NCI");
        def1.setMeaning("An acute episode of focal cerebral, spinal, or retinal dysfunction caused by infarction of brain tissue.");

        var def2 = new Definition();
        def2.setCui(cui);
        def2.setSourceName("HPO");
        def2.setMeaning("Acute ischemic stroke (AIS) is defined by the sudden loss of blood flow to an area of the brain with the resulting loss...");

        var def3 = new Definition();
        def3.setCui(cui);
        def3.setSourceName("MEDLINEPLUS");
        def3.setMeaning("<p>A <a href=\"\"https://medlineplus.gov/stroke.html\"\">stroke</a> is a medical emergency. There are two types - ischemic and <a href=\"\"https://medlineplus.gov/hemorrhagicstroke.html\"\">hemorrhagic</a>....");

        repository.saveAll(Arrays.asList(def1, def2 ,def3));

        assertThat(repository.findByCui(cui)).hasSize(3);
    }

    @Test
    void givenDefinition_whenUsingJsonIgnore_thenCorrect() throws JsonProcessingException {
        var cui = cuiRepository.save(new Cui("C0948008", "Ischemic Stroke", "Disease or Syndrome"));

        var definition = new Definition();
        definition.setCui(cui);
        definition.setSourceName("NCI");
        definition.setMeaning("An acute episode of focal cerebral, spinal, or retinal dysfunction caused by infarction of brain tissue.");

        String result = new ObjectMapper().writeValueAsString(definition);

        assertThat(result).contains("cui", "meaning", "sourceName").doesNotContain("id");
    }
}