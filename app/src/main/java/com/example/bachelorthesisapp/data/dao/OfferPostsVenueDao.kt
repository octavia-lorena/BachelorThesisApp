package com.example.bachelorthesisapp.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.bachelorthesisapp.data.model.entities.ActivityEntity
import com.example.bachelorthesisapp.data.model.entities.BusinessEntity
import com.example.bachelorthesisapp.data.model.entities.OfferPost
import com.example.bachelorthesisapp.data.model.entities.OfferPostVenue

@Dao
abstract class OfferPostsVenueDao : BaseDao<OfferPostVenue> {

    @Transaction
    open suspend fun repopulatePosts(posts: List<OfferPostVenue>): List<Long> {
        deleteAllPosts()
        return insertAll(posts)
    }

    @Query("SELECT * FROM posts_venue")
    abstract suspend fun getAllPosts(): List<OfferPostVenue>

    @Query("SELECT * FROM posts_venue WHERE `id` = :key LIMIT 1")
    abstract suspend fun getPost(key: String): OfferPostVenue?

    @Query("DELETE FROM posts_venue")
    abstract suspend fun deleteAllPosts()


    @Query("SELECT * FROM posts_venue WHERE `businessId` = :businessId")
    abstract suspend fun getPostsByBusinessId(businessId: String): List<OfferPostVenue>

    @Query(
        "SELECT P.* " +
                "FROM businesses B " +
                "JOIN posts_venue P " +
                "ON P.businessId = B.id " +
                "WHERE B.businessType = :type"
    )
    abstract suspend fun getPostsByBusinessType(type: String): List<OfferPostVenue>
}