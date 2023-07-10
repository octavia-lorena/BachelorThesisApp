package com.example.bachelorthesisapp.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.bachelorthesisapp.data.model.entities.ActivityEntity
import com.example.bachelorthesisapp.data.model.entities.BusinessEntity
import com.example.bachelorthesisapp.data.model.entities.OfferPost

@Dao
abstract class OfferPostsDao : BaseDao<OfferPost> {

    @Transaction
    open suspend fun repopulatePosts(posts: List<OfferPost>): List<Long> {
        deleteAllPosts()
        return insertAll(posts)
    }

    @Query("SELECT * FROM posts")
    abstract suspend fun getAllPosts(): List<OfferPost>

    @Query("SELECT * FROM posts WHERE `id` = :key LIMIT 1")
    abstract suspend fun getPost(key: String): OfferPost?

    @Query("DELETE FROM posts")
    abstract suspend fun deleteAllPosts()


    @Query("SELECT * FROM posts WHERE `businessId` = :businessId")
    abstract suspend fun getPostsByBusinessId(businessId: String): List<OfferPost>

    @Query(
        "SELECT P.* " +
                "FROM businesses B " +
                "JOIN posts P " +
                "ON P.businessId = B.id " +
                "WHERE B.businessType = :type"
    )
    abstract suspend fun getPostsByBusinessType(type: String): List<OfferPost>
}