package com.example.elva.repository

interface Repository<T> {

    fun save(entity: T): T
    fun getAll(): List<T>
    fun delete(entity: T): T
    fun edit(oldEntity: T, newEntity: T): T
}