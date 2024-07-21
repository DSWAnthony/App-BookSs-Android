package com.program.bookss.module

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.program.bookss.data.AuthRepositoryImpl
import com.program.bookss.data.BookRepositoryImpl
import com.program.bookss.data.UserRepositoryImpl
import com.program.bookss.domain.repository.AuthRepository
import com.program.bookss.domain.repository.BookRepository
import com.program.bookss.domain.repository.UserRepository
import com.program.bookss.domain.usecase.Authentication.AuthenticationUseCases
import com.program.bookss.domain.usecase.Authentication.FireBaseAuthState
import com.program.bookss.domain.usecase.Authentication.FireBaseSignIn
import com.program.bookss.domain.usecase.Authentication.FireBaseSignOut
import com.program.bookss.domain.usecase.Authentication.FireBaseSignUp
import com.program.bookss.domain.usecase.Authentication.IsUserAuthenticated
import com.program.bookss.domain.usecase.Book.BookUseCases
import com.program.bookss.domain.usecase.Book.DeleteBook
import com.program.bookss.domain.usecase.Book.GetBooks
import com.program.bookss.domain.usecase.Book.UploadBook
import com.program.bookss.domain.usecase.User.GetRoleUser
import com.program.bookss.domain.usecase.User.GetUserDetails
import com.program.bookss.domain.usecase.User.SetUserDetails
import com.program.bookss.domain.usecase.User.UserUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseDatabase(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFireBaseStorage() : FirebaseStorage{
        return FirebaseStorage.getInstance()
    }

    @Provides
    @Singleton
    fun provideFireBaseFireStore() : FirebaseFirestore{
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideAtuhenticationRepository(auth: FirebaseAuth,firestore: FirebaseFirestore):AuthRepository{
        return AuthRepositoryImpl(auth,firestore)
    }

    @Provides
    @Singleton
    fun provideAuthUseCases(repository: AuthRepository) =  AuthenticationUseCases(
        isUserAuthenticated = IsUserAuthenticated(repository = repository),
        fireBaseAuthState = FireBaseAuthState(repository = repository),
        fireBaseSignOut = FireBaseSignOut(repository = repository),
        fireBaseSignIn = FireBaseSignIn(repository = repository),
        fireBaseSignUp = FireBaseSignUp(repository = repository)
    )

    @Provides
    @Singleton
    fun provideUserRepository(firestore: FirebaseFirestore):UserRepository{
        return UserRepositoryImpl(firebaseFirestore = firestore)
    }

    @Provides
    @Singleton
    fun provideUserUseCases(repository: UserRepository) = UserUseCases(
        getUserDetails = GetUserDetails(repository = repository),
        setUserDetails = SetUserDetails(repository = repository),
        getRoleUser = GetRoleUser(repository = repository)

    )

    @Provides
    @Singleton
    fun provideBookRepository(firestorage: FirebaseStorage,auth: FirebaseAuth,firebaseDatabase: FirebaseDatabase)
    :BookRepository{
        return BookRepositoryImpl(firebaseStorage = firestorage, auth = auth, firebaseDatabase = firebaseDatabase)
    }

    @Provides
    @Singleton
    fun provideBookUseCases(repository: BookRepository) = BookUseCases(
        uploadBook = UploadBook(repository = repository),
        getBooks = GetBooks(repository = repository),
        deleteBook = DeleteBook(repository = repository)
    )



}