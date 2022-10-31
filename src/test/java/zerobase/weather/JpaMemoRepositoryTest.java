package zerobase.weather;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.domain.Memo;
import zerobase.weather.repository.JpaMemoRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class JpaMemoRepositoryTest {

    @Autowired
    JpaMemoRepository jpaMemoRepository;

    @Test
    void insertMemoTest(){
    //given

        Memo  memo = new Memo(1,"this is memo");
    //when
    jpaMemoRepository.save(memo);
    //then
        List<Memo> list = jpaMemoRepository.findAll();
        assertNotNull(list);

    }


    @Test
    void findById(){
        //given
        Memo newMemo = new Memo(9,"insertMessage");

        //when
      Memo memo=  jpaMemoRepository.save(newMemo);
        //then
        System.out.println(memo.getId());

        Optional<Memo> result = jpaMemoRepository.findById(memo.getId());
        assertEquals(result.get().getText(),"insertMessage");

            //맞네 키값이 자동생성해놨는데 저게 어떻게 맞아

    }
}
