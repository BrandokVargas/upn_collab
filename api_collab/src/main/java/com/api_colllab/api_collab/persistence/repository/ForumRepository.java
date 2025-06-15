package com.api_colllab.api_collab.persistence.repository;



import com.api_colllab.api_collab.persistence.entity.ForumEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ForumRepository extends CrudRepository<ForumEntity,Long> {



    @Query("SELECT COUNT(f) FROM ForumEntity f WHERE f.user.id_user = :userId")
    Integer countForumsByUserId(@Param("userId") Long userId);






}
