package zerobase.weather.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.domain.Memo;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional

class JdbcMemoRepositoryTest {

    @Autowired
    JdbcMemoRepository jdbcMemoRepository;

    @Test
    void insertMemoTest(){
        Memo newMemo = new Memo(1,"this id memo");
        jdbcMemoRepository.save(newMemo);

    //given
    // Memo newMemo =  new Memo(2,"insertMeMO TEST");

    //when
    jdbcMemoRepository.save(newMemo);
    //then
      Optional<Memo> result =  jdbcMemoRepository.findById(2);
        assertEquals(result.get().getText(),"insertMeMO TEST");

    }
    @Test
    void findAllMemoTest(){
        List<Memo> list = jdbcMemoRepository.findAll();

        System.out.println(list);
        assertNotNull(list);
    }

}