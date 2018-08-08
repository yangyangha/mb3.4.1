package pr.demo;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import pr.demo.entity.User;

import java.io.Reader;

public class DemoTest {
    public static void main(String[] args) throws Exception {
        String resource = "configuration.xml";
        Reader reader = Resources.getResourceAsReader(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder()
                .build(reader);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        User user = sqlSession.selectOne("userTest.selectUser", 1);
        System.out.println(user.getUsername());
        sqlSession.close();
    }
}