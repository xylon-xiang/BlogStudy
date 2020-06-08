package com.studyblog.blog.mapping;


import com.studyblog.blog.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapping {

    @Insert("INSERT INTO User (userId, userName, hashedUserPassword, joinDate, birthday, gender, signature) " +
            "VALUE (#{userId}, #{userName}, #{hashedUserPassword}, #{joinDate}, #{birthday}, #{gender}, #{signature})")
    void AddUser(User user);

    @Delete("DELETE FROM User WHERE userId=#{userId}")
    void DeleteUser(@Param("UserId") int userId);

    @Update("UPDATE User SET userName=#{userName}, hashedUserPassword=#{hashedUserPassword}," +
            " birthday=#{birthday}, gender=#{gender}, signature=#{signature}")
    void UpdateUser(User user);






}