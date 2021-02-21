package com.example.android.politicalpreparedness.database

import com.example.android.politicalpreparedness.network.models.Election

fun List<Election>.toDatabaseEntity(): Array<ElectionEntity>{
    return map{
        ElectionEntity(
            id = it.id,
            name = it.name,
            division = it.division,
            electionDay = it.electionDay,
            isFollowed = it.isFollowed
        )
    }.toTypedArray()
}

fun List<ElectionEntity>.toDomainModel(): List<Election>{
    return map{
        Election(
            id = it.id,
            name = it.name,
            division = it.division,
            electionDay = it.electionDay,
            isFollowed = it.isFollowed
        )
    }
}