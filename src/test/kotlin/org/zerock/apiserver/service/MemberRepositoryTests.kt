package org.zerock.apiserver.service

import com.zaxxer.hikari.HikariDataSource
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import org.zerock.apiserver.domain.entity.Member
import org.zerock.apiserver.domain.entity.MemberRole
import org.zerock.apiserver.logger
import org.zerock.apiserver.repository.MemberRepository
import java.sql.SQLException
import javax.sql.DataSource

@TestPropertySource(properties = ["spring.config.location = classpath:application-mariadb.yml"])
@SpringBootTest
@ActiveProfiles("test")
class MemberRepositoryTests(
    @Autowired val memberRepository: MemberRepository,
    @Autowired val passwordEncoder: PasswordEncoder
) {


    @Throws(SQLException::class)
    private fun useDataSource(dataSource: DataSource) {
        val con1= dataSource.connection
        val con2= dataSource.connection
        logger().info("connection={}, class={}", con1, con1.javaClass)
        logger().info("connection={}, class={}", con2, con2.javaClass)
    }
    @BeforeEach
    @Test
    fun initTest() {
       println(">> inital beforeSpec memberDB")

       val dataSource  = HikariDataSource()
        //dataSource.jdbcUrl = ("org.mariadb.jdbc.Driver")
        dataSource.jdbcUrl = ("jdbc:mariadb://localhost:3306/apidb2")
        dataSource.username = ("root")
        dataSource.password = ("1234")
        dataSource.maximumPoolSize = (10)
        dataSource.poolName = ("MyPool");
        Thread.sleep(10000);
    }


    @Test
    fun testInsertMember() {
        for (i in 0..9) {
            val member = Member(
                email = "user$i@aaa.com",
                pw = passwordEncoder.encode("1111"),
                nickname = "USER$i",
            )

            member.addRole(MemberRole.USER)

            if(i > 5) {
                member.addRole(MemberRole.MANAGER)
            }

            if(i > 8) {
                member.addRole(MemberRole.ADMIN)
            }

            if(i >= 8) {
                member.addRole(MemberRole.ADMIN)
            }

            memberRepository.save(member)

        }
    }
}


//@SpringBootTest
//@ActiveProfiles("test")
//class MemberRepositoryTests(
//    @Autowired val memberRepository: MemberRepository,
//    @Autowired val passwordEncoder: PasswordEncoder
//) : StringSpec(){
//
//
//
//   init {
//       println(">> inital beforeSpec memberDB")
//
//       val dataSourceBuilder = DataSourceBuilder.create()
//           .driverClassName("org.mariadb.jdbc.Driver")
//           .url("jdbc:mariadb://localhost:3306/apidb2")
//           .username("root")
//           .password("1234")
//           .build()
//
//        val jdbcTemplate = JdbcTemplate(dataSourceBuilder)
//       jdbcTemplate.execute {  }
//   }
//}