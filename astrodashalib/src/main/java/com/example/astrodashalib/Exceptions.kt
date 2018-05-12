package com.example.astrodashalib

open class DataSourceException(message: String? = null) : Exception(message)

class RemoteDataNotFoundException : DataSourceException("Data not found in remote data source")
class SearchTextEmptyException : DataSourceException("SearchText cannot be empty")
