package zerobase.weather.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import zerobase.weather.domain.Memo;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcMemoRepository  {


    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcMemoRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        //DB 매핑 정보들을 JDBCTEMPLATE에 저장.
    }

    public Memo save(Memo memo){
        String sql = "insert into memo values(?,?)";
        jdbcTemplate.update(sql,memo.getId(),memo.getText());
        return memo;
    }

    public List<Memo> findAll(){
        String sql ="select * from memo";
       return  jdbcTemplate.query(sql,memoMapper());
        //jdbcTemplate 객체에 쿼리를 담아서 db에서 execute 하고
        // rs형태로 데이터를 반환 매퍼로 memo 객체에 반환
    }

    private RowMapper<Memo> memoMapper(){
        //ResultSet
        //{id=1, text="this, memo"};
        return(rs,rowNum) -> new Memo(
                rs.getInt("id"),
                rs.getString("text")
        );

    }


    public Optional<Memo> findById(int id){
        String sql="select * from memo where id=?";
     return  jdbcTemplate.query(sql,memoMapper(),id).stream().findFirst();
    }
    //혹시 모를 null 값을 우회시키는 Optional 함수


}
