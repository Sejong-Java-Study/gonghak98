package com.example.gimmegonghakauth.dao;

import com.example.gimmegonghakauth.domain.CompletedCoursesDomain;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

@RequiredArgsConstructor
public class CustomCompletedCoursesDaoImpl implements CustomCompletedCoursesDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void saveAll(List<CompletedCoursesDomain> completedCourses) {
        String sql = "insert into completed_course (semester, user_id, year) values (?,?,?)";

        jdbcTemplate.batchUpdate(
            sql,
            new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    CompletedCoursesDomain course = completedCourses.get(i);
                    ps.setString(1,course.getSemester());
                    ps.setLong(2, course.getUserDomain().getId());
                    ps.setInt(3,course.getYear());
                }

                @Override
                public int getBatchSize() {
                    return completedCourses.size();
                }
            });
    }
}
