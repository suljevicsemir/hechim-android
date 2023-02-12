package com.hechim.models.data

sealed class Resource<T>(
    val data: T? = null,
    val errorSpecification: ErrorSpecification? = null,
    val loaderSpecification: LoaderSpecification? = null
    ) {
    class Success<T>(data: T) : Resource<T>(data = data)
    class Error<T>(errorSpecification: ErrorSpecification) : Resource<T>(errorSpecification = errorSpecification)
    class Loading<T>(loaderSpecification: LoaderSpecification? = null) : Resource<T>(loaderSpecification = loaderSpecification)
    class Nothing<T>(): Resource<T>()
}

class ResourceError(
    val useStringResource: Boolean = true,
    val titleResource: Int,
    val descriptionResource: Int,
    val title : String,
    val description: String
) {

    val displayTitle: String get() {
        if(useStringResource) {
            return title
        }
        return "getStrtitleResource"
    }

}

data class APIResponse<T>(
    val data: T?,
    val error: APIError? = null,
    val success: Boolean = true
)

data class APIError(
    val message: String?,
    val errors: List<String>?
) {
   companion object {
       fun new(s: String) : APIError{
           return APIError(
               message = "",
               errors = emptyList()
           )
       }
   }
}

data class LoaderSpecification(
    val title: String,
    val description: String
)

data class ErrorSpecification(
    val useStringResource: Boolean,
    val titleResource: Int? = null,
    val descriptionResource: Int? = null,
    val title : String? = null,
    val description: String? = null,
    val retryText: Int? = null
) {

}