package com.example.bachelorthesisapp.data.datasource

import com.example.bachelorthesisapp.data.dao.OfferPostsDao
import com.example.bachelorthesisapp.data.model.entities.BusinessEntity
import com.example.bachelorthesisapp.data.model.entities.OfferPost
import com.example.bachelorthesisapp.datasource.LocalDataSource
import javax.inject.Inject

class PostsLocalDataSource @Inject constructor(
    private val offerPostsDao: OfferPostsDao
) : LocalDataSource<String, OfferPost> {

    override suspend fun getAllEntities(): List<OfferPost> = offerPostsDao.getAllPosts()

    override suspend fun deleteAllEntities() = offerPostsDao.deleteAllPosts()

    override suspend fun repopulateEntities(entities: List<OfferPost>): List<Long> =
        offerPostsDao.repopulatePosts(entities)

    override suspend fun deleteEntity(entity: OfferPost) = offerPostsDao.delete(entity)

    override suspend fun updateEntity(entity: OfferPost) = offerPostsDao.update(entity)

    override suspend fun insertAll(entities: List<OfferPost>): List<Long> =
        offerPostsDao.insertAll(entities)

    override suspend fun insertEntity(entity: OfferPost): Long = offerPostsDao.insert(entity)

    override suspend fun getEntity(key: String): OfferPost? = offerPostsDao.getPost(key)

    suspend fun getPostsByBusinessId(key: String): List<OfferPost> =
        offerPostsDao.getPostsByBusinessId(key)


}