package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg electionEntity: Election)

    @Query("select * from election_table")
    fun getFollowedElections(): LiveData<List<Election>>

    @Query("Select * from election_table")
    fun selectAllElections(): LiveData<List<Election>>

    @Query("select * from election_table where id = :id")
    fun selectElectionById(id: Int) : LiveData<Election>

    @Delete
    suspend fun delete(election: Election)

    @Query("delete from election_table")
    suspend fun clear()

}